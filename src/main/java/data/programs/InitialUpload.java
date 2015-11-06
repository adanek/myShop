package data.programs;

import java.util.Collection;
import java.util.Iterator;

import data.handler.DataHandler;
import data.model.Category;
import data.model.Item;
import data.model.ItemComment;
import data.model.SavedUser;

public class InitialUpload {

	public static void main(String[] args) {
		
		DataHandler handler = new DataHandler();

//		//categories
//		int categoryID1 = handler.createCategory("Sport").getId();
//		int categoryID2 = handler.createCategory("IT").getId();
//		
//		//user
//		int userID1 = handler.createUser("Andi", "admin").getId();
//		int userID2 = handler.createUser("Pati", "admin").getId();
//		
//		//items
//		int itemID1 = handler.createItem("Adidas Boost", "Laufschuh", categoryID1, userID2).getId();
//		int itemID2 = handler.createItem("Lenovo ThinkPad", "Laptop", categoryID2, userID1).getId();
//		
//		//item comments
//		int commentID1 = handler.createItemComment("Super Laufschuh", itemID1, userID2).getId();
//		int commentID2 = handler.createItemComment("Extrem teuer...", itemID2, userID1).getId();
		
		Collection<Category> categories = handler.getAllCategories();
		
		Iterator<Category> cat_it = categories.iterator();
		
		while(cat_it.hasNext()){
			Category cat = cat_it.next();
			System.out.println(cat.getName());
			
			System.out.println(handler.getCategoryByID(cat.getId()).getName());
		}
		
		Collection<Item> items = handler.getAllItems();
		
		Iterator<Item> item_it = items.iterator();
		
		while(item_it.hasNext()){
			Item item = item_it.next();
			System.out.println(item.getTitle());
			
			System.out.println(handler.getItemByID(item.getId()).getTitle());
		}
		
		Collection<ItemComment> comments = handler.getAllItemComments();
		
		Iterator<ItemComment> comment_it = comments.iterator();
		
		while(comment_it.hasNext()){
			ItemComment comment = comment_it.next();
			System.out.println(comment.getComment());
			
			System.out.println(handler.getItemCommentByID(comment.getId()).getComment());
		}
		
		Collection<SavedUser> users = handler.getAllUsers();
		
		Iterator<SavedUser> user_it = users.iterator();
		
		while(user_it.hasNext()){
			SavedUser user = user_it.next();
			System.out.println(user.getAlias());
			
			System.out.println(handler.getUserByID(user.getId()).getAlias());
		}
		
		handler.closeDatabaseConnection();
		
	}

}
