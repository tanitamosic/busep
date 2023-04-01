package securityproject.service;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.dto.CertificateDto;
import securityproject.model.CertificateData;
import securityproject.pki.data.IssuerData;
import securityproject.pki.data.SubjectData;
import securityproject.pki.keystore.KeyStoreReader;
import securityproject.repository.CertificateRepository;
import securityproject.util.Constants;
import securityproject.util.Helper;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;


@Service
public class CertificateService {

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    KeyStoreReader keyStoreReader;

    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData) {
        try {
            // Posto klasa za generisanje sertifikata ne moze da primi direktno privatni kljuc pravi se builder za objekat
            // Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
            // Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifikata
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            // Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
            builder = builder.setProvider("BC");

            // Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            // Postavljaju se podaci za generisanje sertifikata
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            // Generise se sertifikat
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            // Builder generise sertifikat kao objekat klase X509CertificateHolder
            // Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            // Konvertuje objekat u sertifikat
            return certConverter.getCertificate(certHolder);
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void save(CertificateData data) {
        certificateRepository.saveAndFlush(data);
    }

    public void createCertificateData(CertificateDto dto) {
        Date endDate = Helper.addYears(dto.startDate, dto.duration);
        String serialNumber = Helper.generateSerialNumber();
        CertificateData certData = new CertificateData(dto, endDate, serialNumber);
        save(certData);

        X500Name name = Helper.buildX500Name(dto.givenName, dto.surname, dto.organization, dto.orgUnit, dto.country, dto.email);
        Certificate cert;
        PrivateKey privateKey;
        if (dto.owner) {
            cert = keyStoreReader.readCertificate(Constants.KEYSTORE_PATH, Constants.KEYSTORE_PASSWORD, Constants.OWNER_ALIAS);
            privateKey = keyStoreReader.getOwnerCaPK();
        } else {
            cert = keyStoreReader.readCertificate(Constants.KEYSTORE_PATH, Constants.KEYSTORE_PASSWORD, Constants.RENTER_ALIAS);
            privateKey = keyStoreReader.getRenterCaPK();

        }
        PublicKey publicKey = cert.getPublicKey();
        SubjectData subjectData = new SubjectData(publicKey, name, serialNumber, dto.startDate, endDate);

        IssuerData issuerData = new IssuerData(privateKey, name);

        X509Certificate x509cert = generateCertificate(subjectData, issuerData);
    }
}
