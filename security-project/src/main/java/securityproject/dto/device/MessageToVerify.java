package securityproject.dto.device;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import securityproject.model.enums.LogType;
import securityproject.model.enums.DeviceType;

@AllArgsConstructor
@NoArgsConstructor
public class MessageToVerify {
    public Long deviceId;
    public LogType logType;
    public String message;
    public DeviceType deviceType;
    public String timestamp;

    public MessageToVerify(SignedMessageDTO dto) {
        this.deviceId = dto.deviceId;
        this.message = dto.message;
        this.timestamp = dto.timestamp;
        this.logType = dto.logType;
        this.deviceType = dto.deviceType;
    }
}
