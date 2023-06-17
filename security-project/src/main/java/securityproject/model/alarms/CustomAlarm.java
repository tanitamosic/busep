package securityproject.model.alarms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import securityproject.model.enums.DeviceType;
import securityproject.model.enums.LogType;

import javax.persistence.*;

import static javax.persistence.InheritanceType.SINGLE_TABLE;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "custom_alarms")
public class CustomAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "device_type")
    private DeviceType deviceType;
    @Column(name = "log_type")
    private LogType logType;
    @Column(name = "frequency")
    private Integer frequency;
    @Column(name = "time_window")
    private Integer window;

    public CustomAlarm(DeviceType deviceType, LogType logType, Integer frequency, Integer window) {
        this.deviceType = deviceType;
        this.logType = logType;
        this.frequency = frequency;
        this.window = window;
    }


    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getWindow() {
        return window;
    }

    public void setWindow(Integer window) {
        this.window = window;
    }
}
