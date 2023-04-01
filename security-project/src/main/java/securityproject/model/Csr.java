package securityproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import securityproject.model.enums.RequestStatus;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="csr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Csr {
    @Id
    @Column(name="csr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="given_name")
    private String givenName;
    @Column(name = "surname")
    private String surname;
    @Column(name = "organization")
    private String organization;
    @Column(name = "organization_unit")
    private String organizationUnit;
    @Column(name = "country")
    private String country;
    @Column(name = "email")
    private String email;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "valid")
    private Boolean valid;
    @Column(name = "status")
    private RequestStatus status;
    @Column(name="is_owner")
    private Boolean isOwner;
}
