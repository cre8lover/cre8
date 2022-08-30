package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.xdevapi.Result;

import common.OracleConn;
import dto.Auc;
import dto.Cart;
import dto.Item;
import dto.Orders;
import dto.Pro;

public class BuyDao {
	
	Connection conn = OracleConn.getInstance().getConn();
	PreparedStatement stmt;
	
	public Cart buylist(String id) {
		Cart cart = new Cart();
		Pro pro = new Pro();
		Item item = new Item();
		try {
		String sql ="select i.item_name as item_name, i.item_img as item_img, pro_price, cart_amount,(pro_price * cart_amount) as totalprice"
				+ "                from item i,"
				+ "                (select p.item_seqno, p.pro_price, c.cart_amount"
				+ "                from cart c, pro p"
				+ "                where c.pro_seqno = p.pro_seqno and c.mem_id = ?) z"
				+ "                where i.item_seqno = z.item_seqno";
		
		
		stmt = conn.prepareStatement(sql);
		stmt.setString(1, id);
		ResultSet rs = stmt.executeQuery();
		
		while(rs.next()) {
			item.setItemName(rs.getString("item_name"));
			item.setItemImg(rs.getString("item_img"));
			pro.setProPrice(rs.getInt("pro_price"));
			cart.setCartAmount(rs.getInt("cart_amount"));
			cart.setTotalprice(rs.getInt("totalprice"));
			pro.setItem(item);
			cart.setPro(pro);
			
		}
		
		
		stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cart;
	}
	
	
	
	
	public List<Cart> myCart(String logid, String[] chklist) {
		
		List<Cart> cartlist = new ArrayList<Cart>();
		Cart cart = null;
		Pro pro = null;
		Item item = null;
		if(chklist == null) {
		try {
			String sql = "select i.item_img as item_img,i.item_name as item_name,a.pro_price as pro_price,a.cart_amount as cart_amount,"
					+ " (a.pro_price * a.cart_amount) as totalprice, a.pro_seqno as pro_seqno,a.cart_seqno as cart_seqno"
					+ " from item i,(select p.pro_seqno,c.mem_id,p.pro_price, p.item_seqno,c.cart_seqno as cart_seqno,c.cart_amount"
					+ " from cart c,pro p where c.pro_seqno = p.pro_seqno"
					+ " and c.mem_id = ?) a where i.item_seqno = a.item_seqno";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, logid);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				cart = new Cart();
				pro = new Pro();
				item = new Item();
				
				item.setItemImg(rs.getString("item_img"));
				item.setItemName(rs.getString("item_name"));
				pro.setProPrice(rs.getInt("pro_price"));
				pro.setProSeqno(rs.getInt("pro_seqno"));
				cart.setCartSeqno(rs.getInt("cart_seqno"));
				cart.setCartAmount(rs.getInt("cart_amount"));
				cart.setTotalprice(rs.getInt("totalprice"));
				pro.setItem(item);
				cart.setPro(pro);
				cartlist.add(cart);
			}
			
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cartlist;
		
		}else {
			
			
			String sql = " select (select item_name from item i where i.item_seqno = p.item_seqno) item_name, ";
				   sql+= " (select item_img from item i where i.item_seqno = p.item_seqno) item_img, ";
				   sql+= " c.cart_seqno as cart_seqno, c.cart_amount as cart_amount,p.pro_price as pro_price ";
				   sql+= " from (select * from cart";
				   sql+= " where mem_id = ?) c, ";
				   sql+= " (select * from pro p ";
				   sql+= " where 1=1";
				   sql+= " and pro_seqno = ? ";
					for(int i=1; i<chklist.length; i++) {
				   sql+= " or pro_seqno = ? ";
					}
				   sql+= " ) p ";
				   sql+= " where c.pro_seqno = p.pro_seqno ";
			try {
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, logid);
				stmt.setString(2, chklist[0]);
				for(int i=1; i<chklist.length; i++) {
				stmt.setString(i+2, chklist[i]);
				}
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next()) {
					cart = new Cart();
					pro = new Pro();
					item = new Item();
					
					item.setItemName(rs.getString("item_name"));
					item.setItemImg(rs.getString("item_img"));
					pro.setProPrice(rs.getInt("pro_price"));
					cart.setCartSeqno(rs.getInt("cart_seqno"));
					cart.setCartAmount(rs.getInt("cart_amount"));
					
					pro.setItem(item);
					cart.setPro(pro);
					cartlist.add(cart);
					
				}
				
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			
			
			
			return cartlist;
		}
	}
	
	
	
	
	
	public List<Orders> orderlist(String logid) {
		
		
		List<Orders> orderlistp = new ArrayList<Orders>();
		Orders orders = null;
		Pro pro = null;
		Item item = null;
		
		String ordernum = "";
		
		
		try {
			
			String sql = "select max(order_seqno) from orders where mem_id =?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, logid);
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			ordernum = rs.getString(1);
			
			
			
			sql = "select (select item_name from item i where i.item_seqno = p.item_seqno) as item_name,"
					+ " (select item_img from item i where i.item_seqno = p.item_seqno) as item_img,"
					+ " p.pro_price as pro_price, m.amount as amount,(p.pro_price * amount) as price"
					+ " from pro p,"
					+ " ("
					+ " select pro_seqno, amount"
					+ " from mini_order"
					+ " where order_seqno = ?"
					+ " )m"
					+ " where p.pro_seqno = m.pro_seqno";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, ordernum);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				item = new Item();
				pro = new Pro();
				orders = new Orders();
				
				item.setItemName(rs.getString("item_name"));
				item.setItemImg(rs.getString("item_img"));
				pro.setProPrice(rs.getInt("pro_price"));
				pro.setProAmount(rs.getInt("amount"));
				orders.setOrderAmount(rs.getInt("amount"));
				
				pro.setItem(item);
				orders.setPro(pro);
				orderlistp.add(orders);
				
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return orderlistp;
	}

	
	
	
	public void orderand(HttpServletRequest req, HttpServletResponse resp) {

		String pay_method = req.getParameter("pay_method");
		String buyer_name = req.getParameter("buyer_name");
		String buyer_tel = req.getParameter("buyer_tel");
		String buyer_addr = req.getParameter("buyer_addr");
        
		String merchant_uid = req.getParameter("merchant_uid");
        String totalprice = req.getParameter("amount");
        String id = (String)req.getSession().getAttribute("sess_id");
        String[] a = req.getParameterValues("cart");
        String[] orderamount = req.getParameterValues("orderamount");
//		String itemname = req.getParameter("name");
//      System.out.println(req.getParameter("buyer_email"));
//      System.out.println(req.getParameter("buyer_postcode"));
       
        
        String maxnum = null;
        
        List<String> pro_num = new ArrayList<String>();
        if (a != null) {
        String sql = "select * from cart where cart_seqno = ?";
        for (int i =1; i<a.length; i++) {
        	sql+= " or cart_seqno = ?";
		}
        		
        try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1,a[0]);
			for (int i =1; i<a.length; i++) {
				stmt.setString(i+1, a[i]);
			}
			
			ResultSet rs = stmt.executeQuery();
        
			while (rs.next()) {
				pro_num.add(rs.getString("pro_seqno")); 
			}
        
        
			sql = "insert into orders(order_seqno,order_totalprice,mem_id,order_paynum)"
					+ "values (order_seqno.nextval,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1,totalprice);
			stmt.setString(2,id);
			stmt.setString(3,merchant_uid);
			
			stmt.executeQuery();
       
			
			
			
			sql = "select max(order_seqno) from orders";
        
			stmt = conn.prepareStatement(sql);
        
			rs = stmt.executeQuery();
			rs.next();
			
			
			maxnum = rs.getString(1);
			
			
			for(int i=0; i<pro_num.size();i++) {
			sql = "insert into mini_order(mini_seqno,pro_seqno,order_seqno,amount)"
					+ "values(mini_seqno.nextval,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, pro_num.get(i));
			stmt.setString(2, maxnum);
			stmt.setString(3, orderamount[i]);
			
			stmt.executeQuery();
        
			}
			
			
			for(int i=0; i<pro_num.size();i++) {
			sql = "update pro set pro_amount = pro_amount - "+pro_num.get(i)+" where pro_seqno = "+ orderamount[i];
			stmt = conn.prepareStatement(sql);
			stmt.executeQuery();
			}
			
			sql = "insert into orderdetail(orderdetail_seqno, orderdetail_way,orderdetail_buyer,"
					+ " orderdetail_phone,order_seqno) values (orderdetail_seqno.nextval,?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pay_method);
			stmt.setString(2, buyer_name);
			stmt.setString(3, buyer_tel);
			stmt.setString(4, maxnum);
			stmt.executeQuery();
			
			
			
			
			
			sql = "delete from cart where cart_seqno in (?";
				
			for(int i=1; i<a.length; i++) {
			sql += ",?";
			}
				sql+= ")";
			stmt=conn.prepareStatement(sql);
			
			stmt.setString(1, a[0]);
			for(int i=1; i<a.length; i++) {
			stmt.setString(i+1, a[i]);
			}
			stmt.executeQuery();
			
        
        stmt.close();
        } catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
        }
		
	}

}
