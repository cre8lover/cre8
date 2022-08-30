package controller;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.Cart;
import dto.Orders;
import service.BuyServiceimp;

@WebServlet("/buyer/*")
public class Buycontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	BuyServiceimp buy = new BuyServiceimp();
	
    public Buycontroller() {
        super();
    }

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doAction(req, resp);

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doAction(req, resp);
	}

	private void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf8");
//		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cmd = uri.substring(uri.lastIndexOf("/")+1);
		String path = req.getContextPath();
		HttpSession sess = req.getSession();
		String logid = (String) sess.getAttribute("sess_id");
		
		
		if(cmd.equals("cart")) {
			
			
			String orderbutton = req.getParameter("orderbutton");
			
			String[] chklist =  req.getParameterValues("allponecheck");
			
			if (orderbutton != null) {
				
				
				
				List<Cart> prolist = buy.myCart(logid,chklist);
				
				
				
				if(prolist != null) {
					req.setAttribute("cartp", prolist);
				}
				req.setAttribute("total", req.getParameter("total"));
				goView(req, resp, "/buy/buylist.jsp");
				
				
			} else {
			
				
				List<Cart> prolist = buy.myCart(logid,chklist);
			
			
			if(prolist != null) {
				req.setAttribute("cartp", prolist);
			}
			
			goView(req, resp, "/buy/cart.jsp");
			}
			
		}  else if(cmd.equals("buy")) {
			List<Orders> prolist = buy.orderlist(logid);
			
			
			if(prolist != null) {
			req.setAttribute("cartp", prolist);
			}
			
			goView(req, resp, "/buy/buy.jsp");

		} else if(cmd.equals("order")) {
	        buy.orderand(req,resp);
		}
	
	}

	private void goView(HttpServletRequest req, HttpServletResponse resp, String viewPage) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(viewPage);
		rd.forward(req, resp);		
	}

}
