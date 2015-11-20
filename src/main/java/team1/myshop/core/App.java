package team1.myshop.core;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Created by adanek on 20.11.2015.
 */

@ApplicationPath("api")
public class App extends ResourceConfig {
    public App(){
        packages("team1.myshop.core", "team1.myshop.web");
        System.out.println("Juchu! Ich wurde instanziert!");
    }
}
