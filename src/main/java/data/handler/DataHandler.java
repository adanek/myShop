package data.handler;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
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

public class DataHandler {

	private SessionFactory sessionFactory;
	private ServiceRegistry serviceRegistry;
	private Connection connection;

	public DataHandler() throws IllegalStateException {

		try {
			// connect to db
			connection = connectToDatabase();

			// create session factory
			Configuration configuration = new Configuration();
			// productive DB
			configuration.configure();

			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (HibernateException e) {
			System.out.println("Hibernate problems");
			throw new IllegalStateException("Hibernate problems");
		} catch (URISyntaxException e) {
			System.out.println("wrong URI to database");
			throw new IllegalStateException("wrong URI to database");
		} catch (IllegalStateException e) {
			System.out.println("no connection to database");
			throw new IllegalStateException("no connection to database");
		} catch (Exception e) {
			System.out.println("some connection error");
		}
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

	public void closeDatabaseConnection() throws IllegalStateException {
		sessionFactory.close();
		try {
			connection.close();
		} catch (Exception e) {
			System.out.println("closing connection not possible");
			throw new IllegalStateException("closing connection not possible");
		}
	}

	/**
	 * save an object to the database, when it is an entity
	 * 
	 * @param the
	 *            object of an entity
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
	 * open a new Session
	 * 
	 * @return the session
	 */
	private Session openSession() {

		return sessionFactory.openSession();
	}

	public SavedUser createUser(String alias, String password) throws IllegalStateException {

		// create user instance
		SavedUser user = new SavedUser();
		user.setAlias(alias);
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

	public Category createCategory(String name) throws IllegalStateException {

		// create category instance
		Category category = new Category();
		category.setName(name);

		saveObjectToDb(category);
		return category;

	}

	public Item createItem(String title, String description, int category, int author)
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

		saveObjectToDb(item);
		return item;

	}

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

		saveObjectToDb(itemComment);
		return itemComment;
	}

	// get all categories
	public Collection<Category> getAllCategories() throws IllegalStateException {

		try {

			return (Collection<Category>) getTableData(Category.class);

		} catch (Exception e) {
			// Exception
			throw new IllegalStateException("something went wrong by getting the category list");
		}
	}

	// get all items
	public Collection<Item> getAllItems() throws IllegalStateException {

		try {

			return (Collection<Item>) getTableData(Item.class);

		} catch (Exception e) {
			// Exception
			throw new IllegalStateException("something went wrong by getting the item list");
		}
	}

	// get all item comments
	public Collection<ItemComment> getAllItemComments() throws IllegalStateException {
		
		try {

			return (Collection<ItemComment>) getTableData(ItemComment.class);

		} catch (Exception e) {
			// Exception
			throw new IllegalStateException("something went wrong by getting the item comment list");
		}
	}

	// get all users
	public Collection<SavedUser> getAllUsers() throws IllegalStateException {
		
		try {

			return (Collection<SavedUser>) getTableData(SavedUser.class);

		} catch (Exception e) {
			// Exception
			throw new IllegalStateException("something went wrong by getting the user list");
		}
	}

	//search for user by ID
	public SavedUser getUserByID(int id) throws IllegalArgumentException {
		return this.<SavedUser> searchForID(id, SavedUser.class);
	}
	
	//search for category by ID
	public Category getCategoryByID(int id) throws IllegalArgumentException {
		return this.<Category> searchForID(id, Category.class);
	}
	
	//search for item by ID
	public Item getItemByID(int id) throws IllegalArgumentException {
		return this.<Item> searchForID(id, Item.class);
	}
	
	//search for comment by ID
	public ItemComment getItemCommentByID(int id) throws IllegalArgumentException {
		return this.<ItemComment> searchForID(id, ItemComment.class);
	}
	
	//select all data from table
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
	
	//search for a single dataset by ID
	private <T> T searchForID(int id, Class<T> typeParameterClass)
			throws IllegalArgumentException {

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
			throw new IllegalArgumentException(
					"object with this ID is not in the database", e);
		} finally {
			// close session
			session.close();
		}
	}

}
