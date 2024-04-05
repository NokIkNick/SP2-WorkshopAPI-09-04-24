package groupone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groupone.controllers.EventController;
import groupone.controllers.SecurityController;
import groupone.controllers.UserController;
import groupone.dtos.UserDTO;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;
    private static UserController uc;
    @SuppressWarnings("unused")
    private static UserDTO userDTO;
    private static EventController ec;
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).registerModule(new JavaTimeModule());
    @SuppressWarnings("CodeBlock2Expr")
    public static EndpointGroup getRoutes(Boolean isTesting) {
        ec = EventController.getInstance(isTesting);
        sc = SecurityController.getInstance(isTesting);
        uc = UserController.getInstance(isTesting);
        return () -> {
            before(sc.authenticate());
            path("", () -> {
                get("/", ctx -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully")), roles.ANYONE);
            });
            path("/events", () -> {
                get("/category/{category}", ec.getEventsByCategory(), roles.STUDENT, roles.INSTRUCTOR, roles.ADMIN);
                get("/status/{status}", ec.getEventsByStatus(), roles.STUDENT, roles.INSTRUCTOR, roles.ADMIN);
                // TODO update below endpoint to only show information about upcoming events
                get("", ec.getAllEvents(), roles.STUDENT, roles.INSTRUCTOR, roles.ADMIN);
                get("/{id}", ec.getEventById(), roles.STUDENT, roles.INSTRUCTOR, roles.ADMIN);
                // TODO add tests for below endpoint.
                get("/{id}/users", ec.getEventByIdsParticipants(), roles.INSTRUCTOR);
                // Posts!
                post("", ec.createEvent(), roles.INSTRUCTOR);
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
            path("/admin",()->{
                get("/get_all_events",ec.getAllEvents(),roles.ADMIN);
                // TODO add tests for below endpoint.
                get("/get_all_users",uc.getAllUsers(),roles.ADMIN);
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

