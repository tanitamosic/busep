package securityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.BlacklistedCertificate;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistedCertificate, Long> {
}
