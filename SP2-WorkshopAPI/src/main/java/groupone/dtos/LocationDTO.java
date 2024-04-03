package groupone.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LocationDTO {

    private int id;
    private String street;
    private EventSpecsDTO eventSpecifications;

    public LocationDTO(int id, String street, EventSpecsDTO eventSpecifications) {
        this.id = id;
        this.street = street;
        this.eventSpecifications = eventSpecifications;
    }
}
