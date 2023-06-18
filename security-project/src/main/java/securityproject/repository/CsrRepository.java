package securityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import securityproject.model.Csr;
import securityproject.model.enums.RequestStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CsrRepository extends JpaRepository<Csr, Long> {

    Optional<Csr> findByEmail(@Param("email") String email);
    @Query(nativeQuery = true, value="SELECT * FROM csr WHERE csr.valid=?1")
    List<Csr> findAllByValid(Boolean valid);

    @Query(nativeQuery = true, value="SELECT * FROM csr WHERE csr.email=?1")
    Csr getByEmail( String email);

    @Query(nativeQuery=true, value="UPDATE csr SET status=?1 WHERE csr_id=?2")
    @Modifying
    @Transactional
    void updateStatus(int status, Long id);

    @Query(nativeQuery=true, value="UPDATE csr SET valid=?1 WHERE csr_id=?2")
    @Modifying
    @Transactional
    void updateValidity(boolean b, Long id);
}
