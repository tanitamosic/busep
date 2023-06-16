package securityproject.repository.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.enums.LogType;
import securityproject.model.logs.UserRequestLog;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface UserRequestLogRepository extends MongoRepository<UserRequestLog, String> {

    List<UserRequestLog> findByLogType(LogType type);
    List<UserRequestLog> findByUser(String username);
    List<UserRequestLog> findByUserRegex(String regex);
    List<UserRequestLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
