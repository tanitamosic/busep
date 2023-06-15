package securityproject.dto;

import securityproject.dto.device.SignedMessageDTO;

import java.util.List;

public class HouseDTO {

    public Long id; // ne salje se pri kreaciji. salje se pri updateu
    public String ownerEmail;
    public String renterEmail;
    public String address;
    public List<SignedMessageDTO> devices;
}
