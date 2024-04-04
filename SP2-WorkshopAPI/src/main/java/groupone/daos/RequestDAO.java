package groupone.daos;

import groupone.config.HibernateConfig;
import groupone.model.ResetRequest;
import groupone.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

import java.time.LocalDateTime;

public class RequestDAO extends DAO<ResetRequest, String> {
    private static RequestDAO instance;

    private RequestDAO(boolean isTesting) {
        super(ResetRequest.class, isTesting);
    }

    public static RequestDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new RequestDAO(isTesting);
        }
        return instance;
    }

    /**
     * Call this when you want to clean up expired requests.
     * @return number of expired requests that was cleaned up.
     */
    public int requestCleanup(){
        try(EntityManager em = emf.createEntityManager()){
            Query q = em.createQuery("DELETE FROM ResetRequest a where a.expires < :now");
            q.setParameter("now", LocalDateTime.now());
            return q.executeUpdate();
        }
    }
}
