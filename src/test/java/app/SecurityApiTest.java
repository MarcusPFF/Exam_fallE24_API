package app;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecurityApiTest extends ApiBaseSetup {

    private String login(String u, String p) {
        return given().contentType(ContentType.JSON)
                .body("{\"username\":\""+u+"\",\"password\":\""+p+"\"}")
                .post("/auth/login")
                .then().statusCode(200)
                .extract().path("token");
    }

    @Test
    void loginReturnsJwt() {
        String token = login("recruiter","pw");
        assertNotNull(token);
    }

    @Test
    void writeWithoutTokenIs401() {
        given().contentType(ContentType.JSON)
                .body("{\"name\":\"NoAuth\",\"phone\":\"11111111\",\"education\":\"BSc\"}")
                .post("/candidates")
                .then().statusCode(401);
    }

    @Test
    void recruiterCanWriteCandidates() {
        String token = login("recruiter","pw");
        given().contentType(ContentType.JSON)
                .header("Authorization","Bearer "+token)
                .body("{\"name\":\"WithAuth\",\"phone\":\"22222222\",\"education\":\"BSc\"}")
                .post("/candidates")
                .then().statusCode(201);
    }
}