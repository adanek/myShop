package team1.myshop.web.model.paypal;

import java.util.ArrayList;
import java.util.Collection;

public class Payment {

	public String intent = "sale";
	public RedirectUrls redirect_urls;
	public Payer payer;
	public Collection<Transaction> transactions = new ArrayList<>();
	
}