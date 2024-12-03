package ir.azkivaam.banking_system.exceptions;

import ir.azkivaam.banking_system.domain.enums.ErrorCode;
import lombok.Getter;

@Getter
public class AccountCreationException extends BankAccountException {
    public AccountCreationException(ErrorCode errorCode, Exception cause, String... arguments) {
        super(errorCode, cause, arguments);
    }
}
