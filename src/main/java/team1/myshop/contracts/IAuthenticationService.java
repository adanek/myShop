package team1.myshop.contracts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAuthenticationService {
	void checkGetUserInfo(HttpServletRequest request, HttpServletResponse response, int userid);

	void ensureUserRight(HttpServletRequest request, HttpServletResponse response, UserRights right);

	boolean userHasRight(HttpServletRequest request, UserRights right);
}
