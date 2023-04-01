package securityproject.pki.keystore;

import org.springframework.stereotype.Component;
import securityproject.util.Constants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static securityproject.util.Constants.*;

@Component
public class KeyStoreWriter {
    // KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    // Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni kljucevi, koji se koriste u simetricnima siframa

    private KeyStore keyStore;

    public KeyStoreWriter() {
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
                // Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void writeKeys(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
        try {
            loadKeyStore(KEYSTORE_PATH,KEYSTORE_PASSWORD.toCharArray());
            keyStore.setKeyEntry(alias, privateKey, password, new Certificate[]{certificate});
            saveKeyStore(KEYSTORE_PATH, KEYSTORE_PASSWORD.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void writeCertificate(String alias, X509Certificate certificate) {
        try {
            loadKeyStore(KEYSTORE_PATH,KEYSTORE_PASSWORD.toCharArray());
            keyStore.setCertificateEntry(alias + CERTIFICATE_SUFFIX,certificate);
            saveKeyStore(KEYSTORE_PATH, KEYSTORE_PASSWORD.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public Boolean invalidateCertificate(X509Certificate cert) {
        try {

            // Get the default trust store
            loadKeyStore(KEYSTORE_PATH, KEYSTORE_PASSWORD.toCharArray());
            // Remove the certificate from the trust store
            keyStore.deleteEntry(cert.getSubjectX500Principal().getName());

            // Save the updated trust store
            FileOutputStream out = new FileOutputStream(Constants.KEYSTORE_PATH);
            keyStore.store(out, Constants.KEYSTORE_PASSWORD.toCharArray());
            out.close();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            return false;
        }
        return true;
    }

}
