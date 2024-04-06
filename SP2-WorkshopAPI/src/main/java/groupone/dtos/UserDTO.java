package groupone.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private List<EventDTO> eventDTOList = new ArrayList<>();

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
        this.name = user.getName();
        this.phone = user.getPhoneNumber();
        this.roles = user.getRolesToString();
        user.getEvents().stream().map(x -> new EventDTO(this,x)).forEach(x -> eventDTOList.add(x));
    }
    public UserDTO(EventDTO eventDTO, User user ){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phone = user.getPhoneNumber();
        this.roles = user.getRolesToString();
        this.eventDTOList.add(eventDTO);
    }

}
