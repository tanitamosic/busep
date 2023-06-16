package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securityproject.dto.device.SignedMessageDTO;
import securityproject.service.DeviceService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @PostMapping(value="thermometer")
    public ResponseEntity<String> logTemperature(HttpServletRequest request, @RequestBody SignedMessageDTO payload) {
        try {
            deviceService.handleDeviceMessage(request, payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }
    @PostMapping(value="light")
    public ResponseEntity<String> logLight(HttpServletRequest request, @RequestBody SignedMessageDTO payload) {
        try {
            deviceService.handleDeviceMessage(request, payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }
    @PostMapping(value="camera")
    public ResponseEntity<String> logCamera(HttpServletRequest request, @RequestBody SignedMessageDTO payload) {
        try {
            deviceService.handleDeviceMessage(request, payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }
    @PostMapping(value="lock")
    public ResponseEntity<String> logLock(HttpServletRequest request, @RequestBody SignedMessageDTO payload) {
        try {
            deviceService.handleDeviceMessage(request, payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }
    @PostMapping(value="smoke_detector")
    public ResponseEntity<String> logSmoke(HttpServletRequest request, @RequestBody SignedMessageDTO payload) {
        try {
            deviceService.handleDeviceMessage(request, payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }

}
