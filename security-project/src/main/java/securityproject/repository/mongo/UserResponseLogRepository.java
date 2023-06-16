package securityproject.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.enums.LogType;
import securityproject.model.logs.UserResponseLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserResponseLogRepository extends MongoRepository<UserResponseLog, String> {

    List<UserResponseLog> findByLogType(LogType type);
    List<UserResponseLog> findByUser(String username);
    List<UserResponseLog> findByUserRegex(String regex);
    List<UserResponseLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
