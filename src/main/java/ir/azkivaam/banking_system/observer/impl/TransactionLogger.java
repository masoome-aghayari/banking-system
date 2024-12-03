package ir.azkivaam.banking_system.observer.impl;

import ir.azkivaam.banking_system.config.LocaleConfig;
import ir.azkivaam.banking_system.domain.dto.Event;
import ir.azkivaam.banking_system.domain.dto.TransactionDto;
import ir.azkivaam.banking_system.domain.entity.Transaction;
import ir.azkivaam.banking_system.domain.entity.TransactionRepository;
import ir.azkivaam.banking_system.domain.enums.TransactionStatus;
import ir.azkivaam.banking_system.mapper.TransactionMapper;
import ir.azkivaam.banking_system.observer.TransactionObserver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionLogger implements TransactionObserver {
    private static final Logger logger = LoggerFactory.getLogger("transactionLogger");

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final MessageSource messageSource;

    @Override
    public void update(Event<TransactionDto> event) {
        TransactionDto transactionDto = event.getData();
        switch (transactionDto.getStatus()) {
            case SUCCESS -> onTransactionSuccess(transactionDto);
            case FAILED -> onTransactionFailure(event);
            case INITIALIZED -> onTransactionInitialization(transactionDto);
        }
    }

    private void onTransactionInitialization(TransactionDto transactionDto) {
        saveTransaction(transactionDto);
        logger.info(getLogMessage("transaction.initialization", transactionDto.getTrackingCode()));
    }

    private void onTransactionSuccess(TransactionDto transactionDto) {
        updateTransactionStatus(transactionDto.getId(), TransactionStatus.SUCCESS);
        logger.info(getLogMessage("transaction.success", transactionDto.getTrackingCode()));
    }

    private void updateTransactionStatus(Long transactionId, TransactionStatus newStatus) {
        transactionRepository.updateStatusById(transactionId, newStatus);
    }

    private void onTransactionFailure(Event<TransactionDto> event) {
        TransactionDto transactionDto = event.getData();
        transactionDto.setStatus(TransactionStatus.FAILED);
        updateTransactionStatus(transactionDto.getId(), TransactionStatus.FAILED);
        logger.info(getLogMessage("error.transaction.failure", transactionDto.getTrackingCode(), event.getMessage()));
    }

    private void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transaction = transactionRepository.save(transaction);
        transactionDto.setId(transaction.getId());
    }

    private String getLogMessage(String code, String... params) {
        return messageSource.getMessage(code, params, LocaleConfig.LOCALE);
    }
}
