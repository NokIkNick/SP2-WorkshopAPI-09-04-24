package groupone.daos;

import groupone.model.Event;

public class EventDAO extends DAO<Event,Integer> {

    private static EventDAO instance;

    private EventDAO(boolean isTesting) {
        super(Event.class, isTesting);
    }

    public static EventDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new EventDAO(isTesting);
        }
        return instance;
    }




}
