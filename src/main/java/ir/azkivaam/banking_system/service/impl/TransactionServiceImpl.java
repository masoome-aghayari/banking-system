package ir.azkivaam.banking_system.service.impl;

import ir.azkivaam.banking_system.domain.dto.*;
import ir.azkivaam.banking_system.domain.entity.BankAccount;
import ir.azkivaam.banking_system.domain.entity.TransactionRepository;
import ir.azkivaam.banking_system.domain.enums.EventType;
import ir.azkivaam.banking_system.domain.enums.TransactionStatus;
import ir.azkivaam.banking_system.domain.enums.TransactionType;
import ir.azkivaam.banking_system.exceptions.BankAccountException;
import ir.azkivaam.banking_system.exceptions.TransactionException;
import ir.azkivaam.banking_system.factory.TransactionDtoFactory;
import ir.azkivaam.banking_system.observer.impl.TransactionObserverManager;
import ir.azkivaam.banking_system.repository.BankAccountRepository;
import ir.azkivaam.banking_system.service.TransactionService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static ir.azkivaam.banking_system.domain.enums.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository accountRepository;
    private final ExecutorService notificationExecutor;
    private final TransactionObserverManager observerManager;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TransactionDto deposit(DepositRequest request) {
        EventType eventType = EventType.DEPOSIT;
        TransactionDto transactionDto =
                initializeTransaction(request.getAmount(),
                                      request.getAccountNumber(),
                                      null,
                                      TransactionType.DEPOSIT,
                                      eventType);
        try {
            BankAccount bankAccount = getBankAccount(request.getAccountNumber());
            doDeposit(request, bankAccount);
            transactionDto.setStatus(TransactionStatus.SUCCESS);
            updateTransactionStatus(transactionDto);
            notifyObserversOnSuccess(transactionDto, eventType);
        } catch (Exception exception) {
            handleTransactionError(exception, transactionDto, eventType);
        }
        return transactionDto;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TransactionDto withdraw(WithdrawRequest request) {
        EventType eventType = EventType.WITHDRAW;
        TransactionDto transactionDto =
                initializeTransaction(request.getAmount(),
                                      request.getAccountNumber(),
                                      null,
                                      TransactionType.WITHDRAW,
                                      eventType);
        try {
            BankAccount bankAccount = getBankAccount(request.getAccountNumber());
            doWithDraw(request, bankAccount);
            transactionDto.setStatus(TransactionStatus.SUCCESS);
            notifyObserversOnSuccess(transactionDto, eventType);
        } catch (Exception exception) {
            handleTransactionError(exception, transactionDto, eventType);
        }
        return transactionDto;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public TransactionDto transfer(TransferRequest request) {
        checkBankAccountsExistence(request.getSourceAccountNumber(), request.getDestinationAccountNumber());
        Long amount = request.getAmount();
        EventType eventType = EventType.TRANSFER;
        TransactionDto transactionDto = initializeTransaction(amount,
                                                              request.getSourceAccountNumber(),
                                                              request.getDestinationAccountNumber(),
                                                              TransactionType.TRANSFER,
                                                              eventType);
        try {
            WithdrawRequest withdrawRequest =
                    WithdrawRequest.builder()
                                   .accountNumber(request.getSourceAccountNumber())
                                   .amount(amount)
                                   .build();
            withdraw(withdrawRequest);
            DepositRequest depositRequest =
                    DepositRequest.builder()
                                  .accountNumber(request.getDestinationAccountNumber())
                                  .amount(amount)
                                  .build();
            deposit(depositRequest);
            transactionDto.setStatus(TransactionStatus.SUCCESS);
            notifyObserversOnSuccess(transactionDto, eventType);
        } catch (Exception exception) {
            handleTransactionError(exception, transactionDto, eventType);
        }
        return transactionDto;
    }

    private void checkBankAccountsExistence(String sourceAccountNumber, String destinationAccountNumber) {
        if (!accountRepository.existsByAccountNumber(sourceAccountNumber)) {
            throw new TransactionException(ACCOUNT_NOT_FOUND, sourceAccountNumber);
        }
        if (!accountRepository.existsByAccountNumber(sourceAccountNumber)) {
            throw new TransactionException(ACCOUNT_NOT_FOUND, destinationAccountNumber);
        }
    }

    private synchronized void doDeposit(DepositRequest request, BankAccount bankAccount) {
        bankAccount.setBalance((bankAccount.getBalance() + request.getAmount()));
        accountRepository.save(bankAccount);
    }

    private synchronized void doWithDraw(WithdrawRequest request, BankAccount bankAccount) {
        Long amount = request.getAmount();
        if (bankAccount.getBalance() < amount) {
            throw new BankAccountException(INSUFFICIENT_BALANCE, bankAccount.getAccountNumber());
        }
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        accountRepository.save(bankAccount);
    }

    private void handleTransactionError(Exception exception, TransactionDto transactionDto, EventType eventType) {
        transactionDto.setStatus(TransactionStatus.FAILED);
        String exceptionMessage = exception.getLocalizedMessage() == null ? exception.getMessage() :
                                  exception.getLocalizedMessage();
        notifyObserversOnFailure(transactionDto, exceptionMessage, eventType);
        throw new TransactionException(TRANSACTION_FAILED,
                                       exception,
                                       transactionDto.getTrackingCode(),
                                       exceptionMessage);
    }

    private synchronized void updateTransactionStatus(TransactionDto transactionDto) {
        transactionRepository.updateStatusById(transactionDto.getId(), transactionDto.getStatus());
    }

    private TransactionDto initializeTransaction(Long amount,
                                                 String sourceAccountNumber,
                                                 String destinationAccountNumber,
                                                 TransactionType transactionType,
                                                 EventType eventType) {
        TransactionDto transactionDto = TransactionDtoFactory.createTransactionDto(amount,
                                                                                   transactionType,
                                                                                   sourceAccountNumber,
                                                                                   destinationAccountNumber);
        notifyObserversOnInitialization(transactionDto, eventType);
        return transactionDto;
    }

    private void notifyObserversOnInitialization(TransactionDto transactionDto, EventType eventType) {
        Event<TransactionDto> event = new Event<>(transactionDto, eventType);
        CompletableFuture.runAsync(() -> observerManager.notifyObservers(event), notificationExecutor)
                         .orTimeout(5, TimeUnit.SECONDS)
                         .exceptionally(ex -> {
                             log.error("Notification failed for transaction {}, due to: {}",
                                       event.getData().getTrackingCode(),
                                       ex.getMessage());
                             return null;
                         });
    }

    private void notifyObserversOnFailure(TransactionDto transactionDto, String exceptionMessage, EventType eventType) {
        Event<TransactionDto> event = new Event<>(transactionDto,
                                                  exceptionMessage,
                                                  eventType,
                                                  null);
        CompletableFuture.runAsync(() -> observerManager.notifyObservers(event), notificationExecutor)
                         .orTimeout(5, TimeUnit.SECONDS)
                         .exceptionally(ex -> {
                             log.error("Notification failed for transaction {}, due to: {}",
                                       event.getData().getTrackingCode(),
                                       ex.getMessage());
                             return null;
                         });
    }

    private void notifyObserversOnSuccess(TransactionDto transactionDto, EventType eventType) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                Event<TransactionDto> event = new Event<>(transactionDto, eventType);
                CompletableFuture.runAsync(() -> observerManager.notifyObservers(event), notificationExecutor)
                                 .orTimeout(5, TimeUnit.SECONDS)
                                 .exceptionally(ex -> {
                                     log.error("Notification failed for transaction {}, due to: {}",
                                               event.getData().getTrackingCode(),
                                               ex.getMessage());
                                     return null;
                                 });
            }
        });
    }

    private BankAccount getBankAccount(String accountNumber) {
        return accountRepository.findByAccountNumberForUpdate(accountNumber)
                                .orElseThrow(() -> new BankAccountException(ACCOUNT_NOT_FOUND, accountNumber));
    }

    @PreDestroy
    public void shutdownExecutors() {
        notificationExecutor.shutdown();
        try {
            if (!notificationExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                notificationExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            notificationExecutor.shutdownNow();
        }
    }
}

