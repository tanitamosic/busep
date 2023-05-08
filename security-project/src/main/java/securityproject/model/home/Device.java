package securityproject.model.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy=SINGLE_TABLE)
@Table(name = "devices")
public class Device {

    @Id
    @SequenceGenerator(name = "deviceIdSeqGen", sequenceName = "deviceId", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deviceIdSeqGen")
    @Column(name = "device_id")
    private Long id;

    @Column(name="name")
    private String name;
}
