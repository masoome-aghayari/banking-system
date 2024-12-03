package ir.azkivaam.banking_system.domain.dto;

import ir.azkivaam.banking_system.domain.annotation.AmountRange;
import ir.azkivaam.banking_system.domain.entity.BankAccount;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Date;

/**
 * DTO for {@link BankAccount}
 */
@Getter
@Setter
@Builder
public class BankAccountDto {

    private String accountNumber;

    @Valid
    private PersonDto person;
    @Valid
    private BranchDto branch;

    @AmountRange(min = 100_000, max = 100_000_000, message = "Amount must be between 100,000 and 100,000,000 RIALS")
    private Long balance;

    private Date createdDate;

    @JsonIgnore
    public String getMaskedAccountNumber() {
        if (accountNumber == null || accountNumber.length() < 10) {
            throw new IllegalArgumentException("Account number must be exactly 10 characters long.");
        }
        String start = accountNumber.substring(0, 2);
        String end = accountNumber.substring(8);
        String maskedPart = "*".repeat(6);
        return start + maskedPart + end;
    }

    @JsonIgnore
    public String getAccountInfo() {
        return '{' +
               "account number: " + getMaskedAccountNumber() +
               ", account holder name: " + person.getSureName() + " " + person.getLastname() +
               ", account opener branch: " + branch.getName() + "-" + branch.getCode() +
               ", account balance: " + balance +
               ", created date: " + createdDate +
               '}';
    }
}