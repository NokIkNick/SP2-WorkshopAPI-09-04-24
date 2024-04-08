package groupone.controllers;

import groupone.config.ApplicationConfig;
import groupone.config.HibernateConfig;
import groupone.config.Routes;
import groupone.enums.Category;
import groupone.enums.Status;
import groupone.model.*;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

class EventControllerTest {

    private static EntityManagerFactory emf;
    private static User userStudent, userAdmin,userInstructor;
    private static Role student, admin,instructor;
    private static Object adminToken;
    private static Object studentToken;
    private static Object instructorToken;

    @BeforeAll
    static void setUpAll(){
        emf = HibernateConfig.getEntityManagerFactoryConfigForTesting();
        //emf = HibernateConfig.getEntityManagerFactoryConfig();

        RestAssured.baseURI = "http://localhost:7778/api";
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig.initiateServer()
                .startServer(7778)
                .setExceptionHandling()
                .setRoutes(Routes.getRoutes(true))
                .checkSecurityRoles(true);

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
        adminToken = getToken(userAdmin.getEmail(), "test");
        studentToken = getToken(userStudent.getEmail(), "test");
        instructorToken = getToken(userInstructor.getEmail(),"test");

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

            //em.persist(event1);
            em.persist(event2);

            event1.addUser(userStudent);
            //userStudent.addEvent(event1);

            // no need to persist event1, becouse merge is also a persist.
            Event event124 = em.merge(event1);
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

    public static Object getToken(String email, String password)
    {
        return login(email, password);
    }

    private static Object login(String email, String password)
    {
        String json = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);

        var token = given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7778/api/auth/login")
                .then()
                .extract()
                .response()
                .body()
                .path("token");

        return "Bearer " + token;
    }
    @Test
    void getUpcomingEvents(){
        RestAssured.given()
                .header("Authorization", adminToken)
                .when()
                .get("http://localhost:7778/api/events")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("events.size()",equalTo(3));
    }

    @Test
    void getEventsById() {
        RestAssured.given()
                .header("Authorization", adminToken)
                .when()
                .get("http://localhost:7778/api/events/2")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("title",equalTo("testEvent"));
    }

    @Test
    void getEventsByCategory() {
        RestAssured.given()
                .header("Authorization", adminToken)
                .when()
                .get("http://localhost:7778/api/events/category/event")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("[0].locations[0].eventSpecifications.category",equalTo("EVENT"));
    }

    @Test
    void getEventsByStatus() {
        RestAssured.given()
                .header("Authorization", adminToken)
                .when()
                .get("http://localhost:7778/api/events/status/upcoming")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("[0].locations[0].eventSpecifications.status",equalTo("UPCOMING"))
                .body("[1].locations[0].eventSpecifications.status",equalTo("UPCOMING"));
    }

    @Test
    void createEvent() {
        String json = "{\"title\":\"newTestEvent\",\"description\":\"This is a new test event!\",\"price\":100.0,\"createdAt\":\"2024-04-03\",\"updatedAt\":\"2024-04-03\",\"imageUrl\":\"www.moretexturl.com\",\"locations\":[{\"street\":\"test-street-wahoo\",\"eventSpec\":{\"date\":\"2024-05-05\",\"time\":\"16:30\",\"duration\":2,\"instructorName\":\"Peter Maker\",\"instructorEmail\":\"Peter@Maker.dk\",\"status\":\"UPCOMING\",\"category\":\"EVENT\",\"capacity\":1200},\"zipcodes\":{\"zip\":2970}},{\"street\":\"the-other-test-street-wahoo\",\"eventSpec\":{\"date\":\"2024-06-08\",\"time\":\"17:30\",\"duration\":3,\"instructorName\":\"Morten Bungi\",\"instructorEmail\":\"Morten@Bungi.dk\",\"status\":\"UPCOMING\",\"category\":\"EVENT\",\"capacity\":500},\"zipcodes\":{\"zip\":2970}}]}";

        RestAssured.given()
                .header("Authorization", instructorToken)
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7778/api/events/create")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("title", equalTo("newTestEvent"))
                .body("locations.size()",equalTo(2));
    }
    @Test
    void getEventByIdsPerticipants(){
        RestAssured.given()
                .header("Authorization", instructorToken)
                .when()
                .get("http://localhost:7778/api/events/2/users")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("[0].email",equalTo("test@student.com"));
    }

    @Test
    void getAllEvents(){
            RestAssured.given()
                    .header("Authorization", adminToken)
                    .when()
                    .get("http://localhost:7778/api/admin/get_all_events")
                    .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.OK_200)
                    .body("events.size()",equalTo(3));
    }

}