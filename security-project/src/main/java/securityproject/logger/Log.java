package securityproject.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import securityproject.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;


@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    private String id;
    private String dateTime;
    private String requestURL;
    private String requestMethod;
    private Map<String, String[]> requestParameters;
    private String user = "not logged in";
    private LogType logType;

    public Log(HttpServletRequest request, TokenUtils tokenUtils, LogType type) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        this.setId(uuidString);
        this.setDateTime(now);
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

enum LogType {
    INFO,
    WARN,
    ERROR
}