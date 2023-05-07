package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securityproject.dto.RequestDto;
import securityproject.service.CsrService;
import securityproject.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping(value = "/csr")
public class RegistrationController {
    @Autowired
    private CsrService csrService;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/request")
    public ResponseEntity<String> sendCsr(@RequestBody RequestDto dto){
        try {
            String res = "";
            if(dto.owner) res = csrService.makeOwnerCrf(dto);
            else res = csrService.makeRenterCrf(dto);
            return new ResponseEntity<String>("yay", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<?> v: e.getConstraintViolations()) {
                errorMessage.append(v.getMessage()).append("\n");
            }
            System.out.println(e.getMessage());
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    /**
     * Searches the database for activationString. If it exists - enables user. If it doesn't - it does nothing.
     * @param activationString the authentication request object
     * @return response entity with string message
     */
    @GetMapping(value="/confirm-registration/{activationString}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String activationString) {
        if (userService.activateUser(activationString))
            return new ResponseEntity<>("Registration successful! You can log in now.", HttpStatus.OK);
        else
            return new ResponseEntity<>("Error: User not found", HttpStatus.BAD_REQUEST);
    }
}
