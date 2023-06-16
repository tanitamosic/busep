package securityproject.model.alarms;

import securityproject.model.enums.AlarmSeverity;
import securityproject.model.enums.RequestType;

import java.time.LocalDateTime;

public class RequestAlarm {
    private LocalDateTime timestamp;
    private String source;
    private RequestType requestType;
    private String message;
    private AlarmSeverity severity;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
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

    public RequestAlarm(LocalDateTime timestamp, String source, RequestType requestType, AlarmSeverity severity) {
        this.timestamp = timestamp;
        this.source = source;
        this.requestType = requestType;
        this.severity = severity;
    }
}
