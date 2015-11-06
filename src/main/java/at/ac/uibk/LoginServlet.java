package at.ac.uibk;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<form action='/login' method='POST'>");
        response.getWriter().println("<label for='username'>Username: </label>");
        response.getWriter().println("<input type='text' id='username' name='username' autofocus required/>");
        response.getWriter().println("<label for='password'>Password: </label>");
        response.getWriter().println("<input type='text' id='password' name='password' required/>");
        response.getWriter().println("<input type='submit' class='btn' value='login'/>");
        response.getWriter().println("</form>");  
    }

}
