package team1.myshop.contracts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotAuthorizedException;
import team1.myshop.web.model.UserInfo;

public interface IAuthenticationService {

	void ensureUserRight(HttpServletRequest request, UserRights right) throws NotAuthorizedException;

	boolean userHasRight(HttpServletRequest request, UserRights right);

	UserInfo getUserInfo(HttpServletRequest request) throws NotAuthorizedException, IllegalArgumentException;

	String createAuthToken(UserInfo userInfo);

    String getGitHubToken(HttpServletRequest request);
    UserInfo getGitHubUserInfo(String token);

}
