package securityproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="extensions")
public class Extension {

    @Id
    @Column(name="extension_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Extension name can't be blank")
    private String name;
    @NotNull(message = "\"Critical\" field can't be null")
    private Boolean critical;
    @NotBlank(message = "Extension value can't be blank")
    private String value;
}
