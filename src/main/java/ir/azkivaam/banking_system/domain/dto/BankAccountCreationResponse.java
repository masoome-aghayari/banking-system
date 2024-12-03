package ir.azkivaam.banking_system.domain.dto;

/*
 * @author masoome.aghayari
 * @since 12/2/24
 */

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class BankAccountCreationResponse {
    private String maskedAccountNumber;
    private String accountHolderName;
    private String accountOpenerBranch;
    private Long accountBalance;
    private Date createdDate;

    @Override
    public String toString() {
        return '{' +
               "account number: " + maskedAccountNumber +
               ", account holder name : " + accountHolderName +
               ", account opener branch : " + accountOpenerBranch +
               ", account balance : " + accountBalance +
               ", created date : " + createdDate +
               '}';
    }
}
