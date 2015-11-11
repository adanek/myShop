package at.ac.uibk;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

public class AuthenticationService {

	public static void checkGetUserInfo(HttpServletRequest request, HttpServletResponse response, int userid){
		
		HttpSession session = request.getSession(false);
		
	   	if(session == null || (int)session.getAttribute("userid") != userid){
    		try {
				response.sendError(401);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		
	}
	
}
