package groupone.daos;

import groupone.enums.Category;
import groupone.enums.Status;
import groupone.model.Event;
import groupone.model.EventSpec;
import groupone.model.Location;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class EventDAO extends DAO<Event, Integer>{
    private static EventDAO instance;

    public EventDAO(boolean isTesting) {
        super(Event.class, isTesting);
    }

    @Override
    public List<Event> getAll(){
        List<Event> eventList;
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            TypedQuery<Event> query = em.createQuery("select e from Event e", Event.class);
            eventList = query.getResultList();
            for (Event e: eventList) {
                e.getLocations().size();
                for(Location l : e.getLocations()){
                    l.getEventSpec();
                    l.getZipcodes().getCity();
                }
            }
            em.getTransaction().commit();
        }
        return eventList;
    }

    @Override
    public Event getById(Integer id){
        Event event;
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            event = em.find(Event.class, id);
            event.getLocations().size();
            for(Location l : event.getLocations()){
                l.getEventSpec();
                l.getZipcodes().getCity();
            }
            em.getTransaction().commit();
        }
        return event;
    }

    public List<Event> getEventsByCategory(Category category){
        List<Event> eventList;
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            TypedQuery<Event> query = em.createQuery("select e from Event e JOIN Location l on e.id = l.id JOIN EventSpec es ON l.id = es.id WHERE es.category = ?1", Event.class).setParameter(1, category);
            eventList = query.getResultList();
            for (Event e: eventList) {
                e.getLocations().size();
                for(Location l : e.getLocations()){
                    l.getEventSpec();
                    l.getZipcodes().getCity();
                }
            }
            em.getTransaction().commit();
        }
        return eventList;
    }

    public List<Event> getEventsByStatus(Status status){
        List<Event> eventList;
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            TypedQuery<Event> query = em.createQuery("select e from Event e JOIN Location l on e.id = l.id JOIN EventSpec es ON l.id = es.id WHERE es.status = :status", Event.class);
            query.setParameter("status",status);
            eventList = query.getResultList();
            for (Event e: eventList) {
                e.getLocations().size();
                for(Location l : e.getLocations()){
                    l.getEventSpec();
                    l.getZipcodes().getCity();
                }
            }
            em.getTransaction().commit();
        }
        return eventList;
    }
    public static EventDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new EventDAO(isTesting);
        }
        return instance;
    }
}
