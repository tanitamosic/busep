package securityproject.repository;

import org.springframework.data.jpa.repository.Query;
import securityproject.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(@Param("email") String username);

    @Query(nativeQuery = true, value="SELECT users.enabled FROM users WHERE users.email=?1")
    Boolean isEmailConfirmed(String email);
}
