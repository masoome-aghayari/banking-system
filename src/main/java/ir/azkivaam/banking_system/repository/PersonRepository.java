package ir.azkivaam.banking_system.repository;

import ir.azkivaam.banking_system.domain.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByNationalId(String nationalId);

    Person findByNationalId(String nationalId);
}