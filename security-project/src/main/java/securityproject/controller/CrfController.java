package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import securityproject.dto.CrfDto;
import securityproject.service.CrfService;

@RestController
@RequestMapping(value = "/csr")
public class CrfController {
    @Autowired
    private CrfService service;

    @PostMapping(value = "/request")
    public ResponseEntity<String> sendCsr(CrfDto dto){
        if(dto.owner) service.makeOwnerCrf(dto);
        else service.makeRenterCrf(dto);
        return new ResponseEntity<String>("yay", HttpStatus.OK);
    }
}
