package securityproject.dto;

import securityproject.model.user.User;

public class UserResponse {
    public String name;
    public String surname;
    public String email;

    public UserResponse(User u){
        this.name = u.getName();
        this.surname = u.getSurname();
        this.email = u.getEmail();
    }
}
