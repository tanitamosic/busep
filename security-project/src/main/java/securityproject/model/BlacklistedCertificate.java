package securityproject.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name="blacklist")
public class BlacklistedCertificate {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Invalid email")
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @NotNull(message = "Blacklist date can't be null")
    @Column(name = "blacklist_date", nullable = false)
    private Date blacklist_date; // date cert was blacklisted
    @NotBlank(message = "Reason can't be null")
    @Column(name = "reason", nullable = false)
    private String reason;
}
