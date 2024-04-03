package groupone.controllers;

import groupone.daos.EventDAO;
import groupone.daos.UserDAO;
import groupone.model.Event;
import groupone.model.User;

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

    public void addEventToUser(User user, Event event) {
        User foundUser =  userDAO.getById(user.getEmail());
        Event foundEvent = eventDAO.getById(event.getId());
        foundUser.addEvent(foundEvent);
        userDAO.update(foundUser, foundUser.getEmail());
    }



}
