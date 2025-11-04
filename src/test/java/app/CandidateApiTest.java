package app;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CandidateApiTest extends ApiBaseSetup {

    private String token() {
        return given().contentType(ContentType.JSON)
                .body("{\"username\":\"recruiter\",\"password\":\"pw\"}")
                .post("/auth/login")
                .then().statusCode(200)
                .extract().path("token");
    }

    @Test
    void listCandidates() {
        given().when().get("/candidates").then().statusCode(200).body("$", not(empty()));
    }

    @Test
    void filterBadCategory() {
        given().when().get("/candidates?category=not-a-category").then().statusCode(400);
    }

    @Test
    void deleteNotFound() {
        given().header("Authorization","Bearer "+token())
                .when().delete("/candidates/{id}", 999999)
                .then().statusCode(404);
    }

    @Test
    void updateNotFound() {
        given().header("Authorization","Bearer "+token())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"X\",\"phone\":\"1\",\"education\":\"Y\"}")
                .when().put("/candidates/{id}", 999999)
                .then().statusCode(404);
    }

    @Test
    void getByIdNotFound() {
        given().when().get("/candidates/{id}", 999999).then().statusCode(404);
    }

    @Test
    void createCandidate() {
        given().header("Authorization","Bearer "+token())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Demo\",\"phone\":\"11111111\",\"education\":\"BSc\"}")
                .post("/candidates")
                .then().statusCode(201)
                .body("name", equalTo("Demo"));
    }

    @Test
    void readCandidate() {
        Integer id = given().header("Authorization","Bearer "+token())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Read\",\"phone\":\"11111112\",\"education\":\"BSc\"}")
                .post("/candidates").then().statusCode(201).extract().path("id");

        given().get("/candidates/{id}", id)
                .then().statusCode(200)
                .body("id", equalTo(id));
    }

    @Test
    void updateCandidate() {
        Integer id = given().header("Authorization","Bearer "+token())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Original\",\"phone\":\"11111113\",\"education\":\"EK-Datamatikker\"}")
                .post("/candidates").then().statusCode(201).extract().path("id");

        given().header("Authorization","Bearer "+token())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Updated\",\"phone\":\"22222222\",\"education\":\"EK-Datamatikker\"}")
                .put("/candidates/{id}", id)
                .then().statusCode(200)
                .body("name", equalTo("Updated"));
    }

    @Test
    void deleteCandidate() {
        Integer id = given().header("Authorization","Bearer "+token())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Delete\",\"phone\":\"11111114\",\"education\":\"EK-Datamatikker\"}")
                .post("/candidates").then().statusCode(201).extract().path("id");

        given().header("Authorization","Bearer "+token())
                .delete("/candidates/{id}", id)
                .then().statusCode(204);
    }

    @Test
    void getById_includesEnrichment() {
        Integer id = given().when().get("/candidates").then().extract()
                .path("find { it.skills && it.skills.any { s -> ['java','postgresql','spring-boot'].contains(s.slug) } }.id");

        given().when().get("/candidates/{id}", id)
                .then().statusCode(200)
                .body("skills.find { ['java','postgresql','spring-boot'].contains(it.slug) }.popularityScore", notNullValue())
                .body("skills.find { ['java','postgresql','spring-boot'].contains(it.slug) }.averageSalary", notNullValue());
    }

    @Test
    void linkSkillOk() {
        Integer cid = given().header("Authorization","Bearer "+token())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Link\",\"phone\":\"33333333\",\"education\":\"BSc\"}")
                .post("/candidates").then().statusCode(201).extract().path("id");

        Integer sid = given().get("/candidates").then().extract()
                .path("find { it.skills && it.skills.size() > 0 }.skills[0].id");

        given().header("Authorization","Bearer "+token())
                .put("/candidates/{cid}/skills/{sid}", cid, sid)
                .then().statusCode(204);

        given().get("/candidates/{id}", cid)
                .then().statusCode(200)
                .body("skills.id", hasItem(sid));
    }

    @Test
    void linkSkillNotFound() {
        given().header("Authorization","Bearer "+token())
                .put("/candidates/{cid}/skills/{sid}", 999999, 1)
                .then().statusCode(404);
    }

    @Test
    void filterByCategoryDb_ok() {
        given().when().get("/candidates?category=DB")
                .then().statusCode(200)
                .body("[0].skills.find { it.category == 'DB' }.category", equalTo("DB"));
    }
}