package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.OracleConn;
import dto.AdminKeyWord;
import dto.Auc;
import dto.Cat;
import dto.Item;
import dto.Marketing;
import dto.Mem;
import dto.MemAuth;
import dto.Pro;

public class AdminDao {
	private final Connection conn = OracleConn.getInstance().getConn();
	PreparedStatement stmt;

	public Map<String, String> longinProc(String id, String pw) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		String sql = "select m.mem_id, m.mem_pw from mem m, mem_auth a "
				+ " where m.mem_id = a.mem_id and (a.auth_name = 'A' or a.auth_name = 'M') and m.mem_id = ?";
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				
				if(rs.getString("mem_pw").equals(pw)) {
					//로그인 성공
					map.put("login", "ok");
//					map.put("name",rs.getString("mem_name"));
					
				} else {
					//비밀번호 오류
					map.put("login", "pwfail");
				}
				
			} else {
				map.put("login", "no_member");
			}
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		
		return map;
	}

	public List<Cat> categorylist(AdminKeyWord adkey) {
		List<Cat> cate = new ArrayList<Cat>();
		
		
		String sql = "select rownum, c.cat_name, c.cat_regdate, c.cat_seqno as cat_seqno,";
			   sql += " (select mem.mem_name from mem where mem.mem_id = m.mem_id) name";
			   sql += " from cat c, mem_auth m";
			   sql += " where m.mem_id = c.mem_id";
		if(adkey.getCategory() != null && !adkey.getCategory().equals("999")) sql+= " and cat_seqno ='"+adkey.getCategory()+"'";
		if(adkey.getKeyword() != null) sql+= " and cat_name like ('%"+adkey.getKeyword()+"%')";
			   try {
				stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
						  	  ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery();		

				while(rs.next()) {
					Cat c = new Cat();
					c.setCatSeqno(rs.getInt("rownum"));
					c.setCatName(rs.getString("cat_name"));
					c.setCatRegdate(rs.getDate("cat_regdate"));
					
					Mem m = new Mem();
					m.setMemName(rs.getString("name"));

					c.setMem(m);
					cate.add(c);
				}
				stmt.close();
			   } catch (SQLException e) {
				
				   e.printStackTrace();
			
			   }
		
		return cate;
	}

	public List<Mem> memberlist(AdminKeyWord adkey) {
		 
		List<Mem> member = new ArrayList<Mem>();
		      
	 String sql = " select rownum, a.mem_id as mem_id, a.mem_name as mem_name, a.mem_tel as mem_tel, "
		        + " a.mem_email as mem_email, a.auth_date as auth_date, auth_name";
		    sql += " from(";
		    sql += " select m.mem_id, m.mem_name, m.mem_tel, m.mem_email, a.auth_date, "
		        + " decode(a.auth_name,'A','관리자','C','작가','M','마스터','U','일반회원') auth_name";
		    sql += " from mem m, mem_auth a";
		    sql += " where m.mem_id = a.mem_id";
		    if(adkey.getCategory() != null && !adkey.getCategory().equals("999"))   sql += " and a.auth_name = '"+adkey.getCategory()+"'";
		    sql += " order by mem_name) a";
//		    sql += " where 1 = 1";
		    if(adkey.getClassification() != null && adkey.getClassification().equals("998")) { 
		   
		    	sql += " where mem_id like '%"+adkey.getKeyword()+"%' ";
		    	sql += " or mem_name like '%"+adkey.getKeyword()+"%' ";
		    	sql += " or mem_tel like '%"+adkey.getKeyword()+"%' ";
		    	sql += " or mem_email like '%"+adkey.getKeyword()+"%' ";
		    
		    } else if (adkey.getClassification() != null && !adkey.getClassification().equals("998")) {
		  
		    	sql += " where " + adkey.getClassification()+ " like '%"+adkey.getKeyword()+"%'";
		  
		    }
		            
		    try {
		    	stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
		    									ResultSet.CONCUR_UPDATABLE);
		    	ResultSet rs = stmt.executeQuery();               
	   
	        while(rs.next()) {
	            Mem m = new Mem();
	            
	            m.setSeqno(rs.getInt("rownum"));
	            m.setMemId(rs.getString("mem_id"));
	            m.setMemName(rs.getString("mem_name"));
	            m.setMemTel(rs.getString("mem_tel"));
	            m.setMemEmail(rs.getString("mem_email"));
	            
	            
	            MemAuth a = new MemAuth();
	            a.setAuthDate(rs.getDate("auth_date"));
	            a.setAuthName(rs.getString("auth_name"));
	   
	            
	            m.setMemAuthSet(a);
	            
	            member.add(m);
	         }
	         stmt.close();
	      } catch (SQLException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	            
	      return member;
	  }
