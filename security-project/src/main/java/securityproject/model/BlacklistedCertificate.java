package securityproject.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="blacklist")
public class BlacklistedCertificate {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "blacklist_date", nullable = false)
    private Date blacklist_date; // date cert was blacklisted
}
