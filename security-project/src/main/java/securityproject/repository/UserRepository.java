package securityproject.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import securityproject.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(@Param("email") String username);

    @Query(nativeQuery = true, value="SELECT users.enabled FROM users WHERE users.email=:email")
    Boolean isEmailConfirmed(@Param("email") String email);

    User getUserByActivationString(@Param("act_string") String activationString);
    
    @Query(nativeQuery = true, value = "UPDATE users as u SET failed_attempt=:failed_attempts WHERE u.email=:email")
    @Modifying
    @Transactional
    void updateFailedAttempts(@Param("failed_attempts")int i, @Param("email") String email);

    User getUserById(Long id);
    List<User> getUsersByIsActive(Boolean isActive);

    @Query(nativeQuery = true, value="SELECT * FROM users AS u WHERE u.type='SuperAdmin'")
    List<User> getAllAdmins();
}
