package ir.azkivaam.banking_system.domain.dto;

/*
 * @author masoome.aghayari
 * @since 11/29/24
 */

import ir.azkivaam.banking_system.domain.annotation.AmountRange;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {
    @NotBlank
    private String sourceAccountNumber;

    @NotBlank
    private String destinationAccountNumber;

    @AmountRange(min = 100_000, max = 100_000_000, message = "Amount must be between 100,000 and 100,000,000 RIALS")
    private Long amount;
}
