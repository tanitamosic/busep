package securityproject.dto;

import securityproject.model.user.User;

import java.util.stream.Collectors;

public class UserResponse {
    public String name;
    public String surname;
    public String email;
    public String role;

    public UserResponse(User u){
        this.name = u.getName();
        this.surname = u.getSurname();
        this.email = u.getEmail();



        if (u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()).contains("ROLE_RENTER")){
            this.role = "RENTER";
        } else if (u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()).contains("ROLE_OWNER")){
            this.role = "OWNER";
        }
    }
}
