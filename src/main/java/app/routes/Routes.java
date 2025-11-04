package app.routes;

import app.controllers.AdminController;
import app.controllers.PublicController;
import app.security.controllers.AuthController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static app.routes.handling.RouteDocs.*;

public class Routes {

    public EndpointGroup api(EntityManagerFactory emf) {
        var auth = new AuthController(emf);
        var pub = new PublicController();
        var admin = new AdminController(emf);

        return () -> {
            // Auth – (Anyone endpoints)
            path("/auth", () -> {
                get("/healthcheck", auth.health(), Role.ANYONE);
                post("/login", auth.login(), Role.ANYONE);
                post("/register", auth.register(), Role.ANYONE);
            });

            // Public (Public Endpoints)
            path("/public", () -> {
                get("/info", pub.info(), Role.ANYONE);

            });

            // Admin (Admin-protected)
            path("/admin", () -> {
                get("/panel", admin.panel(), Role.ADMIN);
                get("/users", admin.users(), Role.ADMIN);
            });

            // Will make path when i know the topic (Guest-guarded)
            path("/something", () -> {
            });
        };
    }
}