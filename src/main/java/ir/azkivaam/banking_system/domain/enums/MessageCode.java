package ir.azkivaam.banking_system.domain.enums;

/*
 * @author masoome.aghayari
 * @since 12/13/24
 */

import lombok.Getter;

@Getter
public enum MessageCode {
    ACCOUNT_CREATION("account.creation.success");

    private final String value;

    MessageCode(String value) {
        this.value = value;
    }
}
