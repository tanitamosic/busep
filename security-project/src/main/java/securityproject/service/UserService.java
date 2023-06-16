package securityproject.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import securityproject.dto.FilterDTO;
import securityproject.dto.HouseResponse;
import securityproject.dto.RequestDto;
import securityproject.dto.UserResponse;
import securityproject.mail.MailingService;
import securityproject.model.home.HouseUserRole;
import securityproject.model.user.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import securityproject.model.user.Role;
import securityproject.model.user.StandardUser;
import securityproject.model.user.User;
import securityproject.repository.HouseUserRoleRepository;
import securityproject.repository.RoleRepository;
import securityproject.repository.UserRepository;
import securityproject.util.Helper;
import securityproject.util.ModelValidator;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelValidator modelValidator;

    @Autowired
    FileService fileService;
    
    @Autowired
    MailingService mailingService;

//    @Autowired
//    HomeService homeService;
    @Autowired
    HouseUserRoleRepository houseUserRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new MyUserDetails(userRepository.getUserByEmail(email));
    }

    public User getUserById(Long id){
        return userRepository.getUserById(id);
    }

    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public List<User> getAllClients(){
        List<User> allUsers = userRepository.getUsersByIsActive(true);
        List<User> clients = allUsers.stream()
                .filter(u -> u.getRoles().stream()
                        .map(r -> r.getName())
                        .anyMatch(roleName -> roleName.equals("ROLE_OWNER") || roleName.equals("ROLE_RENTER"))
                        )
                .collect(Collectors.toList());

        return clients;
    }

    public void changeClientRole(String email) throws Exception {
        User u = userRepository.getUserByEmail(email);

        if (u != null && u.getIsActive()){
            validateClientCanBeDeletedOrRoleChanged(email);

            System.out.println("roles:");
            System.out.println(u.getRoles().size());

            for (Role r : u.getRoles()){
                System.out.println(r.getName());

                if (r.getName().equals("ROLE_OWNER")){
                    u.getRoles().remove(r);
                    u.getRoles().add(roleRepository.findByName("ROLE_RENTER"));
                    break;
                } else if (r.getName().equals("ROLE_RENTER")){
                    u.getRoles().remove(r);
                    u.getRoles().add(roleRepository.findByName("ROLE_OWNER"));
                    break;
                }
            }

            userRepository.saveAndFlush(u);
        } else {
            throw new Exception("User does not exist.");
        }
    }

    public void deleteClient(String email) throws Exception {
        User u = userRepository.getUserByEmail(email);

        if (u != null && u.getIsActive()){
            validateClientCanBeDeletedOrRoleChanged(email);
            u.setIsActive(false);
            userRepository.saveAndFlush(u);
        } else {
            throw new Exception("User does not exist.");
        }
    }

    private void validateClientCanBeDeletedOrRoleChanged(String email) throws Exception {
        List<HouseUserRole> houses;
        MyUserDetails userDetails = (MyUserDetails) loadUserByUsername(email);
        Role r = (Role) userDetails.getUser().getRoles().toArray()[0];

        if (r.getName().equals("ROLE_OWNER")) {
            houses = houseUserRoleRepository.getHouseUserRolesByUserIdAndRoleAndIsActive(userDetails.getUser().getId(), "OWNER", true);
        } else {
            houses = houseUserRoleRepository.getHouseUserRolesByUserIdAndRoleAndIsActive(userDetails.getUser().getId(), "RENTER", true);
        }

        if (houses.size() > 0){
            throw new Exception("User has more than 0 objects - Can't be deleted or his role changed.");
        }
    }

    public List<User> filterClients(FilterDTO filterDTO){
        List<User> clients = getAllClients();

        // name: startsWith
        if (filterDTO.filterName != null && filterDTO.filterName.length() > 0){
            clients = clients.stream()
                    .filter(c ->
                            c.getName().toLowerCase()
                                    .startsWith(filterDTO.filterName.toLowerCase())
                            )
                    .collect(Collectors.toList());
        }

        // surname: startsWith
        if (filterDTO.filterSurname != null && filterDTO.filterSurname.length() > 0){
            clients = clients.stream()
                    .filter(c ->
                            c.getSurname().toLowerCase()
                                    .startsWith(filterDTO.filterSurname.toLowerCase())
                    )
                    .collect(Collectors.toList());
        }

        // email: contains
        if (filterDTO.filterEmail != null && filterDTO.filterEmail.length() > 0){
            clients = clients.stream()
                    .filter(c ->
                            c.getEmail().toLowerCase()
                                    .contains(filterDTO.filterEmail.toLowerCase())
                    )
                    .collect(Collectors.toList());
        }

        // role: equals
        if (filterDTO.filterRole != null && filterDTO.filterRole.length() > 0){
            clients = clients.stream()
                    .filter(c ->
                            c.getRoles().stream().map(r -> r.getName())
                                    .anyMatch(roleName ->
                                            roleName.equals("ROLE_" + filterDTO.filterRole.toUpperCase()))
                    )
                    .collect(Collectors.toList());
        }

        return clients;
    }

    public List<UserResponse> makeUserResponses(List<User> users){
        List<UserResponse> responses = users.stream()
                                            .map(u -> new UserResponse(u))
                                            .collect(Collectors.toList());
        return responses;
    }

    // DEPRECATED
    public User registerRenter(RequestDto dto) {
        modelValidator.validatePassword(dto.password);

        Role role = roleService.getRenterRole();
        StandardUser u = createStandardUser(dto, role);
        userRepository.saveAndFlush(u);
        return u;
    }


    // DEPRECATED
    public User registerOwner(RequestDto dto) {
        modelValidator.validatePassword(dto.password);

        Role role = roleService.getOwnerRole();
        StandardUser u = createStandardUser(dto, role);
        userRepository.saveAndFlush(u);
        return u;
    }

    public User registerUser(RequestDto dto) {
        modelValidator.validatePassword(dto.password);

        Role role = null;
        if (dto.owner) role = roleService.getOwnerRole();
        else role = roleService.getRenterRole();

        StandardUser u = createStandardUser(dto, role);
        userRepository.saveAndFlush(u);
        mailingService.sendValidationMail(u.getEmail(),u.getActivationString());

        return u;
    }

    private StandardUser createStandardUser(RequestDto dto, Role role) {
        StandardUser u = new StandardUser();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        u.setRoles(roles);

        u.setEmail(dto.email);
        u.setName(dto.givenName);
        u.setSurname(dto.surname);
        u.setEnabled(false);
        u.setLocked(false);
        u.setLastPasswordResetDate(null);
        u.setPassword(passwordEncoder.encode(dto.password));
        u.setActivationString(Helper.getActivationString());
        u.setPin(Helper.getPin());
        u.setFailedAttempt(0);
        return u;
    }

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }

    public void lock(User user) {
        user.setLocked(true);
        user.setLockTime(new Date());

        userRepository.saveAndFlush(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setLocked(false);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean commonPassword(String password){
        ArrayList<String> passwords = fileService.getPasswordList();
        return passwords.contains(password);
    }
    
    public boolean activateUser(String activationString){
        User user = userRepository.getUserByActivationString(activationString);
        if (user!=null)
        {
            user.setEnabled(true);
            userRepository.saveAndFlush(user);

            return true;
        } else
            return false;
    }
}
