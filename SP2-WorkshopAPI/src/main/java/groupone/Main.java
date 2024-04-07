package groupone;

import io.javalin.Javalin;
import groupone.config.ApplicationConfig;
import groupone.config.Routes;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        boolean isTesting = false;

        ApplicationConfig app = ApplicationConfig.getInstance()
                .initiateServer()
                .setExceptionHandling()
                .startServer(7008)
                .setRoutes(Routes.getRoutes(isTesting))
                .checkSecurityRoles(isTesting);
    }
}
