package data.programs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import team1.myshop.web.model.Category;
import team1.myshop.web.model.paypal.Amount;
import team1.myshop.web.model.paypal.Link;
import team1.myshop.web.model.paypal.Payer;
import team1.myshop.web.model.paypal.Payment;
import team1.myshop.web.model.paypal.PaymentResponse;
import team1.myshop.web.model.paypal.RedirectUrls;
import team1.myshop.web.model.paypal.Transaction;

public class PaypalTest {

	private static String token = "A101.QAeBLMAPAnnTaDCrk14qIBYHS_VPshCSLi5fDoP41rlxUNrOkGn3yjcovID7mAS4.ZRyhbMqGZmK4QZdW2PQZf0e6MGi";

	public static void main(String[] args) {

		Amount amount = new Amount();
		amount.total = "1.02";
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
			
			System.out.println("Return code: " + code);
			
			//Fehler
			if(code >= 300){
				return;
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

					PaymentResponse pr = gson.fromJson(returnValue, new TypeToken<PaymentResponse>() {
					}.getType());
					
					for(Link l : pr.links){
						System.out.println(l.href + "; " + l.rel + "; " + l.method);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

}
