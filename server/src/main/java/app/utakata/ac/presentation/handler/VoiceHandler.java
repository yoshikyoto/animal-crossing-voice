package app.utakata.ac.presentation.handler;

import app.utakata.ac.presentation.response.VoiceResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/voice")
public class VoiceHandler {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public VoiceResponse get() {
        return new VoiceResponse("ok");
    }
}
