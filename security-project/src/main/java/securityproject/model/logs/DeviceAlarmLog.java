package securityproject.model.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import securityproject.model.enums.AlarmSeverity;
import securityproject.model.enums.DeviceType;
import securityproject.model.alarms.DeviceAlarm;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAlarmLog {

    @Id
    private String id;
    private LocalDateTime timestamp;
    private Long deviceId;
    private DeviceType deviceType;
    private String message;
    private AlarmSeverity severity;
    private Long houseId;

    public DeviceAlarmLog(String message, LocalDateTime timestamp, Long deviceId, AlarmSeverity alarmSeverity) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setTimestamp(timestamp);
        this.setDeviceId(deviceId);
        this.setSeverity(alarmSeverity);
        this.setMessage(message);
        this.setDeviceType(deviceType);
    }

    public DeviceAlarmLog(DeviceAlarm alarm) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setTimestamp(alarm.getTimestamp());
        this.setDeviceId(alarm.getDeviceId());
        this.setSeverity(alarm.getSeverity());
        this.setMessage(alarm.getMessage());
        this.setDeviceType(alarm.getDeviceType());
        this.setHouseId(alarm.getHouseId());
    }
}