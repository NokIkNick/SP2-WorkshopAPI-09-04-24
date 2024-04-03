package groupone.model;

import groupone.enums.Status;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EventSpec {
    @Id
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private Double duration;
    private String instructorName;
    private String instructorEmail;
    private Status status;
    private Integer capacity;

    public EventSpec(LocalDate date, LocalTime time, double duration, String instructorName, String instructorEmail, Status status, Integer capacity){
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.status = status;
        this.capacity = capacity;
    }

    @MapsId("id")
    @OneToOne(mappedBy = "eventSpec", cascade = CascadeType.DETACH)
    private Location location;

    public void setLocation(Location location){
        if(this.location != location) {
            this.location = location;
            location.setEventSpec(this);
        }
    }

}
