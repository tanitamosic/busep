package securityproject.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @Column(name="role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="name", unique = true, nullable = false)
    String name;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString(){ return this.name;}
}
