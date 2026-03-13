package app.utakata.ac.presentation.handler;

import app.utakata.ac.application.VoiceGenerator;
import app.utakata.ac.application.VoiceType;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class VoiceHandlerTest {

    @Test
    void shouldPassQueryParamsToVoiceGenerator() {
        SpyVoiceGenerator voiceGenerator = new SpyVoiceGenerator();
        VoiceHandler handler = new VoiceHandler(voiceGenerator);

        var response = handler.get("こんにちは", 10, "high");

        assertEquals("こんにちは", voiceGenerator.text);
        assertEquals(10, voiceGenerator.speed);
        assertEquals(VoiceType.HIGH, voiceGenerator.voiceType);
        assertEquals(200, response.getStatus());
        assertEquals("audio/mpeg", response.getMediaType().toString());
        assertEquals("attachment; filename=\"generated.mp3\"", response.getHeaderString("Content-Disposition"));
        assertInstanceOf(java.io.File.class, response.getEntity());
    }

    private static final class SpyVoiceGenerator extends VoiceGenerator {
        private String text;
        private int speed;
        private VoiceType voiceType;
        private final Path generatedFile = Path.of("/tmp/generated.mp3");

        private SpyVoiceGenerator() {
            super("src/main/resources/voice", "/tmp");
        }

        @Override
        public Path generate(String text, int speed, VoiceType voiceType) {
            this.text = text;
            this.speed = speed;
            this.voiceType = voiceType;
            return generatedFile;
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
