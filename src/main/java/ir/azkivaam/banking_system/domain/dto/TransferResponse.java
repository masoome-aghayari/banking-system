package ir.azkivaam.banking_system.domain.dto;

/*
 * @author masoome.aghayari
 * @since 11/29/24
 */

import ir.azkivaam.banking_system.domain.enums.TransactionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TransferResponse {
    private String trackingNumber;
    private String message;
    private TransactionStatus status;
}
