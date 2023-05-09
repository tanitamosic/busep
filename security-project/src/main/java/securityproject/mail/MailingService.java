package securityproject.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class MailingService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendAccDeletionMail(String target) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(target);
        email.setSubject("Brisanje naloga uspešno");
        email.setText("Vaš zahtev za brisanje naloga je prihvaćen!\r\nŽao nam je što nam odlazite.\r\nPoz");
        mailSender.send(email);
    }

    public void sendRegRejectMail(String target, String reason) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(target);
        email.setSubject("Obaveštenje o registraciji na React Certificate Factory");
        email.setText("Na žalost\r\n, Vaš zahtev za registraciju je odbijen iz navedenih razloga:\r\n" + reason);
        mailSender.send(email);
    }

    public void sendValidationMail(String target, String activationString) {

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setTo(target);
            helper.setSubject("Obaveštenje o registraciji na React Certificate Factory");
            String content = "Dobrodošli u React Certificate Factory!\n\n" +
                    "Molimo Vas da potvrdite Vašu email adresu klikom na link: \n\n";
            String button = "<a href=\"http://localhost:8081/csr/confirm-registration/"+ activationString +"\">\n" +
                    "    <button>Activate Account</button>\n" +
                    "  </a>";
            helper.setText(content + button, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        mailSender.send(message);
    }

    public void sendRegAuthMail(String target, String confNum) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(target);
        email.setSubject("Verifikacioni broj za React Certificate Factory");
        email.setText("Poštovani,\r\n Vaš verifikacioni broj je " + confNum + "." +
                "\r\nDobro došli u Triangular Drive!");
        mailSender.send(email);
    }

    public void sendDeletionNotificationMail(String target) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(target);
        email.setSubject("Obaveštenje - Sertifikat povucen");
        email.setText("Poštovani,\r\nAdministrator sistema je odlučio da vam obriše acc, cuz duhhh.\r\nCya Never Loserrr,\r\nPoz");
        mailSender.send(email);
    }

    public void sendCertificate(String target, String pin) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(target);
            helper.setSubject("Stigo Vam je sertifikat");
            helper.setText("Preuzmite ga da bi ste postali pocasni clan React Certificate Factory-ja!!!! <3\r\n " +
                    "Vas pin je: " + pin +".\n" +
                    "Much love <3");

            Path path = Paths.get("src/main/resources/data/cer/"+target+".cer");
            byte[] fileData = Files.readAllBytes(path);
            InputStreamSource attachmentSource = new ByteArrayResource(fileData);
            helper.addAttachment(path.getFileName().toString(), attachmentSource);

            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}