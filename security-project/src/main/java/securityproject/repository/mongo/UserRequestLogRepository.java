package securityproject.repository.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.logger.logs.UserRequestLog;


@Repository
public interface UserRequestLogRepository extends MongoRepository<UserRequestLog, String> {
}
