package securityproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import securityproject.model.enums.RequestStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "Name can't be blank")
    @Column(name="given_name", nullable = false)
    private String givenName;
    @NotBlank(message = "Surname can't be blank")
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
    @NotNull(message = "Start date field can't be null.")
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @NotNull(message = "End date field can't be null.")
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    @NotNull(message = "\"Valid\" filed can't be null.")
    @Column(name = "valid", nullable = false)
    private Boolean valid;
    @NotNull(message = "Status can't be null.")
    @Column(name = "status", nullable = false)
    private RequestStatus status;
    @NotNull(message = "\"isOwner\" filed can't be null.")
    @Column(name="is_owner", nullable = false)
    private Boolean isOwner;
}
