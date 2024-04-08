package groupone.daos;

import groupone.model.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class LocationDAO extends DAO<Location, Integer> {
    private static LocationDAO instance;

    public static LocationDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new LocationDAO(isTesting);
        }
        return instance;
    }
    public LocationDAO(boolean isTesting) {
        super(Location.class, isTesting);
    }

    public Location findLocationByStreet(String streetName) {
        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Location> query = em.createQuery("select l from Location l where l.street = :street", Location.class);
            query.setParameter("street", streetName);
            return query.getSingleResult();
        }
    }
}
