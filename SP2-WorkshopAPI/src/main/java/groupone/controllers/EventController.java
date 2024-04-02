package groupone.controllers;

import groupone.daos.EventDAO;
import groupone.dtos.EventDTO;
import groupone.model.Event;
import io.javalin.http.Handler;

import java.util.List;

public class EventController {
    private static EventDAO eventDAO = EventDAO.getInstance(false);

    public static Handler getAllEvents() {
        return ctx -> {
            List<Event> eventList = eventDAO.getAll();
            ctx.json(eventList.stream().map(y -> new EventDTO(y.getId(),y.getImageUrl(),y.getTitle(),y.getDescription(),y.getPrice())).toList());
        };
    }

    public static Handler getEventsById() {
        return ctx -> {
            Event event = eventDAO.getById(Integer.parseInt(ctx.pathParam("id")));
            ctx.json(new EventDTO(event.getId(), event.getImageUrl(), event.getTitle(), event.getDescription(), event.getPrice()));
        };
    }
}
