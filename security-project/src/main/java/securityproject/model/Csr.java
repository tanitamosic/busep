package securityproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import securityproject.model.enums.RequestStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="csr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "valid=true")
public class Csr {
    @Id
    @Column(name="csr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="given_name", nullable = false)
    private String givenName;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "organization", nullable = false)
    private String organization;
    @Column(name = "organization_unit", nullable = false)
    private String organizationUnit;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    @Column(name = "valid", nullable = false)
    private Boolean valid;
    @Column(name = "status", nullable = false)
    private RequestStatus status;
    @Column(name="is_owner", nullable = false)
    private Boolean isOwner;
}
