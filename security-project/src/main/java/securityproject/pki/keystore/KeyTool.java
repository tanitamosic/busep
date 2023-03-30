package securityproject.pki.keystore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import securityproject.pki.data.IssuerData;
import securityproject.pki.data.SubjectData;
import securityproject.service.CertificateService;

import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

@Component
public class KeyTool {

    @Autowired
    private CertificateService service;

    private KeyStore createNewKeyStore() {

        try {
            return KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void createNewSelfSignedCertificate(SubjectData subjectData) {

    }

    private X509Certificate createNewIssuedCertificate(SubjectData subjectData, IssuerData issuerData) {

        X509Certificate cert = service.generateCertificate(subjectData, issuerData);
        return cert;

    }



    static public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA256PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void menu() {
        System.out.println("==================================");
        System.out.println("1.	Create new key store");
        System.out.println("2.	Show key store content");
        System.out.println("3.	Create new self signed certificate");
        System.out.println("4.	Create new issued certificate");
        System.out.println("5.	Exit");
        System.out.print(">>>");
    }
}
