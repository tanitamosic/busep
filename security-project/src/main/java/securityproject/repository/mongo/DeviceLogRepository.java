package securityproject.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.logger.logs.DeviceLog;

@Repository
public interface DeviceLogRepository extends MongoRepository<DeviceLog, String> {
}
