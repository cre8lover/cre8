package dao;

import java.sql.CallableStatement;
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
import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;

public class AdminDao {
	private final Connection conn = OracleConn.getInstance().getConn();
	PreparedStatement stmt;
	CallableStatement cstmt;
	
	public Map<String, String> longinProc(String id, String pw) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		String sql = "call p_adminLogin(?,?,?,?)";
		
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.setString(2, pw);
			cstmt.registerOutParameter(3, OracleType.VARCHAR2);
			cstmt.registerOutParameter(4, OracleType.VARCHAR2);
			cstmt.executeQuery();
			
			String id2 = cstmt.getString(3);
			String pw2 = cstmt.getString(4);
			
			if(id2 != null) {
				
				if(pw2.equals(pw)) {
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
			cstmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		
		return map;
	}

	public List<Cat> categorylist(AdminKeyWord adkey) {
		List<Cat> cate = new ArrayList<Cat>();
		
		
		String sql = "call p_categorylist(?,?,?)";
			   try {
				cstmt = conn.prepareCall(sql);
				cstmt.setString(1, adkey.getCategory());
				cstmt.setString(2, adkey.getKeyword());
				cstmt.registerOutParameter(3, OracleTypes.CURSOR);
				cstmt.executeQuery();
				ResultSet rs = (ResultSet)cstmt.getObject(3);		

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
				cstmt.close();
			   } catch (SQLException e) {
				
				   e.printStackTrace();
			
			   }
		
		return cate;
	}

	public List<Mem> memberlist(AdminKeyWord adkey) {
		 
		List<Mem> member = new ArrayList<Mem>();
		      
	 String sql = "call p_memberlist(?,?,?,?)";
		  
		    try {
		    	cstmt = conn.prepareCall(sql);
		    	cstmt.setString(1, adkey.getCategory());
		    	cstmt.setString(2, adkey.getKeyword());
		    	cstmt.setString(3, adkey.getClassification());
		    	cstmt.registerOutParameter(4, OracleTypes.CURSOR);
		    	cstmt.executeQuery();
		    	ResultSet rs = (ResultSet)cstmt.getObject(4);               
	   
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
	         cstmt.close();
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
		
		String sql = "call p_marketinglist(?,?,?)";
	  
		try {	
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, adkey.getKeyword());
			cstmt.setString(2, adkey.getClassification());
			cstmt.registerOutParameter(3, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(3);	
		
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
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return market;
	}

	public List<Marketing> monthlist() {
		
		List<Marketing> mar = new ArrayList<Marketing>();

		String sql = " call p_monthlist(?)";
				
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(1);
			
			Marketing m = null;

			while(rs.next()) {
				m = new Marketing();
				m.setMarSeqno(rs.getInt("rownum"));
				m.setMarOpendate(rs.getString("month"));
				m.setCnt(rs.getInt("cnt"));
				m.setMarPrice(rs.getString("price"));
				
				mar.add(m);
			}
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return mar;
	}

	public List<Marketing> yearlist() {
		
		List<Marketing> ket = new ArrayList<Marketing>();

		String sql = " call p_yearlist(?)";
				
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(1);
			
			Marketing m = null;
			while(rs.next()) {
				m = new Marketing();
				m.setMarSeqno(rs.getInt("rownum"));
				m.setMarOpendate(rs.getString("year"));
				m.setCnt(rs.getInt("cnt"));
				m.setMarPrice(rs.getString("price"));
				ket.add(m);
			}
			cstmt.close();
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
