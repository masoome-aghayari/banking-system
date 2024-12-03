package ir.azkivaam.banking_system.exceptions;

import ir.azkivaam.banking_system.domain.enums.ErrorCode;
import lombok.Getter;

@Getter
public class TransactionException extends BankSystemException {

    public TransactionException(ErrorCode errorCode, String... arguments) {
        super(errorCode, arguments);
    }

    public TransactionException(ErrorCode errorCode, Exception cause, String... arguments) {
        super(errorCode, cause, arguments);
    }
}
