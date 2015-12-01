package data.programs;

import data.handler.DataHandler;
import team1.myshop.contracts.IDataHandler;

public class InitialUpload {

	public static void main(String[] args) {
		
		IDataHandler handler = new DataHandler(true);

		//categories
		int categoryID1 = handler.createCategory("Sport").getId();
		int categoryID2 = handler.createCategory("IT").getId();
		
		//user
		int userID1 = handler.createUser("Andi", "d033e22ae348aeb5660fc2140aec35850c4da997", 1).getId();
		int userID2 = handler.createUser("Pati2", "d033e22ae348aeb5660fc2140aec35850c4da997", 1).getId();
		int userID3 = handler.createUser("TestUser", "d033e22ae348aeb5660fc2140aec35850c4da997", 3).getId();
		
		//items
		int itemID1 = handler.createItem("Fake Item 1", "Laufschuh", categoryID1, userID2, 160.0).getId();
		int itemID2 = handler.createItem("Fake Item 2", "Laptop", categoryID2, userID1, 1200.0).getId();
		
		//item comments
		int commentID1 = handler.createItemComment("Super Laufschuh", itemID1, userID2).getId();
		int commentID2 = handler.createItemComment("Extrem teuer...", itemID2, userID1).getId();
		
		handler.closeDatabaseConnection();
		
	}

}
