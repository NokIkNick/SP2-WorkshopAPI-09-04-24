package groupone.dtos;

import groupone.model.Event;
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
    private String imageUrl;
    private List<User> users;
    private List<Location> locations;
    public EventDTO(Event event){
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.price = event.getPrice();
        this.createdAt = event.getCreatedAt();
        this.updatedAt = event.getUpdatedAt();
        this.deletedAt = event.getDeletedAt();
        this.imageUrl = event.getImageUrl();
        this.users = event.getUsers();
        this.locations = event.getLocations();
    }
}
