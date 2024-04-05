package groupone.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groupone.model.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import groupone.model.Location;
import groupone.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EventDTO {


    private Integer id;
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;

    private List<UserDTO> users = new ArrayList<>();

    private List<LocationDTO> locations = new ArrayList<>();

    public EventDTO(Integer id, String imageUrl, String title, String description, Double price) {
        setId(id);
        setImageUrl(imageUrl);
        setTitle(title);
        setDescription(description);
        setPrice(price);
    }

    public EventDTO(Event event) {
        setId(event.getId());
        setImageUrl(event.getImageUrl());
        setTitle(event.getTitle());
        setDescription(event.getDescription());
        setPrice(event.getPrice());
        setCreatedAt(event.getCreatedAt());
        setUpdatedAt(event.getUpdatedAt());
        setDeletedAt(event.getDeletedAt());
        event.getUsers().stream().map(x -> new UserDTO(this,x)).forEach(x -> users.add(x));
        event.getLocations().stream().map(x -> new LocationDTO(x)).forEach(x -> locations.add(x));
    }

    public EventDTO(UserDTO userDTO,Event event){
        setId(event.getId());
        setImageUrl(event.getImageUrl());
        setTitle(event.getTitle());
        setDescription(event.getDescription());
        setPrice(event.getPrice());
        setCreatedAt(event.getCreatedAt());
        setUpdatedAt(event.getUpdatedAt());
        setDeletedAt(event.getDeletedAt());
        this.users.add(userDTO);
        event.getLocations().stream().map(x -> new LocationDTO(x)).forEach(x -> locations.add(x));
    }

}
