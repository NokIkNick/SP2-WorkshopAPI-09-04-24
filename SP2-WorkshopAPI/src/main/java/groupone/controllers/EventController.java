package groupone.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groupone.daos.EventDAO;
import groupone.dtos.*;
import groupone.model.Event;
import groupone.model.Location;
import io.javalin.http.Handler;

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
                    .map(x -> new EventDTO(x.getId(), x.getImageUrl(), x.getTitle(), x.getDescription(), x.getPrice()))
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
                for (int i = 0; i < locationDTOS.size(); i++) {
                    if (e.getId() == locationDTOS.get(i).getId()) {
                        event.getLocations().add(locationDTOS.get(i));
                        completeEvents.add(event);
                    }
                }
            }

            // Send the list of SuperEventDTOs as the response
            ctx.json(completeEvents);
        };
    }

    /**
     * Handler for the "getEventsById" route.
     * This handler fetches an event from the database by its ID, maps it to a DTO, and sends it as the response.
     * The response includes the event details.
     *
     * @return A Handler that can be used with Javalin to handle requests to the "getEventsById" route.
     */
    public Handler getEventsById() {
        return ctx -> {
            // Fetch the event from the database by its ID
            int id = Integer.parseInt(ctx.pathParam("id"));
            Event event = eventDAO.getById(id);

            // Map the event to an EventDTO
            EventDTO eventDTO = new EventDTO(event.getId(), event.getImageUrl(), event.getTitle(), event.getDescription(), event.getPrice());
            ctx.json(eventDTO);
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
            ctx.json(superEventDTO);
        };
    }

    public Handler getEventsByCategory() {
        return ctx -> {
            String category = (ctx.pathParam("category"));
            List<Event> eventList = eventDAO.getEventsByCategory(category);
            // Map each event to an EventDTO
            List<EventDTO> eventDTOs = eventList.stream()
                    .map(x -> new EventDTO(x.getId(), x.getImageUrl(), x.getTitle(), x.getDescription(), x.getPrice()))
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
                for (int i = 0; i < locationDTOS.size(); i++) {
                    if (e.getId() == locationDTOS.get(i).getId()) {
                        event.getLocations().add(locationDTOS.get(i));
                        completeEvents.add(event);
                    }
                }
            }

            // Send the list of SuperEventDTOs as the response
            ctx.json(completeEvents);
        };
    }

    public Handler getEventsByStatus() {
        return ctx -> {
            String status = (ctx.pathParam("status"));
            List<Event> eventList = eventDAO.getEventsByStatus(status);
            // Map each event to an EventDTO
            List<EventDTO> eventDTOs = eventList.stream()
                    .map(x -> new EventDTO(x.getId(), x.getImageUrl(), x.getTitle(), x.getDescription(), x.getPrice()))
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
                for (int i = 0; i < locationDTOS.size(); i++) {
                    if (e.getId() == locationDTOS.get(i).getId()) {
                        event.getLocations().add(locationDTOS.get(i));
                        completeEvents.add(event);
                    }
                }
            }

            // Send the list of SuperEventDTOs as the response
            ctx.json(completeEvents);
        };
    }

    public Handler createEvent() {
        return ctx -> {
            Event event = ctx.bodyAsClass(Event.class);
            for (int i = 0; i < event.getLocations().size(); i++) {
                event.getLocations().get(i).getEventSpec().setLocation(event.getLocations().get(i));
                eventDAO.create(event);
                ctx.json(event);
            }
            ;
        };

    }
}

