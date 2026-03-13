package app.utakata.ac.presentation.handler;

import app.utakata.ac.application.VoiceGenerator;
import app.utakata.ac.application.VoiceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/voice")
public class VoiceHandler {
    private final VoiceGenerator voiceGenerator;

    public VoiceHandler(VoiceGenerator voiceGenerator) {
        this.voiceGenerator = voiceGenerator;
    }

    @GET
    @Produces("audio/mpeg")
    public Response get(
            @QueryParam("text") @NotBlank(message = "text must not be blank") String text,
            @QueryParam("speed") @Min(value = 1, message = "speed must be greater than or equal to 1") int speed,
            @QueryParam("type") @NotBlank(message = "type must not be blank")
            @Pattern(regexp = "high|highhigh|low", message = "type must be one of high, highhigh, low") String type
    ) {
        java.nio.file.Path outputPath = voiceGenerator.generate(text, speed, VoiceType.fromValue(type));
        return Response.ok(outputPath.toFile(), "audio/mpeg")
                .header("Content-Disposition", "attachment; filename=\"" + outputPath.getFileName() + "\"")
                .build();
    }
}
