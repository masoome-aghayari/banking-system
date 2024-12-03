package ir.azkivaam.banking_system.service.impl;

import ir.azkivaam.banking_system.domain.dto.BankAccountCreationResponse;
import ir.azkivaam.banking_system.domain.dto.BankAccountDto;
import ir.azkivaam.banking_system.domain.dto.Event;
import ir.azkivaam.banking_system.domain.entity.BankAccount;
import ir.azkivaam.banking_system.domain.enums.ErrorCode;
import ir.azkivaam.banking_system.domain.enums.EventType;
import ir.azkivaam.banking_system.exceptions.AccountCreationException;
import ir.azkivaam.banking_system.exceptions.BankAccountException;
import ir.azkivaam.banking_system.exceptions.BranchNotFoundException;
import ir.azkivaam.banking_system.mapper.BankAccountMapper;
import ir.azkivaam.banking_system.observer.impl.BankAccountObserverManager;
import ir.azkivaam.banking_system.repository.BankAccountRepository;
import ir.azkivaam.banking_system.repository.BranchRepository;
import ir.azkivaam.banking_system.repository.PersonRepository;
import ir.azkivaam.banking_system.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static ir.azkivaam.banking_system.domain.enums.ErrorCode.ACCOUNT_CREATION_FAILED;
import static ir.azkivaam.banking_system.domain.enums.ErrorCode.BRANCH_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final PersonRepository personRepository;
    private final BranchRepository branchRepository;
    private final BankAccountObserverManager observerManager;
    private final BankAccountMapper bankAccountMapper;
    private final ExecutorService notificationExecutor;
    private final BankAccountRepository accountRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BankAccountCreationResponse createAccount(BankAccountDto bankAccountDto) {
        BankAccount bankAccount = bankAccountMapper.toEntity(bankAccountDto);
        String branchCode = bankAccount.getBranch().getCode();
        if (branchRepository.existsByCode(branchCode)) {
            bankAccount.setBranch(branchRepository.findByCode(branchCode));
            String nationalId = bankAccountDto.getPerson().getNationalId();
            if (personRepository.existsByNationalId(nationalId)) {
                bankAccount.setPerson(personRepository.findByNationalId(nationalId));
            }
            bankAccount = saveBankAccount(bankAccount);
            bankAccountDto = bankAccountMapper.toDto(bankAccount);
            notifyObserversOnAccountCreationSuccess(bankAccountDto);
            return bankAccountMapper.toResponse(bankAccountDto);
        } else {
            ErrorCode errorCode = BRANCH_NOT_FOUND;
            notifyObserversOnAccountCreationFailure(errorCode.getValue(), branchCode);
            throw new AccountCreationException(ACCOUNT_CREATION_FAILED,
                                               new BranchNotFoundException(errorCode, branchCode));
        }
    }

    private BankAccount saveBankAccount(BankAccount bankAccount) {
        synchronized (accountRepository) {
            try {
                bankAccount = accountRepository.save(bankAccount);
            } catch (Exception exception) {
                notifyObserversOnAccountCreationFailure(ACCOUNT_CREATION_FAILED.getValue(),
                                                        exception.getLocalizedMessage());
                throw new AccountCreationException(ACCOUNT_CREATION_FAILED, exception);
            }
        }
        return bankAccount;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Long getBalance(String accountNumber) {
        Long balance = accountRepository.findBalanceByAccountNumber(accountNumber);
        if (balance == null) {
            throw new BankAccountException(ErrorCode.ACCOUNT_NOT_FOUND, accountNumber);
        }
        return balance;
    }

    private void notifyObserversOnAccountCreationSuccess(BankAccountDto bankAccount) {
        Event<BankAccountDto> event = new Event<>(bankAccount, EventType.ACCOUNT_CREATION);
        CompletableFuture.runAsync(() -> observerManager.notifyObservers(event), notificationExecutor)
                         .orTimeout(5, TimeUnit.SECONDS)
                         .exceptionally(ex -> {
                             log.error("Notification failed for accountCreation success(accountNumber: {}), due to: {}",
                                       event.getData().getMaskedAccountNumber(),
                                       ex.toString());
                             return null;
                         });
    }

    private void notifyObserversOnAccountCreationFailure(String reasonCode, String... arguments) {
        Event<BankAccountDto> event = new Event<>(null, reasonCode, EventType.ACCOUNT_CREATION, arguments);
        CompletableFuture.runAsync(() -> observerManager.notifyObservers(event), notificationExecutor)
                         .orTimeout(5, TimeUnit.SECONDS).exceptionally(ex -> {
                             log.error("Notification failed for accountCreation, due to: {}",
                                       ex.toString());
                             return null;
                         });
    }
}