package team1.myshop.web.model;

import data.model.SavedUser;

public class UserInfo {

	public int id;
	public String alias;
	public int userid; //?
	public String role;
	public UserRights rights;

	public static UserInfo parse(SavedUser user){
		UserInfo ui = new UserInfo();
		UserRights ur = new UserRights();

		ui.id = user.getId();
		ui.alias = user.getAlias();
		ui.userid = user.getId();

		// fill user role
		switch (user.getRole()) {
			case 1:
				ui.role = "admin";

				ur.canCreateCategory = true;
				ur.canCreateItem = true;
				ur.canCreateComment = true;
				ur.canDeleteCategory = true;
				ur.canDeleteItem = true;
				ur.canDeleteComment = true;
				ur.canEditCategory = true;
				ur.canEditItem = true;
				ur.canEditComment = true;
				ur.canDeleteUser = true;
				break;
			case 2:
				ui.role = "author";
				ur.canCreateItem = true;
				ur.canCreateComment = true;
				ur.canEditItem = true;
				ur.canEditComment = true;
				break;
			case 3:
				ui.role = "guest";
				ur.canCreateComment = true;
				break;
		}

		ui.rights = ur;

		return ui;
	}
}
