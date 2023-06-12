package securityproject.dto;

import securityproject.model.home.Device;
import securityproject.model.home.House;
import securityproject.model.user.StandardUser;

import javax.persistence.Column;
import java.util.List;

public class HouseResponse {

    public HouseResponse(House h, String ownerEmail, String renterEmail, List<Device> devices){
        this.id = h.getId();
        this.address = h.getAddress();
        this.isActive = h.getIsActive();
        this.ownerEmail = ownerEmail;
        this.renterEmail = renterEmail;
        this.devices = devices;
    }

    private Long id;
    private String ownerEmail;
    private String renterEmail;
    private String address;
    private List<Device> devices;
    private Boolean isActive;
}
