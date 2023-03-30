package securityproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public abstract class User {

    @Id
    @Column(name = "user_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "password")
    private String password;
    @Column(name = "username")
    private String username;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    // ADMIN USER DELETION => SET LOCKED TO 'TRUE'
    @Column(name = "locked")
    private Boolean locked;
    // DEFAULT TO FALSE => SET TO 'TRUE' ON ACCOUNT ACTIVATION
    @Column(name = "enabled")
    private Boolean enabled;
    @Column(name = "last_password_reset_date")
    private Date lastPasswordResetDate;
}
