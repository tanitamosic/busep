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

    public void sendRegAcceptMail(String target) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(target);
        email.setSubject("Obaveštenje o registraciji na React Certificate Factory");
        email.setText("Čestitamo,\r\n Vaš zahtev za registraciju je prihvaćen!\r\nDobro došli u React Certificate Factory!!");
        mailSender.send(email);
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

    public void sendCertificate(String target, String certPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(target);
            helper.setSubject("Stigo Vam je sertifikat");
            helper.setText("Preuzmite ga da bi ste postali pocasni clan React Certificate Factory-ja!!!! <3\r\n Much love <3");

            Path path = Paths.get(certPath);
            byte[] fileData = Files.readAllBytes(path);
            InputStreamSource attachmentSource = new ByteArrayResource(fileData);
            helper.addAttachment(path.getFileName().toString(), attachmentSource);

            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}