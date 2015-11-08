package at.ac.uibk;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import data.handler.DataHandler;
import data.model.SavedUser;

@Path("/user")
//@Produces("text/html")
public class UserService {

	private DataHandler handler;
	
	public UserService(){
		handler = new DataHandler();
	}
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<SavedUser> getHello() {
        
    	return handler.getAllUsers();
    	
    }

    @GET
    @Path("/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public SavedUser getHello(@PathParam("userid") int userid){

    	return handler.getUserByID(userid);
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test(){
        return "Servlet running!";
    }
}