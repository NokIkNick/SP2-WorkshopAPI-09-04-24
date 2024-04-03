package groupone.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import groupone.daos.EventDAO;
import groupone.daos.UserDAO;
import groupone.dtos.TokenDTO;
import groupone.dtos.UserDTO;
import groupone.exceptions.ApiException;
import groupone.exceptions.ValidationException;
import groupone.model.Event;
import groupone.model.User;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import jakarta.persistence.EntityNotFoundException;

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
    
    public Handler addEventToUser() {
        return (ctx) -> {
            User user = ctx.attribute("user");
            Integer eventId = Integer.parseInt(ctx.pathParam("id"));
            User foundUser =  userDAO.getById(user.getEmail());
            Event foundEvent = eventDAO.getById(eventId);
            foundUser.addEvent(foundEvent);
            User updated = userDAO.update(foundUser, foundUser.getEmail());

        };

    }



}
