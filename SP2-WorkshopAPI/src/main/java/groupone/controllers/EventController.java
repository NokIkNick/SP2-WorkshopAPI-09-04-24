package groupone.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groupone.daos.EventDAO;
import groupone.dtos.EventDTO;
import groupone.dtos.EventSpecsDTO;
import groupone.dtos.LocationDTO;
import groupone.dtos.SuperEventDTO;
import groupone.model.Event;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.List;

public class EventController {
    private static EventDAO eventDAO = EventDAO.getInstance(false);
    private static ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).registerModule(new JavaTimeModule());

    public static Handler getAllEvents() {
        return ctx -> {
            List<Event> eventList = eventDAO.getAll();
            List<EventDTO> eventDTOs = eventList.stream().map(x -> new EventDTO(x.getId(), x.getImageUrl(), x.getTitle(), x.getDescription(), x.getPrice())).toList();
            List<LocationDTO> locationDTOS = eventList.stream()
                    .flatMap(event -> event.getLocations().stream()
                            .map(location -> new LocationDTO(
                                    location.getId(),
                                    location.getStreet(),
                                    new EventSpecsDTO(location.getEventSpec())
                            ))
                    )
                    .toList();
            List<SuperEventDTO> completeEvents = new ArrayList<>();
            for(EventDTO e: eventDTOs){
                SuperEventDTO event = new SuperEventDTO(e);
                for(int i = 0; i < locationDTOS.size(); i++){
                    if(e.getId() == locationDTOS.get(i).getId()){
                        event.getLocations().add(locationDTOS.get(i));
                        completeEvents.add(event);
                    }
                }
            }
            ctx.json(completeEvents);
        };
    }

    public static Handler getEventsById() {
        return ctx -> {
            Event event = eventDAO.getById(Integer.parseInt(ctx.pathParam("id")));
            ctx.json(new EventDTO(event.getId(), event.getImageUrl(), event.getTitle(), event.getDescription(), event.getPrice()));
        };
    }
}
