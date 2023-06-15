package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securityproject.dto.DeviceDTO;
import securityproject.service.DeviceService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @PostMapping(value="thermometer")
    public ResponseEntity<String> logTemperature(HttpServletRequest request, @RequestBody DeviceDTO payload) {
        try {
            deviceService.handleDeviceMessage(request, payload);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }

}
