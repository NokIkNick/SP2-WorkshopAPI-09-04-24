package groupone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupone.controllers.EventController;
import groupone.controllers.SecurityController;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static EndpointGroup getRoutes(Boolean isTesting) {
        sc = SecurityController.getInstance(isTesting);
        return () -> {
            path("/", () -> {
                get("/", ctx -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully")), roles.ANYONE);
                get("/events", EventController.getAllEvents(), roles.ANYONE);
                get("/events/{id}", EventController.getEventsById(), roles.ANYONE);
            });
            path("/auth", () -> {
                post("/login", sc.login(), roles.ANYONE);
                post("/register", sc.register(), roles.ANYONE);
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

