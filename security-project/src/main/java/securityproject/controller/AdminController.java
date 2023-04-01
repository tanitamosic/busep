package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import securityproject.dto.CertificateDto;
import securityproject.model.Csr;
import securityproject.service.CsrService;

import java.util.List;


@RestController
@RequestMapping(value = "/admin")
public class AdminController {
    /*
    1. pregled svih zahteva - get
    2. prikaz jednog zahteva - get
    3. odobravanje zahteva - post
    4. odbijanje zahteva - post

     5. prikaz svih sertifikata
     6. prikaz jednog -||-
     7. provera validnosti/verifikacija (unutar prikaza jednog)
     8. povlacenje

     */

    @Autowired
    CsrService csrService;

    @GetMapping(value="/get-all-csrs")
    public ResponseEntity<List<Csr>> getCSRRequests() {
        List<Csr> retval = csrService.getAllCsrs();
        return new ResponseEntity<>(retval, HttpStatus.OK);
    }

    @GetMapping(value="/get-csr-{id}")
    public ResponseEntity<Csr> getCSRRequest(@PathVariable Long id) {
        Csr csr = csrService.getCsrById(id);
        return new ResponseEntity<>(csr, HttpStatus.OK);
    }

    @PostMapping(value="/accept-request")
    public ResponseEntity<Boolean> acceptRequest(@RequestBody CertificateDto dto) {
        Boolean success = csrService.acceptRequest(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping(value="/reject-request")
    public ResponseEntity<Boolean> rejectRequest(@RequestBody Long id) {
        Boolean success = csrService.rejectRequest(id);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
}
