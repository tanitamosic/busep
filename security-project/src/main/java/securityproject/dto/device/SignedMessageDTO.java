package securityproject.dto.device;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SignedMessageDTO extends MessageToVerify {
    public String signature;
}
