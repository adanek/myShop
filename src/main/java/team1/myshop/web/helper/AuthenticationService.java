package team1.myshop.web.helper;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import java.lang.IllegalArgumentException;

import data.model.SavedUser;
import org.apache.logging.log4j.LogManager;
import team1.myshop.contracts.UserRights;
import team1.myshop.web.ServiceBase;
import team1.myshop.web.model.AuthenticationType;
import team1.myshop.web.model.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class AuthenticationService extends ServiceBase implements team1.myshop.contracts.IAuthenticationService {

    private final String TOKEN_REGEX = "^Bearer ([a-zA-Z0-9\\-_]+\\.[a-zA-Z0-9\\-_]+\\.[a-zA-Z0-9\\-_]+)$";
    private final String SECRET = "MY_SECRET";

    @Override
    public UserInfo getUserInfo(HttpServletRequest request)
            throws NotAuthorizedException, IllegalArgumentException {

        this.initialize();

        String authorizationHeader = request.getHeader("Authorization");

        // Check exitence of token
        if (authorizationHeader == null) {
            throw new NotAuthorizedException("Authorization header is missing");
        }

        // Verify syntax of token
        if (!authorizationHeader.matches(TOKEN_REGEX)) {
            throw new IllegalArgumentException("Token has an invalid format");
        }

        // Extract userdata form token
        String token = authorizationHeader.replaceAll(TOKEN_REGEX, "$1");
        UserInfo userInfo = parseAuthToken(token);
        if (userInfo == null) {
            return null;
        }

        // Create userInfo
        switch (userInfo.authenticationType) {
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

    @Override
    public void ensureUserRight(HttpServletRequest request, UserRights right) throws NotAuthorizedException {

        this.initialize();

        UserInfo userInfo = null;
        boolean hasRight = false;

        try {
            userInfo = this.getUserInfo(request);

            switch (right) {
                case GET_USER_INFO:
                    hasRight = true;
                    break;
                case CAN_CREATE_CATEGORY:
                    hasRight = userInfo.rights.canCreateCategory;
                    break;
                case CAN_CREATE_ITEM:
                    hasRight = userInfo.rights.canCreateItem;
                    break;
                case CAN_CREATE_COMMENT:
                    hasRight = userInfo.rights.canCreateComment;
                    break;
                case CAN_DELETE_CATEGORY:
                    hasRight = userInfo.rights.canDeleteCategory;
                    break;
                case CAN_DELETE_ITEM:
                    hasRight = userInfo.rights.canDeleteItem;
                    break;
                case CAN_DELETE_COMMENT:
                    hasRight = userInfo.rights.canDeleteComment;
                    break;
                case CAN_DELETE_USER:
                    hasRight = userInfo.rights.canDeleteUser;
                    break;
                case CAN_EDIT_CATEGORY:
                    hasRight = userInfo.rights.canEditCategory;
                    break;
                case CAN_EDIT_ITEM:
                    hasRight = userInfo.rights.canEditItem;
                    break;
                case CAN_EDIT_COMMENT:
                    hasRight = userInfo.rights.canEditComment;
                    break;
                case CAN_EDIT_USER:
                    hasRight = userInfo.rights.canEditUser;
                    break;
                case CAN_QUERY_USERS:
                    hasRight = userInfo.rights.canQueryUsers;
                    break;
                case CAN_QUERY_ROLES:
                    hasRight = userInfo.rights.canQueryRoles;
                    break;
                case CAN_CREATE_ORDERS:
                    hasRight = userInfo.rights.canCreateOrders;
                    break;
                default:
                    hasRight = false;
            }

        } catch (NotAuthorizedException ex) {
            ex.printStackTrace();
        }


        if (userInfo == null || !hasRight) {
            throw new NotAuthorizedException(String.format("User %s has not the required right %s", userInfo.alias == null ? "unknown" : userInfo.alias, right));
        }
    }

    public boolean userHasRight(HttpServletRequest request, UserRights right) {

        this.initialize();

        UserInfo userInfo = null;
        try {
            userInfo = getUserInfo(request);
        }catch (NotAuthorizedException ex){}

        if (userInfo == null) {
            return false;
        }

        switch (right) {
            case CAN_DELETE_CATEGORY:
                return userInfo.rights.canDeleteCategory;
            case GET_USER_INFO:
                return true;
            case CAN_CREATE_CATEGORY:
                return userInfo.rights.canCreateCategory;
            case CAN_EDIT_CATEGORY:
                return userInfo.rights.canEditCategory;
            case CAN_CREATE_ITEM:
                return userInfo.rights.canCreateItem;
            case CAN_EDIT_ITEM:
                return userInfo.rights.canEditItem;
            case CAN_DELETE_ITEM:
                return userInfo.rights.canDeleteItem;
            case CAN_CREATE_COMMENT:
                return userInfo.rights.canCreateComment;
            case CAN_EDIT_COMMENT:
                return userInfo.rights.canEditComment;
            case CAN_DELETE_COMMENT:
                return userInfo.rights.canDeleteComment;
            case CAN_DELETE_USER:
                return userInfo.rights.canDeleteUser;
            case CAN_EDIT_USER:
                return userInfo.rights.canEditUser;
            case CAN_QUERY_USERS:
                return userInfo.rights.canQueryUsers;
            case CAN_QUERY_ROLES:
                return userInfo.rights.canQueryRoles;
            case CAN_CREATE_ORDERS:
                return userInfo.rights.canCreateOrders;
            default:
                return false;
        }
    }

    public String createAuthToken(UserInfo userInfo) {
        JWTSigner signer = new JWTSigner(SECRET);
        final HashMap<String, Object> claims = new HashMap<>(10);
        claims.put("iss", "https://webinfo-myshop.herokuapp.com");
        claims.put("sub", userInfo.id);
        claims.put("exp", LocalDateTime.now().plusHours(10).toEpochSecond(ZoneOffset.UTC));
        claims.put("iat", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        claims.put("alias", userInfo.alias);
        claims.put("role", userInfo.role);
        claims.put("auth_type", userInfo.authenticationType);

        return signer.sign(claims);
    }

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

    public team1.myshop.web.model.UserRights getRights(UserInfo user) {

        team1.myshop.web.model.UserRights ur = new team1.myshop.web.model.UserRights();
        switch (user.role) {
            case "admin":
                ur.canCreateCategory = true;
                ur.canCreateItem = true;
                ur.canCreateComment = true;
                ur.canDeleteCategory = true;
                ur.canDeleteItem = true;
                ur.canDeleteComment = true;
                ur.canEditCategory = true;
                ur.canEditItem = true;
                ur.canEditComment = true;
                ur.canDeleteUser = true;
                ur.canEditUser = true;
                ur.canQueryUsers = true;
                ur.canQueryRoles = true;
                ur.canCreateOrders = true;
                break;
            case "author":
                ur.canCreateComment = true;
                ur.canEditComment = true;
                ur.canCreateOrders = true;
                break;
            case "guest":
                ur.canCreateComment = true;
                break;
        }

        return ur;
    }

    public UserInfo getGitHubUserInfo(String token) {
        this.initialize();

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
        userInfo.rights = this.getRights(userInfo);

        return userInfo;
    }

    public String getGitHubToken(HttpServletRequest request) {

        this.initialize();

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

    @Override
    public void initializeLogger() {
        this.setLogger(LogManager.getLogger(AuthenticationService.class));
    }
}
