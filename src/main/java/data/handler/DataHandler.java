package data.handler;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import data.model.Category;
import data.model.Item;
import data.model.ItemComment;
import data.model.SavedUser;
import team1.myshop.contracts.IDataHandler;
import team1.myshop.web.model.Address;

public class DataHandler implements IDataHandler {

	private SessionFactory sessionFactory;
	private ServiceRegistry serviceRegistry;
	private Connection connection;

	// private constructor
	public DataHandler(boolean test) throws IllegalStateException {

		try {
			// connect to db
			// connection = connectToDatabase();

			// create session factory
			Configuration configuration = new Configuration();
			// productive DB
			if (test) {
				configuration.configure("hibernate.cfg.test.xml");
			} else {
				configuration.configure();
			}
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (HibernateException e) {
			System.out.println("Hibernate problems");
			throw new IllegalStateException("Hibernate problems");
		} catch (IllegalStateException e) {
			System.out.println("no connection to database");
			throw new IllegalStateException("no connection to database");
		} catch (Exception e) {
			System.out.println("some connection error");
		}
	}

	// private constructor
	public DataHandler() throws IllegalStateException {

		this(false);
	}

	/**
	 * connect to the database
	 * 
	 * @return the Connection to the database
	 * @throws URISyntaxException
	 *             throw this exception when the URI to the database is wrong
	 * @throws IllegalStateException
	 *             throw this exception when it is not possible to connect
	 */
	private Connection connectToDatabase() throws URISyntaxException, IllegalStateException {

		URI dbUri = new URI(
				"postgres://cokvwecanelrtv:VvwJFAhDt67qXlTACqNZAW9g0r@ec2-54-83-36-203.compute-1.amazonaws.com:5432/dc9eqmlj8jqkap");

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
				+ "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		Connection conn;
		try {
			conn = DriverManager.getConnection(dbUrl, username, password);
		} catch (Exception e) {
			System.out.println("closing connection not possible");
			throw new IllegalStateException("closing connection not possible");
		}

		return conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#closeDatabaseConnection()
	 */
	@Override
	public void closeDatabaseConnection() throws IllegalStateException {
		// sessionFactory.close();
		// try {
		// connection.close();
		// } catch (Exception e) {
		// System.out.println("closing connection not possible");
		// throw new IllegalStateException("closing connection not possible");
		// }
	}

	/**
	 * save an object to the database, when it is an entity
	 * 
	 * @param obj
	 *            the object of an entity
	 * @return the ID of the entity
	 * @throws IllegalStateException
	 *             commit failed by saving from object
	 */
	private Integer saveObjectToDb(Object obj) throws IllegalStateException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// save an object
			int id = (int) session.save(obj);

			// commit
			session.getTransaction().commit();

			return id;

		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("saving from object not possible");
			throw new IllegalStateException("saving from object not possible", e);
		} finally {
			// close session
			session.close();
		}

	}

	/**
	 * deletes an object from the database, when it is an entity
	 * 
	 * @param obj
	 *            the object of an entity
	 * @throws IllegalArgumentException
	 *             deletion of object failed
	 */
	private void deleteObjectFromDb(Object obj) throws IllegalArgumentException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// save an object
			session.delete(obj);

			// commit
			session.getTransaction().commit();

		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();

