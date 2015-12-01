package team1.myshop.web;

import org.apache.logging.log4j.LogManager;

import team1.myshop.web.model.Item;
import team1.myshop.web.model.UserCredentials;
import team1.myshop.web.model.UserInfo;
import data.model.SavedUser;
import team1.myshop.contracts.UserRights;
import team1.myshop.web.helper.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static javax.servlet.http.HttpServletResponse.*;

import java.util.ArrayList;
import java.util.Collection;

@Path("/users")
public class UserService extends ServiceBase {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<UserInfo> getUsers(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		this.initialize();

		// check user rights
		if (!auth.userHasRight(request, UserRights.CAN_QUERY_USERS)) {
			logger.info("User tried to access all users");
			http.cancelRequest(response, SC_UNAUTHORIZED);
			return null;
		}

		Collection<SavedUser> users = dh.getAllUsers();

		return UserInfo.parse(users);
	}
	
	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<String> getRoles(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		this.initialize();

		// check user rights
		if (!auth.userHasRight(request, UserRights.CAN_QUERY_ROLES)) {
			logger.info("User tried to access all roles");
			http.cancelRequest(response, SC_UNAUTHORIZED);
			return null;
		}

		Collection<String> roles = new ArrayList<>();
		
		//add roles
		roles.add("admin");
		roles.add("author");
		roles.add("guest");

		return roles;
	}
	
	@GET
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public UserInfo getUser(@PathParam("userid") int userid, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		this.initialize();

		// check user rights
		if (!auth.checkGetUserInfo(request, response, userid)) {
			logger.info("User tried to access the userinfo of another user");
			http.cancelRequest(response, SC_UNAUTHORIZED);
			return null;
		}

		SavedUser user = dh.getUserByID(userid);

		if (user == null) {
			http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
			return null;
		}

		return UserInfo.parse(user);
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public UserInfo login(String credstring, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		this.initialize();

		UserCredentials credentials = JsonParser.parse(credstring, UserCredentials.class);
		if (credentials == null) {
			logger.info("Could not parse user credentials: " + credstring);
			http.cancelRequest(response, SC_BAD_REQUEST);
			return null;
		}

		assert credentials != null;
		SavedUser user = dh.getUserLogin(credentials.name, credentials.hash);
		if (user == null) {
			http.cancelRequest(response, SC_UNAUTHORIZED);
			return null;
		}

		// create new session
		HttpSession session = request.getSession(true);

		// write userid to session
		assert user != null;
		session.setAttribute("userid", user.getId());

		// save userinfo in session
		UserInfo userInfo = UserInfo.parse(user);
		session.setAttribute("userInfo", userInfo);

		return userInfo;
	}

	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		this.initialize();

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
	public UserInfo register(String credstring, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		this.initialize();

		UserCredentials credentials = JsonParser.parse(credstring, UserCredentials.class);
		if (credentials == null) {
			logger.info("Could not parse user credentials: " + credstring);
			http.cancelRequest(response, SC_BAD_REQUEST);
			return null;
		}

		assert credentials != null;
		
		//user already exists?
		if(dh.getUserByName(credentials.name) != null){
			http.cancelRequest(response, SC_CONFLICT);
			return null;
		}
		
		SavedUser user = dh.createUser(credentials.name, credentials.hash, 2); // to-do

		// user create failed
		if (user == null) {
			http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
			return null;
		}

		assert user != null;
		response.setHeader("Location", "api/users/" + user.getId());
		response.setStatus(201);

		return UserInfo.parse(user);
	}

	@DELETE
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteUser(@PathParam("userid") int user, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		this.initialize();

		// check user rights
		if (!auth.userHasRight(request, UserRights.CAN_DELETE_USER)) {
			logger.info("User tried to delete a user, but did not have the right to");
			http.cancelRequest(response, SC_UNAUTHORIZED);
			return;
		}

		// delete user
		dh.deleteUser(user);

		response.setStatus(SC_NO_CONTENT);
	}

	@PUT
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public UserInfo changeUser(@PathParam("userid") int user, String userString, @Context HttpServletRequest request,
			@Context HttpServletResponse response) {

		this.initialize();

		// check user rights
		if (!auth.userHasRight(request, UserRights.CAN_EDIT_USER)) {
			logger.info("User tried to edit a user, but did not have the right to");
			http.cancelRequest(response, SC_UNAUTHORIZED);
			return null;
		}

		// parse body data
		UserInfo ui = JsonParser.parse(userString, UserInfo.class);
		if (ui == null) {
			http.cancelRequest(response, SC_BAD_REQUEST);
			return null;
		}

		// Parameter müssen übereinstimmen
		assert ui != null;
		if (ui.id != user) {
			http.cancelRequest(response, SC_BAD_REQUEST);
			return null;
		}

		int role = 0;

		switch (ui.role) {
		case "admin":
			role = 1;
			break;
		case "author":
			role = 2;
			break;
		case "guest":
			role = 3;
			break;
		}

		data.model.SavedUser saveduser = dh.changeUser(ui.id, role);
		return UserInfo.parse(saveduser);

	}

	@Override
	public void initializeLogger() {
		this.setLogger(LogManager.getLogger(UserService.class));
	}
}