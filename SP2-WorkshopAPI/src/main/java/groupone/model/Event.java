package groupone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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



    @ManyToMany(cascade ={ CascadeType.DETACH,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH, CascadeType.MERGE},fetch = FetchType.EAGER)
    private List<Location> locations = new ArrayList<>();

    public Event( String title,String description,double price,String imageUrl){
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void addUser(User user){
        if(user != null && !this.users.contains(user)){
            this.users.add(user);
            user.addEvent(this);
        }
    }

    public void removeUser(User user){
        if(user != null && this.users.contains(user)){
            this.users.remove(user);
            user.removeEvent(this);
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Event event = (Event) o;
        return getId() != null && Objects.equals(getId(), event.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
