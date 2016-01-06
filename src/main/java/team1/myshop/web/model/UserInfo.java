package team1.myshop.web.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

import data.model.SavedUser;
import team1.myshop.web.helper.AuthenticationService;

public class UserInfo {

	public String id;
	public String alias;
	public String role;
	public UserRights rights;
	public Address address;
	public String token;
	public AuthenticationType authenticationType = AuthenticationType.LOCAL;

	public static UserInfo parse(SavedUser user){
		UserInfo ui = new UserInfo();
		UserRights ur = new UserRights();

		ui.id = Integer.toString(user.getId());
		ui.alias = user.getAlias();
		
		//Address
		Address addr = new Address();
		addr.zip = user.getZip();
		addr.city = user.getCity();
		addr.country = user.getCountry();
		addr.street = user.getStreet();
		addr.latitude = user.getLatitude();
		addr.longitude = user.getLongitude();
		
        //get calculated geo coordinates from address
        final Geocoder geocoder = new Geocoder();
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(addr.street + " " + addr.zip + " " + addr.city + ", " + addr.country).setLanguage("de").getGeocoderRequest();
        try {
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			List<GeocoderResult> result = geocoderResponse.getResults();
			
			if(result.size() > 0){
				GeocoderResult location = result.get(0);
				addr.latitude_calc  = location.getGeometry().getLocation().getLat().doubleValue();
				addr.longitude_calc = location.getGeometry().getLocation().getLng().doubleValue();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ui.address = addr;

		// fill user role
		switch (user.getRole()) {
			case 1:
				ui.role = "admin";			
				break;
			case 2:
				ui.role = "author";				
				break;
			case 3:
				ui.role = "guest";			
				break;
		}

		ui.rights = new AuthenticationService().getRights(ui);

		return ui;
	}
	
	public static Collection<UserInfo> parse(Collection<SavedUser> users){

		List<UserInfo> info = new ArrayList<>();
		
		for(SavedUser u : users){
			info.add(UserInfo.parse(u));
		}
		return info;
		
	}
	
}
