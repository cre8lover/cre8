package controller;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.Auc;
import dto.Creator;
import dto.Marketing;
import dto.Mem;
import dto.Pro;
import service.CreatorServiceImp;

@WebServlet("/cre/*")
public class Creatercontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public Creatercontroller() {
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
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		String cmd = uri.substring(uri.lastIndexOf("/")+1);
		
		CreatorServiceImp cs = new CreatorServiceImp();
		
		if(cmd.equals("creReg")) {
			String add = (String)req.getSession().getAttribute("auth");
			String id = (String)req.getSession().getAttribute("sess_id");
//			System.out.println(id);
			
			if(id == null || add == null) {
				goView(req, resp, "/member/memreg.jsp");
			}
			else if(add.equals("C")) {
				
				List<Pro> prolist = cs.Prolist(id);
				List<Auc> auclist = cs.Auclist(id);
				
				String total = cs.totalmoney(id);
				
				req.setAttribute("prolist", prolist);
				req.setAttribute("auclist", auclist);
				req.setAttribute("total", total);
			
				goView(req, resp, "/creater/artistpage.jsp");
			}else {
				cs.CreatorName(id);
				goView(req, resp, "/creater/creReg.jsp");
			}
			
			
	/////여기서 시작!!//////////////////////
		}else if(cmd.equals("artistpage")) {
			//여기서 작가로 등급을 줌
			cs.Creatoradd(req);//다오에접근을 하기위한 수단이라 생각!
			goView(req, resp, "/cre/creReg");
				
		} else if(cmd.equals("cremodify")) { 
	         //못함
	         String id = (String)req.getSession().getAttribute("sess_id");
	         Creator cre = cs.infomodify(id);
	         
	         req.setAttribute("cre", cre);
	         goView(req, resp, "/creater/creReg2.jsp");
	         
	      } //광고 리스트보여주는 페이지 
			else if(cmd.equals("Adlist")) {
			List<Marketing> marketing = cs.mk(); 
			req.setAttribute("marketing", marketing);
			goView(req, resp, "/listimg/product_ad.jsp");
			
		}//광고 하나를 클릭했을때 나오는 페이지 
			else if(cmd.equals("marketingDetail")) {	
			int seqno = Integer.parseInt(req.getParameter("seqno"));
//				System.out.println(seqno);
			//List<Marketing> marketing2 = cs.mkk(seqno); 
			
			req.setAttribute("marketing", cs.mkk(seqno));
			
			goView(req, resp, "/creater/marketingDetail.jsp");
				
		}
			//아직 sql문 미완성!
		else if(cmd.equals("salesHistory")) {
			String id = (String)req.getSession().getAttribute("sess_id");
			List<Pro> salesHistory = cs.salesHistory(id);
			req.setAttribute("cre", salesHistory);
			
			goView(req, resp, "/creater/jmh_salesHistory.jsp");
			
			
		} else if(cmd.equals("auction_reg")) {
		
			String seqno = req.getParameter("seqno");
		
			if(seqno != null) {
		     Auc auc = cs.aucdetail(seqno);
		     req.setAttribute("auc", auc);
			}
		         
		 goView(req, resp, "/creater/auction_registration.jsp");
		 
		 //옥션 수정등록
		  } else if(cmd.equals("auction_modify")) {
			  
		 String seqno = req.getParameter("seqno");
		 if (seqno == null) { seqno = cs.aucadd(req);}
		 else { cs.aucmodify(req);}
		 
		 goView(req, resp, "/cre/auction_reg?seqno="+seqno);
		 
		 
		 //수정등록창 띄우는곳
		  } else if(cmd.equals("product_registration")) {
		    
		 String seqno = req.getParameter("seqno");
		 
		    if(seqno != null) {
		       Pro pro = cs.productdetail(seqno);
		       req.setAttribute("pro", pro);
		}
		
		goView(req,resp,"/creater/product_registration.jsp");
		
		//일반 수정 등록
		   } else if(cmd.equals("promodify")) {
			   
		  String seqno = req.getParameter("seqno");
		  if(seqno != null) {
		    seqno = cs.productmodify(req);
		  }else {
		   seqno = cs.productadd(req);
		  }
		  goView(req,resp,"/cre/product_registration?seqno="+seqno);
		   } 
		
		   else if(cmd.equals("cremodifyreg")) { 
		          //크리에이터 정보수정
		            Map<String, String> cremo = cs.cremodifyreg(req);
		            req.setAttribute("modi", cremo);
		           goView(req, resp, "/cre/cremodify");
		            
		        }
		  
		  
		
	}

	private void goView(HttpServletRequest req, HttpServletResponse resp, String viewPage) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(viewPage);
		rd.forward(req, resp);		
	}

}
