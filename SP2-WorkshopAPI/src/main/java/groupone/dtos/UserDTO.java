package groupone.dtos;

import groupone.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String email;
    private String password;
    private Integer phone;
    private String name;
    private Set<String> roles;

    private List<EventDTO> evetDTOList = new ArrayList<>();

    public UserDTO(String email, String password, Set<String> roles){
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserDTO(String email, Set<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public UserDTO(User user){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phone = user.getPhoneNumber();
        this.roles = user.getRolesToString();
        user.getEvents().stream().map(x -> new EventDTO(x)).forEach(x -> evetDTOList.add(x));
    }

}
