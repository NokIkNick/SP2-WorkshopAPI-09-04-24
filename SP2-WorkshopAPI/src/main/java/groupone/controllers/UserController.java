package groupone.controllers;

import groupone.daos.EventDAO;
import groupone.daos.UserDAO;
import groupone.dtos.UserDTO;
import groupone.model.Event;
import groupone.model.User;
import io.javalin.http.Context;

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
    
    public void addEventToUser(Context ctx) {
        String userEmail = ctx.pathParam("email");
        Integer eventId = Integer.parseInt( ctx.pathParam("eventId"));

        User foundUser =  userDAO.getById(userEmail);
        Event foundEvent = eventDAO.getById(eventId);
        foundUser.addEvent(foundEvent);
        userDAO.update(foundUser, foundUser.getEmail());
    }



}
