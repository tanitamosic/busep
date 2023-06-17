package securityproject.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.logs.BlacklistedIpLog;

@Repository
public interface BlacklistedIpLogRepository extends MongoRepository<BlacklistedIpLog, String> {
}
