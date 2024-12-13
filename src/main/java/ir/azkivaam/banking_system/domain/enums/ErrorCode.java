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
    TRANSACTION_FAILED("error.transaction.failed"),
    VALIDATION("error.validation"),
    GENERAL("error.general"),
    TYPE_MISMATCH("error.type.mismatch"),
    UN_AUTHORIZED("error.unauthorized"),
    FORBIDDEN("error.forbidden"),
    NOT_FOUND("error.resource.not.found"),
    METHOD_NOT_ALLOWED("error.method.not.allowed"),
    MEDIA_NOT_SUPPORTED("error.not.supported.media.type"),
    WRONG_MEDIA_TYPE("error.wrong.media.type");
    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }
}
