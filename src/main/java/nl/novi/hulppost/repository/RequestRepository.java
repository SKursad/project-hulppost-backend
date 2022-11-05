package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    boolean existsByTitle (String title);

    Optional<Request> findById (Long requestId);

    List<Request> findByUserId(Long user);

}
