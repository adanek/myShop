package team1.myshop.web;

import org.apache.logging.log4j.LogManager;
import team1.myshop.web.model.UserCredentials;
import team1.myshop.web.model.UserInfo;
import data.model.SavedUser;
import team1.myshop.web.helper.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static javax.servlet.http.HttpServletResponse.*;

@Path("/users")
public class UserService extends ServiceBase {

    @GET
    @Path("/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo getUser(@PathParam("userid") int userid, @Context HttpServletRequest request,
                            @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        if(!auth.checkGetUserInfo(request, response, userid)){
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

    @Override
    public void initializeLogger() {
        this.setLogger(LogManager.getLogger(UserService.class));
    }
}