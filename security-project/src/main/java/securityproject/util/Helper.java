package securityproject.util;


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
}
