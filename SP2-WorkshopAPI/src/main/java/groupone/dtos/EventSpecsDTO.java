package groupone.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groupone.enums.Status;
import groupone.model.EventSpec;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventSpecsDTO {

    private int id;
    private String date;
    private String time;
    private double duration;
    private String instructorName;
    private String instructorEmail;
    private Status status;
    private Integer capacity;

    public EventSpecsDTO(EventSpec e) {
        this.id = e.getId();
        this.date = e.getDate().toString();
        this.time = e.getTime().toString();
        this.duration = e.getDuration();
        this.instructorName = e.getInstructorName();
        this.instructorEmail = e.getInstructorEmail();
        this.status = e.getStatus();
        this.capacity = e.getCapacity();
    }
}
