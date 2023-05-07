package securityproject.service;
import securityproject.util.Helper;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.dto.CertificateDto;
import securityproject.dto.RequestDto;
import securityproject.model.Csr;
import securityproject.model.enums.RequestStatus;
import securityproject.pki.data.IssuerData;
import securityproject.pki.data.SubjectData;
import securityproject.pki.keystore.KeyStoreReader;
import securityproject.pki.keystore.KeyStoreWriter;
import securityproject.pki.keystore.KeyTool;
import securityproject.repository.CsrRepository;

import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

import static securityproject.util.Constants.KEYSTORE_PASSWORD;
import static securityproject.util.Constants.SIGNATURE_ALGORITHM;

@Service
public class CsrService {

    @Autowired
    KeyStoreReader keyStoreReader;
    @Autowired
    KeyStoreWriter keyStoreWriter;
    @Autowired
    FileService fileService;
    @Autowired
    CertificateService certificateService;
    @Autowired
    CsrRepository csrRepository;
    @Autowired
    UserService userService;

    public List<Csr> getAllCsrs() {
        return csrRepository.findAll();
    }

    public Csr getCsrById(Long id) {
        Optional<Csr> opt = csrRepository.findById(id);
        return opt.orElse(null);
    }

    public Boolean acceptRequest(CertificateDto dto) {
        Optional<Csr> opt = csrRepository.findByEmail(dto.email);
        try{
            if (opt.isPresent()) {
                Csr csr = opt.get();
                csr.setStatus(RequestStatus.APPROVED);
                csrRepository.saveAndFlush(csr);
                certificateService.registerCertificate(dto);
                return true;
            }
            return false;
        } catch (Exception e){
             e.printStackTrace();
             return false;
        }
    }

    public Boolean rejectRequest(Long id) {
        Optional<Csr> opt = csrRepository.findById(id);
        if (opt.isPresent()) {
            Csr csr = opt.get();
            csr.setStatus(RequestStatus.REJECTED);
            csrRepository.saveAndFlush(csr);
            return true;
        }
        return false;
    }

    private IssuerData generateIssuerData(PrivateKey issuerKey, String givenName,
                                          String surname, String org, String orgUnit,
                                          String country, String email, String UID) {
        X500Name x500Name = Helper.buildX500Name(givenName, surname, org, orgUnit, country, email);

        // Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
        // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
        // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
        return new IssuerData(issuerKey,x500Name);
    }

    private SubjectData generateSubjectData(PublicKey key, X500Name name, Integer duration){
        Date startDate = new Date();
        Date endDate = Helper.addYears(startDate, duration);
        String sn = Helper.generateSerialNumber();
        return new SubjectData(key,name,sn,startDate,endDate);
    }

    public String makeCrf(RequestDto dto){
        String res = "";
        if (userService.commonPassword(dto.password))
            return null;
        if(dto.owner) res = makeOwnerCrf(dto);
        else res = makeRenterCrf(dto);
        return res;
    }

    public String makeOwnerCrf(RequestDto dto) {
        PKCS10CertificationRequest csr = createCertificateRequest(dto);
        fileService.writeCsrFile(dto.email, csr);
        String pem = fileService.readCsrFile(dto.email);
        PKCS10CertificationRequest read = (PKCS10CertificationRequest) KeyTool.getObjectFromPem(pem);

        userService.registerOwner(dto);  // save user
        return pem;
    }

    public String makeRenterCrf(RequestDto dto) {
        PKCS10CertificationRequest csr = createCertificateRequest(dto);
        fileService.writeCsrFile(dto.email, csr);

        userService.registerRenter(dto);  // save user
        return fileService.readCsrFile(dto.email);
    }

    private PKCS10CertificationRequest createCertificateRequest(RequestDto dto) {
        KeyPair keyPair = KeyTool.generateKeyPair();
        X500Name x500Name = Helper.buildX500Name(
                dto.givenName,dto.surname, dto.organization, dto.orgUnit, dto.country, dto.email);


        SubjectData sub = generateSubjectData(keyPair.getPublic(),x500Name,1);
        IssuerData is = new IssuerData(keyPair.getPrivate(),x500Name);
        X509Certificate cert = certificateService.generateCertificate(sub, is);
        keyStoreWriter.writeKeys(dto.email,keyPair.getPrivate(),KEYSTORE_PASSWORD.toCharArray(),cert);

        Csr csr = getCsr(dto);
        csrRepository.saveAndFlush(csr);

        JcaPKCS10CertificationRequestBuilder p10Builder =
                new JcaPKCS10CertificationRequestBuilder(x500Name, keyPair.getPublic());

        try {
            PKCS10CertificationRequest pkcs10 = p10Builder.build(
                    new JcaContentSignerBuilder(SIGNATURE_ALGORITHM).build(keyPair.getPrivate()));
            return new JcaPKCS10CertificationRequest(pkcs10);
        } catch (OperatorCreationException e) {
            throw new RuntimeException(e);
        }

    }

    private Csr getCsr(RequestDto dto) {

        Date currentDate = new Date();

        Date oneYearLater = Helper.addYears(currentDate, 1);

        Csr csr = new Csr();
        csr.setGivenName(dto.givenName);
        csr.setSurname(dto.surname);
        csr.setOrganization(dto.organization);
        csr.setOrganizationUnit(dto.orgUnit);
        csr.setEmail(dto.email);
        csr.setCountry(dto.country);
        csr.setValid(true);
        csr.setStatus(RequestStatus.PENDING);
        csr.setStartDate(currentDate);
        csr.setEndDate(oneYearLater);
        csr.setIsOwner(dto.owner);
        return csr;
    }
}
