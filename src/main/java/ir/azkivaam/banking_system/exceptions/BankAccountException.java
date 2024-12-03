package ir.azkivaam.banking_system.exceptions;

import ir.azkivaam.banking_system.domain.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BankAccountException extends BankSystemException {
    public BankAccountException(ErrorCode errorCode, String... arguments) {
        super(errorCode, arguments);
    }

    public BankAccountException(ErrorCode errorCode, Exception cause, String... arguments) {
        super(errorCode, cause, arguments);
    }
}
