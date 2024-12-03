package ir.azkivaam.banking_system.domain.enums;

/*
 * @author masoome.aghayari
 * @since 12/1/24
 */

import lombok.Getter;

@Getter
public enum ErrorCode {
    SERVICE_ERROR("error.service"),
    ACCOUNT_CREATION_FAILED("error.account.creation.failed"),
    ACCOUNT_NOT_FOUND("error.account.not.found"),
    BRANCH_NOT_FOUND("error.branch.not.found"),
    INSUFFICIENT_BALANCE("error.insufficient.balance"),
    TRANSACTION_FAILED("error.transaction.failed");

    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }
}
