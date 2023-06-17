package securityproject.service;

import org.checkerframework.checker.units.qual.A;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.runtime.conf.ForceEagerActivationOption;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.model.alarms.CustomAlarm;
import securityproject.model.logs.DeviceLog;
import securityproject.repository.CustomAlarmRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomAlarmService {

    private final String TEMPLATE_PATH = "src/main/resources/rules/custom/custom_alarm.drt";
    private String drl;
    private KieSession kieSession;
    @Autowired
    AlarmService alarmService;

    CustomAlarmRepository alarmRepository;


    public CustomAlarmService(CustomAlarmRepository alarmRepository) throws FileNotFoundException {
        this.alarmRepository = alarmRepository;
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        File file = new File(TEMPLATE_PATH);
        InputStream template = new FileInputStream(file);
        ObjectDataCompiler converter = new ObjectDataCompiler();
        List<CustomAlarm> data = alarmRepository.findAll();
        drl = converter.compile(data, template);
//        System.out.println(drl);

        kieSession = createKieSessionFromDRL(drl);
        kieSession.setGlobal("alarmService", alarmService);
    }

    private void restartSession() throws FileNotFoundException {
        if(kieSession!=null) kieSession.dispose();

        File file = new File(TEMPLATE_PATH);
        InputStream template = new FileInputStream(file);
        ObjectDataCompiler converter = new ObjectDataCompiler();
        List<CustomAlarm> data = alarmRepository.findAll();
        String drl = converter.compile(data, template);
//        System.out.println(drl);

        kieSession = createKieSessionFromDRL(drl);
        kieSession.setGlobal("alarmService", alarmService);
    }

    public void newCustomAlarm(CustomAlarm alarm) throws FileNotFoundException {
        alarmRepository.saveAndFlush(alarm);
        restartSession();
    }

    public void handleDeviceLog(DeviceLog msg) {
        kieSession.insert(msg);
        kieSession.fireAllRules();
        alarmService.handleDeviceLog(msg);
    }

    private KieSession createKieSessionFromDRL(String drl) {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

        Results results = kieHelper.verify();

        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: " + message.getText());
            }

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        KieBaseConfiguration kieBaseConfig = kieHelper.ks.newKieBaseConfiguration();
        kieBaseConfig.setOption(EventProcessingOption.STREAM);

        return kieHelper.build(kieBaseConfig).newKieSession();
    }
}