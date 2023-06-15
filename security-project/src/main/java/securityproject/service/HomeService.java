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
import securityproject.model.user.Role;
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
        List<House> houses = houseRepository.findHousesByIsActive(true);
        List<HouseResponse> response = makeHouseResponses(houses);

        return response;
    }

    public HouseResponse getHouseById(Long id) {
        House house = houseRepository.findHouseByIdAndIsActive(id, true);

        if (house == null){
            return null;
        }

        return makeHouseResponse(house);
    }

    public List<HouseResponse> getAllHousesWithOwner(StandardUser user) {
        List<House> houses = getHousesByOwner(user.getId());
        List<HouseResponse> responses = makeHouseResponses(houses);

        return responses;
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

        String ownerEmail = (o != null) ? o.getEmail() : "";
        String renterEmail = (r != null) ? r.getEmail() : "";

        return new HouseResponse(house, ownerEmail, renterEmail, devices);
    }

    public HouseResponse createHome(HouseDTO houseDTO) throws Exception {
        validateUserExistanceAndRoleUponHouseCreation(houseDTO);
        House h = new House();
        h.setAddress(houseDTO.address);
        h.setIsActive(true);
        House savedH = houseRepository.saveAndFlush(h);

        if (houseDTO.ownerEmail != null && houseDTO.ownerEmail.length() > 0){
            addHouseRole(savedH.getId(), houseDTO.ownerEmail, "OWNER");
        }

        if (houseDTO.renterEmail != null && houseDTO.renterEmail.length() > 0){
            addHouseRole(savedH.getId(), houseDTO.renterEmail, "RENTER");
        }

        return makeHouseResponse(savedH);
    }

    private void validateUserExistanceAndRoleUponHouseCreation(HouseDTO houseDTO) throws Exception {
        if (houseDTO.ownerEmail != null && houseDTO.ownerEmail.length() > 0){
            validateUserExistenceAndRole(houseDTO.ownerEmail, "ROLE_OWNER");
        }
        if (houseDTO.renterEmail != null && houseDTO.renterEmail.length() > 0){
            validateUserExistenceAndRole(houseDTO.renterEmail, "ROLE_RENTER");
        }
    }

    public void deleteHome(Long id) throws Exception {
        Optional<House> _h = houseRepository.findById(id);

        if (_h.isPresent()){
            House h = _h.get();
            List<Device> devices = deviceRepository.getDevicesByHouseIdAndIsActive(h.getId(), true);

            for (Device d : devices){
                deleteDevice(d.getId());
            }

            deactivateHouseUserRole(h.getId(), "OWNER");
            deactivateHouseUserRole(h.getId(), "RENTER");

            h.setIsActive(false);
            houseRepository.saveAndFlush(h);
        } else {
            throw new Exception("No such house");
        }
    }

    public void updateHome(HouseDTO dto) throws Exception {
        House h = houseRepository.findHouseByIdAndIsActive(dto.id, true);

        if (h != null) {
            Long houseId = h.getId();

            if (dto.ownerEmail != null && dto.ownerEmail.length() > 0 && (getOwner(houseId) == null || dto.ownerEmail != getOwner(houseId).getEmail())){
                updateHouseUserRole(h.getId(), dto.ownerEmail, "OWNER");
            }

            if (dto.renterEmail != null && dto.renterEmail.length() > 0 && (getRenter(houseId) == null || dto.renterEmail != getRenter(houseId).getEmail())){
                updateHouseUserRole(h.getId(), dto.renterEmail, "RENTER");
            }

            if (dto.address != null && dto.address.length() > 0 && !h.getAddress().equals(dto.address)){
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

        if (hur != null) {
            hur.setIsActive(false);
            houseUserRoleRepository.saveAndFlush(hur);
        }
    }

    private void updateHouseUserRole(Long houseId, String userEmail, String role) throws Exception {
        validateUserExistenceAndRole(userEmail, "ROLE_" + role);
        deactivateHouseUserRole(houseId, role);
        addHouseRole(houseId, userEmail, role);
    }

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
                    .map(houseId -> houseRepository.findHouseByIdAndIsActive(houseId, true))
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

    public void deleteDevice(Long id) {
        Optional<Device> _d = deviceRepository.findById(id);

        if (_d.isPresent()){
            Device d = _d.get();
            d.setIsActive(false);
            deviceRepository.saveAndFlush(d);
        }
    }

    public void validateUserExistenceAndRole(String email, String neededRole) throws Exception {
        User u = userService.getUserByEmail(email);

        if (u == null){
            throw new Exception("Non existant user");
        } else if (!u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()).contains(neededRole)){
            throw new Exception("User has invalid role");
        }
    }
}
