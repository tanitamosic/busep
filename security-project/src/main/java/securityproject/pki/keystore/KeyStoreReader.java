package securityproject.pki.keystore;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Component;
import securityproject.pki.data.IssuerData;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static securityproject.util.Constants.*;

@Component
public class KeyStoreReader {
    // KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    // Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni kljucevi, koji se koriste u simetricnima siframa
    private KeyStore keyStore;

    public KeyStoreReader() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if (fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
     * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
     *
     * @param keyStoreFile - datoteka odakle se citaju podaci
     * @param alias        - alias putem kog se identifikuje sertifikat izdavaoca
     * @param password     - lozinka koja je neophodna da se otvori key store
     * @param keyPass      - lozinka koja je neophodna da se izvuce privatni kljuc
     * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
     */
    public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
        try {
            // Datoteka se ucitava
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, password);

            // Iscitava se sertifikat koji ima dati alias
            Certificate cert = keyStore.getCertificate(alias);

            // Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
            PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            return new IssuerData(privKey, issuerName);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
                 | UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava sertifikat is KS fajla
     */
    public Certificate readCertificate(String alias) {
        try {
            loadKeyStore(KEYSTORE_PATH,KEYSTORE_PASSWORD.toCharArray());
            if (keyStore.isKeyEntry(alias)) {
                Certificate cert = keyStore.getCertificate(alias);
                return cert;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava privatni kljuc is KS fajla
     */
    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
                return pk;
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException
                 | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Certificate> showKeyStoreContent(String keyStoreFile, String keyStorePass) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());
            Enumeration<String> aliases = ks.aliases();

            // Iterate over the aliases and get the certificate for each one
            List<Certificate> certificates = new LinkedList<>();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                Certificate cert = ks.getCertificate(alias);
                // Do something with the certificate
                certificates.add(cert);
            }
            return certificates;

        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
                 | CertificateException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PrivateKey getRenterCaPk(){
        return getCaPk(RENTER_ALIAS);
    }

    public PrivateKey getOwnerCaPk(){
        return getCaPk(OWNER_ALIAS);
    }

    public X500Name getRenterCaName(){
        return getIssuerName(RENTER_ALIAS);
    }

    public X500Name getOwnerCaName(){
        return getIssuerName(OWNER_ALIAS);
    }

    private PrivateKey getCaPk(String alias){
        try {
            return (PrivateKey) keyStore.getKey(alias, KEYSTORE_PASSWORD.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private X500Name getIssuerName(String alias){
        try {
            loadKeyStore(KEYSTORE_PATH,KEYSTORE_PASSWORD.toCharArray());

            if (keyStore.isKeyEntry(alias)) {
                X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
                return new X500Name(cert.getSubjectX500Principal().getName());
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public X509Certificate readCertificate(String alias){
//        try{
//            keyStore.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASSWORD.toCharArray());
//            Certificate cert = keyStore.getCertificate(alias);
//        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

}