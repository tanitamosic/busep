package securityproject.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.logs.DeviceLog;
import securityproject.model.enums.LogType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceLogRepository extends MongoRepository<DeviceLog, String> {
    List<DeviceLog> findByLogType(LogType type);
    List<DeviceLog> findByDeviceId(Long id);
    List<DeviceLog> findByDeviceTypeRegex(String regex);
    List<DeviceLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<DeviceLog> findAllByLogTypeAndTimestampBetween(LogType type, LocalDateTime start, LocalDateTime end);
    List<DeviceLog> findAllByHouseIdInAndLogTypeAndTimestampBetween(List<Long> houseId, LogType type, LocalDateTime start, LocalDateTime end);
    List<DeviceLog> findAllByHouseIdAndLogTypeAndTimestampBetween(Long houseId, LogType type, LocalDateTime start, LocalDateTime end);
}
