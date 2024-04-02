package groupone.daos;

import groupone.model.Event;

import java.util.List;

public class EventDAO extends DAO<Event, String>{
    private static EventDAO instance;

    public EventDAO(boolean isTesting) {
        super(Event.class, isTesting);
    }

    public List<Event> getAll() {
        try(var em = emf.createEntityManager()){
            return em.createQuery("SELECT e FROM Event e", Event.class).getResultList();
        }
    }

    public Event getById(int id) {
        try(var em = emf.createEntityManager()){
            return em.createQuery("SELECT e from Event e where id = "+id, Event.class).getSingleResult();
        }
    }

    public static EventDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new EventDAO(isTesting);
        }
        return instance;
    }
}
