package app;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PublicApiTest extends ApiBaseSetup {

    //public double version = 1.0;
    @Test
    void infoCheck() {
        given().when().get("/public/info")
                .then().statusCode(200)
                .body("name", equalTo("ExamAPI"))
                .body("version", equalTo("1.0"))
                .body("time", notNullValue());
    }
}