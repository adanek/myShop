package team1.myshop.web.helper;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import data.model.SavedUser;
import org.apache.logging.log4j.LogManager;
import team1.myshop.contracts.UserRights;
import team1.myshop.web.ServiceBase;
import team1.myshop.web.model.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Enumeration;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class AuthenticationService extends ServiceBase implements team1.myshop.contracts.IAuthenticationService {

    @Override
    public boolean checkGetUserInfo(HttpServletRequest request, HttpServletResponse response, int userId) {

        initialize();
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }
        final int storedId = (int) session.getAttribute("userid");
        logger.debug(String.format("Comparing: %d with %d", userId, storedId));
        return  storedId == userId;
    }

    @Override
    public UserInfo getUserInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        UserInfo user = null;

        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            if (attributeNames.nextElement().equals("userInfo")) {
                user = (UserInfo) session.getAttribute("userInfo");
                break;
            }
        }

        return user;

    }

    @Override
    public void ensureUserRight(HttpServletRequest request, HttpServletResponse response, UserRights right) {

        this.initialize();
        boolean auth = false;

        HttpSession session = request.getSession(false);

        if (session != null) {
            int userid = (int) session.getAttribute("userid");

            // get user
            SavedUser user = dh.getUserByID(userid);

            if (user != null) {
                // guest authority
                if (right == UserRights.CAN_CREATE_COMMENT) {
                    if (user.getRole() <= 3) {
                        auth = true;
                    }
                    // author authority
                } else if (right == UserRights.CAN_CREATE_ITEM || right == UserRights.CAN_EDIT_ITEM
                        || right == UserRights.CAN_EDIT_COMMENT) {
                    if (user.getRole() <= 2) {
                        auth = true;
                    }
                    // admin authority
                } else {
                    if (user.getRole() == 1) {
                        auth = true;
                    }
                }
            }
        }

        // user is not authorized for this action
        if (!auth) {
            http.cancelRequest(response, SC_UNAUTHORIZED);
        }
    }

    public boolean userHasRight(HttpServletRequest request, UserRights right) {

        this.initialize();

        JWTVerifier verifier = new JWTVerifier("MY_SECRET");
        String token = null;
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            token = authorization.split(" ")[1];
        }
        if (token != null) {
            try {
                Map<String, Object> data = verifier.verify(token);
                this.logger.info("Authtoken successful verified");
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
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        UserInfo user = null;
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            if (attributeNames.nextElement().equals("userInfo")) {
                user = (UserInfo) session.getAttribute("userInfo");
                break;
            }
        }
        if (user == null) {
            return false;
        }

        switch (right) {
            case CAN_DELETE_CATEGORY:
                return user.rights.canDeleteCategory;
            case CAN_CREATE_CATEGORY:
                return user.rights.canCreateCategory;
            case CAN_EDIT_CATEGORY:
                return user.rights.canEditCategory;
            case CAN_CREATE_ITEM:
                return user.rights.canCreateItem;
            case CAN_EDIT_ITEM:
                return user.rights.canEditItem;
            case CAN_DELETE_ITEM:
                return user.rights.canDeleteItem;
            case CAN_CREATE_COMMENT:
                return user.rights.canCreateComment;
            case CAN_EDIT_COMMENT:
                return user.rights.canEditComment;
            case CAN_DELETE_COMMENT:
                return user.rights.canDeleteComment;
            case CAN_DELETE_USER:
                return user.rights.canDeleteUser;
            case CAN_EDIT_USER:
                return user.rights.canEditUser;
            case CAN_QUERY_USERS:
                return user.rights.canQueryUsers;
            case CAN_QUERY_ROLES:
                return user.rights.canQueryRoles;
            case CAN_CREATE_ORDERS:
                return user.rights.canCreateOrders;
            default:
                return false;
        }
    }

    @Override
    public void initializeLogger() {
        this.setLogger(LogManager.getLogger(AuthenticationService.class));
    }
}
