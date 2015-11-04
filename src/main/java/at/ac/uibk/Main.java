package at.ac.uibk;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Main {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8001);
        ServletHandler h = new ServletHandler();
        server.setHandler(h);

        h.addServletWithMapping(MyServlet.class, "/*");

        server.start();
        server.join();
    }

}
