package securityproject.util;


import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

// TODO: rename
public class Helper {

    public static Date addYears(Date currentDate, int amount) {
        // create a calendar object and set it to the current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // add 1 year to the calendar object
        calendar.add(Calendar.YEAR, amount);

        // get the new date object
        return calendar.getTime();
    }

    public static String generateSerialNumber() {
        // Generate a unique serial number
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] serialNumber = new byte[20];
        random.nextBytes(serialNumber);
        BigInteger serial = new BigInteger(1, serialNumber);
        return String.valueOf(serial); // internally calls BigInteger.toString()
    }

    public static X500Name buildX500Name(String givenName, String surname, String org,
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
}
