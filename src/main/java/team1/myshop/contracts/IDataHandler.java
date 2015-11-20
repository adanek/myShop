package team1.myshop.contracts;

import java.util.Collection;

import data.model.Category;
import data.model.Item;
import data.model.ItemComment;
import data.model.SavedUser;

public interface IDataHandler {

	void closeDatabaseConnection() throws IllegalStateException;
	
	// change comment
	ItemComment changeComment(int commentID, String comment_text)
			throws IllegalArgumentException, IllegalStateException;

	// change item
	Item changeItem(int itemID, String title, String description, int categoryID)
			throws IllegalArgumentException, IllegalStateException;

	// change category
	Category changeCategory(int categoryID, String name) throws IllegalArgumentException, IllegalStateException;

	// change user
	SavedUser changeUser(int userID, String alias, int role) throws IllegalArgumentException, IllegalStateException;

	SavedUser createUser(String alias, String password, int role) throws IllegalStateException;

	Category createCategory(String name) throws IllegalStateException;

	Item createItem(String title, String description, int category, int author)
			throws IllegalStateException, IllegalArgumentException;

	// create item comment
	ItemComment createItemComment(String comment, int itemID, int author)
			throws IllegalStateException, IllegalArgumentException;

	// delete item comment
	void deleteComment(int commentID) throws IllegalArgumentException;

	// delete item
	void deleteItem(int itemID) throws IllegalArgumentException;

	// delete category
	void deleteCategory(int categoryID) throws IllegalArgumentException;

	// get all categories
	Collection<Category> getAllCategories() throws IllegalStateException;

	// get all items
	Collection<Item> getAllItems() throws IllegalStateException;

	// get all item comments
	Collection<ItemComment> getAllItemComments() throws IllegalStateException;

	// get all users
	Collection<SavedUser> getAllUsers() throws IllegalStateException;

	// search for user by ID
	SavedUser getUserByID(int id) throws IllegalArgumentException;

	// search for category by ID
	Category getCategoryByID(int id) throws IllegalArgumentException;

	// search for item by ID
	Item getItemByID(int id) throws IllegalArgumentException;

	// search for comment by ID
	ItemComment getItemCommentByID(int id) throws IllegalArgumentException;

	// get all comments from item
	Collection<ItemComment> getCommentsFromItem(int itemID) throws IllegalArgumentException, IllegalStateException;

	// get all items from category
	Collection<Item> getItemsFromCategory(int categoryID) throws IllegalArgumentException, IllegalStateException;

	//login user
	SavedUser getUserLogin(String alias, String password) throws IllegalStateException;

}