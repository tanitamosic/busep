package securityproject.model.alarms;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import securityproject.model.enums.AlarmSeverity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Document
public class DeviceAlarm {

    @Id
    private String id;
    private Long deviceId;
    private Long houseId;
    private String message;
    private AlarmSeverity severity;

    private LocalDateTime timestamp;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AlarmSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlarmSeverity severity) {
        this.severity = severity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public DeviceAlarm(Long deviceId, Long houseId, String message, LocalDateTime timestamp, AlarmSeverity severity) {
        String format = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.deviceId = deviceId;
        this.houseId = houseId;
        this.message = message;
        this.severity = severity;
        this.timestamp = timestamp;
    }
}
