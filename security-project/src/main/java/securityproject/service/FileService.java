package securityproject.service;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import securityproject.util.Helper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static securityproject.util.Constants.CERTIFICATE_SUFFIX;

@Service
public class FileService {
    private ArrayList<String> passwordList;
    private static final String PASSWORD_FILE = "src/main/resources/password-blacklist.txt";
    public FileService(){
        passwordList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PASSWORD_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                passwordList.add(line);
            }
            reader.close();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void writeCsrFile(String alias, PKCS10CertificationRequest cert){
        try{
            String cerString = Helper.toPemFormat(cert);
            String filename = "src/main/resources/data/crs/" + alias + ".crs";
            FileOutputStream out = new FileOutputStream(filename);
            out.write(cerString.getBytes());
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public String readCsrFile(String alias){
        String filename = "src/main/resources/data/crs/" + alias + ".crs";
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(encoded);
    }

    public void writeCerFile(X509Certificate cert, String alias) {
        try{
            String cerString = Helper.toPemFormat(cert);
            String filename = "src/main/resources/data/cer/" + alias + CERTIFICATE_SUFFIX + ".cer";
            FileOutputStream out = new FileOutputStream(filename);
            out.write(cerString.getBytes());
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCerFile(String alias) {
        String filename = "src/main/resources/data/cer/" + alias + CERTIFICATE_SUFFIX + ".cer";
        File file = new File(filename);
        if (file.exists()) {
            boolean deleted = file.delete();
            // Check if the file was deleted successfully
            if (deleted) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
        } else {
            System.out.println("File does not exist");
        }
    }
    public ArrayList<String> getPasswordList(){return passwordList;};
}
