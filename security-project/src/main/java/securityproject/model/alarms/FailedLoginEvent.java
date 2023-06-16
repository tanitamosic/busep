package securityproject.model.alarms;

import java.time.LocalDateTime;

public class FailedLoginEvent {

    private String email;
    private LocalDateTime timestamp;

    public FailedLoginEvent(String email, LocalDateTime timestamp){
        this.email = email;
        this.timestamp = timestamp;
    }

}
