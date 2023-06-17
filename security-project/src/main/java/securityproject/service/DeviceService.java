package securityproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import securityproject.dto.device.SignedMessageDTO;
import securityproject.dto.device.MessageToVerify;
import securityproject.model.home.Device;
import securityproject.model.logs.DeviceLog;
import securityproject.model.enums.LogType;
import securityproject.model.user.User;
import securityproject.repository.DeviceRepository;
import securityproject.repository.mongo.DeviceLogRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.Signature;
import java.util.List;
import java.util.Objects;


@Service
public class DeviceService {

    Logger logger = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    DeviceLogRepository logRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    HomeService homeService;
    @Autowired
    public CustomAlarmService alarmService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    UserService userService;

    static Path publicKeyPath = Paths.get("src/main/resources/keys/public_device_key.pem");
    static String publicKeyPEM;
    static PublicKey publicKey;

    static {
        try {
            publicKeyPEM = Files.readString(publicKeyPath);
            PEMParser pemParser = new PEMParser(new StringReader(publicKeyPEM));
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            Object obj = pemParser.readObject();
            if (obj instanceof PEMKeyPair) {
                publicKey = converter.getPublicKey(((PEMKeyPair) obj).getPublicKeyInfo());
            } else if (obj instanceof SubjectPublicKeyInfo) {
                publicKey = converter.getPublicKey((SubjectPublicKeyInfo) obj);
            } else {
                throw new RuntimeException("Unsupported key format");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify(String payload, String signatureHex) {

        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(payload.getBytes(StandardCharsets.UTF_8));

            byte[] signatureBytes = hexStringToByteArray(signatureHex);

            return signature.verify(signatureBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }

    public void handleDeviceMessage(HttpServletRequest request, SignedMessageDTO payload) {
        String message = dtoToString(payload);
        if (null == message) {
            logger.error("Device message serialization failed; deviceId: {}", payload.deviceId);
            return;
        }
        if (verify(message, payload.signature)) {
            DeviceLog log = new DeviceLog(request, payload.logType, payload.message, payload.timestamp, payload.deviceId, payload.deviceType, payload.houseId);
            logRepository.insert(log);
            sendLog(log);
            logger.info("Inserted " +payload.logType + " device log; deviceId: {}", payload.deviceId);
            alarmService.handleDeviceLog(log);
        } else {
            DeviceLog log = new DeviceLog(request, LogType.ERROR, "INVALID SIGNATURE", payload.timestamp, payload.deviceId,payload.deviceType, payload.houseId);
            logRepository.insert(log);
            sendLog(log);
            logger.error("Inserted ERROR device log; INVALID SIGNATURE; deviceId: {}", payload.deviceId);
            alarmService.handleDeviceLog(log);
        }
    }

    private String dtoToString(SignedMessageDTO dto) {
        MessageToVerify mtv = new MessageToVerify(dto);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(mtv);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendLog(DeviceLog log) {
        Device device = deviceRepository.getDeviceById(log.getDeviceId());
        User owner = homeService.getOwner(device.getHouseId());
        User renter = homeService.getRenter(device.getHouseId());
        List<User> admins = userService.getAllAdmins();
        String message = Objects.requireNonNull(convertToJson(log));
        messagingTemplate.convertAndSend("/topic/log/" + owner.getEmail(), message);
        messagingTemplate.convertAndSend("/topic/log/" + renter.getEmail(), message);
        for (User u: admins) {
            messagingTemplate.convertAndSend("/topic/log/" + u.getEmail(), message);
        }
    }

    public static String convertToJson(Object obj) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
