package at.ac.uibk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.http.NameValuePair;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RestApi {

	@Test
	public void getCategories() {

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/categories", null, "GET");

		// check HTTP status code --> OK
		Assert.assertEquals(code, HttpURLConnection.HTTP_OK);

	}

	@Test
	public void getItems() {

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/items", null, "GET");

		// check HTTP status code --> OK
		Assert.assertEquals(code, HttpURLConnection.HTTP_OK);

	}

	@Test
	public void login() {

		JSONObject cred   = new JSONObject();	
		try {
			cred.put("name","Pati2");
			cred.put("hash", "d033e22ae348aeb5660fc2140aec35850c4da997");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int code = callURL("http://webinfo-myshop.herokuapp.com/api/users/login", cred, "POST");
		
		// check HTTP status code --> OK
		Assert.assertEquals(code, HttpURLConnection.HTTP_OK);

	}

	private int callURL(String urlString, JSONObject json, String method) {

		URL url;

		int code = 0;
		try {
			url = new URL(urlString);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			
			if(json != null && json.length() > 0){
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects( false );
				connection.setRequestProperty("Content-Type", "application/json");
				
				OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
				wr.write(json.toString());
				wr.flush();	
			}
			
			connection.connect();

			code = connection.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return code;
	}
	
}
