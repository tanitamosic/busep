package securityproject.logger.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLog {

    @Id
    private String id;
    private String timestamp;
    private String deviceId;
    private String ipAddress;
    private String requestURL;
    private String requestMethod;
    private Map<String, String[]> requestParameters;
    private String message;
    private LogType logType;

    public DeviceLog(HttpServletRequest request, LogType type, String message) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setIpAddress(request.getRemoteAddr());
        this.setTimestamp(now);
        this.setRequestMethod(request.getMethod());
        this.setRequestURL(request.getRequestURL().toString());
        this.setRequestParameters(request.getParameterMap());
        this.setLogType(type);
        this.setMessage(message);

    }
}
