package securityproject.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.enums.AlarmSeverity;
import securityproject.model.logs.DeviceAlarmLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceAlarmLogRepository extends MongoRepository<DeviceAlarmLog, String> {
    List<DeviceAlarmLog> findBySeverity(AlarmSeverity severity);
    List<DeviceAlarmLog> findByDeviceId(Long id);
    List<DeviceAlarmLog> findBySeverityRegex(AlarmSeverity severity);
    List<DeviceAlarmLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}

