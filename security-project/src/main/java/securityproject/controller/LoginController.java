package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import securityproject.model.user.Role;
import securityproject.model.user.User;
import securityproject.util.TokenUtils;


@RestController
@RequestMapping(value = "/")
public class LoginController {

    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    AuthenticationManager manager;

    @GetMapping(value="login-success")
    public ResponseEntity<String> success() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(auth);
        User u = (User) auth.getPrincipal();
        Role r = (Role) u.getRoles().toArray()[0];
        String token = tokenUtils.generateToken((String) auth.getPrincipal(), r.getName());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        return ResponseEntity.ok().headers(responseHeaders).body("logged in");
    }

    @GetMapping(value="login-failure")
    public ResponseEntity<String> failure() {
        return new ResponseEntity<>("not logged in", HttpStatus.UNAUTHORIZED);
    }
}
