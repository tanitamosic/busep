package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securityproject.dto.UserResponse;
import securityproject.model.user.User;
import securityproject.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserService userService;

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
}
