package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securityproject.dto.RequestDto;
import securityproject.service.CrfService;

@RestController
@RequestMapping(value = "/csr")
public class CsrController {
    @Autowired
    private CrfService service;

    @PostMapping(value = "/request")
    public ResponseEntity<String> sendCsr(@RequestBody RequestDto dto){
        String res = "";
        if(dto.owner) res = service.makeOwnerCrf(dto);
        else res = service.makeRenterCrf(dto);
        return new ResponseEntity<String>("yay", HttpStatus.OK);
    }
}
