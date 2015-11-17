package at.ac.uibk;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import data.handler.IDataHandler;
import data.model.SavedUser;

public class AuthenticationService {

	public static void checkGetUserInfo(HttpServletRequest request, HttpServletResponse response, int userid) {

		HttpSession session = request.getSession(false);

		if (session == null || (int) session.getAttribute("userid") != userid) {
			HTTPStatusService.sendError(response.SC_UNAUTHORIZED, response);
		}

	}

	public static void checkAuthority(HttpServletRequest request, HttpServletResponse response, Rights right,
			IDataHandler handler) {

		boolean auth = false;

		HttpSession session = request.getSession(false);

		if (session != null) {
			int userid = (int) session.getAttribute("userid");

			// get user
			SavedUser user = handler.getUserByID(userid);
	
			if (user != null) {
				// guest authority
				if (right == Rights.CAN_CREATE_COMMENT) {
					if (user.getRole() <= 3) {
						auth = true;
					}
					// author authority
				} else if (right == Rights.CAN_CREATE_ITEM || right == Rights.CAN_EDIT_ITEM
						|| right == Rights.CAN_EDIT_COMMENT) {
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
		if (auth == false) {
			HTTPStatusService.sendError(response.SC_UNAUTHORIZED, response);
		}

	}

}
