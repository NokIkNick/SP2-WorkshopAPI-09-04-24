package groupone.controllers;

import groupone.daos.EventDAO;
import groupone.daos.UserDAO;
import groupone.model.Event;
import groupone.model.User;
import io.javalin.http.Handler;

import java.util.List;

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
            ctx.json(updated);
        };

    }


    public Handler getAll() {
        return (ctx) -> {
            User users = userDAO.getById("test@student.com");
            ctx.json(users);
        };
    }
}
