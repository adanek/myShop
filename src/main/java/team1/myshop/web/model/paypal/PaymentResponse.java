package team1.myshop.web.model.paypal;

import java.util.ArrayList;
import java.util.Collection;

public class PaymentResponse {

	public String id;
	public String create_time;
	public String update_time;
	public String state;
	public String intent;
	public Payer payer;
	public Collection<Transaction> transactions = new ArrayList<>();
	public Collection<Link> links = new ArrayList<>();
	
}
