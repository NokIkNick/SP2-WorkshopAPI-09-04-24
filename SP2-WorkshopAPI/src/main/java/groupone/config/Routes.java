package groupone.config;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Routes {

    public static EndpointGroup getRoutes(Boolean isTesting){
        EntityManager em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager();
        return null;
    }
}
