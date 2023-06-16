package securityproject.model.home;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class AlarmEvent {
    public Integer deviceId;
    public Integer houseId;
    public String message;
    public AlarmSeverity severity;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
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

    public AlarmEvent(Integer deviceId, Integer houseId, String message, AlarmSeverity severity) {
        this.deviceId = deviceId;
        this.houseId = houseId;
        this.message = message;
        this.severity = severity;
    }
}
