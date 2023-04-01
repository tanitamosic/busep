package securityproject.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.model.user.Role;
import securityproject.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role getRenterRole() {
        return roleRepository.findByName("ROLE_RENTER");
    }

    public Role getOwnerRole() {
        return roleRepository.findByName("ROLE_OWNER");
    }
}
