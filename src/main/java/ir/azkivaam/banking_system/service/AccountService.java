package ir.azkivaam.banking_system.service;

import ir.azkivaam.banking_system.domain.dto.BankAccountCreationResponse;
import ir.azkivaam.banking_system.domain.dto.BankAccountDto;

public interface AccountService {


    BankAccountCreationResponse createAccount(BankAccountDto accountCreationRequest);

    Long getBalance(String accountNumber);
}
