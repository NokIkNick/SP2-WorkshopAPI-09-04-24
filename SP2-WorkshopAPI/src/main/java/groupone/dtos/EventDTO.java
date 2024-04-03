package groupone.dtos;

import groupone.model.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class EventDTO {


    private Integer id;
    private String title;
    private String description;
    private Double price;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;
    private String imageUrl;
    public EventDTO(Event event){
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.price = event.getPrice();
        this.createdAt = event.getCreatedAt();
        this.updatedAt = event.getUpdatedAt();
        this.deletedAt = event.getDeletedAt();
        this.imageUrl = event.getImageUrl();
    }

}
