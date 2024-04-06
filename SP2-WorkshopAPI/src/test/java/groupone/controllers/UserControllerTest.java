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

class UserControllerTest {

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

        RestAssured.baseURI = "http://localhost:7777/api";
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig.initiateServer()
                .startServer(7777)
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
            em.createQuery("delete from EventSpec").executeUpdate();
            em.createQuery("delete from Location").executeUpdate();
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

            em.persist(event1);
            em.persist(event2);

            userStudent.addEvent(event1);
            //userStudent.addRole(student);

            User user124 = em.merge(userStudent);
            em.getTransaction().commit();
            //User found = em.find(User.class,user124.getEmail());


        }
    }

    @AfterAll
    static void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
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
                .post("http://localhost:7777/api/auth/login")
                .then()
                .extract()
                .response()
                .body()
                .path("token");

        return "Bearer " + token;
    }

    @Test
    void addEventToUser() {
        RestAssured.given()
                .header("Authorization", studentToken)
                .when()
                .post("http://localhost:7777/api/student/toevent/2")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK_200)
                .body("eventDTOList.size()",equalTo(2));

    }
    @Test
    void getAllUsers(){
        RestAssured.given()
                .header("Authorization", adminToken)
                .when()
                .get("/admin/get_all_users")
                .then().log().all()
                .body("[0].email",equalTo("test@student.com"));
    }

    @Test
    void testStuff(){
        RestAssured.given()
                .when()
                .get("/test")
                .then().log().all()
                .body("email",equalTo("test@student.com"));
    }
}