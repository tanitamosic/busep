package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import securityproject.dto.CertificateDto;
import securityproject.model.CertificateData;
import securityproject.model.Csr;
import securityproject.service.CertificateService;
import securityproject.service.CsrService;

import java.util.List;


@RestController
@RequestMapping(value = "/admin")
@PreAuthorize("hasRole('ADMIN')")
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
    @Autowired
    CertificateService certificateService;

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

    @GetMapping(value="/get-csr-by-email/{email}")
    public ResponseEntity<Csr> getCSRRequest(@PathVariable String email) {
        Csr csr = csrService.getCsrByEmail(email);
        return new ResponseEntity<>(csr, HttpStatus.OK);
    }

    @PostMapping(value="/accept-request")
    public ResponseEntity<Boolean> acceptRequest(@RequestBody CertificateDto dto) {
        Boolean success = csrService.acceptRequest(dto);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping(value="/decline-request")
    public ResponseEntity<Boolean> declineRequest(@RequestBody Long id) {
        Boolean success = csrService.rejectRequest(id);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @DeleteMapping(value="/invalidate-certificate-{id}/reason={reason}")
    public ResponseEntity<Boolean> invalidateCertificate(@PathVariable Long id, @PathVariable String reason) {
        Boolean success = certificateService.invalidateCertificate(id, reason);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @GetMapping(value="/get-all-valid-certificates")
    public ResponseEntity<List<CertificateData>> getAllValidCertificates() {
        List<CertificateData> certificates = certificateService.getValidCertificates();
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping(value="/check-certificate-validity-{id}")
    public ResponseEntity<Boolean> isCertValid(@PathVariable Long id) {
        Boolean isValid = certificateService.isCertificateValid(id);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
}
