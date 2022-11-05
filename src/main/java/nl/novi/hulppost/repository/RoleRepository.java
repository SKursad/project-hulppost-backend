package nl.novi.hulppost.repository;

import nl.novi.hulppost.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository <Role, Long> {

    Role findByName(String name);
}
