package securityproject.service;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import securityproject.dto.GenericLogDTO;
import securityproject.model.alarms.FailedLoginEvent;
import securityproject.model.alarms.RequestAlarm;
import securityproject.model.enums.AlarmSeverity;
import securityproject.model.enums.DeviceType;
import securityproject.model.enums.RequestType;
import securityproject.model.logs.DeviceAlarmLog;
import securityproject.model.logs.DeviceLog;
import securityproject.model.alarms.DeviceAlarm;

import securityproject.model.home.Device;
import securityproject.model.logs.RequestAlarmLog;
import securityproject.model.user.User;
import securityproject.repository.DeviceRepository;
import securityproject.repository.mongo.DeviceAlarmLogRepository;
import securityproject.repository.mongo.RequestAlarmLogRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

import static securityproject.util.Helper.convertToJson;


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
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    HomeService homeService;

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

    public void raiseErrorAlarm(Long deviceId, String message, LocalDateTime timestamp, AlarmSeverity severity, DeviceType deviceType){
        Long houseId = getHouseIdFromDevice(deviceId);
        DeviceAlarm alarm = new DeviceAlarm(deviceId,houseId,message,timestamp,severity, deviceType);
        logAlarm(alarm);
    }

    public void raiseEmailAlarm(String email, LocalDateTime timestamp, AlarmSeverity severity){
        logAlarm(new RequestAlarm(timestamp, email, RequestType.LOGIN, severity ));
    }

    public void raiseMaliciousAlarm(String email, AlarmSeverity severity){
        logAlarm(new RequestAlarm(LocalDateTime.now(), email, RequestType.MALICIOUS, severity));
    }

    public void logAlarm(DeviceAlarm alarm){
        DeviceAlarmLog log = new DeviceAlarmLog(alarm);
        deviceLogRepository.insert(log);
        sendLog(log);
        logger.error("Inserted " + alarm.getSeverity() + " alarm log; deviceId: {}", alarm.getDeviceId());
    }

    public void logAlarm(RequestAlarm alarm){
        RequestAlarmLog log = new RequestAlarmLog(alarm);
        requestLogRepository.insert(log);
        sendLog(log);
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

    public void sendLog(DeviceAlarmLog log) {
        GenericLogDTO genericLog = new GenericLogDTO(log);
        Device device = deviceRepository.getDeviceById(log.getDeviceId());
        User owner = homeService.getOwner(device.getHouseId());
        User renter = homeService.getRenter(device.getHouseId());
        String message = Objects.requireNonNull(convertToJson(genericLog));
        messagingTemplate.convertAndSend("/topic/log/" + owner.getEmail(), message);
        messagingTemplate.convertAndSend("/topic/log/" + renter.getEmail(), message);
        messagingTemplate.convertAndSend("/topic/log", message);
    }
    public void sendLog(RequestAlarmLog log) {
        GenericLogDTO genericLog = new GenericLogDTO(log);
        String message = Objects.requireNonNull(convertToJson(genericLog));
        messagingTemplate.convertAndSend("/topic/log", message);
    }
}
