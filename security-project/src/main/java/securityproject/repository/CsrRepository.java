package securityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.Csr;

import java.util.Optional;

@Repository
public interface CsrRepository extends JpaRepository<Csr, Long> {

    Optional<Csr> findByEmail(String email);
}
