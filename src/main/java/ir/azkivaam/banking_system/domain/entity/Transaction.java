package ir.azkivaam.banking_system.domain.entity;

import ir.azkivaam.banking_system.domain.enums.TransactionStatus;
import ir.azkivaam.banking_system.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackingCode;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(updatable = false, length = 10)
    private String sourceAccountNumber;

    @Column(updatable = false, length = 10)
    private String destinationAccountNumber;

    @CreatedDate
    private Date date;
}