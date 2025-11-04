package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

//US-7
public class ApiBaseSetup {
    protected static Javalin server;

    @BeforeAll
    static void setup() {
        HibernateConfig.setTest(true);
        server = ApplicationConfig.startServer(0);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = server.port();
        RestAssured.basePath = "/api";
    }

    @AfterAll
    static void teardown() {
        ApplicationConfig.stopServer(server);
    }
}