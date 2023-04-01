package securityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.CertificateData;

@Repository
public interface CertificateRepository extends JpaRepository<CertificateData, Long> {
}
