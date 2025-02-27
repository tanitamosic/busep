package securityproject.model.logs;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import securityproject.model.enums.LogType;
import securityproject.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;


@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestLog {

    @Id
    private String id;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String requestURL;
    private String requestMethod;
    private Map<String, String[]> requestParameters;
    private String user = "not logged in";
    private LogType logType;

    public UserRequestLog(HttpServletRequest request, TokenUtils tokenUtils, LogType type) {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setIpAddress(request.getRemoteAddr());
        this.setTimestamp(LocalDateTime.now());
        this.setRequestMethod(request.getMethod());
        this.setRequestURL(request.getRequestURL().toString());
        this.setRequestParameters(request.getParameterMap());
        String token = tokenUtils.getToken(request);
        if (null != token) {
            String username = tokenUtils.getUsernameFromToken(token);
            this.setUser(username);
        }
        this.setLogType(type);
    }
}

