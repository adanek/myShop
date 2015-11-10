package data.programs;

import java.util.Collection;
import java.util.Iterator;

import data.handler.DataHandler;
import data.model.Category;
import data.model.Item;
import data.model.ItemComment;
import data.model.SavedUser;

public class DatabaseTest {

	public static void main(String[] args) {
		
		DataHandler handler = new DataHandler();
		
		handler.deleteCategory(1);
		handler.deleteItem(6);
		
		handler.closeDatabaseConnection();
		
	}

}
