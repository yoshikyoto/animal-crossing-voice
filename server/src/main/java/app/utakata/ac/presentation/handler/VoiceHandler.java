package app.utakata.ac.presentation.handler;

import app.utakata.ac.application.VoiceGenerator;
import app.utakata.ac.application.VoiceType;
import app.utakata.ac.presentation.response.VoiceResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/voice")
public class VoiceHandler {
    private final VoiceGenerator voiceGenerator;

    public VoiceHandler(VoiceGenerator voiceGenerator) {
        this.voiceGenerator = voiceGenerator;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public VoiceResponse get(
            @QueryParam("text") String text,
            @QueryParam("speed") int speed,
            @QueryParam("type") String type
    ) {
        voiceGenerator.generate(text, speed, VoiceType.fromValue(type));
        return new VoiceResponse("ok");
    }
}
