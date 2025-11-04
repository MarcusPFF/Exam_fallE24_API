package app.security.controllers;

import app.security.enums.Role;
import app.security.utils.JwtUtil;
import app.utils.Utils;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class AuthController {
    private final String user;
    private final String pass;

    public AuthController(EntityManagerFactory emf) {
        String u, p;
        try {
            u = Utils.getPropertyValue("AUTH_USER", "config.properties");
        } catch (Exception e) {
            u = null;
        }
        try {
            p = Utils.getPropertyValue("AUTH_PASSWORD", "config.properties");
        } catch (Exception e) {
            p = null;
        }
        if (u == null || u.isBlank()) u = "recruiter";
        if (p == null || p.isBlank()) p = "pw";
        this.user = u;
        this.pass = p;
    }

    public Handler health() {
        return ctx -> ctx.json(Map.of("msg", "API is up and running"));
    }

    public Handler register() {
        return ctx -> ctx.status(404).json(Map.of("error", "Registration is disabled"));
    }

    public Handler login() {
        return ctx -> {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String u = body == null ? null : String.valueOf(body.get("username"));
            String p = body == null ? null : String.valueOf(body.get("password"));
            if (u == null || p == null) {
                ctx.status(400).json(Map.of("error", "username and password are required"));
                return;
            }
            if (!user.equals(u) || !pass.equals(p)) {
                ctx.status(401).json(Map.of("error", "Invalid username or password"));
                return;
            }
            String token = JwtUtil.generateToken(u, Role.RECRUITER);
            ctx.json(Map.of("token", token, "username", u, "role", Role.RECRUITER.name()));
        };
    }
}