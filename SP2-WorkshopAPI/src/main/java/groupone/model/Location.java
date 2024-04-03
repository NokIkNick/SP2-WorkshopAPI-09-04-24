package groupone.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String street;

    @ManyToMany(mappedBy = "locations")
    private List<Event> events = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    private EventSpec eventSpec;

    @OneToMany(cascade = CascadeType.PERSIST)
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

}
