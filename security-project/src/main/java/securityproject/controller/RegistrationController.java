package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import securityproject.dto.RequestDto;
import securityproject.mail.MailingService;
import securityproject.service.CsrService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping(value = "/csr")
public class RegistrationController {
    @Autowired
    private CsrService service;

    @PostMapping(value = "/request")
    public ResponseEntity<String> sendCsr(@RequestBody RequestDto dto){
        try {
            String res = "";
            if(dto.owner) res = service.makeOwnerCrf(dto);
            else res = service.makeRenterCrf(dto);
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
     * Searches the database for url_id. If it exists - enables user. If it doesn't - it does nothing.
     * @param url_id the authentication request object
     * @return response entity with string message
     */
    @GetMapping(value="/confirm-registration/acc={url_id}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String url_id) {
        /*
        * impl...
        * */
        return new ResponseEntity<>("Registration successful! You can log in now.", HttpStatus.OK);
    }
}
