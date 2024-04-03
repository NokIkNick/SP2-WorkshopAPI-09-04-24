package groupone.controllers;

import groupone.config.ApplicationConfig;
import groupone.config.HibernateConfig;
import groupone.config.Routes;
import groupone.enums.Status;
import groupone.model.*;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @BeforeAll
    static void setUpAll(){
        RestAssured.baseURI = "http://localhost:7777/api";
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig.initiateServer()
                .startServer(7777)
                .setExceptionHandling()
                .setRoutes(Routes.getRoutes(true))
                .checkSecurityRoles(true);
    }

    @BeforeEach
    void setUp() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfigForTesting();

        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("delete from users").executeUpdate();
            em.createQuery("delete from Zipcode").executeUpdate();
            em.createQuery("delete from EventSpec").executeUpdate();
            em.createQuery("delete from Location").executeUpdate();
            em.createQuery("delete from Event").executeUpdate();

            em.createNativeQuery("ALTER SEQUENCE event_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE location_id_seq RESTART WITH 1").executeUpdate();

            User user = new User("test@test.com","test","Patrick",456765353);
            Role role = new Role("STUDENT");
            Event event = new Event("testEvent","test med test på",99999,"pictureUrl");
            Location location = new Location("Fredensvej 19");
            EventSpec eventSpec = new EventSpec(LocalDate.now(), LocalTime.now(),30,"Den vilde","denVilde@hej.com", Status.UPCOMING,30);
            Zipcode zipcode = new Zipcode(2970,"Hørsholm");

            location.addZipcode(zipcode);
            location.setEventSpec(eventSpec);

            event.addLocation(location);
            user.addEvent(event);
            user.addRole(role);

            em.persist(user);
            em.persist(event);
            em.persist(location);
            em.persist(eventSpec);
            em.persist(zipcode);
            em.getTransaction().commit();
        }
    }

    @Test
    void addEventToUser() {

    }
    @Test
    void getAllInfo(){
        RestAssured.given().log().all()
                .when()
                .get("/hotels")
                .then().log().all()
                .statusCode(200)
                .body("[0].name",equalTo("Charmander"));
    }
}