package groupone.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;
    private Double price;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;
    private String imageUrl;


    @ManyToMany
    private List<User> users = new ArrayList<>();

    @ManyToMany
    private List<Location> locations = new ArrayList<>();

    public Event( String title,String description,double price,String imageUrl){
        this.title = title;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageUrl = imageUrl;
    }

    public void addUser(User user){
        if(user != null && !users.contains(user)){
            users.add(user);
            user.addEvent(this);
        }
    }

    public void addLocation(Location location) {
        if(location != null && !locations.contains(location)){
            locations.add(location);
            location.addEvent(this);
        }
    }
    @PrePersist
    public void prePersist(){
        if(this.createdAt == null) {
            this.createdAt = LocalDate.now();
        }
        this.updatedAt = LocalDate.now();
    }
    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDate.now();
    }

}
