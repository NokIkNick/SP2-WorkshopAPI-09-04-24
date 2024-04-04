package groupone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    private String name;

    @ManyToMany
    @JsonIgnore
    List<User> users = new ArrayList<>();

    public Role(String name){
        this.name = name;
    }

    public void addUser(User user) {
        if(user != null && !this.users.contains(user)){
            this.users.add(user);
            user.addRole(this);
        }
    }

}
