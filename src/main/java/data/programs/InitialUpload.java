package data.programs;

import data.handler.DataHandler;

public class InitialUpload {

	public static void main(String[] args) {
		
		DataHandler handler = new DataHandler();

		//categories
		int categoryID1 = handler.createCategory("Sport").getId();
		int categoryID2 = handler.createCategory("IT").getId();
		
		//user
		int userID1 = handler.createUser("Andi", "admin").getId();
		int userID2 = handler.createUser("Pati", "admin").getId();
		
		//items
		int itemID1 = handler.createItem("Adidas Boost", "Laufschuh", categoryID1, userID2).getId();
		int itemID2 = handler.createItem("Lenovo ThinkPad", "Laptop", categoryID2, userID1).getId();
		
		//item comments
		int commentID1 = handler.createItemComment("Super Laufschuh", itemID1, userID2).getId();
		int commentID2 = handler.createItemComment("Extrem teuer...", itemID2, userID1).getId();
		
		handler.closeDatabaseConnection();
		
	}

}
