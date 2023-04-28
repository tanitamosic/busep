package securityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import securityproject.model.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(@Param("name") String roleName);
}
