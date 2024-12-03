package ir.azkivaam.banking_system.repository;

import ir.azkivaam.banking_system.domain.entity.BankAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = :accountNumber")
    Optional<BankAccount> findByAccountNumberForUpdate(String accountNumber);

    @Query("SELECT b.balance FROM BankAccount b WHERE b.accountNumber = :accountNumber")
    Long findBalanceByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);


}
