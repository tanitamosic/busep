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
@Table(name = "devices_messages")
public class DeviceMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "message_name")
    private Long messageName;

    @Column(name = "value")
    private String value;

    @Column(name = "is_alarm")
    private Boolean isAlarm;

}
