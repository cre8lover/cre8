package service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BuyDao;
import dto.Cart;
import dto.Orders;

public class BuyServiceimp implements BuyService{
	BuyDao Dao = new BuyDao();
	
	public List<Cart> myCart(String logid, String[] chklist) {
		
		return Dao.myCart(logid, chklist);
	}
	
	public Cart buylist(String id) {

		return Dao.buylist(id);
				
	}

	public List<Orders> orderlist(String logid) {
			return Dao.orderlist(logid);
	}

	public void orderand(HttpServletRequest req, HttpServletResponse resp) {
		
		Dao.orderand(req,resp);
	}
}
