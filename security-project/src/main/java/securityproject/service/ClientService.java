package securityproject.service;

import org.springframework.stereotype.Service;

@Service
public class ClientService {
    public boolean isIdentityConfirmed(String email){
        return true; //TODO: treba da vuce enabled iz baze
    }
}