			// deletion failed
			System.out.println("deletion of object failed");
			throw new IllegalArgumentException("deletion of object failed", e);
		} finally {
			// close session
			session.close();
		}
	}

	/**
	 * open a new Session
	 * 
	 * @return the session
	 */
	private Session openSession() {

		return sessionFactory.openSession();
	}

	// change comment
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#changeComment(int,
	 * java.lang.String)
	 */
	@Override
	public ItemComment changeComment(int commentID, String comment_text)
			throws IllegalArgumentException, IllegalStateException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// get comment
			Criteria cr = session.createCriteria(ItemComment.class);
			cr.add(Restrictions.eq("id", commentID));
			List<ItemComment> results = cr.list();

			if (results.size() == 0)
				throw new IllegalArgumentException("commentID");

			ItemComment comment = results.get(0);

			// change comment text
			comment.setComment(comment_text);
			comment.setChangeDate(new Date());

			// update comment
			session.update(comment);

			// commit
			session.getTransaction().commit();

			return comment;

		} catch (IllegalArgumentException e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("no comment with this ID in the database");
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("comment change");
		} finally {
			// close session
			session.close();
		}

	}

	// change item
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#changeItem(int,
	 * java.lang.String, java.lang.String, int)
	 */
	@Override
	public Item changeItem(int itemID, String title, String description, int categoryID, double price)
			throws IllegalArgumentException, IllegalStateException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// get item
			Criteria cr = session.createCriteria(Item.class);
			cr.add(Restrictions.eq("id", itemID));
			List<Item> results = cr.list();

			if (results.size() == 0)
				throw new IllegalArgumentException("itemID");

			// get category
			Category cat = getCategoryByID(categoryID);

			if (cat == null) {
				throw new IllegalArgumentException("categoryID");
			}

			Item item = results.get(0);

			// change item
			item.setTitle(title);
			item.setDescription(description);
			item.setCategory(cat);
			item.setChangeDate(new Date());
			item.setPrice(price);

			// update item
			session.update(item);

			// commit
			session.getTransaction().commit();

			return item;

		} catch (IllegalArgumentException e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("no item with this ID in the database");
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("item change");
		} finally {
			// close session
			session.close();
		}

	}

	// change category
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#changeCategory(int,
	 * java.lang.String)
	 */
	@Override
	public Category changeCategory(int categoryID, String name, String searchtoken) throws IllegalArgumentException, IllegalStateException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// get category
			Criteria cr = session.createCriteria(Category.class);
			cr.add(Restrictions.eq("id", categoryID));
			List<Category> results = cr.list();

			if (results.size() == 0)
				throw new IllegalArgumentException("categoryID");

			Category category = results.get(0);

			// change category name
			category.setName(name);
			// change category searchtoken
			category.setSearchtoken(searchtoken);

			// update category
			session.update(category);

			// commit
			session.getTransaction().commit();

			return category;

		} catch (IllegalArgumentException e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("no category with this ID in the database");
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("category change");
		} finally {
			// close session
			session.close();
		}

	}

	// change user
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#changeUser(int,
	 * java.lang.String, int)
	 */
	@Override
	public SavedUser changeUser(int userID, int role)
			throws IllegalArgumentException, IllegalStateException {

		Session session = openSession();

		if (role < 1 || role > 3) {
			throw new IllegalArgumentException("invalid role");
		}

		try {

			// begin transaction
			session.beginTransaction();

			// get user
			Criteria cr = session.createCriteria(SavedUser.class);
			cr.add(Restrictions.eq("id", userID));
			List<SavedUser> results = cr.list();

			if (results.size() == 0)
				throw new IllegalArgumentException("userID");

			SavedUser user = results.get(0);

			// change user data
			user.setRole(role);

			// update user
			session.update(user);

			// commit
			session.getTransaction().commit();

			return user;

		} catch (IllegalArgumentException e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("no user with this ID in the database");
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("user change");
		} finally {
			// close session
			session.close();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#createUser(java.lang.String,
	 * java.lang.String, int)
	 */
	@Override
	public SavedUser createUser(String alias, String password, int role, Address address) throws IllegalStateException {

		// create user instance
		SavedUser user = new SavedUser();
		user.setAlias(alias);

		if (role < 1 || role > 3) {
			throw new IllegalStateException("invalid user role");
		}

		user.setRole(role);
		user.setZip(address.zip);
		user.setStreet(address.street);
		user.setCity(address.city);
		user.setCountry(address.country);
		user.setLatitude(address.latitude);
		user.setLongitude(address.longitude);

		try {
			user.setPassword(PasswordHash.getSaltedHash(password));
		} catch (Exception e) {
			System.out.println("Creation of Hash failed");
			throw new IllegalStateException("Creation of Hash failed", e);
		}

		// save user in database
		saveObjectToDb(user);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#createCategory(java.lang.String)
	 */
	@Override
	public Category createCategory(String name, String searchtoken) throws IllegalStateException {

		// create category instance
		Category category = new Category();
		category.setName(name);
		category.setSearchtoken(searchtoken);

		saveObjectToDb(category);
		return category;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#createItem(java.lang.String,
	 * java.lang.String, int, int)
	 */
	@Override
	public Item createItem(String title, String description, int category, int author, double price)
			throws IllegalStateException, IllegalArgumentException {

		SavedUser user;
		Category cat;

		Session session = openSession();

		try {
			Criteria cr = session.createCriteria(SavedUser.class);
			cr.add(Restrictions.eq("id", author));
			user = (SavedUser) cr.list().get(0);
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("authorID in database not found");
			// close session
			session.close();
			throw new IllegalArgumentException("authorID: not in database found", e);
		}

		try {
			Criteria cr = session.createCriteria(Category.class);
			cr.add(Restrictions.eq("id", category));
			cat = (Category) cr.list().get(0);
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("categoryID in database not found");
			// close session
			session.close();
			throw new IllegalArgumentException("category: not in database found", e);
		}

		// create item instance
		Item item = new Item();
		item.setTitle(title);
		item.setDescription(description);
		item.setAuthor(user);
		item.setCategory(cat);
		item.setCreationDate(new Date());
		item.setPrice(price);

		saveObjectToDb(item);
		return item;

	}

	// create item comment
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * team1.myshop.contracts.IDataHandler#createItemComment(java.lang.String,
	 * int, int)
	 */
	@Override
	public ItemComment createItemComment(String comment, int itemID, int author)
			throws IllegalStateException, IllegalArgumentException {

		Item item;
		SavedUser user;

		Session session = openSession();

		try {
			Criteria cr = session.createCriteria(Item.class);
			cr.add(Restrictions.eq("id", itemID));
			item = (Item) cr.list().get(0);
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("categoryID in database not found");
			// close session
			session.close();
			throw new IllegalArgumentException("category: not in database found", e);
		}

		try {
			Criteria cr = session.createCriteria(SavedUser.class);
			cr.add(Restrictions.eq("id", author));
			user = (SavedUser) cr.list().get(0);
		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("authorID in database not found");
			// close session
			session.close();
			throw new IllegalArgumentException("authorID not in database found", e);
		}

		ItemComment itemComment = new ItemComment();

		itemComment.setComment(comment);
		itemComment.setItem(item);
		itemComment.setAuthor(user);
		itemComment.setCreationDate(new Date());
		itemComment.setChangeDate(new Date());

		saveObjectToDb(itemComment);
		return itemComment;
	}

	// delete item comment
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#deleteComment(int)
	 */
	@Override
	public void deleteComment(int commentID) throws IllegalArgumentException {
		try {
			// get comment
			ItemComment comment = getItemCommentByID(commentID);
			// delete comment from database
			deleteObjectFromDb(comment);
		} catch (IllegalArgumentException e) {
			System.out.println("deletion or getting comment from ID failed");
			throw new IllegalArgumentException("deletion or getting comment from ID failed", e);
		}
	}

	@Override
	public void deleteUser(int userID) throws IllegalArgumentException {
		try {
			// SavedUser
			SavedUser user = getUserByID(userID);
			// delete comment from database
			deleteObjectFromDb(user);
		} catch (IllegalArgumentException e) {
			System.out.println("deletion or getting user from ID failed");
			throw new IllegalArgumentException("deletion or getting user from ID failed", e);
		}
	}

	// delete item
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#deleteItem(int)
	 */
	@Override
	public void deleteItem(int itemID) throws IllegalArgumentException {
		try {
			// get comment
			Item item = getItemByID(itemID);
			// delete item from database
			deleteObjectFromDb(item);
		} catch (IllegalArgumentException e) {
			System.out.println("deletion or getting item from ID failed");
			throw new IllegalArgumentException("deletion or getting item from ID failed", e);
		}
	}

	// delete category
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#deleteCategory(int)
	 */
	@Override
	public void deleteCategory(int categoryID) throws IllegalArgumentException {
		try {
			// get category
			Category category = getCategoryByID(categoryID);
			// delete category from database
			deleteObjectFromDb(category);
		} catch (IllegalArgumentException e) {
			System.out.println("deletion or getting category from ID failed");
			throw new IllegalArgumentException("deletion or getting category from ID failed", e);
		}
	}

	// get all categories
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getAllCategories()
	 */
	@Override
	public Collection<Category> getAllCategories() throws IllegalStateException {

		try {

			return (Collection<Category>) getTableData(Category.class);

		} catch (Exception e) {
			// Exception
			throw new IllegalStateException("something went wrong by getting the category list");
		}
	}

	// get all items
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getAllItems()
	 */
	@Override
	public Collection<Item> getAllItems() throws IllegalStateException {

		try {

			return (Collection<Item>) getTableData(Item.class);

		} catch (Exception e) {
			// Exception
			throw new IllegalStateException("something went wrong by getting the item list");
		}
	}

	// get all item comments
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getAllItemComments()
	 */
	@Override
	public Collection<ItemComment> getAllItemComments() throws IllegalStateException {

		try {

			return (Collection<ItemComment>) getTableData(ItemComment.class);

		} catch (Exception e) {
			// Exception
			throw new IllegalStateException("something went wrong by getting the item comment list");
		}
	}

	// get all users
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getAllUsers()
	 */
	@Override
	public Collection<SavedUser> getAllUsers() throws IllegalStateException {

		try {

			return (Collection<SavedUser>) getTableData(SavedUser.class);

		} catch (Exception e) {
			// Exception
			throw new IllegalStateException("something went wrong by getting the user list");
		}
	}

	// search for user by ID
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getUserByID(int)
	 */
	@Override
	public SavedUser getUserByID(int id) throws IllegalArgumentException {
		return this.<SavedUser> searchForID(id, SavedUser.class);
	}

	// search for user by name
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getUserByID(int)
	 */
	@Override
	public SavedUser getUserByName(String alias) throws IllegalArgumentException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			Criteria cr = session.createCriteria(SavedUser.class);
			cr.add(Restrictions.eq("alias", alias));
			List<SavedUser> results = cr.list();

			// commit
			session.getTransaction().commit();

			//user not available
			if(results.size() < 1){
				return null;
			}
			
			// only one element in the list because the id is unique
			return results.get(0);

		} catch (IndexOutOfBoundsException e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalArgumentException("object with this ID is not in the database", e);
		} finally {
			// close session
			session.close();
		}

		
	}
	
	// search for category by ID
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getCategoryByID(int)
	 */
	@Override
	public Category getCategoryByID(int id) throws IllegalArgumentException {
		return this.<Category> searchForID(id, Category.class);
	}

	// search for item by ID
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getItemByID(int)
	 */
	@Override
	public Item getItemByID(int id) throws IllegalArgumentException {
		return this.<Item> searchForID(id, Item.class);
	}

	// search for comment by ID
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getItemCommentByID(int)
	 */
	@Override
	public ItemComment getItemCommentByID(int id) throws IllegalArgumentException {
		return this.<ItemComment> searchForID(id, ItemComment.class);
	}

	// get all comments from item
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getCommentsFromItem(int)
	 */
	@Override
	public Collection<ItemComment> getCommentsFromItem(int itemID)
			throws IllegalArgumentException, IllegalStateException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			Criteria cr = session.createCriteria(Item.class);
			cr.add(Restrictions.eq("id", itemID));
			List<Item> results = cr.list();

			if (results.size() == 0)
				throw new IllegalArgumentException();
			// item not found with this id

			Collection<ItemComment> comments = results.get(0).getComments();

			Collection<ItemComment> ret = new ArrayList<>(comments);

			// commit
			session.getTransaction().commit();

			return ret;

		} catch (IllegalArgumentException e) { // Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("no item with this ID in the database");
			throw new IllegalArgumentException("no item with this ID in the database");
		} catch (Exception e) { // Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the item comment list");
		} finally { // close session
			session.close();
		}

	}

	// get all items from category
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getItemsFromCategory(int)
	 */
	@Override
	public Collection<Item> getItemsFromCategory(int categoryID)
			throws IllegalArgumentException, IllegalStateException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			Criteria cr = session.createCriteria(Category.class);
			cr.add(Restrictions.eq("id", categoryID));
			List<Category> results = cr.list();

			if (results.size() == 0)
				throw new IllegalArgumentException();
			// category not found with this id

			Collection<Item> items = results.get(0).getItems();

			Collection<Item> ret = new ArrayList<>(items);

			// commit
			session.getTransaction().commit();

			return ret;

		} catch (IllegalArgumentException e) { // Exception -> rollback
			session.getTransaction().rollback();
			System.out.println("no category with this ID in the database");
			throw new IllegalArgumentException("no category with this ID in the database");
		} catch (Exception e) { // Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the item list");
		} finally { // close session
			session.close();
		}

	}

	// select all data from table
	private Collection<?> getTableData(Class cls) throws IllegalStateException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// get all categories
			Criteria cr = session.createCriteria(cls);
			List<?> results = cr.list();

			// commit
			session.getTransaction().commit();

			return results;

		} catch (Exception e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the data");
		} finally {
			// close session
			session.close();
		}

	}

	// search for a single dataset by ID
	private <T> T searchForID(int id, Class<T> typeParameterClass) throws IllegalArgumentException {

		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			Criteria cr = session.createCriteria(typeParameterClass);
			cr.add(Restrictions.eq("id", id));
			List<T> results = cr.list();

			// commit
			session.getTransaction().commit();

			// only one element in the list because the id is unique
			return results.get(0);

		} catch (IndexOutOfBoundsException e) {
			// Exception -> rollback
			session.getTransaction().rollback();
			throw new IllegalArgumentException("object with this ID is not in the database", e);
		} finally {
			// close session
			session.close();
		}
	}

	// login user
	/*
	 * (non-Javadoc)
	 * 
	 * @see team1.myshop.contracts.IDataHandler#getUserLogin(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public SavedUser getUserLogin(String alias, String password) throws IllegalStateException {
		Session session = openSession();

		try {

			// begin transaction
			session.beginTransaction();

			// System.out.println("Transaction started");

			Criteria cr = session.createCriteria(SavedUser.class);
			cr.add(Restrictions.eq("alias", alias));
			List<SavedUser> results = cr.list();

			// commit
			session.getTransaction().commit();

			// only one element in the list because the id is unique
			for (SavedUser user : results) {
				try {
					// System.out.println("PW check started");
					if (PasswordHash.check(password, user.getPassword()))
						// System.out.println("Return user");
						return user;
				} catch (Exception e) {
					// System.out.println("PW check failed");
					throw new IllegalStateException("Fail by checking the user password");
				}
			}
			// System.out.println("no users found");
		} catch (HibernateException e) {
			// Exception -> rollback
			// System.out.println("Error!!");
			session.getTransaction().rollback();
			throw new IllegalStateException("something went wrong by getting the user");
		} finally {
			// close session
			session.close();
		}
		// no appropriate user found in database
		return null;
	}

}
