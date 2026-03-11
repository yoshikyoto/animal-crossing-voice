package app.utakata.ac.presentation.handler;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class VoiceHandlerTest {

    @Test
    void shouldReturnOkStatus() {
        given()
                .when().get("/voice")
                .then()
                .statusCode(200)
                .body("status", is("ok"));
    }
}
