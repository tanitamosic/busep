package securityproject.pki;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Arrays;


/**
 * Generise i proverava digitalni potpis
 */
public class SecureCommunication {

    private static final String KEYPAIR_ALGORITHM = "RSA";
    private static final String SECRETKEY_ALGORITHM = "AES";
    private static final String HASH_ALGORITHM = "SHA256PRNG";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    public void testIt() {
        String message = "Ovo su podaci koje Alisa treba da posalje Bobu";

        // TODO: Implementirati pozivanje operacija tako da se simulira sifrovanje i digitalno potpisivanje poruke od strane Alise i desifrovanje i provera digitalnog potpisa od strane Boba
        // Dozvoljeno je kreiranje nove klase za DTO (data transfer object) ukoliko ima potrebe
    }

    private SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(SECRETKEY_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(HASH_ALGORITHM, "SUN");

            keyGen.init(256, random);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEYPAIR_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(HASH_ALGORITHM, "SUN");

            keyGen.initialize(2048, random);

            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] encrypt(String plainText, PublicKey publicKey) {
        byte[] dataToEncrypt = plainText.getBytes();
        try {
            SecretKey skey = generateSecretKey();
            Cipher cipher = Cipher.getInstance(SECRETKEY_ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, skey);
            byte[] encryptedData = cipher.doFinal(dataToEncrypt);

            Cipher asymmetricCipher = Cipher.getInstance(KEYPAIR_ALGORITHM);
            asymmetricCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedKey = asymmetricCipher.doFinal(skey.getEncoded());

            byte[] ENCRYPTED_SYMMETRIC_KEY_SIZE = ByteBuffer.allocate(4).putInt(encryptedKey.length).array();

            // spajamo enkriptovani simetrični ključ i enkriptovane podatke u jedan niz bajtova

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(ENCRYPTED_SYMMETRIC_KEY_SIZE);
            outputStream.write(encryptedKey);
            outputStream.write(encryptedData);

            return outputStream.toByteArray();
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private byte[] decrypt(byte[] dataToDecrypt, PrivateKey key) {
        try {
            byte[] bytes = new byte[] {dataToDecrypt[0], dataToDecrypt[1], dataToDecrypt[2], dataToDecrypt[3]};
            int ENCRYPTED_SYMMETRIC_KEY_SIZE = ByteBuffer.wrap(bytes).getInt();

            byte[] encryptedKeyBytes = Arrays.copyOfRange(dataToDecrypt,4, ENCRYPTED_SYMMETRIC_KEY_SIZE + 4 + 1);
            byte[] encryptedTextBytes = Arrays.copyOfRange(dataToDecrypt,ENCRYPTED_SYMMETRIC_KEY_SIZE + 4 + 1, dataToDecrypt.length);

            Cipher keyCipher = Cipher.getInstance(KEYPAIR_ALGORITHM);
            keyCipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encodedKey = keyCipher.doFinal(encryptedKeyBytes);
            SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, SECRETKEY_ALGORITHM);

            Cipher textCipher = Cipher.getInstance(SECRETKEY_ALGORITHM);
            textCipher.init(Cipher.DECRYPT_MODE, originalKey);

            return textCipher.doFinal(encryptedTextBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] sign(byte[] data, PrivateKey privateKey) {
        try {
            // Kreiranje objekta koji nudi funkcionalnost digitalnog potpisivanja
            // Prilikom getInstance poziva prosledjujemo algoritam koji cemo koristiti
            // U ovom slucaju cemo generisati SHA-1 hes kod koji cemo potpisati upotrebom AES asimetricne sifre
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);

            // Navodimo kljuc kojim potpisujemo
            sig.initSign(privateKey);

            // Postavljamo podatke koje potpisujemo
            sig.update(data);

            // Vrsimo potpisivanje
            return sig.sign();
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean verify(byte[] data, byte[] signature, PublicKey publicKey) {
        try {
            // Kreiranje objekta koji nudi funkcionalnost digitalnog potpisivanja
            // Prilikom getInstance poziva prosledjujemo algoritam koji cemo koristiti
            // U ovom slucaju cemo generisati SHA-1 hes kod koji cemo potpisati upotrebom RSA asimetricne sifre
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);

            // Navodimo kljuc sa kojim proveravamo potpis
            sig.initVerify(publicKey);

            // Postavljamo podatke koje potpisujemo
            sig.update(data);

            // Vrsimo proveru digitalnog potpisa
            return sig.verify(signature);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SecureCommunication sec = new SecureCommunication();
        sec.testIt();
    }
}
