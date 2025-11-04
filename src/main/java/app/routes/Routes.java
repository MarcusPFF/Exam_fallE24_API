package app.routes;

import app.controllers.AdminController;
import app.controllers.CandidateController;
import app.controllers.PublicController;
import app.controllers.ReportController;
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
        var candidate = new CandidateController(emf);
        var reports = new ReportController(emf);

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

            //Candidates (Role.anyone for now, security i will add later)
            path("/candidates", () -> {
                get("/", candidate.list(), Role.ANYONE);
                get("/{id}", candidate.getById(), Role.ANYONE);
                post("/", candidate.create(), Role.ANYONE);
                put("/{id}", candidate.update(), Role.ANYONE);
                delete("/{id}", candidate.delete(), Role.ANYONE);
                put("/{candidateId}/skills/{skillId}", candidate.linkSkill(), Role.ANYONE);
            });

            path("/reports", () -> {
                get("/candidates/top-by-popularity", reports.topByPopularity(), Role.ANYONE);
            });

        };
    }
}