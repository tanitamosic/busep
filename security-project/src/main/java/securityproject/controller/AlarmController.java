package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securityproject.model.home.DeviceMessage;
import securityproject.service.AlarmService;

@RestController
@RequestMapping(value = "/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @PostMapping(value = "/test-error")
    public ResponseEntity<String> testErrorAlarm(@RequestBody DeviceMessage msg) {
        String res = alarmService.handleMessage(msg);
        return ResponseEntity.ok(res);
    }
}
