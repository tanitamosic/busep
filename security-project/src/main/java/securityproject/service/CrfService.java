package securityproject.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.stereotype.Service;
import securityproject.dto.CrfDto;
import securityproject.pki.data.IssuerData;
import securityproject.pki.data.SubjectData;
import securityproject.pki.keystore.KeyTool;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class CrfService {

    public void makeOwnerCrf(CrfDto dto) {
        KeyPair kp = KeyTool.generateKeyPair();


    }


    public X500Name buildX500Name (String givenName,
                                   String surname, String org, String orgUnit,
                                   String country, String email, String UID){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, givenName.concat(" ").concat(surname));
        builder.addRDN(BCStyle.SURNAME, surname);
        builder.addRDN(BCStyle.GIVENNAME, givenName);
        builder.addRDN(BCStyle.O, org);
        builder.addRDN(BCStyle.OU, orgUnit);
        builder.addRDN(BCStyle.C, country);
        builder.addRDN(BCStyle.E, email);

        // UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, UID);

        return builder.build();
    }

    private IssuerData generateIssuerData(PrivateKey issuerKey, String givenName,
                                          String surname, String org, String orgUnit,
                                          String country, String email, String UID) {
        X500Name x500Name = buildX500Name(givenName, surname,org,orgUnit,country,email,UID);

        // Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
        // - privatni kljuc koji ce se koristiti da potpise sertifikat koji se izdaje
        // - podatke o vlasniku sertifikata koji izdaje nov sertifikat
        return new IssuerData(issuerKey,x500Name);
    }


    public void makeRenterCrf(CrfDto dto) {
    }
}
