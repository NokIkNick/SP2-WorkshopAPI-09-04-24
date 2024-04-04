package groupone;

import groupone.config.HibernateConfig;
import groupone.daos.RoleDAO;
import groupone.dtos.UserDTO;
import groupone.model.Role;
import groupone.model.User;
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
        /*RoleDAO roleDAO = RoleDAO.getInstance(isTesting);
        roleDAO.createRoles();*/

    }
}