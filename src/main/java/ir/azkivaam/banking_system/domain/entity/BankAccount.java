package ir.azkivaam.banking_system.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "BANK_ACCOUNT",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_person_branch_constraint",
                columnNames = {"PERSON_NATIONAL_ID", "BRANCH_CODE"}
        ))
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "bankAccountCache")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "accountNumber cannot be blank")
    @Column(length = 10, unique = true)
    @Size(min = 10, max = 10, message = "accountNumber must contain exactly 10 digits")
    private String accountNumber;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PERSON_NATIONAL_ID", referencedColumnName = "nationalId")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "BRANCH_CODE", referencedColumnName = "code")
    private Branch branch;

    private Long balance;

    @CreatedDate
    private Date createdDate;

    @Version
    private Integer version;
}
