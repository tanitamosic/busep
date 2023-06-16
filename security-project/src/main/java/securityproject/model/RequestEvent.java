package securityproject.model;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
@Expires("10m")
public class RequestEvent {
    public String address;

    public RequestEvent(String address){
        this.address = address;

    }
}
