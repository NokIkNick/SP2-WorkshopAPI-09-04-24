package groupone.daos;

import groupone.config.HibernateConfig;
import groupone.enums.Category;
import groupone.enums.Status;
import groupone.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LocationDAOTest {

    private static EntityManagerFactory emf;
    private static User userStudent, userAdmin,userInstructor;
    private static Role student, admin,instructor;
    private static LocationDAO locationDAO;

    @BeforeAll
    static void setUpAll(){
        emf = HibernateConfig.getEntityManagerFactoryConfigForTesting();
        //emf = HibernateConfig.getEntityManagerFactoryConfig();

        locationDAO = new LocationDAO(true);

        student = new Role("STUDENT");
        admin = new Role("ADMIN");
        instructor = new Role("INSTRUCTOR");

        userStudent = new User("test@student.com","test","PatrickStudent",456765353,student);
        userAdmin = new User("test@admin.com","test","PatrickAdmin",456765353,admin);
        userInstructor = new User("test@instructor.com","test","PatrickInstructor",456765353,instructor);

        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(student);
            em.persist(admin);
            em.persist(instructor);
            em.persist(userStudent);
            em.persist(userAdmin);
            em.persist(userInstructor);
            em.getTransaction().commit();
        }

    }

    @BeforeEach
    void setUp() {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            //em.createQuery("delete from users").executeUpdate();
            em.createQuery("delete from Location").executeUpdate();
            em.createQuery("delete from EventSpec").executeUpdate();
            em.createQuery("delete from Event").executeUpdate();
            em.createQuery("delete from Zipcode").executeUpdate();

            em.createNativeQuery("ALTER SEQUENCE event_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE location_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE eventspec_id_seq RESTART WITH 1").executeUpdate();

            Event event1 = new Event("testEvent","test med test på",99999,"pictureUrl");
            Location location1 = new Location("Fredensvej 19");
            EventSpec eventSpec1 = new EventSpec(LocalDate.now(), LocalTime.now(),30,"Den vilde","denVilde@hej.com", Status.UPCOMING,30, Category.EVENT);
            Zipcode zipcode1 = new Zipcode(2970,"Hørsholm");

            Event event2 = new Event("testEvent2","test med test på",99999,"pictureUrl");
            Location location2 = new Location("Thyrasvej 4");
            EventSpec eventSpec2 = new EventSpec(LocalDate.now(), LocalTime.now(),30,"Den vilde","denVilde@hej.com", Status.UPCOMING,30,Category.WORKSHOP);
            //Zipcode zipcode2 = new Zipcode(2970,"Hørsholm");

            em.persist(zipcode1);

            location1.addZipcode(zipcode1);
            location1.setEventSpec(eventSpec1);

            location2.addZipcode(zipcode1);
            location2.setEventSpec(eventSpec2);

            event1.addLocation(location1);
            event2.addLocation(location2);

            em.persist(event2);

            event1.addUser(userStudent);
            //userStudent.addEvent(event1);

            em.persist(event1);
            em.getTransaction().commit();
            em.clear();
            //User found = em.find(User.class,user124.getEmail());
        }
    }

    @AfterAll
    static void tearDown(){
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("delete from roles").executeUpdate();
            em.createQuery("delete from users").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    void findLocationByStreet() {
        Location location = locationDAO.findLocationByStreet("Fredensvej 19");
        assertNotNull(location);
        assertEquals("Fredensvej 19",location.getStreet());
    }
}