package securityproject.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue("StandardUser")
public class StandardUser extends User{
}
