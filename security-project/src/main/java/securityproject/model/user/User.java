package securityproject.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonIgnore
    private Long id;
    @NotBlank(message = "Name can't be blank")
    @Column(name = "name", nullable = false)
    private String name;
    @NotBlank(message = "Surname can't be blank")
    @Column(name = "surname", nullable = false)
    private String surname;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[!@#$%^&*()_+={}|:<>,.?/])(?=.*?[0-9]).{12,}$",
            message = "Password must have at least 12 characters, 1 capital 1 lowercase letter, 1 number and 1 special character.")
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;
    @Email(message = "Invalid email")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();
    // ADMIN USER DELETION => SET LOCKED TO 'TRUE'
    @NotNull(message = "\"Locked\" field can't be null")
    @Column(name = "locked", nullable = false)
    @JsonIgnore
    private Boolean locked;
    // DEFAULT TO FALSE => SET TO 'TRUE' ON ACCOUNT ACTIVATION
    @NotNull(message = "\"Enabled\" field can't be null")
    @Column(name = "enabled", nullable = false)
    @JsonIgnore
    private Boolean enabled;
    @Column(name = "last_password_reset_date", nullable = true)
    @JsonIgnore
    private Date lastPasswordResetDate;

    @Column(name = "failed_attempt")
    @JsonIgnore
    private Integer failedAttempt;
    @Column(name = "lock_time")
    @JsonIgnore
    private Date lockTime;

    @Column(name = "act_string")
    @JsonIgnore
    private String activationString;
    @Column(name = "pin")
    @JsonIgnore
    private Integer pin;
}
