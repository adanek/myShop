package at.ac.uibk;


import org.eclipse.jetty.annotations.AnnotationParser;
import org.eclipse.jetty.io.RuntimeIOException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class Main{

    public static void main(String[] args) throws Exception {
//        Server server = new Server(8001);
//        //server.setHandler(new Main());
//        ServletHandler h = new ServletHandler();
//        server.setHandler(h);
//
//        h.addServletWithMapping(MyServlet.class, "/*");
//
//        server.start();
//        server.join();

        // Create a basic jetty server object that will listen on port 8001.
        // Note that if you set this to port 0 then a randomly available port
        // will be assigned that you can either look in the logs for the port,
        // or programmatically obtain it for use in test cases.
        Server server = new Server(8001);

        // The WebAppContext is the entity that controls the environment in
        // which a web application lives and breathes. In this example the
        // context path is being set to "/" so it is suitable for serving
        // root context requests and then we see it setting the location of
        // the war. A whole host of other configurations are available,
        // ranging from configuring to support annotation scanning in the
        // webapp (through PlusConfiguration) to choosing where the webapp
        // will unpack itself.
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        File war = new File("war/app2.war");
        if(!war.exists()){
            throw new RuntimeException("Unable to find war file " + war.getAbsolutePath());
        }

        webapp.setWar(war.getAbsolutePath());

        // A WebAppContext is a ContextHandler as well so it needs to be set to
        // the server so it is aware of where to send the appropriate requests.
        server.setHandler(webapp);

        // Start things up! By using the server.join() the server thread will
        // join with the current thread.
        // See http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Thread.html#join()
        // for more details.
        server.start();
        server.join();
    }
}
