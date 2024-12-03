package ir.azkivaam.banking_system.service;

import ir.azkivaam.banking_system.domain.dto.DepositRequest;
import ir.azkivaam.banking_system.domain.dto.TransactionDto;
import ir.azkivaam.banking_system.domain.dto.TransferRequest;
import ir.azkivaam.banking_system.domain.dto.WithdrawRequest;

public interface TransactionService {

    TransactionDto deposit(DepositRequest request);

    TransactionDto withdraw(WithdrawRequest request);

    TransactionDto transfer(TransferRequest request);
}
