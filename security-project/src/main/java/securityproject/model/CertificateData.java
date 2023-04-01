package securityproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import securityproject.dto.CertificateDto;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "certificates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "valid=true")
public class CertificateData {
    @Id
    @Column(name="cert_id")
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
    @Column(name = "serial_number", unique = true, nullable = false)
    private String serialNumber;
    @Column(name = "public_key", unique = true, nullable = false)
    private String publicKey;

    @Column(name = "issuer")
    private String issuer; // issuer alias TODO: sta treba biti identifikator issueru?

    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    @Column(name = "valid", nullable = false)
    private Boolean valid;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Extension> extensions;


    public CertificateData(CertificateDto dto, Date endDate, String serialNumber) {
        this.givenName = dto.givenName;
        this.surname = dto.surname;
        this.organization = dto.organization;
        this.organizationUnit = dto.orgUnit;
        this.country = dto.country;
        this.email = dto.email;
        this.startDate = dto.startDate;
        this.endDate = endDate;
        this.serialNumber = serialNumber;
        this.valid = true;

        this.extensions = dto.extensions;
    }
}
