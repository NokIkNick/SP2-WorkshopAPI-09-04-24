package groupone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groupone.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private String email;
    private String password;
    private String name;
    private Integer phoneNumber;

    @ManyToMany(cascade = {CascadeType.DETACH/*,CascadeType.PERSIST*/}, mappedBy = "users",fetch = FetchType.EAGER)
    Set<Role> roles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH}, mappedBy = "users",fetch = FetchType.EAGER)
    Set<Event> events = new HashSet<>();


    @PrePersist
    private void PrePersist(){
        //this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User(UserDTO userDTO){
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
        this.name = userDTO.getName();
        this.phoneNumber = userDTO.getPhone();
    }

    // test constructor
    public User(String email, String password, String name, Integer phoneNumber,Role role){
        this.email = email;
        setPassword(password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        addRole(role);
    }
    public void setPassword(String password){
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    @JsonIgnore
    public Set<String> getRolesToString(){
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    public boolean verifyPassword(String password){
        return BCrypt.checkpw(password, this.password);
    }


    public void addRole(Role role){
        if(role != null && !roles.contains(role)){
            roles.add(role);
            role.addUser(this);
        }
    }

    public void addEvent(Event event) {
            if(event != null && !events.contains(event)){
                events.add(event);
                event.addUser(this);
            }
    }
}
