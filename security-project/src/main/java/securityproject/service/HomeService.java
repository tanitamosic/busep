package securityproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.model.home.House;
import securityproject.model.user.StandardUser;
import securityproject.repository.DeviceRepository;
import securityproject.repository.HouseRepository;

import java.util.List;

@Service
public class HomeService {

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
}
