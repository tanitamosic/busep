package securityproject.model.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import securityproject.model.enums.DeviceType;
import securityproject.model.enums.LogType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Role(Role.Type.EVENT)
@Expires("10m")
public class DeviceLog {

    @Id
    private String id;
    private LocalDateTime timestamp;
    private Long deviceId;
    private DeviceType deviceType;
    private String ipAddress;
    private String message;
    private LogType logType;

    public DeviceLog(HttpServletRequest request, LogType type, String message, String timestamp, Long deviceId, DeviceType deviceType) {
        String format = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setIpAddress(request.getRemoteAddr());
        this.setTimestamp(LocalDateTime.parse(timestamp, formatter));
        this.setDeviceId(deviceId);
        this.setLogType(type);
        this.setMessage(message);
        this.setDeviceType(deviceType);

    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }
}
