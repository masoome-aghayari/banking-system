package ir.azkivaam.banking_system.repository;

import ir.azkivaam.banking_system.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * @author masoome.aghayari
 * @since 11/30/24
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    boolean existsByCode(String code);

    Branch findByCode(String code);
}
