package groupone.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groupone.daos.EventDAO;
import groupone.dtos.*;
import groupone.enums.Category;
import groupone.enums.Status;
import groupone.model.Event;
import groupone.model.User;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The EventController class handles the routing for event-related requests.
 * It uses an instance of EventDAO to interact with the database, and an ObjectMapper to map between objects.
 */
public class EventController {
    /**
     * A singleton instance of EventDAO used for database operations related to events.
     * The instance is fetched once and reused for all database operations.
     */
    private static EventController instance;
    private static EventDAO eventDAO;

    /**
     * An ObjectMapper used for mapping between different object types.
     * It is configured to not fail on empty beans and to support Java Time objects.
     */
    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    private static ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).registerModule(new JavaTimeModule());

    public static EventController getInstance(Boolean isTesting) {
        if (instance == null) {
            instance = new EventController();
            eventDAO = EventDAO.getInstance(isTesting);
        }
        return instance;
    }


    /**
     * Handler for the "getAllEvents" route.
     * This handler fetches all events from the database, maps them to DTOs, and sends them as the response.
     * The response includes the event details, locations, and zipcodes.
     *
     * @return A Handler that can be used with Javalin to handle requests to the "getAllEvents" route.
     */
    public Handler getAllEvents() {
        return ctx -> {
            // Fetch all events from the database
            List<Event> eventList = eventDAO.getAll();

            // Map each event to an EventDTO
            List<EventDTO> eventDTOs = eventList.stream()
                    .map(EventDTO::new)
                    .toList();
            ctx.json(eventDTOs);
            // TODO had to refactor a bit, check if you want to the below code to stay.
           /* // Map each location of each event to a LocationDTO, including the zipcodes
            List<LocationDTO> locationDTOS = eventList.stream()
                    .flatMap(event -> event.getLocations().stream()
                            .map(location -> {
                                LocationDTO locationDTO = new LocationDTO(
                                        location.getId(),
                                        location.getStreet(),
                                        new EventSpecsDTO(location.getEventSpec())
                                );
                                locationDTO.setZipcode(new ZipcodeDTO(location.getZipcodes()));
                                return locationDTO;
                            })
                    )
                    .toList();

            // Map each EventDTO to a SuperEventDTO and add the corresponding LocationDTOs
            List<SuperEventDTO> completeEvents = new ArrayList<>();
            for (EventDTO e : eventDTOs) {
                SuperEventDTO event = new SuperEventDTO(e);
                for (LocationDTO locationDTO : locationDTOS) {
                    if (e.getId() == locationDTO.getId()) {
                        event.getLocations().add(locationDTO);
                        completeEvents.add(event);
                    }
                }
            }

            // Send the list of SuperEventDTOs as the response
            ctx.json(completeEvents);*/
        };
    }

    /**
     * Handler for the "getEventsById" route.
     * This handler fetches an event from the database by its ID, maps it to a DTO, and sends it as the response.
     * The response includes the event details.
     *
     * @return A Handler that can be used with Javalin to handle requests to the "getEventsById" route.
     */
    public Handler getEventById() {
        return ctx -> {
            // Fetch the event from the database by its ID
            int id = Integer.parseInt(ctx.pathParam("id"));
            Event event = eventDAO.getById(id);
            EventDTO eventDTO = new EventDTO(event);
            ctx.json(eventDTO);
            // TODO had to refactor a bit, check if you want to the below code to stay.
           /* // Map the event to an EventDTO
            EventDTO eventDTO = new EventDTO(event.getId(), event.getImageUrl(), event.getTitle(), event.getDescription(), event.getPrice());
            // Map each location of the event to a LocationDTO, including the zipcodes
            List<LocationDTO> locationDTOS = event.getLocations().stream()
                    .map(location -> {
                        LocationDTO locationDTO = new LocationDTO(
                                location.getId(),
                                location.getStreet(),
                                new EventSpecsDTO(location.getEventSpec())
                        );
                        locationDTO.setZipcode(new ZipcodeDTO(location.getZipcodes()));
                        return locationDTO;
                    })
                    .toList();

            // Create a SuperEventDTO and set the EventDTO and LocationDTOs
            SuperEventDTO superEventDTO = new SuperEventDTO(eventDTO);
            superEventDTO.setLocations(locationDTOS);

            // Send the SuperEventDTO as the response
            ctx.json(superEventDTO);*/
        };
    }

    public Handler getEventsByCategory() {
        return ctx -> {
            String category = (ctx.pathParam("category"));
            List<Event> eventList = eventDAO.getEventsByCategory(Category.valueOf(category.toUpperCase()));
            List<EventDTO> eventDTOList = eventList.stream().map(event -> new EventDTO(event)).collect(Collectors.toList());
            ctx.json(eventDTOList);

            // TODO had to refactor a bit, check if you want to the below code to stay.
            /* // Map each event to an EventDTO
            List<EventDTO> eventDTOs = eventList.stream()
                    .map(EventDTO::new)
                    .toList();

            // Map each location of each event to a LocationDTO, including the zipcodes
            List<LocationDTO> locationDTOS = eventList.stream()
                    .flatMap(event -> event.getLocations().stream()
                            .map(location -> {
                                LocationDTO locationDTO = new LocationDTO(
                                        location.getId(),
                                        location.getStreet(),
                                        new EventSpecsDTO(location.getEventSpec())
                                );
                                locationDTO.setZipcode(new ZipcodeDTO(location.getZipcodes()));
                                return locationDTO;
                            })
                    )
                    .toList();

            // Map each EventDTO to a SuperEventDTO and add the corresponding LocationDTOs
            List<SuperEventDTO> completeEvents = new ArrayList<>();
            for (EventDTO e : eventDTOs) {
                SuperEventDTO event = new SuperEventDTO(e);
                for (LocationDTO locationDTO : locationDTOS) {
                    if (e.getId() == locationDTO.getId()) {
                        event.getLocations().add(locationDTO);
                        completeEvents.add(event);
                    }
                }
            }

            // Send the list of SuperEventDTOs as the response
            ctx.json(completeEvents);*/
        };
    }

    public Handler getEventsByStatus() {
        return ctx -> {
            String status = (ctx.pathParam("status"));
            List<Event> eventList = eventDAO.getEventsByStatus(Status.valueOf(status.toUpperCase()));
            List<EventDTO> eventDTOList = eventList.stream().map(event -> new EventDTO(event)).collect(Collectors.toList());
            ctx.json(eventDTOList);
            // TODO had to refactor a bit, check if you want to the below code to stay.
           /* // Map each event to an EventDTO
            List<EventDTO> eventDTOs = eventList.stream()
                    .map(EventDTO::new)
                    .toList();

            // Map each location of each event to a LocationDTO, including the zipcodes
            List<LocationDTO> locationDTOS = eventList.stream()
                    .flatMap(event -> event.getLocations().stream()
                            .map(location -> {
                                LocationDTO locationDTO = new LocationDTO(
                                        location.getId(),
                                        location.getStreet(),
                                        new EventSpecsDTO(location.getEventSpec())
                                );
                                locationDTO.setZipcode(new ZipcodeDTO(location.getZipcodes()));
                                return locationDTO;
                            })
                    )
                    .toList();

            // Map each EventDTO to a SuperEventDTO and add the corresponding LocationDTOs
            List<SuperEventDTO> completeEvents = new ArrayList<>();
            for (EventDTO e : eventDTOs) {
                SuperEventDTO event = new SuperEventDTO(e);
                for (LocationDTO locationDTO : locationDTOS) {
                    if (e.getId() == locationDTO.getId()) {
                        event.getLocations().add(locationDTO);
                        completeEvents.add(event);
                    }
                }
            }

            // Send the list of SuperEventDTOs as the response
            ctx.json(completeEvents);*/
        };
    }

    public Handler createEvent() {
        return ctx -> {
            Event event = ctx.bodyAsClass(Event.class);
            // TODO remove below code its not needed
           /* for (int i = 0; i < event.getLocations().size(); i++) {
                event.getLocations().get(i).getEventSpec().setLocation(event.getLocations().get(i));*/
                eventDAO.create(event);
                EventDTO eventDTO = new EventDTO(event);
                ctx.status(200);
                ctx.json(eventDTO);
            //}
        
        };

    }

    public Handler getUpcomingEvents() {
        return ctx -> {
            String status = "upcoming";
            List<Event> eventList = eventDAO.getEventsByStatus(Status.valueOf(status.toUpperCase()));
            List<EventDTO> eventDTOList = eventList.stream().map(event -> new EventDTO(event)).collect(Collectors.toList());
            ctx.json(eventDTOList);
        };
    }

    public Handler getEventByIdsParticipants() {
        return ctx -> {
            String event = (ctx.pathParam("id"));
            int eventID;
            try {
                eventID = Integer.parseInt(event);
            } catch (NumberFormatException e) {
                ctx.status(HttpStatus.BAD_GATEWAY);
                return;
            }
            Event FoundEvent = eventDAO.getById(eventID, (ev) -> {
                Hibernate.initialize(ev.getUsers()); // initialize users
                for(User u : ev.getUsers()) {
                    Hibernate.initialize(u.getRoles());
                }
            }); // toString so everything being part of toString on users is also initialized.
            if(FoundEvent == null) {
                ctx.status(HttpStatus.NOT_FOUND);
                return;
            }
            List<UserDTO> users = FoundEvent.getUsers().stream().map(UserDTO::new).collect(Collectors.toList());
            ctx.json(users);
        };
    }
}

