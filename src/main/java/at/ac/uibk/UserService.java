package at.ac.uibk;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.UserCredentials;
import core.UserInfo;
import core.UserRights;
import data.handler.DataHandler;
import data.model.SavedUser;

@Path("/users")
public class UserService {

	private DataHandler handler;

	public UserService() {
		//handler = new DataHandler();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<SavedUser> getUsers(@Context HttpServletRequest req, @Context HttpServletResponse response) {

		HttpSession session = req.getSession(true);

		if (session == null) {
			HTTPStatusService.sendError(response.SC_UNAUTHORIZED, response);
		}

		session.setAttribute("User", "Pati");

    	handler = new DataHandler();
		
		Collection<SavedUser> users = handler.getAllUsers();
		
		//close db connection
		handler.closeDatabaseConnection();
		return users;
	}

	@GET
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public UserInfo getUser(@PathParam("userid") int userid, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

    	handler = new DataHandler();
		
		// check user rights
		AuthenticationService.checkGetUserInfo(request, response, userid);

		SavedUser user = handler.getUserByID(userid);
		
		//close db connection
		handler.closeDatabaseConnection();
		// SavedUser user = new SavedUser();
		//
		// user.setId(userid);
		// user.setAlias("Pati");
		// user.setRole(2);

		return mapUserDate(user);

	}

	@GET
	@Path("/test")
	@Produces("text/plain")
	public String test() {
		return "Servlet running!";
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public UserInfo login(String credstring, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		ObjectMapper om = new ObjectMapper();
		SavedUser user = null;

		try {
			UserCredentials uc = om.readValue(credstring, UserCredentials.class);

        	handler = new DataHandler();
			
			user = handler.getUserLogin(uc.name, uc.hash);
			// user = new SavedUser();
			//
			// user.setAlias(uc.name);
			//
			// if (uc.name.equals("Pati")) {
			// user.setRole(1);
			// } else {
			// user.setRole(2);
			// }
			// user.setId(3);
			// user.setPassword(uc.hash);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//close db connection
			handler.closeDatabaseConnection();
		}

		if (user == null) {
			HTTPStatusService.sendError(response.SC_UNAUTHORIZED, response);
		}

		// create session
		HttpSession session = request.getSession(true);

		// write userid to session
		session.setAttribute("userid", user.getId());

		// map data for output
		return mapUserDate(user);

	}

	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		HttpSession session = request.getSession(false);

		// destroy session
		if (session != null) {
			session.invalidate();
		}
		
		response.setHeader("Location", "/");
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	public void register(String credstring, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		ObjectMapper om = new ObjectMapper();
		SavedUser user = null;

		try {
			UserCredentials uc = om.readValue(credstring, UserCredentials.class);

        	handler = new DataHandler();
			
			// create user
			user = handler.createUser(uc.name, uc.hash, 2); // to-do

			// user = new SavedUser();
			//
			// user.setId(17);
			// user.setAlias("Pati");
			// user.setRole(2);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//close db connection
			handler.closeDatabaseConnection();
		}

		// user create failed
		if (user == null) {
			HTTPStatusService.sendError(response.SC_INTERNAL_SERVER_ERROR, response);
		} else {
			// response.setHeader("Location", "api/users/login");
			response.setStatus(201);
		}
	}

	private UserInfo mapUserDate(SavedUser user) {

		UserInfo ui = new UserInfo();
		UserRights ur = new UserRights();

		ui.id = user.getId();
		ui.alias = user.getAlias();
		ui.userid = user.getId();

		// fill user role
		switch (user.getRole()) {
		case 1:
			ui.role = "admin";

			ur.canCreateCategory = true;
			ur.canCreateItem = true;
			ur.canCreateComment = true;
			ur.canDeleteCategory = true;
			ur.canDeleteItem = true;
			ur.canDeleteComment = true;
			ur.canEditCategory = true;
			ur.canEditItem = true;
			ur.canEditComment = true;
			break;
		case 2:
			ui.role = "author";
			ur.canCreateItem = true;
			ur.canCreateComment = true;
			ur.canEditItem = true;
			ur.canEditComment = true;
			break;
		case 3:
			ui.role = "guest";
			ur.canCreateComment = true;
			break;
		}

		ui.rights = ur;

		return ui;
	}

}