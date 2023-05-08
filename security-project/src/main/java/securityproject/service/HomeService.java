package securityproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.dto.DeviceDTO;
import securityproject.dto.HouseDTO;
import securityproject.model.home.Device;
import securityproject.model.home.House;
import securityproject.model.user.MyUserDetails;
import securityproject.model.user.StandardUser;
import securityproject.repository.DeviceRepository;
import securityproject.repository.HouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {
    @Autowired
    UserService userService;
    @Autowired
    HouseRepository houseRepository;
    @Autowired
    DeviceRepository deviceRepository;

    public List<House> getAllHousesWithOwner(StandardUser user) {
        return houseRepository.findHousesByOwner(user);
    }

    public List<House> getAllHousesWithRenter(StandardUser user) {
        return houseRepository.findHousesByRenter(user);
    }

    public void createHome(HouseDTO houseDTO) {
        List<Device> devices = getDevices(houseDTO);

        House h = new House();
        h.setAddress(houseDTO.address);
        h.setDevices(devices);
        h.setRenter((StandardUser) ((MyUserDetails)userService.loadUserByUsername(houseDTO.renterEmail)).getUser());
        h.setOwner((StandardUser) ((MyUserDetails)userService.loadUserByUsername(houseDTO.ownerEmail)).getUser());
        houseRepository.saveAndFlush(h);
    }

    private static List<Device> getDevices(HouseDTO houseDTO) {
        List<Device> devices = new ArrayList<>();
        for (DeviceDTO deviceDTO : houseDTO.devices) {
            Device newDevice = new Device();
            newDevice.setName(deviceDTO.name);
            devices.add(newDevice);
        }
        return devices;
    }

    public void deleteHome(Long id) {
        if (houseRepository.findById(id).isPresent())
            houseRepository.delete(houseRepository.findById(id).get());
    }

    public void deleteDevice(Long id) {
        if (deviceRepository.findById(id).isPresent())
            deviceRepository.delete(deviceRepository.findById(id).get());
    }

    public void updateHome(HouseDTO dto) {
        Optional<House> opt = houseRepository.findById(dto.id);
        if (opt.isPresent()) {
            House h = opt.get();

            StandardUser owner = (StandardUser) ((MyUserDetails)userService.loadUserByUsername(dto.ownerEmail)).getUser();
            StandardUser renter = (StandardUser) ((MyUserDetails)userService.loadUserByUsername(dto.renterEmail)).getUser();
            List<Device> devices = getDevices(dto);
            h.setOwner(owner);
            h.setRenter(renter);
            h.setAddress(dto.address);
            h.setDevices(devices);
            houseRepository.saveAndFlush(h);

        }
    }
}
