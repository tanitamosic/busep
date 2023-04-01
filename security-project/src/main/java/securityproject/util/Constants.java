package securityproject.util;

public class Constants {
    public static final String KEYPAIR_ALGORITHM = "RSA";
    public static final String SECRETKEY_ALGORITHM = "AES";
    public static final String HASH_ALGORITHM = "SHA256PRNG";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String KEYSTORE_PASSWORD = "rootpassword";
    public static final String KEYSTORE_PATH = "../../oneks.jks"; //TODO: upisi path

    public static final String OWNER_ALIAS = "ownerca (rootca)";
    public static final String RENTER_ALIAS = "renterca (rootca)";
}
