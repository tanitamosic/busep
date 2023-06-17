package securityproject.model.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import securityproject.model.enums.LogType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedIpLog {

    @Id
    private String id;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String message = "IP address is blacklisted";
    private LogType logType = LogType.ERROR;

    public BlacklistedIpLog(String ip) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setTimestamp(LocalDateTime.now());
        this.setIpAddress(ip);

    }
}
