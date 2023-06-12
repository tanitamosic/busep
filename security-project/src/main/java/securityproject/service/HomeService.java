package securityproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.dto.DeviceDTO;
import securityproject.dto.DevicesDTO;
import securityproject.dto.HouseDTO;
import securityproject.dto.HouseResponse;
import securityproject.model.home.Device;
import securityproject.model.home.House;
import securityproject.model.home.HouseUserRole;
import securityproject.model.user.MyUserDetails;
import securityproject.model.user.StandardUser;
import securityproject.model.user.User;
import securityproject.repository.DeviceRepository;
import securityproject.repository.HouseRepository;
import securityproject.repository.HouseUserRoleRepository;
import securityproject.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeService {
    @Autowired
    UserService userService;
    @Autowired
    HouseRepository houseRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    HouseUserRoleRepository houseUserRoleRepository;
    @Autowired
    private UserRepository userRepository;

    public List<HouseResponse> getAllHouses() {
        return makeHouseResponses(houseRepository.findAll());
    }

    public List<HouseResponse> getAllHousesWithOwner(StandardUser user) {
        return makeHouseResponses(getHousesByOwner(user.getId()));
    }

    public List<HouseResponse> getAllHousesWithRenter(StandardUser user) {
        return makeHouseResponses(getHousesByRenter(user.getId()));
    }

    public List<HouseResponse> makeHouseResponses(List<House> houses){
        return houses.stream().map(h -> makeHouseResponse(h)).collect(Collectors.toList());
    }

    public HouseResponse makeHouseResponse(House house){
        User o = getOwner(house.getId());
        User r = getRenter(house.getId());
        List<Device> devices = getHouseDevices(house.getId());

        return new HouseResponse(house, o.getEmail(), r.getEmail(), devices);
    }

    public HouseResponse createHome(HouseDTO houseDTO) {
        House h = new House();
        h.setAddress(houseDTO.address);
        h.setIsActive(true);
        House savedH = houseRepository.saveAndFlush(h);
        addHouseRole(savedH.getId(), houseDTO.ownerEmail, "OWNER");
        addHouseRole(savedH.getId(), houseDTO.renterEmail, "RENTER");

        return getHouseResponse(savedH.getId());
    }

    public HouseResponse getHouseResponse(Long houseId){
        House h = houseRepository.getReferenceById(houseId);

        if (h == null){
            return null;
        }

        User o = getOwner(houseId);
        User r = getRenter(houseId);
        List<Device> devices = getHouseDevices(houseId);

        return new HouseResponse(h, o.getEmail(), r.getEmail(), devices);
    }

//    private static List<Device> parseDevices(HouseDTO houseDTO) {
//        List<Device> devices = new ArrayList<>();
//
//        for (DeviceDTO deviceDTO : houseDTO.devices) {
//            Device newDevice = new Device();
//            newDevice.setName(deviceDTO.name);
//            devices.add(newDevice);
//        }
//
//        return devices;
//    }

    public void deleteHome(Long id) throws Exception {
        Optional<House> _h = houseRepository.findById(id);

        if (_h.isPresent()){
            House h = _h.get();
            List<Device> devices = deviceRepository.getDevicesByHouseIdAndIsActive(h.getId(), true);

            for (Device d : devices){
                deleteDevice(d.getId());
            }

            h.setIsActive(false);
            houseRepository.saveAndFlush(h);
        } else {
            throw new Exception("No such house");
        }
    }



    public void updateHome(HouseDTO dto) {
        Optional<House> opt = houseRepository.findById(dto.id);
        if (opt.isPresent()) {
            House h = opt.get();
            Long houseId = h.getId();

            if (dto.ownerEmail != getOwner(houseId).getEmail()){
                updateHouseUserRole(h.getId(), dto.ownerEmail, "OWNER");
            }

            if (dto.renterEmail != getRenter(houseId).getEmail()){
                updateHouseUserRole(h.getId(), dto.renterEmail, "RENTER");
            }

            if (!h.getAddress().equals(dto.address)){
                h.setAddress(dto.address);
            }

            houseRepository.saveAndFlush(h);
        }
    }

    private void addHouseRole(Long houseId, String userEmail, String role){
        if (userEmail != null && userEmail.length() > 0){
            User u = userRepository.getUserByEmail(userEmail);

            if (u != null){
                HouseUserRole hur = new HouseUserRole();
                hur.setHouseId(houseId);
                hur.setUserId(u.getId());
                hur.setRole(role);
                hur.setIsActive(true);
                houseUserRoleRepository.saveAndFlush(hur);
            }
        }
    }

    public void deactivateHouseUserRole(Long houseId, String role){
        HouseUserRole hur;
        if ("OWNER".equals(role)){
            hur = getHouseUserRoleOwner(houseId);
        } else {
            hur = getHouseUserRoleRenter(houseId);
        }

        hur.setIsActive(false);
        houseUserRoleRepository.saveAndFlush(hur);
    }

    private void updateHouseUserRole(Long houseId, String userEmail, String role){
        deactivateHouseUserRole(houseId, role);
        addHouseRole(houseId, userEmail, role);
    }

//    public HouseResponse createHome(HouseDTO houseDTO) {
//        House h = new House();
//        h.setAddress(houseDTO.address);
//        h.setIsActive(true);
//        House savedH = houseRepository.saveAndFlush(h);
//        addHouseRole(savedH.getId(), houseDTO.ownerEmail, "OWNER");
//        addHouseRole(savedH.getId(), houseDTO.renterEmail, "RENTER");
//
//        return getHouseResponse(savedH.getId());
//    }

    public User getOwner(Long houseId){
        User o = null;
        HouseUserRole hur = getHouseUserRoleOwner(houseId);

        if (hur != null){
            Long userId = hur.getUserId();
            o = userService.getUserById(userId);
        }

        return o;
    }

    public User getRenter(Long houseId){
        User o = null;
        HouseUserRole hur = getHouseUserRoleRenter(houseId);

        if (hur != null){
            Long userId = hur.getUserId();
            o = userService.getUserById(userId);
        }

        return o;
    }

    private HouseUserRole getHouseUserRoleOwner(Long houseId){
        return houseUserRoleRepository.getHouseUserRoleByHouseIdAndRoleAndIsActive(houseId, "OWNER", true);
    }

    private HouseUserRole getHouseUserRoleRenter(Long houseId){
        return houseUserRoleRepository.getHouseUserRoleByHouseIdAndRoleAndIsActive(houseId, "RENTER", true);
    }

    public List<House> getHousesByOwner(Long userId){
        List<House> houses = new ArrayList<>();
        List<HouseUserRole> hurs = getHouseUserRolesByUserIdOwner(userId);

        if (hurs.size() > 0){
            houses = hurs.stream()
                    .map(hur -> hur.getHouseId())
                    .map(houseId -> houseRepository.getReferenceById(houseId))
                    .collect(Collectors.toList());
        }

        return houses;
    }

    public List<House> getHousesByRenter(Long userId){
        List<House> houses = new ArrayList<>();
        List<HouseUserRole> hurs = getHouseUserRolesByUserIdRenter(userId);

        if (hurs.size() > 0){
            houses = hurs.stream()
                    .map(hur -> hur.getHouseId())
                    .map(houseId -> houseRepository.getReferenceById(houseId))
                    .collect(Collectors.toList());
        }

        return houses;
    }

    private List<HouseUserRole> getHouseUserRolesByUserIdOwner(Long userId){
        return houseUserRoleRepository.getHouseUserRolesByUserIdAndRoleAndIsActive(userId, "OWNER", true);
    }

    private List<HouseUserRole> getHouseUserRolesByUserIdRenter(Long userId){
        return houseUserRoleRepository.getHouseUserRolesByUserIdAndRoleAndIsActive(userId, "RENTER", true);
    }

    public List<Device> getHouseDevices(Long houseId) {
        List<Device> devices = deviceRepository.getDevicesByHouseIdAndIsActive(houseId, true);

        return devices;
    }

    // devices

    public Device addDevice(DeviceDTO deviceDTO){
        Device d = new Device();
        d.setHouseId(deviceDTO.houseId);
        d.setName(deviceDTO.name);
        d.setType(deviceDTO.type);
        d.setFilterRegex(deviceDTO.filterRegex);
        d.setReadTime(deviceDTO.readTime);
        d.setIsActive(true);

        return deviceRepository.saveAndFlush(d);
    }

    public void addHouseDevices(Long houseId, DevicesDTO devicesDTO) {
        for (DeviceDTO dto : devicesDTO.deviceDTOs){
            Device d = new Device(dto);

            if (d.getHouseId() == null){
                d.setHouseId(houseId);
            }

            deviceRepository.save(d);
        }
    }

    public void deleteDevice(Long id) {
        Optional<Device> _d = deviceRepository.findById(id);

        if (_d.isPresent()){
            Device d = _d.get();
            d.setIsActive(false);
            deviceRepository.saveAndFlush(d);
        }
    }
}
