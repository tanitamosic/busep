package securityproject.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.logger.logs.DeviceLog;
import securityproject.logger.logs.LogType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceLogRepository extends MongoRepository<DeviceLog, String> {
    List<DeviceLog> findByLogType(LogType type);
    List<DeviceLog> findByDeviceId(String id);
    List<DeviceLog> findByDeviceTypeRegex(String regex);
    List<DeviceLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
