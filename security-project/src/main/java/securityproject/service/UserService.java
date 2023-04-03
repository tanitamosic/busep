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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new MyUserDetails(userRepository.getUserByEmail(email));
    }

    public User registerRenter(RequestDto dto) {
        Role role = roleService.getRenterRole();
        User u = createUser(dto, role);
        userRepository.saveAndFlush(u);
        return u;
    }

    public User registerOwner(RequestDto dto) {
        Role role = roleService.getOwnerRole();
        User u = createUser(dto, role);
        userRepository.saveAndFlush(u);
        return u;
    }

    private User createUser(RequestDto dto, Role role) {
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
}
