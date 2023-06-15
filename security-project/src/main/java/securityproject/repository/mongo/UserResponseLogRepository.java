package securityproject.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.logger.logs.UserResponseLog;

@Repository
public interface UserResponseLogRepository extends MongoRepository<UserResponseLog, String> {
}
