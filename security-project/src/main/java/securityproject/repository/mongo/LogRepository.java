package securityproject.repository.mongo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import securityproject.logger.Log;


@Repository
public interface LogRepository extends MongoRepository<Log, Long> {
}
