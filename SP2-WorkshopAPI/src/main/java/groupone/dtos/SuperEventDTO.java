package groupone.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class SuperEventDTO {

    private EventDTO event;
    private List<LocationDTO> locations = new ArrayList<>();

    public SuperEventDTO(EventDTO event) {
        this.event = event;
    }
}
