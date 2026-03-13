package app.utakata.ac.handler;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class IndexPageHandler {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String hello() {
        return "<!DOCTYPE html><html><body>Hello</body></html>";
    }
}
