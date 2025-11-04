package app;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AuthApiTest extends ApiBaseSetup {
    @Test
    void healthcheck() {
        given().when().get("/auth/healthcheck")
                .then().statusCode(200)
                .body("msg", equalTo("API is up and running"));
    }
}
