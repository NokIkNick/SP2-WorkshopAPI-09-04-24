package groupone.daos;

import groupone.config.HibernateConfig;
import groupone.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleDAOTest {
    @Test
    public void testCreateRoles() {
        // Get the EntityManagerFactory for testing
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfigForTesting();

        // Create a RoleDAO instance
        RoleDAO roleDAO = RoleDAO.getInstance(true);

        // Call createRoles()
        roleDAO.createRoles();

        // Create an EntityManager to fetch the roles from the database
        EntityManager em = emf.createEntityManager();

        // Fetch the roles
        Role adminRole = em.find(Role.class, "ADMIN");
        Role studentRole = em.find(Role.class, "STUDENT");
        Role instructorRole = em.find(Role.class, "INSTRUCTOR");

        // Verify that the roles were correctly persisted
        assertNotNull(adminRole);
        assertNotNull(studentRole);
        assertNotNull(instructorRole);

        // Close the EntityManager
        em.close();
    }
}