package groupone.daos;

import groupone.config.HibernateConfig;
import groupone.model.Event;
import groupone.model.Role;

import groupone.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class RoleDAO extends DAO<Role, String>{
    private static RoleDAO instance;

    private RoleDAO(boolean isTesting) {
        super(Role.class, isTesting);
    }

    public static RoleDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new RoleDAO(isTesting);
        }
        return instance;
    }

    public void createRoles() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        // Define your roles
        Role roles = new Role();
        roles.setName("STUDENT");
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        Role InstructorRole = new Role();
        InstructorRole.setName("INSTRUCTOR");

        // Persist roles
        em.persist(adminRole);
        em.persist(roles);
        em.persist(InstructorRole);

        em.getTransaction().commit();
        em.close();
    }
}