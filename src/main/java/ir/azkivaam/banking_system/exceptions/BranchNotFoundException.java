package ir.azkivaam.banking_system.exceptions;

/*
 * @author masoome.aghayari
 * @since 12/1/24
 */

import ir.azkivaam.banking_system.domain.enums.ErrorCode;

public class BranchNotFoundException extends BankSystemException {

    public BranchNotFoundException(ErrorCode errorCode, String... arguments) {
        super(errorCode, arguments);
    }

    public BranchNotFoundException(ErrorCode errorCode, Exception cause, String... arguments) {
        super(errorCode, cause, arguments);
    }
}
