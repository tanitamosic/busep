package securityproject.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {
    public void writeCrsPem(String alias,String pem){
        String filename = "src/main/resources/data/crs/" + alias + ".crs";
        try {
            File file = new File(filename);

            // Ensure that the directory exists, creating it if necessary
            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create a FileWriter object to write the contents to the file
            FileWriter writer = new FileWriter(file);

            // Write the PEM content to the file
            writer.write(pem);

            // Close the FileWriter object
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String readCrsPem(String alias){
        String filename = "src/main/resources/data/crs/" + alias + ".crs";
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(encoded);
    }
}
