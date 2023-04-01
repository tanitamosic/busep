package securityproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.repository.UserRepository;

@Service
public class ClientService {
    @Autowired
    UserRepository userRepository;
    public boolean isIdentityConfirmed(String email){
        return userRepository.isEmailConfirmed(email);
    }
}
