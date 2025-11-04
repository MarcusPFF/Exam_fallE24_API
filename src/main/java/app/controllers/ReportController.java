package app.controllers;

import app.services.ReportService;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;

//US-6
public class ReportController {
    private final ReportService reports;

    public ReportController(EntityManagerFactory emf) {
        this.reports = new ReportService(emf);
    }

    public Handler topByPopularity() {
        return ctx -> ctx.json(reports.topByPopularity());
    }
}