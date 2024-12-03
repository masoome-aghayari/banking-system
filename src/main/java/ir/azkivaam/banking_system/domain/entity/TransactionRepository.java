package ir.azkivaam.banking_system.domain.entity;

import ir.azkivaam.banking_system.domain.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Transaction t SET t.status = :status WHERE t.id = :id")
    void updateStatusById(Long id, TransactionStatus status);
}