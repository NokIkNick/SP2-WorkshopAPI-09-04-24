package groupone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String street;

    public Location(String street){
        this.street = street;
    }

    @ManyToMany(mappedBy = "locations")
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH},fetch = FetchType.EAGER)
    private EventSpec eventSpec;

    @OneToMany(cascade = {/*CascadeType.PERSIST,*/CascadeType.DETACH},fetch = FetchType.EAGER)
    private List<Zipcode> zipcodes = new ArrayList<>();

    public void addEvent(Event event) {
        if(event != null && !events.contains(event)){
            events.add(event);
            event.addLocation(this);
        }
    }

    public void addZipcode(Zipcode zipcode) {
        if(zipcode != null){
            zipcodes.add(zipcode);
        }
    }
    public void setEventSpec(EventSpec eventSpec){
        if(this.eventSpec != eventSpec){
            this.eventSpec = eventSpec;
            eventSpec.setLocation(this);
        }
    }
}
