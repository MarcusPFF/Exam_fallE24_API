package app;


import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReportsApiTest extends ApiBaseSetup{

    @Test
    void topByPopularity() {
        given().when().get("/reports/candidates/top-by-popularity")
                .then().statusCode(200)
                .body("candidateId", notNullValue())
                .body("averagePopularity", greaterThanOrEqualTo(0.0f));
    }
}
