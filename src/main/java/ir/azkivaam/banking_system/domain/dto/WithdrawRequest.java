package ir.azkivaam.banking_system.domain.dto;

/*
 * @author masoome.aghayari
 * @since 11/29/24
 */

import ir.azkivaam.banking_system.domain.annotation.AmountRange;
import ir.azkivaam.banking_system.domain.annotation.Digital;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class WithdrawRequest {

    @Length(min = 10, max = 10)
    @Digital(message = "accountNumber must be a fully 10 digits string")
    private String accountNumber;

    @AmountRange(min = 100_000, max = 100_000_000, message = "Amount must be between 100,000 and 100,000,000 RIALS")
    private Long amount;
}
