package groupone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import groupone.controllers.SecurityController;
import groupone.controllers.UserController;
import groupone.dtos.UserDTO;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private static SecurityController sc;
    private static UserController uc;
    private static UserDTO userDTO;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static EndpointGroup getRoutes(Boolean isTesting) {
        sc = SecurityController.getInstance(isTesting);
        uc = UserController.getInstance(isTesting);
        return () -> {
            path("/", () -> {
                get("/", ctx -> ctx.json(objectMapper.createObjectNode().put("Message", "Connected Successfully")), roles.ANYONE);
            });
            path("/auth", () -> {
                post("/login", sc.login(), roles.ANYONE);
                post("/register", sc.register(), roles.ANYONE);
            });
            path("/admin",()->{
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

