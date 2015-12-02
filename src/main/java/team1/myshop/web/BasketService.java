package team1.myshop.web;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import team1.myshop.contracts.UserRights;
import team1.myshop.web.helper.JsonParser;
import team1.myshop.web.model.CartItem;
import team1.myshop.web.model.Category;
import team1.myshop.web.model.Item;
import team1.myshop.web.model.paypal.Amount;
import team1.myshop.web.model.paypal.Execution;
import team1.myshop.web.model.paypal.Link;
import team1.myshop.web.model.paypal.Payer;
import team1.myshop.web.model.paypal.Payment;
import team1.myshop.web.model.paypal.PaymentResponse;
import team1.myshop.web.model.paypal.RedirectUrls;
import team1.myshop.web.model.paypal.TokenResponse;
import team1.myshop.web.model.paypal.Transaction;

@Path("/orders")
public class BasketService extends ServiceBase {

	//private String token = "A101.tN_GXxT-B4MxI4KOMQ6-6gOJ8QyAN178kjQWg3og5YZgaVLLTBZNQbdzFTmr8cfy.RKmoB0qInoxFOmxWPY-4jkZnD6q";
	private static String client_id = "ASew5p32yz4tAteBGZmjju_zwFEwx6sI0LSUnN_G5TkNRHPvWJJBfspcnzjUMMDASc5_I6S-1vx3M2pe";
	private static String secret = "EFmX2ZHHNpUYkXVlwE4FXD2zl1xWwoCuNAty0yfhcr_AM3FybMZy5PKCGomN0FwIK0GhG6iaIf924IPO";
	
    public BasketService() {
        super();
    }
	
	@Override
	public void initializeLogger() {
		this.setLogger(LogManager.getLogger(BasketService.class));
	}
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String postOrder(String orderString, @Context HttpServletRequest request,
                                   @Context HttpServletResponse response) {

        this.initialize();

        // check user rights
        if(!auth.userHasRight(request, UserRights.CAN_CREATE_ORDERS)){
            logger.info("User wants to create an order, but did not have the right to");
            http.cancelRequest(response, SC_UNAUTHORIZED);
            return "Unauthorized";
        }

        Gson gson = new Gson();
        
        //parse itemString to item Array
        CartItem[] items = gson.fromJson(orderString, new TypeToken<CartItem[]>() {
		}.getType());
        
        if (items == null || items.length < 1) {
            http.cancelRequest(response, SC_BAD_REQUEST);
            return "Bad request";
        }

        //calculate amount
        double amount = calculateBasketAmount(items);
        
        //get access token
        String token = getAccessToken();
        
        //call payment
        PaymentResponse pr = callPaypalPayment(amount, token);
        
        for(Link l : pr.links){
        	if(l.rel.equals("approval_url") == true){
        		try {
					response.sendRedirect(l.href);
				} catch (IOException e) {
					e.printStackTrace();
					try {
						response.sendError(SC_INTERNAL_SERVER_ERROR);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return "Redirect failed";
				}
        		//response.setHeader("Location", l.href);
        		//response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        		return "Success";
        	}
        }
        
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "Error";
        
    }
    
    //calculate basket amount
    private double calculateBasketAmount(CartItem[] items) {
		
    	double amount = 0;
    	
    	for(CartItem it : items){
    		data.model.Item item = dh.getItemByID(it.itemId);
    		amount += (item.getPrice() * it.amount);
    	}
    	
		return amount;
	}

	@GET
    @Path("/execute")
    public void executeOrder(@Context HttpServletRequest request,
            @Context HttpServletResponse response) {
    	
        //get access token
        String token = getAccessToken();
        
        //get request parameters
        String payment = request.getParameter("paymentId");
        String payer   = request.getParameter("PayerID");
        
        //execute payment
        int code = callPaypalExecute(token, payer, payment);
    	
        //return status code
        response.setStatus(code);
        
    }

    //call execution
    private int callPaypalExecute(String token, String payer, String payment) {
		
		try {
			URL url = new URL("https://api.sandbox.paypal.com/v1/payments/payment/" + payment + "/execute/");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			Execution exec = new Execution();
			exec.payer_id = payer;
			
			Gson gson = new Gson();

			// convert java object to JSON format,
			// and returned as JSON formatted string
			String json = gson.toJson(exec);
			
			if (json != null && json.length() > 0) {
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Authorization", "Bearer " + token);

				OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
				wr.write(json.toString());
				wr.flush();
			}

			connection.connect();

			//get response code
			return connection.getResponseCode();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//internal server error
		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		
	}

	//call payment
	private PaymentResponse callPaypalPayment(double a, String token) {
		
		Amount amount = new Amount();
		amount.total = Double.toString(a);
		amount.currency = "EUR";

		Transaction trans = new Transaction();
		trans.amount = amount;

		Payment payment = new Payment();
		payment.payer = new Payer();
		payment.redirect_urls = new RedirectUrls();
		payment.transactions.add(trans);

		Gson gson = new Gson();

		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(payment);

		URL url;

		PaymentResponse pr = null;
		
		int code = 0;
		try {
			url = new URL("https://api.sandbox.paypal.com/v1/payments/payment");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			if (json != null && json.length() > 0) {
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Authorization", "Bearer " + token);

				OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
				wr.write(json.toString());
				wr.flush();
			}

			connection.connect();

			code = connection.getResponseCode();
			
			//Fehler
			if(code >= 300){
				return null;
			}
			
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
					String returnValue = buffer.toString();

					pr = gson.fromJson(returnValue, new TypeToken<PaymentResponse>() {
					}.getType());
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return pr;
	}

	//get access token
	private String getAccessToken() {
		URL url;

		TokenResponse tr = null;

		int code = 0;
		try {
			url = new URL("https://api.sandbox.paypal.com/v1/oauth2/token");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestProperty("Accept", "application/json");
			
			String userpass = client_id + ":" + secret;
			
			String basic = new String(Base64.getEncoder().encode(userpass.getBytes()));
			
			connection.setRequestProperty("Authorization", "Basic " + basic);
			
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			wr.write("grant_type=client_credentials");
			wr.flush();
			wr.close();
			
			connection.connect();

			code = connection.getResponseCode();

			// Fehler
			if (code >= 300) {
				System.out.println("Return code: " + code);
				return null;
			}

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
					String returnValue = buffer.toString();

					Gson gson = new Gson();
					
					tr = gson.fromJson(returnValue, new TypeToken<TokenResponse>() {
					}.getType());

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(tr != null){
			return tr.access_token;
		}
		
		return null;
	}
	
}
