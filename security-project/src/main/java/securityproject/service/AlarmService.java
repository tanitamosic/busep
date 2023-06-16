package securityproject.service;

import org.kie.api.KieServices;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.model.home.AlarmEvent;
import securityproject.model.home.ObjectAddedListener;
import securityproject.model.home.DeviceMessage;

import java.util.List;

@Service
public class AlarmService {


    private KieContainer kieContainer;
    private KieSession kieSession;
    private ObjectAddedListener listener;

    public AlarmService(){
        KieServices ks = KieServices.Factory.get();
        KieRepository kieRepository = ks.getRepository();
        KieContainer kContainer = ks.getKieClasspathContainer();
        kieSession = kContainer.newKieSession("alarmKsession");
        listener = new ObjectAddedListener();
        kieSession.addEventListener(listener);

    }

    public String handleMessage(DeviceMessage msg) {
        kieSession.insert(msg);
        kieSession.fireAllRules();
        List<Object> insertedObjects = listener.getInsertedObjects();
        AlarmEvent raisedAlarm;
        if (!insertedObjects.isEmpty())
            try{
                 raisedAlarm = (AlarmEvent) insertedObjects.get(0);
                 return raisedAlarm.severity.toString();
            } catch (Exception e){
                e.printStackTrace();
            }
        return "no alarm";
    }
}
