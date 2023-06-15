package securityproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import securityproject.logger.logs.LogType;

public class DeviceDTO {
    public Integer deviceId;
    // TODO: DELETE NAME
    public String name = "";
    public String message;
    public LogType logType;
    public String deviceType; // TODO: CHANGE TO ENUM

    private String signature;

    @JsonIgnore
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
