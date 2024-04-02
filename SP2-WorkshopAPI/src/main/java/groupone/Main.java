package groupone;

import io.javalin.Javalin;
import groupone.config.ApplicationConfig;
import groupone.config.Routes;

public class Main {
    public static void main(String[] args) {
        boolean isTesting = false;
        ApplicationConfig app = ApplicationConfig.getInstance()
                .initiateServer()
                .setExceptionHandling()
                .startServer(69420)
                .setRoutes(Routes.getRoutes(isTesting));
                //.checkSecurityRoles(isTesting);

    }
}