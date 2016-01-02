package team1.myshop.web;

import com.auth0.jwt.JWTSigner;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import org.apache.logging.log4j.LogManager;

import team1.myshop.web.model.AuthenticationType;
import team1.myshop.web.model.UserCredentials;
import team1.myshop.web.model.UserInfo;
import data.model.SavedUser;
import team1.myshop.contracts.UserRights;
import team1.myshop.web.helper.JsonParser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static javax.servlet.http.HttpServletResponse.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

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

    private final String TOKEN_REGEX = "^Bearer ([a-zA-Z0-9\\-_]+\\.[a-zA-Z0-9\\-_]+\\.[a-zA-Z0-9\\-_]+)$";

    @GET
    @Path("/{alias}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo getUser(@PathParam("alias") String alias, @Context HttpServletRequest request,
                            @Context HttpServletResponse response) {

        this.initialize();
        String authorizationHeader = request.getHeader("Authorization");

        // Check exitence of token
        if (authorizationHeader == null) {
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return null;
        }

        // Verify syntax of token
        if (!authorizationHeader.matches(TOKEN_REGEX)) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        // Extract userdata form token
        String token = authorizationHeader.replaceAll(TOKEN_REGEX, "$1");
        UserInfo userInfo = parseAuthToken(token);
        if(userInfo == null){
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        // Check user right
        if(!userInfo.alias.equals(alias)){
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return null;
        }

        // Create userInfo
        switch(userInfo.authenticationType){
            case LOCAL:
                userInfo = UserInfo.parse(dh.getUserByID(userInfo.userid));
                break;
            case OAUTH_GITHUB:
                userInfo = getGitHubUserInfo(userInfo.id);
                break;
        }
        userInfo.token = token;

        return userInfo;
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

        createAuthToken(userInfo);
        return userInfo;
    }

    private final String SECRET = "MY_SECRET";

    private UserInfo parseAuthToken(String token) {

        UserInfo userInfo = null;
        JWTVerifier verifier = new JWTVerifier(SECRET);
        try {
            Map<String, Object> payload = verifier.verify(token);

            userInfo = new UserInfo();
            userInfo.id = (String) payload.get("uid");
            userInfo.alias = (String) payload.get("alias");
            userInfo.role = (String) payload.get("role");
            userInfo.authenticationType = AuthenticationType.valueOf((String) payload.get("auth_type"));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (JWTVerifyException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    private void createAuthToken(UserInfo userInfo) {
        JWTSigner signer = new JWTSigner(SECRET);
        final HashMap<String, Object> claims = new HashMap<>(10);
        claims.put("uid", userInfo.id);
        claims.put("alias", userInfo.alias);
        claims.put("role", userInfo.role);
        claims.put("auth_type", userInfo.authenticationType);
        userInfo.token = signer.sign(claims);
    }


    @GET
    @Path("/login/oauth")
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo loginWithGithub(@Context HttpServletRequest request, @Context HttpServletResponse response) {

        this.initialize();

        String token = getGitHubToken(request);
        UserInfo userInfo = getGitHubUserInfo(token);
        createAuthToken(userInfo);

        return userInfo;
    }

    private UserInfo getGitHubUserInfo(String token) {
        String url = "https://api.github.com/user";
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "token " + token);
        String userdata = http.get(url, params, headers);

        UserInfo userInfo = new UserInfo();
        userInfo.alias = JsonParser.get(userdata, "login");
        userInfo.id = token;
        userInfo.role = "author";
        userInfo.authenticationType = AuthenticationType.OAUTH_GITHUB;
        userInfo.rights = UserInfo.getRights(userInfo);

        return userInfo;
    }

    private String getGitHubToken(@Context HttpServletRequest request) {
        String url = "https://github.com/login/oauth/access_token";

        Map<String, String> params = new HashMap<>();
        params.put("client_id", "2cd8ce35fb2392ce6d04");
        params.put("client_secret", "ef073352dc0ea7cb169ea75e8393d3f5dfa4a51e");
        params.put("redirect_uri", "http://localhost:8000/api/users/login/oauth/response");
        params.put("code", request.getParameter("code"));
        params.put("state", request.getParameter("state"));

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", MediaType.APPLICATION_JSON);

        String response = http.post(url, params, headers);
        return JsonParser.get(response, "access_token");
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
        if (dh.getUserByName(credentials.name) != null) {
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