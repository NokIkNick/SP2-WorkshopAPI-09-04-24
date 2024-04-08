package groupone.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groupone.daos.EventDAO;
import groupone.dtos.*;
import groupone.enums.Category;
import groupone.enums.Status;
import groupone.model.Event;
import groupone.model.Location;
import groupone.model.User;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.hibernate.Hibernate;

import java.time.LocalDate;
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
        };
    }

    public Handler getEventsByCategory() {
        return ctx -> {
            String category = (ctx.pathParam("category"));
            List<Event> eventList = eventDAO.getEventsByCategory(Category.valueOf(category.toUpperCase()));
            List<EventDTO> eventDTOList = eventList.stream().map(event -> new EventDTO(event)).collect(Collectors.toList());
            ctx.json(eventDTOList);
        };
    }

    public Handler getEventsByStatus() {
        return ctx -> {
            String status = (ctx.pathParam("status"));
            List<Event> eventList = eventDAO.getEventsByStatus(Status.valueOf(status.toUpperCase()));
            List<EventDTO> eventDTOList = eventList.stream().map(event -> new EventDTO(event)).collect(Collectors.toList());
            ctx.json(eventDTOList);
        };
    }

    public Handler createEvent() {
        return ctx -> {
            Event event = ctx.bodyAsClass(Event.class);
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


    /**
     * Handler for the cancelEvent route.
     * Fetches the user from the token, the id, street name and zip from the path.
     * Cancels the specific event at a specific location based on the inputs given.
     * If all locations are cancelled for the event, the event gets a deletion date.
     * @return A Handler that can be used with Javalin to handle requests to the "cancelEvent" route.
     */
    public Handler cancelEvent() {
        return ctx -> {
            //Fetches parameters:
            UserDTO user = ctx.attribute("user");
            int id = Integer.parseInt(ctx.pathParam("id"));
            int zip = Integer.parseInt(ctx.pathParam("zip"));
            String street = ctx.pathParam("address");

            //Fetches the event:
            Event event = eventDAO.getById(id);

            //Initialization of local variables:
            ObjectNode toReturn;
            String location ="";
            int locationZip = 0;
            boolean found = false;

            //If the event has a deletion date already, notify the user, that all locations has been cancelled already.
            if(event.getDeletedAt() != null){
                toReturn = objectMapper.createObjectNode().put("Message","Event: "+event.getTitle()+" already cancelled at all locations. The date of deletion was: "+event.getDeletedAt());
                ctx.json(toReturn);
                return;
            }

            //Checks every location on the event if it matches the fetched parameters. If so, it changes the status to cancelled.
            for(Location l : event.getLocations()){
                if(l.getEventSpec().getInstructorEmail().equals(user.getEmail()) && l.getZipcodes().getZip() == zip && l.getStreet().equals(street)){
                    l.getEventSpec().setStatus(Status.CANCELLED);
                    location = l.getStreet();
                    locationZip = l.getZipcodes().getZip();
                    found = true;
                }
            }

            //Checks every location and counts every cancelled event.
            int count = 0;
            for(Location l : event.getLocations()){
                if(l.getEventSpec().getStatus() == Status.CANCELLED){
                    count++;
                }
            }

            /*
            If the amount of cancelled locations is equal to the size of the list of locations, then every location is cancelled
            and the event should be cancelled as well. The date of deletion is registered here.
            If that isn't the case, but the method found a location it cancelled, the boolean "found" will be true, and
            we notify the user about which specific location that has been cancelled.
            If none of the above happens we notify the user that no event or location was cancelled.
            */
            if(count == event.getLocations().size()){
                event.setDeletedAt(LocalDate.now());
                toReturn = objectMapper.createObjectNode().put("Message","Successfully cancelled event: "+event.getTitle()+" at all locations. Date of deletion is: "+LocalDate.now());
            }else if (found){
                toReturn = objectMapper.createObjectNode().put("Message", "Successfully cancelled event: "+event.getTitle()+" at "+location+", "+locationZip);
            }else {
                toReturn = objectMapper.createObjectNode().put("Message", "No event was cancelled");
            }
            //We keep the merged object in case we need it later. Currently not used.
            Event merged = eventDAO.update(event, event.getId());
            ctx.json(toReturn);
        };
    }

    public Handler getLocationsByCurrentInstructor(){
        return ctx -> {
            UserDTO user = ctx.attribute("user");
            assert user != null;
            List<Location> locations = eventDAO.getLocationsByInstructor(user.getEmail());
            List<LocationDTO> locationDTOS = locations.stream().map(LocationDTO::new).toList();
            ctx.json(locationDTOS);
        };
    }
}

