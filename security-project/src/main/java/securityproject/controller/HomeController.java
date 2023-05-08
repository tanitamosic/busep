package securityproject.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securityproject.model.home.House;
import securityproject.model.user.MyUserDetails;
import securityproject.model.user.Role;
import securityproject.model.user.StandardUser;
import securityproject.service.HomeService;
import securityproject.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    HomeService homeService;

    @GetMapping("/get-all-homes-by-user-{email}")
    public ResponseEntity<List<House>> getAllHousesWithUser(@PathVariable String email) {
        List<House> houses;
        MyUserDetails userDetails = (MyUserDetails) userService.loadUserByUsername(email);
        Role r = (Role) userDetails.getUser().getRoles().toArray()[0];
        try {
            if (r.getName().equals("ROLE_OWNER")) {
                houses = homeService.getAllHousesWithOwner((StandardUser) userDetails.getUser());
            } else {
                houses = homeService.getAllHousesWithRenter((StandardUser) userDetails.getUser());
            }
            return new ResponseEntity<>(houses, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

}
