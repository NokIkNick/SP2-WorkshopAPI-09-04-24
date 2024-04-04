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
    private LocalTime time;
    private Double duration;
    private String instructorName;
    private String instructorEmail;
    private Status status;
    private Integer capacity;

    @MapsId("id")
    @OneToOne(mappedBy = "eventSpec", cascade = CascadeType.DETACH)
    private Location location;

    public enum Status{
        ONGOING,
        CANCELLED,
        UPCOMING,
        ENDED,
        TBD
    }
}