/*
	public List<Pro> itemlist() {
		List<Pro> pro = new ArrayList<Pro>();
		
		String sql = "select rownum, a.*";
				sql += " from (select";
				sql += " (select c.cat_name from cat c where c.cat_seqno = p.cat_seqno) category, a.item_name, p.pro_price, p.pro_opendate,";
				sql += " (select nvl2(u.auc_stat, 'Y', 'N')";
				sql += " from auc u";
				sql += " where u.item_seqno(+) = a.item_seqno) as auc_stat, p.pro_stat";
				sql += " from item a, pro p";
				sql += " where p.item_seqno = a.item_seqno";
				sql += " order by p.pro_seqno desc) a";
		
			try {
				stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
							  	  ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stmt.executeQuery();	
				
				while(rs.next()) {
					Cat c = new Cat();
					c.setCatName(rs.getString("category"));
					
					Item i = new Item();
					i.setItemName(rs.getString("item_name"));
					
					Pro p = new Pro();
					p.setProSeqno(rs.getInt("rownum"));
					p.setProPrice(rs.getInt("pro_price"));
					p.setProOpendate(rs.getDate("pro_opendate"));
					p.setProStat(rs.getString("pro_stat"));
					
					Auc a = new Auc();
					a.setAucStat(rs.getString("auc_stat"));
					
					i.setAucSet(a);
					p.setItem(i);
					p.setCat(c);
					
					pro.add(p);
				}
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return pro;
	}
*/
	public List<Marketing> marketinglist(AdminKeyWord adkey) {
		
		List<Marketing> market = new ArrayList<Marketing>();
		
		String sql = " select rownum, a.*";
				sql += " from(";
				sql += " select m.mar_seqno, m.mar_product, m.mar_company, m.mar_ceo, m.mar_phone, m.mar_regnum, m.mar_opendate";
				sql += " from marketing m";
				sql += " order by m.mar_opendate desc) a";
				
		if(adkey.getClassification() != null && adkey.getClassification().equals("999")) { 
			
				sql += " where mar_product like '%"+adkey.getKeyword()+"%' ";
				sql += " or mar_company like '%"+adkey.getKeyword()+"%' ";
				sql += " or mar_ceo like '%"+adkey.getKeyword()+"%' ";
				sql += " or mar_phone like '%"+adkey.getKeyword()+"%' ";
				sql += " or mar_regnum like '%"+adkey.getKeyword()+"%' ";
				
		} else if (adkey.getClassification() != null && !adkey.getClassification().equals("999")) {
			  
	    	sql += " where " + adkey.getClassification()+ " like '%"+adkey.getKeyword()+"%'";
	  
	    }
		
		try {	
			stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
												ResultSet.CONCUR_UPDATABLE);	
			ResultSet rs = stmt.executeQuery();	
		
			Marketing m = null;
			
			while(rs.next()) {
				m = new Marketing();
				m.setNo(rs.getInt("rownum"));
				m.setMarSeqno(rs.getInt("mar_seqno"));
				m.setMarProduct(rs.getString("mar_product"));
				m.setMarCompany(rs.getString("mar_company"));
				m.setMarCeo(rs.getString("mar_ceo"));
				m.setMarPhone(rs.getString("mar_phone"));
				m.setMarRegnum(rs.getString("mar_regnum"));
				m.setMarOpendate(rs.getString("mar_opendate"));
				market.add(m);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return market;
	}

	public List<Marketing> monthlist() {
		
		List<Marketing> mar = new ArrayList<Marketing>();

		String sql = " select rownum, a.*";
				sql += " from(";
				sql += " select to_char(mar_opendate, 'YYYY-MM') month, count(*) cnt, sum(mar_price) price";
				sql += " from marketing ";
				sql += " where mar_stat = 'FINISH' or mar_stat = 'ING'";
				sql += " group by to_char(mar_opendate,'YYYY-MM')";
				sql += " order by month) a";
				
		try {
			stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
											ResultSet.CONCUR_UPDATABLE);
			
			ResultSet rs = stmt.executeQuery();
			
			Marketing m = null;

			while(rs.next()) {
				m = new Marketing();
				m.setMarSeqno(rs.getInt("rownum"));
				m.setMarOpendate(rs.getString("month"));
				m.setCnt(rs.getInt("cnt"));
				m.setMarPrice(rs.getString("price"));
				
				mar.add(m);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return mar;
	}

	public List<Marketing> yearlist() {
		
		List<Marketing> ket = new ArrayList<Marketing>();

		String sql = " select rownum, a.*";
				sql += " from(";
				sql += " select to_char(mar_opendate, 'YYYY') year, count(*) cnt, sum(mar_price) price";
				sql += " from marketing ";
				sql += " where mar_stat = 'FINISH' or mar_stat = 'ING'";
				sql += " group by to_char(mar_opendate,'YYYY')";
				sql += " order by year) a";
				
		try {
			stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
											ResultSet.CONCUR_UPDATABLE);
			
			ResultSet rs = stmt.executeQuery();
			
			Marketing m = null;
			while(rs.next()) {
				m = new Marketing();
				m.setMarSeqno(rs.getInt("rownum"));
				m.setMarOpendate(rs.getString("year"));
				m.setCnt(rs.getInt("cnt"));
				m.setMarPrice(rs.getString("price"));
				ket.add(m);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return ket;
	}

	public void marketReg(Marketing market) {
		String sql = "insert into marketing (mar_seqno, mar_cate, mar_product, mar_price, mar_company, "
				+ " mar_opendate, mar_closedate, mar_detail, mar_ceo, mar_phone, mar_regnum)"
				+ " values (mar_seqno.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, market.getMarCategory());
			stmt.setString(2, market.getMarProduct());
			stmt.setString(3, market.getMarPrice());
			stmt.setString(4, market.getMarCompany());
			stmt.setString(5, market.getMarOpendate());
			stmt.setString(6, market.getMarClosedate());
			stmt.setString(7, market.getMarDetail());
			stmt.setString(8, market.getMarCeo());
			stmt.setString(9, market.getMarPhone());
			stmt.setString(10, market.getMarRegnum());
			
			stmt.executeQuery();
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Marketing> buylist(AdminKeyWord adkey) {
		List<Marketing> list = new ArrayList<Marketing>();

		String sql ="SELECT rownum, d.*"
				+ " FROM("
				+ " SELECT m.mar_opendate, a.mem_name, m.mar_price, count(*) over(partition by a.mem_name) count,"
				+ "    ((count(*) over(partition by a.mem_name)) * m.mar_price) total, m.mar_product, "
				+ "		decode(m.mar_stat, 'ING', '판매중', 'END', '판매종료') as mar_stat"
				+ " FROM("
				+ "    SELECT o.order_seqno, o.mem_id, o.order_date, o.mar_seqno, e.mem_name "
				+ "    FROM("
				+ "       select o.order_seqno, o.mem_id, o.order_date, m.mar_seqno"
				+ "       from orders o, mini_order m "
				+ "       where o.order_seqno = m.order_seqno) o, mem e"
				+ "    where e.mem_id = o.mem_id and o.mar_seqno is not null"
				+ "    order by o.order_date desc) a, marketing m";
			sql	+= " where m.mar_seqno = a.mar_seqno) d";
		
		if(adkey.getClassification() != null && adkey.getClassification().equals("998")) { 
				sql += " where mar_stat like '%"+adkey.getKeyword()+"%' ";	
				sql += " or mar_product like '%"+adkey.getKeyword()+"%' ";
				sql += " or mem_name like '%"+adkey.getKeyword()+"%' ";
	/*			
				if (adkey.getSdate() != null && adkey.getFdate() != null) {
					sql+=" and mar_opendate >= TO_DATE('"+adkey.getSdate()+"', 'YYYY-MM-DD') "
							+ " and mar_opendate <= TO_DATE('"+adkey.getFdate()+"', 'YYYY-MM-DD')";
						
				} else if (adkey.getSdate() != null && adkey.getFdate() == null) {
					
						sql+=" and mar_opendate >= TO_DATE('"+adkey.getSdate()+"', 'YYYY-MM-DD') ";
						
				} else if (adkey.getSdate() == null && adkey.getFdate() != null) {
						sql+=" and mar_opendate >= TO_DATE('"+adkey.getFdate()+"', 'YYYY-MM-DD') ";

				}
	*/			
		} else if (adkey.getClassification() != null && !adkey.getClassification().equals("998")) {
		  
				sql += " where " + adkey.getClassification()+ " like '%"+adkey.getKeyword()+"%'";
    	
		} 
		
		try {
			stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
											ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Marketing m = new Marketing();
				m.setMarSeqno(rs.getInt("rownum"));
				m.setMarOpendate(rs.getString("mar_opendate"));
				m.setMarInfo(rs.getString("mem_name"));
				m.setMarPrice(rs.getString("mar_price"));
				m.setMarProduct(rs.getString("mar_product"));
				m.setCnt(rs.getInt("count"));
				m.setMarDetail(rs.getString("total"));
				m.setMarStat(rs.getString("mar_stat"));
				list.add(m);
			}
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

	public Marketing modify(String seqno) {

		Marketing m = new Marketing();
		
		String sql = "select mar_seqno, mar_cate, mar_product, mar_price, mar_company,"
				+ " mar_opendate, mar_closedate, mar_detail, mar_ceo, mar_phone, mar_regnum "
				+ " from marketing where mar_seqno = ?";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, seqno);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				m.setMarSeqno(rs.getInt("mar_seqno"));
				m.setMarCategory(rs.getString("mar_cate"));
				m.setMarProduct(rs.getString("mar_product"));
				m.setMarPrice(rs.getString("mar_price"));
				m.setMarCompany(rs.getString("mar_company"));
				m.setMarOpendate(rs.getString("mar_opendate"));
				m.setMarClosedate(rs.getString("mar_closedate"));
				m.setMarDetail(rs.getString("mar_detail"));
				m.setMarCeo(rs.getString("mar_ceo"));
				m.setMarPhone(rs.getString("mar_phone"));
				m.setMarRegnum(rs.getString("mar_regnum"));
				
			}
			
			
		stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

	public void update(Marketing market) {
		String sql = "update marketing set mar_cate =?, mar_product=?, mar_price=?, mar_company=?, "
				+ " mar_opendate=?, mar_closedate=?, mar_detail=?, mar_ceo=?, mar_phone=?, mar_regnum=?"
				+ " where mar_seqno =?";
		
			PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, market.getMarCategory());
				stmt.setString(2, market.getMarProduct());
				stmt.setString(3, market.getMarPrice());
				stmt.setString(4, market.getMarCompany());
				stmt.setString(5, market.getMarOpendate());
				stmt.setString(6, market.getMarClosedate());
				stmt.setString(7, market.getMarDetail());
				stmt.setString(8, market.getMarCeo());
				stmt.setString(9, market.getMarPhone());
				stmt.setString(10, market.getMarRegnum());
				stmt.setInt(11, market.getMarSeqno());
				
				stmt.executeQuery();
				
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	

}
