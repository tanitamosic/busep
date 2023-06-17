package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securityproject.dto.DeviceDTO;
import securityproject.dto.HouseDTO;
import securityproject.dto.HouseResponse;
import securityproject.dto.HouseResponses;
import securityproject.model.home.Device;
import securityproject.model.user.MyUserDetails;
import securityproject.model.user.Role;
import securityproject.model.user.StandardUser;
import securityproject.service.HomeService;
import securityproject.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    HomeService homeService;

    @GetMapping("/get-all-homes-by-user/{email}")
    public ResponseEntity<List<HouseResponse>> getAllHousesWithUser(@PathVariable String email) {
        List<HouseResponse> houses;
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

    @GetMapping("/get-all-homes")
    public ResponseEntity<HouseResponses> getAllHouses() {
        List<HouseResponse> houses;
        try {
            houses = homeService.getAllHouses();
            HouseResponses r = new HouseResponses(houses);
            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-home/{houseId}")
    public ResponseEntity<HouseResponse> getHouse(@PathVariable Long houseId) {
        try {
            HouseResponse house = homeService.getHouseResponseById(houseId);
            return new ResponseEntity<>(house, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create-home")
    public ResponseEntity<HouseResponse> createHome(@RequestBody HouseDTO houseDTO) {
        try {
            HouseResponse h = homeService.createHome(houseDTO);
            return new ResponseEntity<>(h, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update-home")
    public ResponseEntity<String> updateHome(@RequestBody HouseDTO dto) {
        try {
            homeService.updateHome(dto);
            return ResponseEntity.ok("Home updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/remove-renter/{houseId}")
    public ResponseEntity<String> removeRenter(@PathVariable Long houseId) {
        try {
            homeService.deactivateHouseUserRole(houseId, "RENTER");
            return ResponseEntity.ok("Renter removed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-home-{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> deleteHome(@PathVariable Long id) {
        try {
            homeService.deleteHome(id);
            return ResponseEntity.ok("Home deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete-device/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> deleteDevice(@PathVariable Long id) {
        try {
            homeService.deleteDevice(id);
            return ResponseEntity.ok("Device deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-device")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Device> addDevice(@RequestBody DeviceDTO dto) {
        try {
            Device d = homeService.addDevice(dto);
            return new ResponseEntity<>(d, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
