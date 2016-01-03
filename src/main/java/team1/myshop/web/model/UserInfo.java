package team1.myshop.web.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import data.model.SavedUser;
import team1.myshop.web.helper.AuthenticationService;

public class UserInfo {

	public String id;
	public String alias;
	public int userid; //?
	public String role;
	public UserRights rights;
	public String token;
	public AuthenticationType authenticationType = AuthenticationType.LOCAL;

	public static UserInfo parse(SavedUser user){
		UserInfo ui = new UserInfo();
		UserRights ur = new UserRights();

		ui.id = Integer.toString(user.getId());
		ui.alias = user.getAlias();
		ui.userid = user.getId();

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
