package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByFirstNameIgnoreCase(String name);

    boolean existsByFirstName(String firstName);

    boolean existsBySurname(String surname);

    void findByFirstNameOrSurname(String firstName, String Surname);

}