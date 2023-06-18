package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securityproject.dto.FilterDTO;
import securityproject.dto.HouseDTO;
import securityproject.dto.UserResponse;
import securityproject.model.enums.DeviceType;
import securityproject.model.enums.LogType;
import securityproject.model.user.User;
import securityproject.service.DeviceService;
import securityproject.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    DeviceService deviceService;

    @GetMapping(value="info/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        UserResponse response = null;
        User u = userService.getUserByEmail(email);

        if (u != null){
            response = new UserResponse(u);
        }
        System.out.println(response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value="all-clients")
    public ResponseEntity<List<UserResponse>> getAllClients() {
       List<User> clients = userService.getAllClients();
       List<UserResponse> responses = userService.makeUserResponses(clients);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping(value="filter-clients")
    public ResponseEntity<List<UserResponse>> filterClients(@RequestBody FilterDTO filterDto) {
        List<User> clients = userService.filterClients(filterDto);
        List<UserResponse> responses = userService.makeUserResponses(clients);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping(value="delete-client/{email}")
    public ResponseEntity<String> deleteClient(@PathVariable String email) {
        try {
            userService.deleteClient(email);
            return ResponseEntity.ok("Client was deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="change-client-role/{email}")
    public ResponseEntity<String> changeClientRole(@PathVariable String email) {
        try {
            userService.changeClientRole(email);
            return ResponseEntity.ok("Client role was changed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private static class FilterParams {
        Long houseId;
        DeviceType deviceType;
        LogType logType;
        String regex;
    }

    @PostMapping(value="filter-logs/{email}")
    public ResponseEntity<String> filterLogs(@RequestBody FilterParams dto, @PathVariable String email) {
        return ResponseEntity.ok().body(deviceService.userFilterLogs(email, dto.houseId, dto.deviceType, dto.logType, dto.regex));
    }
}
