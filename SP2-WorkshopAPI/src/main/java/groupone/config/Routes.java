package groupone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupone.controllers.SecurityController;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static EndpointGroup getRoutes(Boolean isTesting) {
        sc = SecurityController.getInstance(isTesting);
        return () -> {
            path("/", () -> {
                get("/", ctx -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully")), roles.ANYONE);
            });
            path("/auth", () -> {
                post("/login", sc.login(), roles.ANYONE);
                post("/register", sc.register(), roles.ANYONE);
                get("/request/password/reset", sc.requestPasswordReset(), roles.ANYONE);
                get("/reset/password", sc.resetPassword(), roles.ANYONE);
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

