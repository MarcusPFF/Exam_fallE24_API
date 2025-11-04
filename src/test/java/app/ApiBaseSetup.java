package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

//US-7
public class ApiBaseSetup {
    protected static Javalin server;

    protected static String TOKEN;

    @BeforeAll
    static void setup() {
        HibernateConfig.setTest(true);
        server = ApplicationConfig.startServer(0);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = server.port();
        RestAssured.basePath = "/api";

        TOKEN = given().contentType(ContentType.JSON)
                .body("{\"username\":\"recruiter\",\"password\":\"pw\"}")
                .post("/auth/login").then().statusCode(200).extract().path("token");
    }

    @AfterAll
    static void teardown() {
        ApplicationConfig.stopServer(server);
    }
}