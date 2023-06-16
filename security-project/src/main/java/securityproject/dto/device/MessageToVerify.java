package securityproject.dto.device;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import securityproject.logger.logs.LogType;

@AllArgsConstructor
@NoArgsConstructor
public class MessageToVerify {
    public Integer deviceId;
    public LogType logType;
    // TODO: DELETE NAME
//    public String name = "";
    public String message;
    public String deviceType; // TODO: CHANGE TO ENUM
    public String timestamp;

    public MessageToVerify(SignedMessageDTO dto) {
        this.deviceId = dto.deviceId;
        this.message = dto.message;
        this.timestamp = dto.timestamp;
        this.logType = dto.logType;
        this.deviceType = dto.deviceType;
    }
}
