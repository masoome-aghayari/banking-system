package ir.azkivaam.banking_system.domain.dto;

import ir.azkivaam.banking_system.domain.entity.Transaction;
import ir.azkivaam.banking_system.domain.enums.TransactionStatus;
import ir.azkivaam.banking_system.domain.enums.TransactionType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link Transaction}
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto implements Serializable {
    private Long id;
    private Date date;
    private Long amount;
    private String trackingCode;
    private TransactionType type;
    private TransactionStatus status;
    private String sourceAccountNumber;
    private String destinationAccountNumber;

}