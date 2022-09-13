package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import common.OracleConn;
import dto.Att;
import dto.Auc;
import dto.Creator;
import dto.Item;
import dto.Marketing;
import dto.Mem;
import dto.Pro;
import dto.Thumbnail;
import oracle.jdbc.OracleTypes;
import oracle.sql.ANYDATA;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

public class CreatorDao {
	
	Connection conn = OracleConn.getInstance().getConn();
	PreparedStatement stmt;
	CallableStatement cstmt;
	
	FileDao filedao = new FileDao();
	
	public void Creatoradd(HttpServletRequest request){
		try {
			String sql = "call p_creatoradd(?,?,?,?,?,?,?,?)";
		     	   //프로시저시험해봅니
		     	  
		    String str = (request.getParameter("creadress") + request.getParameter("creadress2"));

		    stmt = conn.prepareStatement(sql);
		    stmt.setString(1, request.getParameter("crecompany"));
			stmt.setString(2, request.getParameter("cretel"));
			stmt.setString(3, request.getParameter("crename"));
			stmt.setString(4, str);
			stmt.setString(5, request.getParameter("crenum"));
			stmt.setString(6, request.getParameter("crenum2"));
			stmt.setString(7, request.getParameter("intro"));
			stmt.setString(8, request.getParameter("mem_id"));
			stmt.executeQuery();
			
			 sql = "update mem_auth set auth_name ='C' where auth_name='M' and mem_id = ?";
			 stmt = conn.prepareStatement(sql);
			 stmt.setString(1, (String)request.getSession().getAttribute("sess_id"));
			 stmt.executeQuery();
			
			 //등급 c를 줘야됨 아이디필요 (id를 받아옴)
			//id 값을 가져와서 그아이디회원의 등급을 c로 올려줘야하는sql = update
			
			stmt.close();
		 }catch (SQLException e) {
				e.printStackTrace();
			}		
		}
	
	
	public Mem CreatorName(String id) {
		Mem mem = new Mem();
		try{
		String sql = "select mem_pw, mem_tel, mem_email from mem";
		
		stmt = conn.prepareStatement(sql);
		stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return mem;
		}
		
	////광고 리스트보여주는 페이지 
	public List<Marketing> mk(){ 
		List<Marketing> mk = new ArrayList<Marketing>();
		
			String sql = "select mar_seqno, mar_product, mar_img, mar_price from Marketing";
		try{
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
		while(rs.next()) {
			Marketing m = new Marketing();
			m.setMarSeqno(rs.getInt("mar_seqno"));
			m.setMarProduct(rs.getString("mar_product"));
			m.setMarImg(rs.getString("mar_img"));
			m.setMarPrice(rs.getString("mar_price"));
			
			mk.add(m);
			
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mk;
	}
	
	
	//광고 하나를 클릭했을때 나오는 페이지 
	public Marketing mkk(int seqno) {
		Marketing mkk = new Marketing();
			String sql = "select * from Marketing where mar_seqno=?";
		try{
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, seqno);
			ResultSet rs = stmt.executeQuery();
			
		while(rs.next()) {
			mkk.setMarSeqno(seqno);
			mkk.setMarProduct(rs.getString("mar_product"));
			mkk.setMarImg(rs.getString("mar_img"));
			mkk.setMarPrice(rs.getString("mar_price"));
			mkk.setMarCompany(rs.getString("mar_company"));
			mkk.setMarDetail(rs.getString("mar_detail"));
		}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mkk;
	}
	
	
	
	
	
	
	
	//creater/artistpage.jsp
	public List<Creator> Creatorpage(){
		Creator c = new Creator();
		List<Creator>cre = new ArrayList<Creator>();
		
		try {
			String sql = "select sum(totalPrice), saleman";
				   sql += " from";
				   sql += " (";
				   sql += " select sum(order_totalprice) totalPrice ,mem_id saleman";
				   sql += " from ";
				   sql += " (";
				   sql += "     select order_totalprice, p.mem_id";
				   sql += "     from pro p,";
				   sql += "     (";
				   sql += "         select (select o.pro_seqno from orderdetail od where od.order_seqno = o.order_seqno and od.orderdetail_stat = 'END') pro_seqno, ";
				   sql += "         (select o.order_totalprice from orderdetail od where od.order_seqno = o.order_seqno and od.orderdetail_stat = 'END') order_totalprice,"; 
				   sql += "         (select pro_seqno from cart c,(select cart_seqno from orderdetail od where od.order_seqno = o.order_seqno and od.orderdetail_stat = 'END') o where c.cart_seqno = o.cart_seqno) cpro_seqno";
				   sql += "         from orders o";
				   sql += "  ) z";
				   sql += "     where p.pro_seqno = z.pro_seqno";
				   sql += "     or p.pro_seqno = z.cpro_seqno";
				   sql += " )";
				   sql += " group by mem_id";
				   sql += " 	union";
				   sql += " select sum(auc_closeprice) totalPrice ,saleman";
				   sql += " from ";
			       sql += " (";
			       sql += "     select auc_closeprice, (select mem_id from item i where i.item_seqno = p.item_seqno) saleman";
			       sql += "     from auc p,";
			       sql += "    (";
	               sql += "         select (select o.order_totalprice from orderdetail od where od.order_seqno = o.order_seqno and od.orderdetail_stat = 'END') order_totalprice,"; 
				   sql += "        (select auc_seqno from cart c,(select cart_seqno from orderdetail od where od.order_seqno = o.order_seqno and od.orderdetail_stat = 'END') o where c.cart_seqno = o.cart_seqno) auc_seqno";
			       sql += "      from orders o";
			  	   sql += "   ) z";
		           sql += "  where p.auc_seqno = z.auc_seqno";
			       sql += " )";
			       sql += " group by saleman";
				   sql += " )";
			       sql += " where saleman = '?'";
				   sql += " group by saleman";
		
				   stmt = conn.prepareStatement(sql);
				   stmt.setString(1, c.getCreName());
				   ResultSet rs = stmt.executeQuery();
					
		 }catch (SQLException e) {
				e.printStackTrace();
			}		
			return cre;
		}
		
	
	public Map<String, String> cremodifyreg(HttpServletRequest req) {
	      
	      Map<String, String> cremodi = new HashMap<>();
	      
	      String MEM_ID = req.getParameter("id");
	      String MEM_PW = req.getParameter("pw");
	      String MEM_TEL = req.getParameter("mobile");
	      String MEM_EMAIL = req.getParameter("emailfirst")+"@"+req.getParameter("selDomain");
	      String MEM_SNSINFO = req.getParameter("sns");
	      String CRE_COMPANY = req.getParameter("cre_company");
	      String CRE_PHONE = req.getParameter("cre_phone");
	      String CRE_NAME = req.getParameter("cre_name");
	      String CRE_ADDRESS = req.getParameter("address") + req.getParameter("address_detail");
	      String CRE_REGNUM = req.getParameter("cre_regnum");
	      String CRE_SALENUM = req.getParameter("cre_salenum");
	      String CRE_POT = req.getParameter("intro");
	      

	      String sql = "select mem_id,mem_pw from mem where mem_id = ?";
	      
	      try {
	         stmt = conn.prepareStatement(sql);
	         stmt.setString(1, MEM_ID);
	         ResultSet rs = stmt.executeQuery();
	         
	         if(rs.next()) {
	            if(MEM_PW.equals(rs.getString("mem_pw"))){
	               
	               sql = "update mem set mem_tel = ?, mem_email = ?, "
	                     + " MEM_SNSINFO = ? where mem_id = ?";
	               
	               stmt = conn.prepareStatement(sql);
	               stmt.setString(1, MEM_TEL);
	               stmt.setString(2, MEM_EMAIL);
	               stmt.setString(3, MEM_SNSINFO);
	               stmt.setString(4, MEM_ID);
	               stmt.executeQuery();
	               
	               
	               sql = "update creator set CRE_COMPANY =?,"
	                     + " CRE_PHONE = ?,"
	                     + " CRE_NAME = ?,"
	                     + " CRE_ADDRESS = ?,"
	                     + " CRE_REGNUM = ?,"
	                     + " CRE_SALENUM = ?,"
	                     + " CRE_POT = ? "
	                     + " where mem_id = ?";
	               
	               stmt = conn.prepareStatement(sql);
	               stmt.setString(1, CRE_COMPANY);
	               stmt.setString(2, CRE_PHONE);
	               stmt.setString(3, CRE_NAME);
	               stmt.setString(4, CRE_ADDRESS);
	               stmt.setString(5, CRE_REGNUM);
	               stmt.setString(6, CRE_SALENUM);
	               stmt.setString(7, CRE_POT);
	               stmt.setString(8, MEM_ID);
	               stmt.executeQuery();
	               
	               cremodi.put("msg", "ok");
	            }else {
	               cremodi.put("msg", "pwcheck");
	            }
	            
	         } 
	         stmt.close();
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	      return cremodi;
	   }

	
	
	
	
	////////////////////////sql문 수정
	public List<Pro> salesHistory(String id){
		List<Pro>cre = new ArrayList<Pro>();
		Item item = null;
		Pro pro = null;
		
	try {
		String sql = "select distinct rownum,total,orderdetail_stat, item_name,item_img,pro_opendate,pro_stat "
				+ " from"
				+ " ( "
				+ " select (select item_name from item i where i.item_seqno = p.item_seqno) as item_name, "
				+ " (select item_img from item i where i.item_seqno = p.item_seqno) as item_img, "
				+ " (select mem_id from item i where i.item_seqno = p.item_seqno) as mem_id, "
				+ " pro_opendate,orderdetail_stat,(p.pro_price * amount) as total, pro_stat "
				+ " from pro p , "
				+ "    ( "
				+ "    select pro_seqno, mo.amount, orderdetail_stat "
				+ "    from  "
				+ "        ( "
				+ "        select od.orderdetail_stat,o.order_seqno "
				+ "        from orderdetail od , orders o "
				+ "        where od.order_seqno = o.order_seqno "
				+ "        ) o, "
				+ "        ( "
				+ "        select pro_seqno,order_seqno,amount "
				+ "        from mini_order "
				+ "        ) mo "
				+ "    where o.order_seqno = mo.order_seqno "
				+ "    ) o "
				+ " where p.pro_seqno = o.pro_seqno order by pro_opendate desc "
				+ " ) "
				+ " where mem_id = ? ";
 	
			   stmt = conn.prepareStatement(sql);
			   stmt.setString(1, id);
			   ResultSet rs = stmt.executeQuery();
	
			   while(rs.next()) {
				   pro = new Pro();
				   item = new Item();
				   item.setItemName(rs.getString("item_name"));
				   item.setItemImg(rs.getString("item_img"));
				   pro.setProOpendate(rs.getString("pro_opendate"));
				   pro.setProStat(rs.getString("pro_stat"));
				   pro.setProAmount(rs.getInt("total"));
				   pro.setProDetail(rs.getString("orderdetail_stat"));
				   item.setItemAmount(rs.getInt("rownum"));
				   pro.setItem(item);
				   cre.add(pro);
			   }
			   
			   
			   
	}catch (SQLException e) {
		e.printStackTrace();
	}
	
	return cre;
	
	}

	
	//일반물품이랑 경매물품 작가페이지에서 띄우기
	public List<Pro> Prolist(String id) {
		List<Pro> prolist = new ArrayList<>();
		try {
			String sql= "select i.item_seqno,i.item_img,i.item_detail,p.pro_amount,p.pro_saleprice, p.pro_seqno"
					+ " from (select * from pro where pro_stat not in 'END') p, item i"
					+ " where p.item_seqno = i.item_seqno and i.mem_id =?";
			
		stmt = conn.prepareStatement(sql);
		stmt.setString(1, id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			Pro p = new Pro();
			Item i = new Item();
			int itemseqno = rs.getInt("item_seqno");
			i.setItemImg(rs.getString("item_img"));
			i.setItemDetail(rs.getString("item_detail"));
			p.setProAmount(rs.getInt("pro_amount"));
			p.setProSaleprice(rs.getInt("pro_saleprice"));
			p.setProSeqno(rs.getInt("pro_seqno"));
			
			
			
			sql = " select THUMB_FILENAME, THUMB_FILEPATH "
					+ " from att_thumb"
					+ " where att_seqno = (select att_seqno from att where item_seqno = ?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, itemseqno);
			ResultSet rs2 = stmt.executeQuery();
			if(rs2.next()) {
				
				i.setItemImg(rs2.getString("thumb_filename"));
				
			}
			p.setItem(i);
			prolist.add(p);
		}
		stmt.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return prolist;
		}

	

	public List<Auc> Auclist(String id) {
	      List<Auc> auclist = new ArrayList<>();
	      Auc a = null;
	      Item i = null;
	      try {
	         String sql = "select i.item_seqno as item_seqno, a.auc_img, a.auc_detail, a.auc_price, a.auc_closeprice, a.auc_stat, a.auc_seqno"
	               + " from (select * from auc where auc_stat not in 'END') a,item i"
	               + " where a.item_seqno = i.item_seqno and i.mem_id =?";
	      stmt = conn.prepareStatement(sql);
	      stmt.setString(1, id);
	      ResultSet rs = stmt.executeQuery();
	      
	      while(rs.next()) {
	         a = new Auc();
	         i = new Item();
	         String itemseqno = rs.getString("item_seqno");
	         a.setAucImg(rs.getString("auc_img"));
	         a.setAucDetail(rs.getString("auc_detail"));
	         a.setAucPrice(rs.getInt("auc_price"));
	         a.setAucCloseprice(rs.getInt("auc_closeprice"));
	         a.setAucStat(rs.getString("auc_stat"));
	         a.setAucSeqno(rs.getInt("auc_seqno"));
	         
	         
				
				
				sql = " select THUMB_FILENAME, THUMB_FILEPATH "
						+ " from att_thumb"
						+ " where att_seqno = (select att_seqno from att where item_seqno = ?)";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, itemseqno);
				ResultSet rs2 = stmt.executeQuery();
				if(rs2.next()) {
					
					i.setItemImg(rs2.getString("thumb_filename"));
					
					a.setItem(i);
				}
	         
	         
	         auclist.add(a);
	      }
	      
	      stmt.close();
	      }catch (SQLException e) {
	         e.printStackTrace();
	      }
	      
	      return auclist;
	   }
	
	public Creator infomodify(String id) {
	      
	      Mem mem = new Mem();
	      Creator cre = new Creator();
	      
	      String sql = "select c.cre_company as cre_company, c.cre_phone as cre_phone, c.cre_name as cre_name,"
	            + " c.cre_address as cre_address, c.cre_regnum as cre_regnum, c.cre_salenum as cre_salenum,"
	            + " c.cre_pot as cre_pot, m.mem_pw as mem_pw, m.mem_tel as mem_tel, m.mem_email as mem_email, m.mem_snsinfo as mem_snsinfo"
	            + " from creator c,"
	            + "    ("
	            + "    select mem_pw,mem_tel,mem_email,mem_snsinfo,mem_id"
	            + "    from mem"
	            + "    where mem_id = ?"
	            + "    ) m"
	            + " where c.mem_id = m.mem_id";
	      
	      try {
	         
	         stmt = conn.prepareStatement(sql);
	         stmt.setString(1, id);
	         ResultSet rs = stmt.executeQuery();
	         while (rs.next()) {
	            cre.setCreCompany(rs.getString("cre_company"));
	            cre.setCrePhone(rs.getString("cre_phone"));
	            cre.setCreName(rs.getString("cre_name"));
	            cre.setCreAddress(rs.getString("cre_address"));
	            cre.setCreRegnum(rs.getString("cre_regnum"));
	            cre.setCreSalenum(rs.getString("cre_salenum"));
	            cre.setCrePot((rs.getString("cre_pot").trim()));
	            mem.setMemPw(rs.getString("mem_pw"));
	            mem.setMemTel(rs.getString("mem_tel"));
	            
	            String email = rs.getString("mem_email");
	            if(email != null) {
	               String first = email.substring(0, email.indexOf("@"));
	               String second = email.substring(email.indexOf("@")+1);
	               email = first + second;
	               mem.setMemEmail(first);
	               mem.setMemId(second);
	            }else {
	               mem.setMemEmail(email);
	            }
	            mem.setMemSnsinfo(rs.getString("mem_snsinfo"));
	            cre.setMem(mem);
	            
	         }
	         stmt.close();
	      } catch (SQLException e) {
	         e.printStackTrace();
	      }
	      
	      return cre;
	   }


	public String totalmoney(String id) {
		
		String total = "";
		
		String sql = "select sum(total) as total"
				+ " from"
				+ " ("
				+ " select sum(o.amount * p.pro_price) total"
				+ " from pro p,"
				+ "    ("
				+ "    select pro_seqno, mo.amount"
				+ "    from orders o,"
				+ "        ("
				+ "        select pro_seqno,order_seqno,amount"
				+ "        from mini_order"
				+ "        ) mo"
				+ "    where o.order_seqno = mo.order_seqno"
				+ "    and o.order_seqno = (select od.order_seqno from orderdetail od where o.order_seqno = od.order_seqno and od.orderdetail_stat = 'END')"
				+ "    ) o"
				+ " where p.pro_seqno = o.pro_seqno"
				+ " and mem_id = ?"
				+ " union all"
				+ " select sum(auc_closeprice) total"
				+ " from "
				+ "    ("
				+ "    select auc_closeprice,(select mem_id from item i where i.item_seqno = a.item_seqno) as mem_id"
				+ "    from auc a,"
				+ "        ("
				+ "        select auc_seqno"
				+ "        from orders o,"
				+ "            ("
				+ "            select auc_seqno,order_seqno"
				+ "            from mini_order"
				+ "            ) mo"
				+ "        where o.order_seqno = mo.order_seqno"
				+ "        and o.order_seqno = (select od.order_seqno from orderdetail od where o.order_seqno = od.order_seqno and od.orderdetail_stat = 'END')"
				+ "        ) o"
				+ "    where a.auc_seqno = o.auc_seqno"
				+ "    )"
				+ " where mem_id = ?"
				+ " )";
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, id);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				
				total = rs.getString(1);
			
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return total;
	}


	public Pro productdetail(String seqno) {
		Pro pro = new Pro();
		      
		String sql = " select (select item_name from item i where i.item_seqno = p.item_seqno) as item_name, "
		            + " (select item_img from item i where i.item_seqno = p.item_seqno) as item_img, "
		            + " (select item_detail from item i where i.item_seqno = p.item_seqno) as item_detail, "
		            + " (select item_seqno from item i where i.item_seqno = p.item_seqno) as item_seqno, "
		            + " cat_seqno, pro_stat, pro_price, pro_opendate, pro_closedate, pro_detail,pro_saleprice,pro_amount "
		            + " from pro p where pro_seqno = ? ";
		      
		
		
		
		try {
		         stmt =conn.prepareStatement(sql);
		         stmt.setString(1, seqno);
		         ResultSet rs = stmt.executeQuery();
		         
		         if(rs.next()) {
		            Item item = new Item();
		            item.setItemName(rs.getString("item_name"));
		            item.setItemImg(rs.getString("item_img"));
		            item.setItemDetail(rs.getString("item_detail"));
		            item.setItemSeqno(rs.getInt("item_seqno"));
		            pro.setProStat(rs.getString("pro_stat"));
		            pro.setProPrice(rs.getInt("pro_price"));
		            pro.setProOpendate(rs.getString("pro_opendate"));
		            pro.setProClosedate(rs.getString("pro_closedate"));
		            pro.setProDetail(rs.getString("pro_detail"));
		            pro.setProSaleprice(rs.getInt("pro_saleprice"));
		            pro.setProHits(rs.getInt("cat_seqno"));
		            pro.setProAmount(rs.getInt("pro_amount"));
		            pro.setProSeqno(Integer.parseInt(seqno));
		            
		            
		            sql = " select att_name,att_savename,thumb_filename, att_path, thumb_filepath"
		            		+ " from att a, att_thumb at"
		            		+ " where a.att_seqno = at.att_seqno"
		            		+ " and item_seqno = ? ";
		            
		            stmt =conn.prepareStatement(sql);
			         stmt.setInt(1, rs.getInt("item_seqno"));
			         ResultSet rs2 = stmt.executeQuery();
		            
			         if (rs2.next()) {
			        	 Att att = new Att ();
			        	 Thumbnail thum = new Thumbnail();
			        	 
			        	 att.setAttName(rs2.getString("att_name"));
			        	 att.setSavefilename(rs2.getString("att_savename"));
			        	 att.setAttPath(rs2.getString("att_path"));
			        	 thum.setFileName(rs2.getString("thumb_filename"));
			        	 thum.setFilePath(rs2.getString("thumb_filepath"));
			        	 pro.setAtt_file(att);
			        	 pro.setThumb(thum);
			         }
		            
		            
		            
		            
		            pro.setItem(item);
		            
		            
		         }
		         stmt.close();
		} catch (SQLException e) {
		         e.printStackTrace();
		      
		}

		      return pro;
		   
	}


	public String productmodify(HttpServletRequest req) {
		      
		
		String proStat = req.getParameter("proStat");
		if(proStat == null) {
			proStat = "WAIT";
		}
		String proPrice = req.getParameter("proPrice");
		String proHits = req.getParameter("proHits");
		String proSaleprice = req.getParameter("proSaleprice");
		String proOpendate = req.getParameter("proOpendate");
		String proClosedate = req.getParameter("proClosedate");
		String proDetail = req.getParameter("proDetail");
		String seqno = req.getParameter("seqno");
	    String proAmount = req.getParameter("amount");
      
		
		String itemDetail = req.getParameter("itemDetail");
		String itemName = req.getParameter("itemName");
		String itemseqno = req.getParameter("itemseqno");
		      
//      System.out.println(proStat +"/"+ proPrice +"/"+ proHits +"/"+ proSaleprice +"/"+ proOpendate +"/"+ proClosedate +"/"+ proDetail +"/"+ seqno +"/"+ itemDetail +"/"+  itemName +"/"+ itemseqno);
		      
		      
		String sql = "update item set ITEM_NAME = ? , "
		            + " ITEM_DETAIL = ? "
		            + " where item_seqno = ? ";
		      
		try {
		         stmt = conn.prepareStatement(sql);
		         stmt.setString(1, itemName);
		         stmt.setString(2, itemDetail);
		         stmt.setString(3, itemseqno);
		         stmt.executeQuery();
		         
		         sql = "update pro set pro_stat = ?, "
		               + " pro_price = ?, "
		               + " pro_hits = ?, "
		               + " pro_saleprice = ?, "
		               + " pro_opendate = ?, "
		               + " pro_closedate = ?, "
		               + " pro_detail = ?, "
		               + " pro_amount = ? "
		               + " where pro_seqno = ? ";
		         
		         stmt = conn.prepareStatement(sql);
		         
		         stmt.setString(1, proStat);
		         stmt.setString(2, proPrice);
		         stmt.setString(3, proHits);
		         stmt.setString(4, proSaleprice);
		         stmt.setString(5, proOpendate);
		         stmt.setString(6, proClosedate);
		         stmt.setString(7, proDetail);
		         stmt.setString(8, proAmount);
		         stmt.setString(9, seqno);
		         stmt.executeQuery();
		         
		         stmt.close();
		 } catch (SQLException e) {
		         // TODO Auto-generated catch block
		         e.printStackTrace();
		 }
		      
//		      System.out.println(seqno);
		 return seqno;
	}

	   public String aucadd(Auc auc, String id) {
		      
		      String seqno = "";
		      
		      String item_name = auc.getItem().getItemName();
		      String auc_stat = auc.getAucStat();
		      if(auc_stat == null) auc_stat = "WAIT";
		      int auc_price = auc.getAucPrice();
		      String auc_start = auc.getAucStart();
		      String auc_finish = auc.getAucFinish();
		      String auc_shortdetail = auc.getAucShortdetail();
		      String auc_detail = auc.getAucDetail();
		      
		      
		      
		      
		      String sql = "insert into item(item_seqno,item_name,mem_id) values (item_seqno.nextval,?,?)";
		         
		      try {
		         stmt = conn.prepareStatement(sql);
		         stmt.setString(1, item_name);
		         stmt.setString(2, id);
		         stmt.executeQuery();
		         
		         sql = "select max(item_seqno) from item";
		         stmt = conn.prepareStatement(sql);
		         ResultSet rs = stmt.executeQuery();
		         
		         rs.next();
		         String itemseqno = rs.getString(1);
		         
		         sql = "insert into auc(auc_seqno, auc_stat, auc_price, auc_start, auc_finish,"
		               + "auc_shortdetail, auc_detail,item_seqno) values (auc_seqno.nextval, ?,?,?,?,?,?,?)";
		         
		         
		         stmt = conn.prepareStatement(sql);
		         stmt.setString(1, auc_stat);
		         stmt.setInt(2, auc_price);
		         stmt.setString(3, auc_start);
		         stmt.setString(4, auc_finish);
		         stmt.setString(5, auc_shortdetail);
		         stmt.setString(6, auc_detail);
		         stmt.setString(7, itemseqno);
		         stmt.executeQuery();
		         
		         sql = "select max(auc_seqno) from auc";
		         stmt = conn.prepareStatement(sql);
		         rs = stmt.executeQuery();
		         rs.next();
		         
		         seqno = rs.getString(1);
		         
		         
		         
		     	if (auc.getAtt_file() != null) {
					
					String att_seqno = filedao.insertAttachFile(itemseqno, auc.getAtt_file());
					String fileType = auc.getAtt_file().getAttType();
					if (fileType.substring(0,fileType.indexOf("/")).equals("image")) {
					
						filedao.insertThumbNail(auc.getAtt_file(),att_seqno);
					
					}
				}
	         
		         
		         
		         
		         
		         
		         
		         
		         stmt.close();
		      } catch (SQLException e) {
		         e.printStackTrace();
		      }
		      return seqno;
		   }

		   
		   
		   

		   public Auc aucdetail(String seqno) {
		      Auc auc = new Auc();
		      Item item = new Item();
		      
		      String sql = " select (select item_name from item i where i.item_seqno = a.item_seqno) as item_name,"
		            + " (select item_img from item i where i.item_seqno = a.item_seqno) as item_img,"
		            + " (select item_seqno from item i where i.item_seqno = a.item_seqno) as item_seqno,"
		            + " to_char(auc_start,'YYYY-MM-DD') as auc_start,"
		            + " to_char(auc_finish,'YYYY-MM-DD') as auc_finish,"
		            + " auc_stat,auc_price,auc_shortdetail,auc_detail,auc_seqno"
		            + " from auc a"
		            + " where auc_seqno = ?";
		      
		      try {
		         stmt = conn.prepareStatement(sql);
		         stmt.setString(1, seqno);
		         ResultSet rs = stmt.executeQuery();
		         
		         if(rs.next()) {
		            
		            item.setItemName(rs.getString("item_name"));
		            item.setItemImg(rs.getString("item_img"));
		            item.setItemSeqno(rs.getInt("item_seqno"));
		            auc.setAucStat(rs.getString("auc_stat"));
		            auc.setAucPrice(rs.getInt("auc_price"));
		            auc.setAucStart(rs.getString("auc_start"));
		            auc.setAucFinish(rs.getString("auc_finish"));
		            auc.setAucShortdetail(rs.getString("auc_shortdetail"));
		            auc.setAucDetail(rs.getString("auc_detail"));
		            auc.setAucSeqno(rs.getInt("auc_seqno"));
		            auc.setItem(item);
		         }
		         
		         
		         stmt.close();
		      } catch (SQLException e) {
		         // TODO Auto-generated catch block
		         e.printStackTrace();
		      }
		      
		      
		      
		      
		      return auc;
		   }


		   public void aucmodify(HttpServletRequest req) {
		      
		      String seqno = req.getParameter("seqno");
		      String item_name = req.getParameter("item_name");
		      String item_seqno = req.getParameter("itemseqno");
		      
		      String auc_stat = req.getParameter("auc_stat");
		      if(auc_stat == null) auc_stat = "WAIT";
		      String auc_price = req.getParameter("auc_price");
		      String auc_start = req.getParameter("auc_start");
		      String auc_finish = req.getParameter("auc_finish");
		      String auc_shortdetail = req.getParameter("auc_shortdetail");
		      String auc_detail = req.getParameter("auc_detail");
		      
		      String sql = "update item set item_name = ?"
		            + " where item_seqno = ?";
		      
		         try {
		            stmt = conn.prepareStatement(sql);
		            stmt.setString(1, item_name);
		            stmt.setString(2, item_seqno);
		            
		            stmt.executeQuery();
		            
		            sql = "update auc set auc_stat = ?,"
		                  + " auc_price = ?,"
		                  + " auc_start = ?,"
		                  + " auc_finish = ?,"
		                  + " auc_shortdetail = ?,"
		                  + " auc_detail = ?"
		                  + " where auc_seqno = ?";
		            
		            stmt = conn.prepareStatement(sql);
		            stmt.setString(1, auc_stat);
		            stmt.setString(2, auc_price);
		            stmt.setString(3, auc_start);
		            stmt.setString(4, auc_finish);
		            stmt.setString(5, auc_shortdetail);
		            stmt.setString(6, auc_detail);
		            stmt.setString(7, seqno);
		            
		            stmt.executeQuery();
		            
		            
		            
		            stmt.close();
		            
		         } catch (SQLException e) {
		            e.printStackTrace();
		         }
		      
		      
		      
		      
		   }


		   public String productadd(Pro pro, String id) {
			   String seqno = "";
			      String sql = "call p_pro_add_test(?,?)";
			      try {
			    	  
			    	  if(pro.getProStat() == null) pro.setProStat("WAIT");
			    	  
			    	  StructDescriptor st_thumb = StructDescriptor.createDescriptor("OBJ_THUMB",conn);
			    	  Object[] thumb_obj = {pro.getAtt_file().getAttThumb().getFileName(),
						    			    pro.getAtt_file().getAttThumb().getFileSize(),
						    			    pro.getAtt_file().getAttThumb().getFilePath()
						    	  			};
			    	  STRUCT thumb_rec = new STRUCT(st_thumb,conn,thumb_obj);
			    	  
			    	  StructDescriptor st_att = StructDescriptor.createDescriptor("OBJ_ATT",conn);
			    	  Object[] att_obj = {pro.getAtt_file().getAttName(),
						    			  pro.getAtt_file().getSavefilename(),
						    			  pro.getAtt_file().getAttSize(),
						    			  pro.getAtt_file().getAttType(),
						    			  pro.getAtt_file().getAttPath(),
						    			  thumb_rec
						    	  		  };
			    	  STRUCT att_rec = new STRUCT(st_att,conn,att_obj);
			    	  
			    	  
			    	  
			    	  StructDescriptor st_pro = StructDescriptor.createDescriptor("OBJ_PRO",conn);
			    	  Object[] pro_obj = {pro.getItem().getItemDetail(),pro.getItem().getItemName(),
			    			  			  id,pro.getProPrice(),pro.getProHits(),pro.getProSaleprice(),
			    			  			  pro.getProOpendate(),pro.getProClosedate(),pro.getProDetail(),
			    			  			  pro.getProAmount(),pro.getProStat(),pro.getProSeqno(),att_rec
			    			  			  };
			    	  
			    	  STRUCT proadd_rec = new STRUCT(st_pro,conn,pro_obj);
			         cstmt = conn.prepareCall(sql);
			         cstmt.setObject(1, proadd_rec);
			         cstmt.registerOutParameter(2, OracleTypes.INTEGER);
			         
			         cstmt.executeQuery();
			         
			         seqno = cstmt.getString(2);
			         
			         cstmt.close();
			         
	      } catch (SQLException e) {
	         e.printStackTrace();
		}
	      
	      
	      return seqno;
	   }
		   
//		   public String productadd(Pro pro, String id) {
//			   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//			   String seqno = "";
//			      
////			      String proStat = req.getParameter("proStat");
////			      String proPrice = req.getParameter("proPrice");
////			      String proHits = req.getParameter("proHits");
////			      String proSaleprice = req.getParameter("proSaleprice");
////			      String proOpendate = req.getParameter("proOpendate");
////			      String proClosedate = req.getParameter("proClosedate");
////			      String proDetail = req.getParameter("proDetail");
////			      String proAmount = req.getParameter("amount");
////			      String itemDetail = req.getParameter("itemDetail");
////			      String itemName = req.getParameter("itemName");
//			      
//			      
////			      String id = (String)req.getSession().getAttribute("sess_id");
//			      String sql = "insert into item(item_seqno,item_detail,item_name,mem_id) "
//			      			 + "values(item_seqno.nextval,?,?,?)";
//			      
//			      
//			      
//			      try {
//			         stmt = conn.prepareStatement(sql);
//			         stmt.setString(1, pro.getItem().getItemDetail());
////			         stmt.setString(1, itemDetail);
//			         stmt.setString(2, pro.getItem().getItemName());
////			         stmt.setString(2, itemName);
//			         stmt.setString(3, id);
//			         
//			         stmt.executeQuery();
//			         
//			         sql = "select max(item_seqno) from item";
//			         stmt = conn.prepareStatement(sql);
//
//			         ResultSet rs = stmt.executeQuery();
//			         
//			         rs.next();
//			         
//			         String itemseqno = rs.getString(1);
//			         
//			         sql = " insert into pro(pro_seqno, pro_price, cat_seqno, pro_saleprice, pro_opendate,";
//			         sql += " pro_closedate, pro_detail,mem_id,item_seqno,pro_amount ";
//			if (pro.getProStat() != null) sql += ",pro_stat";
//			         sql   += " ) values (pro_seqno.nextval,?,?,?,?,?,?,?,?,?";
//			if (pro.getProStat() != null) sql += ",?";
//			         sql += " )";
//			         
//			         stmt = conn.prepareStatement(sql);
//			         stmt.setInt(1, pro.getProPrice());
//			         stmt.setInt(2, pro.getProHits());
//			         stmt.setInt(3, pro.getProSaleprice());
//			         stmt.setString(4,dateFormat.format(pro.getProOpendate()));
//			         stmt.setString(5, dateFormat.format(pro.getProClosedate()));
//			         stmt.setString(6, pro.getProDetail());
//			         stmt.setString(7, id);
//			         stmt.setString(8, itemseqno);
//			         stmt.setInt(9, pro.getProAmount());
////			         stmt.setString(1, proPrice);
////			         stmt.setString(2, proHits);
////			         stmt.setString(3, proSaleprice);
////			         stmt.setString(4, proOpendate);
////			         stmt.setString(5, proClosedate);
////			         stmt.setString(6, proDetail);
////			         stmt.setString(7, id);
////			         stmt.setString(8, rs.getString(1));
////			         stmt.setString(9, proAmount);
//			         
//			if (pro.getProStat() != null) stmt.setString(10, pro.getProStat());
//			         stmt.executeQuery();
//			         
//			         sql = "select max(pro_seqno) from pro";
//			         stmt = conn.prepareStatement(sql);
//			         ResultSet rs2 = stmt.executeQuery();
//
//			         if(rs2.next()) seqno = rs2.getString(1);
//			         
//			         
//						if (pro.getAtt_file() != null) {
//						
//							String att_seqno = filedao.insertAttachFile(itemseqno, pro.getAtt_file());
//							String fileType = pro.getAtt_file().getAttType();
//							if (fileType.substring(0,fileType.indexOf("/")).equals("image")) {
//							
//								filedao.insertThumbNail(pro.getAtt_file(),att_seqno);
//							
//							}
//						}
//			         
//			         
//			         
//			         
//			         
//			         
//			         stmt.close();
//			      } catch (SQLException e) {
//			         e.printStackTrace();
//			      }
//			      
//			      
//			      return seqno;
//			   }
	
}	

	
	
	
	
	
	


	

