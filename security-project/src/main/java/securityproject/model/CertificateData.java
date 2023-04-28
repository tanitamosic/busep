package securityproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import securityproject.dto.CertificateDto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "Name can't be blank.")
    @Column(name="given_name", nullable = false)
    private String givenName;
    @NotBlank(message = "Surname can't be blank.")
    @Column(name = "surname", nullable = false)
    private String surname;
    @NotBlank(message = "Organization can't be blank.")
    @Column(name = "organization", nullable = false)
    private String organization;
    @NotBlank(message = "Organization unit can't be blank.")
    @Column(name = "organization_unit", nullable = false)
    private String organizationUnit;
    @NotBlank(message = "Country can't be blank.")
    @Column(name = "country", nullable = false)
    private String country;
    @Email(message = "Invalid email")
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @NotBlank(message = "Serial number can't be blank.")
    @Column(name = "serial_number", unique = true, nullable = false)
    private String serialNumber;
    @NotBlank(message = "Public key can't be blank.")
    @JsonIgnore
    @Column(name = "public_key", unique = true, nullable = false)
    private String publicKey;

    @Column(name = "issuer")
    private String issuer; // issuer alias TODO: sta treba biti identifikator issueru?
    @NotNull(message = "Start date field can't be null.")
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @NotNull(message = "End date field can't be null.")
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    @NotNull(message = "\"Valid\" filed can't be null.")
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
