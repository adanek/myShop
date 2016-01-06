package team1.myshop.web;

import data.model.SavedUser;
import javassist.bytecode.Descriptor.Iterator;

import org.apache.logging.log4j.LogManager;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLngBounds;

import team1.myshop.contracts.UserRights;
import team1.myshop.web.helper.JsonParser;
import team1.myshop.web.model.UserCredentials;
import team1.myshop.web.model.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

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
    @Path("/{alias}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo getUser(@PathParam("alias") String alias,
                            @Context HttpServletRequest request,
                            @Context HttpServletResponse response) {

        this.initialize();

        try {
            UserInfo userInfo = this.auth.getUserInfo(request);

            // Check user right
            if (!userInfo.alias.equals(alias)) {
                http.cancelRequest(response, SC_UNAUTHORIZED);
                return null;
            }

            return userInfo;

        } catch (NotAuthorizedException ex) {
            http.cancelRequest(response, SC_UNAUTHORIZED);

        } catch (IllegalArgumentException ex) {
            http.cancelRequest(response, SC_BAD_REQUEST);
        }

        return null;
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

        // Create auth token
        UserInfo userInfo = UserInfo.parse(user);
        
        userInfo.token = this.auth.createAuthToken(userInfo);


//        // create new session
//        HttpSession session = request.getSession(true);
//
//        // write userid to session
//        assert user != null;
//        session.setAttribute("userid", user.getId());
//
//        // save userinfo in session
//        session.setAttribute("userInfo", userInfo);

        return userInfo;
    }


    @GET
    @Path("/login/oauth")
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo loginWithGithub(@Context HttpServletRequest request,
                                    @Context HttpServletResponse response) {

        this.initialize();

        String token = this.auth.getGitHubToken(request);
        UserInfo userInfo = this.auth.getGitHubUserInfo(token);
        userInfo.token = this.auth.createAuthToken(userInfo);

        return userInfo;
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
        if (dh.getUserByName(credentials.name) != null) {
            http.cancelRequest(response, SC_CONFLICT);
            return null;
        }

        SavedUser user = dh.createUser(credentials.name, credentials.hash, 2, credentials.address);

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
    public UserInfo changeUser(@PathParam("userid") int userID, String userString, @Context HttpServletRequest request,
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
        if (!ui.id.equals(Integer.toString(userID))) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        //der eigene User darf nicht ge�ndert werden
        if (auth.getUserInfo(request).id.equals(Integer.toString(userID))) {
            http.cancelRequest(response, SC_FORBIDDEN);
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

        data.model.SavedUser saveduser = dh.changeUser(Integer.parseInt(ui.id), role);
        return UserInfo.parse(saveduser);
    }

    @Override
    public void initializeLogger() {
        this.setLogger(LogManager.getLogger(UserService.class));
    }
}