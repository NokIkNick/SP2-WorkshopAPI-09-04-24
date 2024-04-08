package groupone.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groupone.model.Location;
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
public class Location2DTO {

    private String eventName;
    private int id;
    private String street;
    private EventSpecsDTO eventSpecifications;
    private ZipcodeDTO zipcode;


    public Location2DTO(int id, String street, EventSpecsDTO eventSpecifications) {
        this.id = id;
        this.street = street;
        this.eventSpecifications = eventSpecifications;
    }


    public Location2DTO(Location location){
        setId(location.getId());
        setStreet(location.getStreet());
        setEventSpecifications(new EventSpecsDTO(location.getEventSpec()));
        setZipcode(new ZipcodeDTO(location.getZipcodes()));
        if(location.getEvents().size()>0) {
            setEventName(location.getEvents().get(0).getTitle());
        }else{
            setEventName("Undefined");
        }

    }
}
