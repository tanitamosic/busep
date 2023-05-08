package securityproject.dto;

import java.util.List;

public class HouseDTO {

    public Long id; // ne salje se pri kreaciji. salje se pri updateu
    public String ownerEmail;
    public String renterEmail;
    public String address;
    public List<DeviceDTO> devices;
}
