package securityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import securityproject.model.alarms.CustomAlarm;

@Repository
public interface CustomAlarmRepository extends JpaRepository<CustomAlarm, Long> {

}
