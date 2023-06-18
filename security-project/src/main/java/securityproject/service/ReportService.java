package securityproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.model.enums.LogType;
import securityproject.model.home.House;
import securityproject.model.logs.DeviceAlarmLog;
import securityproject.model.logs.DeviceLog;
import securityproject.model.user.User;
import securityproject.repository.mongo.DeviceAlarmLogRepository;
import securityproject.repository.mongo.DeviceLogRepository;
import securityproject.util.containers.SourceReport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    DeviceAlarmLogRepository deviceAlarmLogRepository;
    @Autowired
    DeviceLogRepository deviceLogRepository;
    @Autowired
    UserService userService;
    @Autowired
    HomeService homeService;

    public List<SourceReport> allHouses(String type, LocalDateTime from, LocalDateTime to) {
        List<DeviceLog> logs = getDeviceLogs(type, from, to);
        List<DeviceAlarmLog> alarmLogs = getDeviceAlarmLogs(type, from, to);

        return getSourceReports(alarmLogs, logs);
    }
    public List<SourceReport> allDevices(String type, LocalDateTime from, LocalDateTime to) {
        List<DeviceLog> logs = getDeviceLogs(type, from, to);
        HashMap<String, Integer> deviceReports = new HashMap<>();
        if (null == logs) throw new RuntimeException("Bad params");
        else fillDeviceReportMapWithDeviceId(logs, deviceReports);
        return deviceReports.keySet().stream().map(e -> new SourceReport(e, deviceReports.get(e))).collect(Collectors.toList());
    }

    private List<DeviceLog> getDeviceLogs(String type, LocalDateTime from, LocalDateTime to) {
        List<DeviceLog> logs =  new ArrayList<>();
        if (null == type || type.equals("")) {
            logs = deviceLogRepository.findByTimestampBetween(from, to);
        } else {
            if (!type.equals("ALARM")) {
                LogType logType = LogType.valueOf(type);
                logs = deviceLogRepository.findAllByLogTypeAndTimestampIsBetween(logType, from, to);
            }
        }
        return logs;
    }

    private List<DeviceAlarmLog> getDeviceAlarmLogs(String type, LocalDateTime from, LocalDateTime to) {
        List<DeviceAlarmLog> logs = new ArrayList<>();
        if (null == type || type.equals("")) {
            logs = deviceAlarmLogRepository.findByTimestampBetween(from, to);
        } else {
            if (type.equals("ALARM")) {
                logs = deviceAlarmLogRepository.findByTimestampBetween(from, to);
            }
        }
        return logs;
    }

    private static void fillDeviceReportMapWithHouseId(List<DeviceLog> logs, HashMap<String, Integer> deviceReports) {
        for (DeviceLog log: logs) {
            String houseId = String.valueOf(log.getHouseId());
            incrementAmount(deviceReports, houseId);
        }
    }
    private static void fillDeviceReportMapWithDeviceId(List<DeviceLog> logs, HashMap<String, Integer> deviceReports) {
        for (DeviceLog log: logs) {
            String houseId = String.valueOf(log.getDeviceId());
            incrementAmount(deviceReports, houseId);
        }
    }
    private static void fillAlarmReportMap(List<DeviceAlarmLog> alarmLogs, HashMap<String, Integer> alarmReports) {
        for (DeviceAlarmLog log: alarmLogs) {
            String deviceId = String.valueOf(log.getDeviceId());
            incrementAmount(alarmReports, deviceId);
        }
    }
    private static void incrementAmount(HashMap<String, Integer> reports, String houseId) {
        if (reports.containsKey(houseId)) {
            Integer currentValue = reports.get(houseId);
            Integer incrementedValue = currentValue + 1;
            reports.put(houseId, incrementedValue);
        }
        reports.putIfAbsent(houseId, 1);
    }

    public List<SourceReport> userHousesReports(String email, String type, LocalDateTime from, LocalDateTime to) {
        User u = userService.getUserByEmail(email);
        List<House> houses = homeService.getHousesByOwner(u.getId());
        houses.addAll(homeService.getHousesByRenter(u.getId()));
        List<Long> houseIds = houses.stream().map(House::getId).collect(Collectors.toList());

        List<DeviceAlarmLog> alarmLogs = new ArrayList<>();
        List<DeviceLog> deviceLogs = new ArrayList<>();
        if (null == type || type.equals("")) {
            alarmLogs = deviceAlarmLogRepository.findAllByHouseIdInAndTimestampBetween(houseIds, from, to);
            deviceLogs = deviceLogRepository.findAllByHouseIdInAndLogTypeAndTimestampBetween(houseIds, LogType.valueOf(type), from, to);
        } else {
            if (!type.equals("ALARM")) {
                deviceLogs = deviceLogRepository.findAllByHouseIdInAndLogTypeAndTimestampBetween(houseIds, LogType.valueOf(type), from, to);
            } else {
                alarmLogs = deviceAlarmLogRepository.findAllByHouseIdInAndTimestampBetween(houseIds, from, to);
            }
        }
        return getSourceReports(alarmLogs, deviceLogs);
    }

    private List<SourceReport> getSourceReports(List<DeviceAlarmLog> alarmLogs, List<DeviceLog> deviceLogs) {
        HashMap<String, Integer> deviceReports = new HashMap<>();
        HashMap<String, Integer> alarmReports = new HashMap<>();

        fillDeviceReportMapWithHouseId(deviceLogs, deviceReports);
        List<SourceReport> deviceSourceReports = deviceReports.keySet().stream().map(e -> new SourceReport(e, deviceReports.get(e))).collect(Collectors.toList());
        fillAlarmReportMap(alarmLogs, alarmReports);
        List<SourceReport> alarmSourceReports = alarmReports.keySet().stream().map(e -> new SourceReport(e, alarmReports.get(e))).collect(Collectors.toList());

        deviceSourceReports.addAll(alarmSourceReports);
        return deviceSourceReports;
    }

    public List<SourceReport> houseDevices(Long houseId, String type, LocalDateTime from, LocalDateTime to) {
        List<DeviceAlarmLog> alarmLogs = new ArrayList<>();
        List<DeviceLog> deviceLogs = new ArrayList<>();
        if (null == type || type.equals("")) {
            alarmLogs = deviceAlarmLogRepository.findAllByHouseIdAndTimestampBetween(houseId, from, to);
            deviceLogs = deviceLogRepository.findAllByHouseIdAndLogTypeAndTimestampBetween(houseId, LogType.valueOf(type), from, to);
        } else {
            if (!type.equals("ALARM")) {
                deviceLogs = deviceLogRepository.findAllByHouseIdAndLogTypeAndTimestampBetween(houseId, LogType.valueOf(type), from, to);
            } else {
                alarmLogs = deviceAlarmLogRepository.findAllByHouseIdAndTimestampBetween(houseId, from, to);
            }
        }
        return getSourceReports(alarmLogs, deviceLogs);
    }

    public List<SourceReport> userHouseReports(Long houseId, String email, String type, LocalDateTime from, LocalDateTime to) {
        User u = userService.getUserByEmail(email);
        List<House> houses = homeService.getHousesByOwner(u.getId());
        if (!houses.stream().map(House::getId).collect(Collectors.toList()).contains(houseId)) {
            throw new RuntimeException("User nije vlasnik ove kuce");
        }

        return houseDevices(houseId, type, from, to);
    }
}
