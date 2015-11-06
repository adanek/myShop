package at.ac.uibk;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/user")
@Produces("text/html")
public class UserService {

    @GET
    public String getHello() {
        return "Hello User!";
    }

    @GET
    @Path("/{username}")
    public String getHello(@PathParam("username") String username){
        return "Hello " + username + "!";
    }
}