package securityproject.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import securityproject.dto.RequestDto;
import securityproject.model.user.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import securityproject.model.user.Role;
import securityproject.model.user.StandardUser;
import securityproject.model.user.User;
import securityproject.repository.UserRepository;
import securityproject.util.ModelValidator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelValidator modelValidator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new MyUserDetails(userRepository.getUserByEmail(email));
    }

    public User registerRenter(RequestDto dto) {
        modelValidator.validatePassword(dto.password);

        Role role = roleService.getRenterRole();
        StandardUser u = createStandardUser(dto, role);
        userRepository.saveAndFlush(u);
        return u;
    }

    public User registerOwner(RequestDto dto) {
        modelValidator.validatePassword(dto.password);

        Role role = roleService.getOwnerRole();
        StandardUser u = createStandardUser(dto, role);
        userRepository.saveAndFlush(u);
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
}
