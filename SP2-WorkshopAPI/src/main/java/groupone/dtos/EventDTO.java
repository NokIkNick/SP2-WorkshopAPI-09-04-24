package groupone.dtos;

import groupone.model.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private Integer id;
    private String imageUrl;
    private String title;
    private String description;
    private Double price;

    private List<LocationDTO> locations = new ArrayList<>();

    public EventDTO(Integer id,String imageUrl,String title,String description,Double price){
        setId(id);
        setImageUrl(imageUrl);
        setTitle(title);
        setDescription(description);
        setPrice(price);
    }

    public EventDTO(Event event){
        setId(event.getId());
        setImageUrl(event.getImageUrl());
        setTitle(event.getTitle());
        setDescription(event.getDescription());
        setPrice(event.getPrice());
        event.getLocations().stream().map( x -> new LocationDTO(x)).forEach(x -> locations.add(x));
    }
}
