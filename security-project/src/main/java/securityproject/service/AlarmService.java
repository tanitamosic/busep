package securityproject.service;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.model.alarms.FailedLoginEvent;
import securityproject.model.alarms.RequestAlarm;
import securityproject.model.enums.AlarmSeverity;
import securityproject.model.enums.RequestType;
import securityproject.model.logs.DeviceAlarmLog;
import securityproject.model.logs.DeviceLog;
import securityproject.model.alarms.DeviceAlarm;

import securityproject.model.home.Device;
import securityproject.model.logs.RequestAlarmLog;
import securityproject.repository.DeviceRepository;
import securityproject.repository.mongo.DeviceAlarmLogRepository;
import securityproject.repository.mongo.RequestAlarmLogRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Service
public class AlarmService {

    Logger logger = LoggerFactory.getLogger(DeviceService.class);
    private KieSession kieSession;

    @Autowired
    private DeviceAlarmLogRepository deviceLogRepository;

    @Autowired
    private RequestAlarmLogRepository requestLogRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    public AlarmService(){
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kieSession = kContainer.newKieSession("alarmKsession");
        kieSession.setGlobal("alarmService", this);

    }
    public void handleDeviceLog(DeviceLog msg) {
        kieSession.insert(msg);
        kieSession.fireAllRules();
    }

    public void raiseErrorAlarm(Long deviceId, String message, LocalDateTime timestamp, AlarmSeverity severity){
        Long houseId = getHouseIdFromDevice(deviceId);
        DeviceAlarm alarm = new DeviceAlarm(deviceId,houseId,message,timestamp,severity);
        logAlarm(alarm);
    }

    public void raiseEmailAlarm(String email, LocalDateTime timestamp, AlarmSeverity severity){
        logAlarm(new RequestAlarm(timestamp, email, RequestType.LOGIN, severity ));
    }

    public void raiseMaliciousAlarm(String email, AlarmSeverity severity){
        logAlarm(new RequestAlarm(LocalDateTime.now(), email, RequestType.MALICIOUS, severity));
    }

    public void logAlarm(DeviceAlarm alarm){
        deviceLogRepository.insert(new DeviceAlarmLog(alarm));
        logger.error("Inserted " + alarm.getSeverity() + " alarm log; deviceId: {}", alarm.getDeviceId());
    }

    public void logAlarm(RequestAlarm alarm){
        requestLogRepository.insert(new RequestAlarmLog(alarm));
        logger.info("Inserted " + alarm.getSeverity() + " alarm log; type: {}, source: {}", alarm.getRequestType(), alarm.getSource());
    }

    public Long getHouseIdFromDevice(Long deviceId) {
        Device device = deviceRepository.getDeviceById(deviceId);
        return device.getHouseId();
    }

    public void parseFailedLoginRequest(String email) {
        kieSession.insert(new FailedLoginEvent(email, LocalDateTime.now()));
        kieSession.fireAllRules();
        raiseEmailAlarm(email, LocalDateTime.now(), AlarmSeverity.LOW);
    }

    public void parseAnyRequest(HttpServletRequest request) {
    }

    public void parseMaliciousRequest(String remoteAddr) {
        raiseMaliciousAlarm(remoteAddr, AlarmSeverity.MEDIUM);
    }
}
