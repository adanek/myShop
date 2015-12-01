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
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import team1.myshop.contracts.UserRights;
import team1.myshop.web.helper.JsonParser;
import team1.myshop.web.model.Category;
import team1.myshop.web.model.Item;
import team1.myshop.web.model.paypal.Amount;
import team1.myshop.web.model.paypal.Link;
import team1.myshop.web.model.paypal.Payer;
import team1.myshop.web.model.paypal.Payment;
import team1.myshop.web.model.paypal.PaymentResponse;
import team1.myshop.web.model.paypal.RedirectUrls;
import team1.myshop.web.model.paypal.Transaction;

public class BasketService extends ServiceBase {

	private String token = "A101.QAeBLMAPAnnTaDCrk14qIBYHS_VPshCSLi5fDoP41rlxUNrOkGn3yjcovID7mAS4.ZRyhbMqGZmK4QZdW2PQZf0e6MGi";
	
    public BasketService() {
        super();
    }
	
	@Override
	public void initializeLogger() {
		this.setLogger(LogManager.getLogger(BasketService.class));
	}
	
    @POST
    @Path("/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    public void postOrder(String itemString, @Context HttpServletRequest request,
                                   @Context HttpServletResponse response) {

        this.initialize();

//        // check user rights
//        if(!auth.userHasRight(request, UserRights.CAN_CREATE_ORDERS)){
//            logger.info("User wants to create an order, but did not have the right to");
//            http.cancelRequest(response, SC_UNAUTHORIZED);
//            return;
//        }
//
//        Gson gson = new Gson();
//        
//        //parse itemString to item Array
//		Collection<Item> items = gson.fromJson(itemString, new TypeToken<ArrayList<Item>>() {
//		}.getType());
//        
//        if (items == null || items.size() < 1) {
//            http.cancelRequest(response, SC_BAD_REQUEST);
//            return;
//        }

        double amount = 1.12;
        
        PaymentResponse pr = callPaypalPayment(amount);
        
        for(Link l : pr.links){
        	if(l.rel.equals("approval_url") == true){
        		response.setHeader("Location", l.href);
        		response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        		return;
        	}
        }
        
    }

	private PaymentResponse callPaypalPayment(double a) {
		
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


}
