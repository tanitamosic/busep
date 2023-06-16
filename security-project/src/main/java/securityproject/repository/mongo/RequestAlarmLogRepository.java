package securityproject.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.enums.AlarmSeverity;
import securityproject.model.enums.RequestType;
import securityproject.model.logs.RequestAlarmLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestAlarmLogRepository extends MongoRepository<RequestAlarmLog, String> {
    List<RequestAlarmLog> findBySeverity(AlarmSeverity severity);
    List<RequestAlarmLog> findByRequestType(RequestType type);
    List<RequestAlarmLog> findBySeverityRegex(AlarmSeverity severity);
    List<RequestAlarmLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}

