package groupone.controllers;

import groupone.daos.EventDAO;
import groupone.daos.UserDAO;
import groupone.dtos.EventDTO;
import groupone.dtos.UserDTO;
import groupone.model.Event;
import groupone.model.User;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {

    private static UserDAO userDAO ;
    private static EventDAO eventDAO;
    private static UserController instance;

    public static UserController getInstance(boolean isTesting){
        if(instance == null){
            instance = new UserController();
            userDAO = userDAO.getInstance(isTesting);
            eventDAO = EventDAO.getInstance(isTesting);
        }
        return instance;
    }

    public void getAllEvents(Context ctx){
        String role = ctx.pathParam("roles");
        List<Event> events = eventDAO.getAll();
        List<EventDTO> eventDTOS = new ArrayList<>();
        for(Event e : events){
            EventDTO eventDTO = new EventDTO(e);
            eventDTOS.add(eventDTO);
        }
        ctx.json(eventDTOS);
    }

    public void getAllUsers(Context ctx){
        String role = ctx.pathParam("roles");

          List<User> users = userDAO.getAll();
          List<UserDTO> userDTOS = new ArrayList<>();
          for(User u: users){
              UserDTO userDTO = new UserDTO(u);
              userDTOS.add(userDTO);
          }
          ctx.json(userDTOS);

    }
    
    public void addEventToUser(Context ctx) {
        String userEmail = ctx.pathParam("email");
        Integer eventId = Integer.parseInt( ctx.pathParam("eventId"));

        User foundUser =  userDAO.getById(userEmail);
        Event foundEvent = eventDAO.getById(eventId);
        foundUser.addEvent(foundEvent);
        userDAO.update(foundUser, foundUser.getEmail());
    }



}
