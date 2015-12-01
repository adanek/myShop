package data.programs;

import data.handler.DataHandler;
import team1.myshop.contracts.IDataHandler;
import data.model.SavedUser;

public class DatabaseTest {

	public static void main(String[] args) {

		IDataHandler handler = new DataHandler(true);

		handler.deleteUser(4);

		//SavedUser user = handler.getUserByID(4);
		
		//System.out.println("User: " + user.getId() + " " + user.getAlias() + " " + user.getRole());

		handler.closeDatabaseConnection();

	}

}
