package at.ac.uibk;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class HTTPStatusService {

	public static void sendError(int sc, HttpServletResponse response){
		
		try {
			response.sendError(sc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
