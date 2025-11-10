package app.controllers;

import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;

import java.time.Instant;
import java.util.Map;

public class PublicController {

    public Handler info() {
        return ctx -> ctx.json(Map.of(
                "name", "ExamAPI",
                "version", "1.1",
                "time", Instant.now().toString()
        ));
    }
}