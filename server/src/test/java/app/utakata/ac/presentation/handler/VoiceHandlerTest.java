package app.utakata.ac.presentation.handler;

import app.utakata.ac.application.VoiceGenerator;
import app.utakata.ac.application.VoiceType;
import app.utakata.ac.presentation.response.VoiceResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VoiceHandlerTest {

    @Test
    void shouldPassQueryParamsToVoiceGenerator() {
        SpyVoiceGenerator voiceGenerator = new SpyVoiceGenerator();
        VoiceHandler handler = new VoiceHandler(voiceGenerator);

        VoiceResponse response = handler.get("こんにちは", 10, "high");

        assertEquals("ok", response.status());
        assertEquals("こんにちは", voiceGenerator.text);
        assertEquals(10, voiceGenerator.speed);
        assertEquals(VoiceType.HIGH, voiceGenerator.voiceType);
    }

    private static final class SpyVoiceGenerator extends VoiceGenerator {
        private String text;
        private int speed;
        private VoiceType voiceType;

        private SpyVoiceGenerator() {
            super("../PokemonBattleTool/animal_crossing_voice", "/tmp");
        }

        @Override
        public void generate(String text, int speed, VoiceType voiceType) {
            this.text = text;
            this.speed = speed;
            this.voiceType = voiceType;
        }
    }
}

@QuarkusTest
class VoiceHandlerValidationTest {

    @Test
    void shouldRejectBlankText() {
        given()
                .queryParam("text", "")
                .queryParam("speed", 10)
                .queryParam("type", "high")
                .when()
                .get("/voice")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldRejectNonPositiveSpeed() {
        given()
                .queryParam("text", "こんにちは")
                .queryParam("speed", 0)
                .queryParam("type", "high")
                .when()
                .get("/voice")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldRejectUnknownType() {
        given()
                .queryParam("text", "こんにちは")
                .queryParam("speed", 10)
                .queryParam("type", "unknown")
                .when()
                .get("/voice")
                .then()
                .statusCode(400);
    }
}
