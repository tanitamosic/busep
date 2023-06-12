package securityproject.model.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import securityproject.dto.DeviceDTO;
import securityproject.model.enums.DeviceType;

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

    public Device(DeviceDTO dto){
        this.houseId = dto.houseId;
        this.type = dto.type;
        this.readTime = dto.readTime;
        this.filterRegex = dto.filterRegex;
        this.name = dto.name;
        this.isActive = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long id;

    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "type")
    private DeviceType type;

    @Column(name = "read_time")
    private Integer readTime;

    @Column(name = "filterRegex")
    private String filterRegex;

    @Column(name = "name")
    private String name;

    @Column(name="is_active")
    private Boolean isActive;
}
