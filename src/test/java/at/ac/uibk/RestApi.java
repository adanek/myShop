package at.ac.uibk;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.BeforeClass;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import data.handler.DataHandler;
import data.model.SavedUser;
import team1.myshop.web.model.Item;
import team1.myshop.contracts.IDataHandler;
import team1.myshop.web.model.Category;

public class RestApi {

	private String returnValue = null;
	private List<Category> categories;
	private List<Item> items;
	private IDataHandler dh;

	@Test
	public void getCategories() {

		Gson gson = new Gson();

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/categories", null, "GET");

		if (this.returnValue != null) {
			this.categories = gson.fromJson(this.returnValue, new TypeToken<ArrayList<Category>>() {
			}.getType());
		}

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(code, HttpURLConnection.HTTP_OK);

	}

	@Test
	public void getRoles() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users/roles", null, "GET");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);

	}

	@Test
	public void getUsers() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users", null, "GET");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);

	}

	@Test
	public void getItems() {

		Gson gson = new Gson();

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/items", null, "GET");

		if (this.returnValue != null) {
			this.items = gson.fromJson(this.returnValue, new TypeToken<ArrayList<Item>>() {
			}.getType());
		}

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);

	}

	@Test
	public void createCategory() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		JSONObject json = new JSONObject();
		try {
			json.put("name", "Kategory3");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/categories", json, "POST");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);

	}

	@Test
	public void createUser() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		JSONObject json = new JSONObject();
		try {
			json.put("name", "TestUser");
			json.put("hash", "d033e22ae348aeb5660fc2140aec35850c4da997");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users/register", json, "POST");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);

	}

	@Test
	public void createItem() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		int catID;

		if (this.categories == null || this.categories.size() == 0) {
			getCategories();
		}

		if (this.categories == null || this.categories.size() == 0) {

			System.out.println("Dummy cat used");

			// dummy data
			catID = 1;
		} else {
			// get first category
			catID = this.categories.get(0).id;
		}

		JSONObject json = new JSONObject();
		try {
			json.put("author", "Pati2");
			json.put("authorID", 9);
			json.put("category", "IT");
			json.put("categoryID", catID);
			json.put("description", "Test description");
			json.put("title", "Taschentuch");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/items", json, "POST");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);

	}

	@Test
	public void createComment() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		int itemID;

		if (this.items == null || this.items.size() == 0) {
			getItems();
		}

		if (this.items == null || this.items.size() == 0) {

			System.out.println("Dummy item used");

			// dummy data
			itemID = 1;
		} else {
			// get first category
			itemID = this.items.get(0).id;
		}

		JSONObject json = new JSONObject();
		try {
			json.put("author", "Pati2");
			json.put("authorID", 9);
			json.put("content", "Content");
			json.put("changeDate", new Date().getTime());
			json.put("creationDate", new Date().getTime());
			json.put("itemID", itemID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/comments/new", json, "POST");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);
	}

	@Test
	public void deleteUser() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		IDataHandler dh = new DataHandler(true);

		SavedUser user = dh.getUserByName("TestUser");

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users/" + user.getId(), null, "DELETE");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, code);
	}

	@Test
	public void changeToAdmin() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		//create local datahandler
		if (this.dh == null) {
			this.dh = new DataHandler(true);
		}
		
		SavedUser user = dh.getUserByName("TestUser");

		JSONObject json = new JSONObject();
		try {
			json.put("id", user.getId());
			json.put("alias", "TestUser");
			json.put("userid", user.getId());
			json.put("role", "admin");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users/" + user.getId(), json, "PUT");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);
	}

	@Test
	public void changeOwnUser() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		//create local datahandler
		if (this.dh == null) {
			this.dh = new DataHandler(true);
		}
		
		SavedUser user = dh.getUserByName("Pati2");

		JSONObject json = new JSONObject();
		try {
			json.put("id", user.getId());
			json.put("alias", user.getAlias());
			json.put("userid", user.getId());
			json.put("role", "guest");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users/" + user.getId(), json, "PUT");

		// check HTTP status code --> FORBIDDEN
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_FORBIDDEN, code);
	}
	
	@Test
	public void login() {

		JSONObject cred = new JSONObject();
		try {
			cred.put("name", "Pati2");
			cred.put("hash", "d033e22ae348aeb5660fc2140aec35850c4da997");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users/login", cred, "POST");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);
	}

	private int callURL(String urlString, JSONObject json, String method) {

		this.returnValue = null;

		URL url;

		int code = 0;
		try {
			url = new URL(urlString);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);

			if (json != null && json.length() > 0) {
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestProperty("Content-Type", "application/json");

				OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
				wr.write(json.toString());
				wr.flush();
			}

			connection.connect();

			code = connection.getResponseCode();

			try {
				// Object test = connection.getContent();
				InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
				BufferedReader buff = new BufferedReader(in);
				String line;
				StringBuffer buffer = new StringBuffer();
				do {
					line = buff.readLine();
					if (line != null) {
						buffer.append(line);
					}
				} while (line != null);

				if (buffer != null) {
					this.returnValue = buffer.toString();
				}
			} catch (IOException e) {
				this.returnValue = null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return code;
	}

	@Test
	public void changeToGuest() {

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		// first we have to login
		login();

		//create local datahandler
		if (this.dh == null) {
			this.dh = new DataHandler(true);
		}

		SavedUser user = dh.getUserByName("TestUser");

		JSONObject json = new JSONObject();
		try {
			json.put("id", user.getId());
			json.put("alias", "TestUser");
			json.put("userid", user.getId());
			json.put("role", "guest");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users/" + user.getId(), json, "PUT");

		// check HTTP status code --> OK
		AssertJUnit.assertEquals(HttpURLConnection.HTTP_OK, code);
	}

}
