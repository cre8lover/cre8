package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.OracleConn;
import dto.AdminKeyWord;
import dto.Att;
import dto.Cat;
import dto.Marketing;
import dto.Mem;
import dto.MemAuth;
import dto.Thumbnail;
import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

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
		String sql = null;
		if(market.getMarSeqno() != null) {			
			sql = "call p_marketModiy(?,?,?,?)";
		} else {
			sql = "call p_market_insert(?,?,?)";
		}
		Att att = market.getAttSet();
		
		String mobile = null;
		String phone = market.getMarPhone();
		if(phone.length() == 11) {
			mobile = phone.replaceFirst("(^[0-9]{3})([0-9]{4})([0-9]{4})$","$1-$2-$3");
		}
		
		try {
			cstmt = conn.prepareCall(sql);
			
			StructDescriptor st_mar = StructDescriptor.createDescriptor("OBJ_MAR",conn);
			Object[] mar_obj = {market.getMarCategory(),
								market.getMarProduct(),
								market.getMarPrice(),
								market.getMarCompany(),
								market.getMarOpendate(),
								market.getMarClosedate(),
								market.getMarDetail(),
								market.getMarCeo(),
								mobile,
								market.getMarRegnum()
							   };
			STRUCT mar_rec = new STRUCT(st_mar, conn, mar_obj);
			
			
			if(att != null) {
				
				StructDescriptor st_thumb = StructDescriptor.createDescriptor("OBJ_THUMB",conn);
				Object[] thumb_obj = {market.getAttSet().getAttThumb().getFileName(),
									  market.getAttSet().getAttThumb().getFileSize(),
									  market.getAttSet().getAttThumb().getFilePath()
									 };
				STRUCT thumb_rec = new STRUCT(st_thumb, conn, thumb_obj);
				
				StructDescriptor st_att = StructDescriptor.createDescriptor("OBJ_ATT",conn);
				Object[] att_obj = {market.getAttSet().getAttName(),
									market.getAttSet().getSavefilename(),
									market.getAttSet().getAttSize(),
									market.getAttSet().getAttType(),
									market.getAttSet().getAttPath(),
									thumb_rec
								   };
				STRUCT att_rec = new STRUCT(st_att, conn, att_obj);
				
				cstmt = conn.prepareCall(sql);
				cstmt.setObject(1, att_rec);
				
				if(market.getMarSeqno() != null) {
					cstmt.setObject(2, mar_rec);
					cstmt.setInt(3, market.getMarSeqno());
					cstmt.setString(4, market.getAttSet().getMem().getMemId());
				} else {
					cstmt.setObject(2, mar_rec);
					cstmt.setString(3, market.getAttSet().getMem().getMemId());
				}
				cstmt.executeQuery();
			}
			
			cstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Marketing> buylist(AdminKeyWord adkey) {
		List<Marketing> list = new ArrayList<Marketing>();

		String sql = " call p_buylist(?,?,?) ";
		
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, adkey.getClassification());
			cstmt.setString(2, adkey.getKeyword());
			cstmt.registerOutParameter(3, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(3);
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
			
			cstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

	public Marketing modify(String seqno) {

		Marketing m = new Marketing();
		Att att = new Att();
		String sql = "call p_modify(?,?)";
		
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, seqno);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(2);
			
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
			 sql = "select * from att where mar_seqno = ?";
			 stmt = conn.prepareStatement(sql);
			 stmt.setString(1, seqno);

			 rs = stmt.executeQuery();
			 
			 
			 if(rs.next()) {
				 
				 att.setAttSeqno(rs.getInt("att_seqno"));
				 att.setAttName(rs.getString("att_name"));
				 att.savefilename(rs.getString("att_savename"));
				 att.setAttSize(rs.getString("att_size"));
				 att.setAttType(rs.getString("att_type"));
				 att.setAttPath(rs.getString("att_path"));

				 if(rs.getString("att_type").contains("image")) {
				 
					 sql = "select * from att_thumb where att_seqno = ?";
					 stmt = conn.prepareStatement(sql);
					 stmt.setString(1, rs.getString("att_seqno"));
					 ResultSet rs2 = stmt.executeQuery();
				 
					 while(rs2.next()) {
						 
						 Thumbnail th = new Thumbnail();
						 th.setThumbSeqNo(rs2.getString("thumb_seqno"));
						 th.setFileName(rs2.getString("thumb_filename"));
						 th.setFileSize(rs2.getString("thumb_filesize"));
						 th.setFilePath(rs2.getString("thumb_filepath"));
						 att.setAttThumb(th);
						 
					 }
					 
				 }
				 
			 }
			 System.out.println(att.getAttName());
		m.setAttSet(att);
		System.out.println(m.getAttSet().getAttName());
		stmt.close();
		cstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

	/*
	public void update(Marketing market) {
//		System.out.println(market.getMarCategory());
//		System.out.println(market.getMarProduct());
//		System.out.println(market.getMarPrice());
//		System.out.println(market.getMarCompany());
//		System.out.println(market.getMarOpendate());
//		System.out.println(market.getMarClosedate());
//		System.out.println(market.getMarDetail());
//		System.out.println(market.getMarCeo());
//		System.out.println(market.getMarPhone());
//		System.out.println(market.getMarRegnum());
//		System.out.println(market.getMarSeqno());
		String sql = "call p_marketModiy(?,?,?,?,?,?,?,?,?,?,?)";
		
			try {
				cstmt = conn.prepareCall(sql);
				
				cstmt.setString(1, market.getMarCategory());
				cstmt.setString(2, market.getMarProduct());
				cstmt.setString(3, market.getMarPrice());
				cstmt.setString(4, market.getMarCompany());
				cstmt.setString(5, market.getMarOpendate());
				cstmt.setString(6, market.getMarClosedate());
				cstmt.setString(7, market.getMarDetail());
				cstmt.setString(8, market.getMarCeo());
				cstmt.setString(9, market.getMarPhone());
				cstmt.setString(10, market.getMarRegnum());
				cstmt.setInt(11, market.getMarSeqno());
				cstmt.executeQuery();
				
				cstmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	 */	
	

}
