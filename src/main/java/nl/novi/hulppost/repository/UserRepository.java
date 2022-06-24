package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query("select e from User e where e.username = ?1 and e.email = ?2")
    User findByJPQL(String username, String email);

    @Query("select e from User e where e.username =:username and e.email =:email")
    User findByJPQLNamedParams(@Param("username") String username, @Param("email") String email);

}
