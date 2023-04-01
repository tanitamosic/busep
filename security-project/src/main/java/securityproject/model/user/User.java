package securityproject.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy=SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=STRING)
@Table(name = "users")
public abstract class User {

    @Id
    @SequenceGenerator(name = "userIdSeqGen", sequenceName = "userId", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdSeqGen")
    @Column(name = "user_id")
    private Long id;
    @Column(name = "name", nullable = true)
    private String name;
    @Column(name = "surname", nullable = true)
    private String surname;
    @Column(name = "password", nullable = true)
    private String password;
    @Column(name = "email", nullable = true)
    private String email;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    // ADMIN USER DELETION => SET LOCKED TO 'TRUE'
    @Column(name = "locked", nullable = true)
    private Boolean locked;
    // DEFAULT TO FALSE => SET TO 'TRUE' ON ACCOUNT ACTIVATION
    @Column(name = "enabled", nullable = true)
    private Boolean enabled;
    @Column(name = "last_password_reset_date", nullable = true)
    private Date lastPasswordResetDate;
}
