package team1.myshop.web.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import team1.myshop.web.helper.PositionCalculator;
import team1.myshop.web.model.shops.OverpassResponse;

public class Category {
    public int id;
    public String name;
    public OverpassResponse or;

    private final static double SEARCH_RADIUS = 20;
    
    @Override
    public boolean equals(Object obj){
        boolean result = false;

        if(obj == null){
            return false;
        }

        Category other = (Category) obj;

        return other.id == this.id && other.name.equals(this.name);
    }

    public static Category parse(data.model.Category category) {
        if (category == null) {
            return null;
        }

        Category cat = new Category();

        cat.id = category.getId();
        cat.name = category.getName();

        Position pos = new Position();
        pos.latitude = 47.263472;    //default
        pos.longitude = 11.345929;   //default
        
        cat.or = getShops(cat.name, pos);
        
        return cat;
    }

    public static Collection<Category> parse(Collection<data.model.Category> categories) {

        List<Category> cats = new ArrayList<>();

        if (categories != null) {
            for (data.model.Category category : categories) {
                cats.add(Category.parse(category));
            }
        }

        // Katzen zurückgeben
        return cats;
    }
    
    private static OverpassResponse getShops(String name, Position pos){
    	
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


