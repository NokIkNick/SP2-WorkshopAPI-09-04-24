package groupone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groupone.controllers.EventController;
import groupone.controllers.SecurityController;
import groupone.controllers.UserController;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;

    private static UserController uc;
    private static EventController ec;
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).registerModule(new JavaTimeModule());
    public static EndpointGroup getRoutes(Boolean isTesting) {
        sc = SecurityController.getInstance(isTesting);
        uc = UserController.getInstance(isTesting);
        ec = EventController.getInstance(isTesting);
        return () -> {
            before(sc.authenticate());
            path("/", () -> {
                before(sc.authenticate());
                get("/", ctx -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully")), roles.ANYONE);
                get("/events/category/{category}", ec.getEventsByCategory(), roles.STUDENT, roles.INSTRUCTOR, roles.ADMIN);
                get("/events/status/{status}", ec.getEventsByStatus(), roles.STUDENT, roles.INSTRUCTOR, roles.ADMIN);
                get("/events", ec.getAllEvents(), roles.STUDENT, roles.INSTRUCTOR, roles.ADMIN);
                get("/events/{id}", ec.getEventsById(), roles.STUDENT, roles.INSTRUCTOR, roles.ADMIN);
                // Posts!
                post("/events", ec.createEvent(), roles.INSTRUCTOR);
            });
            path("/auth", () -> {
                post("/login", sc.login(), roles.ANYONE);
                post("/register", sc.register(), roles.ANYONE);
                get("/request/password/reset", sc.requestPasswordReset(), roles.ANYONE);
                get("/reset/password", sc.resetPassword(), roles.ANYONE);
            });
            path("/student",() ->{
                post("/toevent/{id}",uc.addEventToUser(),roles.STUDENT);
            });
            path("/test", () ->{
                get("/",uc.getAll(),roles.ANYONE);
            });
        };
    }

     enum roles implements RouteRole {
        STUDENT,
        ADMIN,
        INSTRUCTOR,
        ANYONE
    }
}

