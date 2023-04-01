package securityproject.service;
import securityproject.util.Helper;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
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

import java.nio.charset.StandardCharsets;
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
        if (opt.isPresent()) {
            Csr csr = opt.get();
            csr.setStatus(RequestStatus.APPROVED);
            csrRepository.saveAndFlush(csr);

            certificateService.createCertificateData(dto);
            return true;
        }
        return false;
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

    public X500Name buildX500Name (String givenName, String surname, String org,
                                   String orgUnit, String country, String email){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, givenName.concat(" ").concat(surname));
        builder.addRDN(BCStyle.SURNAME, surname);
        builder.addRDN(BCStyle.GIVENNAME, givenName);
        builder.addRDN(BCStyle.O, org);
        builder.addRDN(BCStyle.OU, orgUnit);
        builder.addRDN(BCStyle.C, country);
        builder.addRDN(BCStyle.E, email);

        // UID (USER ID) je ID korisnika
        // builder.addRDN(BCStyle.UID, UID);

        return builder.build();
    }

    private IssuerData generateIssuerData(PrivateKey issuerKey, String givenName,
                                          String surname, String org, String orgUnit,
                                          String country, String email, String UID) {
        X500Name x500Name = buildX500Name(givenName, surname, org, orgUnit, country, email);

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

    public String makeOwnerCrf(RequestDto dto) {
        try {
            PKCS10CertificationRequest csr = createCertificateRequest(dto);
            String csrPEM = new String(Base64.getEncoder().encode(csr.getEncoded()), StandardCharsets.UTF_8);
            fileService.writeCsrPem(dto.email, csrPEM);
            String pem = fileService.readCsrPem(dto.email);
            PKCS10CertificationRequest read = (PKCS10CertificationRequest) KeyTool.getObjectFromPem(pem);

            userService.registerOwner(dto);  // save user
            return pem;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String makeRenterCrf(RequestDto dto) {
        try {
            PKCS10CertificationRequest csr = createCertificateRequest(dto);
            String csrPEM = new String(Base64.getEncoder().encode(csr.getEncoded()), StandardCharsets.UTF_8);
            fileService.writeCsrPem(dto.email, csrPEM);

            userService.registerRenter(dto);  // save user
            return fileService.readCsrPem(dto.email);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private PKCS10CertificationRequest createCertificateRequest(RequestDto dto) {
        KeyPair keyPair = KeyTool.generateKeyPair();
        X500Name x500Name = buildX500Name(
                dto.givenName,dto.surname, dto.organization, dto.orgUnit, dto.country, dto.email);


        SubjectData sub = generateSubjectData(keyPair.getPublic(),x500Name,1);
        IssuerData is = new IssuerData(keyPair.getPrivate(),x500Name);
        X509Certificate cert = certificateService.generateCertificate(sub, is);
        keyStoreWriter.write(dto.email,keyPair.getPrivate(),KEYSTORE_PASSWORD.toCharArray(),cert);
        //TODO: tanita - pravi fajl ali ne nalazi ks

        //napravi CRF objekat i sacuvaj u repo/bazu - DONE
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
