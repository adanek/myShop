package at.ac.uibk;


import org.eclipse.jetty.annotations.AnnotationParser;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Main extends AbstractHandler {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8001);
        //server.setHandler(new Main());
        ServletHandler h = new ServletHandler();
        server.setHandler(h);

        h.addServletWithMapping(MyServlet.class, "/*");

        server.start();
        server.join();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(200);
        response.getWriter().println("<h1>Hello Jetty embedded</h1>");
        response.getWriter().println("<p>Built with gradle!</p>");
        baseRequest.setHandled(true);
    }
}
