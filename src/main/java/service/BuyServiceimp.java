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
	@Override
	public List<Cart> myCart(String logid, String[] chklist) {
		
		return Dao.myCart(logid, chklist);
	}
	
	/*
	 * public Cart buylist(String id) {
	 * 
	 * return Dao.buylist(id);
	 * 
	 * }
	 */
	@Override
	public List<Orders> orderlist(String logid, String o_seqno) {
			return Dao.orderlist(logid,o_seqno);
	}
	@Override
	public int orderand(HttpServletRequest req, HttpServletResponse resp) {
		
		return Dao.orderand(req,resp);
	}
}
