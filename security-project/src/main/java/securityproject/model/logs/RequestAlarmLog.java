package securityproject.model.logs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import securityproject.model.alarms.RequestAlarm;
import securityproject.model.enums.AlarmSeverity;
import securityproject.model.enums.RequestType;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAlarmLog {

    @Id
    private String id;
    private LocalDateTime timestamp;
    private String source;
    private RequestType requestType;
    private String message;
    private AlarmSeverity severity;

    public RequestAlarmLog(String message, LocalDateTime timestamp, RequestType requestType, String source, AlarmSeverity alarmSeverity) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setTimestamp(timestamp);
        this.setSeverity(alarmSeverity);
        this.setMessage(message);
        this.setRequestType(requestType);
        this.setSource(source);
    }

    public RequestAlarmLog(RequestAlarm alarm) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setTimestamp(alarm.getTimestamp());
        this.setSeverity(severity);
        this.setMessage(message);

    }

}