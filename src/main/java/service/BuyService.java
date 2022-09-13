package service;

import java.util.List;
import java.util.Map;

import dto.Cart;
import dto.Orders;

public interface BuyService {
	
	public List<Cart> myCart(String logid, String[] chklist);
	public Cart buylist(String cart_seqno);
	public List<Orders> orderlist(String logid);
}
