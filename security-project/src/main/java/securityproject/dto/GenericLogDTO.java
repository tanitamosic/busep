package securityproject.dto;

import securityproject.model.enums.AlarmSeverity;
import securityproject.model.enums.DeviceType;
import securityproject.model.enums.LogType;
import securityproject.model.enums.RequestType;
import securityproject.model.logs.DeviceAlarmLog;
import securityproject.model.logs.DeviceLog;
import securityproject.model.logs.RequestAlarmLog;

import java.time.LocalDateTime;

public class GenericLogDTO {

    private String id;
    private LocalDateTime timestamp;
    private String source;
    private RequestType requestType;
    private String message;
    private AlarmSeverity severity;
    private Long deviceId;
    private DeviceType deviceType;
    private String ipAddress;
    private LogType logType;
    private Long houseId;
    
    public GenericLogDTO(RequestAlarmLog ral){
        this.setId(ral.getId());
        this.setTimestamp(ral.getTimestamp());
        this.setSeverity(ral.getSeverity());
        this.setMessage(ral.getMessage());
        this.setRequestType(ral.getRequestType());
        this.setSource(ral.getSource());

    }
    
    public GenericLogDTO(DeviceAlarmLog dal){
        this.setId(dal.getId());
        this.setTimestamp(dal.getTimestamp());
        this.setDeviceId(dal.getDeviceId());
        this.setSeverity(dal.getSeverity());
        this.setMessage(dal.getMessage());
        this.setDeviceType(dal.getDeviceType());
    }
    
    public GenericLogDTO(DeviceLog dl){
        this.setId(dl.getId());
        this.setIpAddress(dl.getIpAddress());
        this.setTimestamp(dl.getTimestamp());
        this.setDeviceId(dl.getDeviceId());
        this.setLogType(dl.getLogType());
        this.setMessage(dl.getMessage());
        this.setDeviceType(dl.getDeviceType());
        this.setHouseId(dl.getHouseId());
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

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }
}
