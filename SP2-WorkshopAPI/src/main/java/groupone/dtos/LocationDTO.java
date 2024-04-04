package groupone.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationDTO {

    private int id;
    private String street;
    private EventSpecsDTO eventSpecifications;
    private ZipcodeDTO zipcode;

    public LocationDTO(int id, String street, EventSpecsDTO eventSpecifications) {
        this.id = id;
        this.street = street;
        this.eventSpecifications = eventSpecifications;
    }
}
