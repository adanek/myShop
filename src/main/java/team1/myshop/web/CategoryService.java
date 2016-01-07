package team1.myshop.web;

import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import team1.myshop.contracts.UserRights;
import team1.myshop.web.model.Category;
import team1.myshop.web.model.OsloMarker;
import team1.myshop.web.model.Position;
import team1.myshop.web.model.ShopRequest;
import team1.myshop.web.model.shops.OverpassElement;
import team1.myshop.web.model.shops.OverpassResponse;
import team1.myshop.web.helper.JsonParser;
import team1.myshop.web.helper.PositionCalculator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@Path("/categories")
public class CategoryService extends ServiceBase {

    private final static double SEARCH_RADIUS = 20;
	
    public CategoryService() {
        super();
    }

    @Override
    public void initializeLogger() {
        this.setLogger(LogManager.getLogger(CategoryService.class));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Category> getCategories(@Context HttpServletResponse response) {

        this.initialize();
        Collection<data.model.Category> categories = dh.getAllCategories();

        if (categories == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
            return null;
        }

        return Category.parse(categories);
    }

    @GET
    @Path("/shops/{searchtoken}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<OsloMarker> getShops(@PathParam("searchtoken") String searchtoken, String tokenString, 
    		                               @Context HttpServletRequest request, @Context HttpServletResponse response){
    	
    	this.initialize();
    	
    	ShopRequest sr = JsonParser.parse(tokenString, ShopRequest.class);
    	
        // Validate data
        if (sr == null || sr.searchtoken != searchtoken) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }
    	
        OverpassResponse or = getShopsByToken(searchtoken, sr.position);
        
        List<OsloMarker> markers = new ArrayList<OsloMarker>();
        
        Iterator<OverpassElement> it = or.elements.iterator();
        
        while(it.hasNext()){
        	OverpassElement element = it.next();
        	
        	OsloMarker marker = new OsloMarker();
        	
        	marker.focus = false;
        	marker.lat = element.lat;
        	marker.lng = element.lon;
        	marker.message = element.tags.name;
        	
        	markers.add(marker);
        }
        
        return markers;
    }
    
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Category createCategory(String catString, @Context HttpServletRequest request,
                                   @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        if(!auth.userHasRight(request, UserRights.CAN_CREATE_CATEGORY)){
            logger.info("User wants to create a category, but did not have the right to");
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return null;
        }


        Category cat = JsonParser.parse(catString, Category.class);

        // parse body data
        if (cat == null) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        // Save the new category
        assert cat != null;
        data.model.Category category = dh.createCategory(cat.name, cat.searchtoken);

        if (category == null) {
            http.cancelRequest(response, SC_INTERNAL_SERVER_ERROR);
            return null;
        }

        // Set response headers
        response.setHeader("Location", "api/categories/" + cat.id);
        response.setStatus(HttpServletResponse.SC_CREATED);

        return Category.parse(category);
    }

    @PUT
    @Path("/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Category changeCategory(@PathParam("category") int category, String catString,
                                   @Context HttpServletRequest request, @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        if(!auth.userHasRight(request, UserRights.CAN_EDIT_CATEGORY)){
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return null;
        }

        Category cat = JsonParser.parse(catString, Category.class);

        // Validate data
        if (cat == null || cat.id != category) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return null;
        }

        // Save category
        assert cat != null;
        data.model.Category categoryDB = dh.changeCategory(category, cat.name, cat.searchtoken);

        return Category.parse(categoryDB);
    }

    @DELETE
    @Path("/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteCategory(@PathParam("category") int category, @Context HttpServletRequest request,
                               @Context HttpServletResponse response) {
        this.initialize();

        logger.debug("User tries to delete category "+category );

        // check user rights
        if (!auth.userHasRight(request, UserRights.CAN_DELETE_CATEGORY)) {
            logger.info("User tried to delete category " + category + " but did not have the right" + category);
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return;
        }

        // delete category
        logger.debug("User has the right to delete category " + category);
        dh.deleteCategory(category);
        logger.debug("User has deleted category " + category);

        response.setStatus(SC_NO_CONTENT);
    }
    
    private static OverpassResponse getShopsByToken(String name, Position pos){
    	
		double latitudeOffset = PositionCalculator.calcLongitudeOffset(pos.latitude, SEARCH_RADIUS);
		double longitudeOffset = PositionCalculator.calcLatitudeOffset(SEARCH_RADIUS);
    	
		String returnValue = null;
		
		try {

			URL url = new URL("http://overpass-api.de/api/interpreter?data=[out:json];node(" + (pos.latitude - latitudeOffset) + "," + (pos.longitude - longitudeOffset) + "," + (pos.latitude + latitudeOffset) + "," + (pos.longitude + longitudeOffset) + ")[shop=" + name + "];out;");
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.connect();
	
			try {
				// Object test = connection.getContent();
				InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
				BufferedReader buff = new BufferedReader(in);
				String line;
				StringBuffer buffer = new StringBuffer();
				do {
					line = buff.readLine();
					if (line != null) {
						buffer.append(line);
					}
				} while (line != null);

				if (buffer != null) {
					returnValue = buffer.toString();
				}
			} catch (IOException e) {
				returnValue = null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		OverpassResponse or = new OverpassResponse();
		Gson gson = new Gson();
		
		if (returnValue != null) {
			or = gson.fromJson(returnValue, new TypeToken<OverpassResponse>() {
			}.getType());
		}
		
    	return or;
    }
}
