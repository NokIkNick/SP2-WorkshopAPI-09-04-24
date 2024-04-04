package groupone.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    private LocalTime time = LocalTime.of(16,30);
    private Double duration;
    private String instructorName;
    private String instructorEmail;
    private Status status;
    private Integer capacity;
    private Category category;


    @OneToOne(cascade = CascadeType.DETACH)
    private Location location;

    public enum Status{
        ONGOING,
        CANCELLED,
        UPCOMING,
        ENDED,
        TBD
    }

    public enum Category{
        EVENT,
        WORKSHOP,
        OTHER
    }
}
