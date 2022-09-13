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

import javax.servlet.http.HttpServletRequest;

import common.OracleConn;
import dto.Address;
import dto.Att;
import dto.Auc;
import dto.AucNowing;
import dto.Cart;
import dto.Item;
import dto.Mem;
import dto.Orderdetail;
import dto.Orders;
import dto.Pro;
import dto.Ship;
import dto.Thumbnail;
import dto.Waybill;
import oracle.jdbc.internal.OracleTypes;

public class MemberDao {
	private final Connection conn = OracleConn.getInstance().getConn();
	PreparedStatement stmt;
	CallableStatement cstmt;
	public Map<String, String> longinProc(String id, String pw) {

		Map<String, String> map = new HashMap<String, String>();
		
		String sql = "call p_login(?,?)";
		
		try {
			cstmt = conn.prepareCall(sql);
		
			cstmt.setString(1, id);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(2);
			
			if(rs.next()) {
				
				if(rs.getString("mem_pw").equals(pw)) {
					//로그인 성공
					map.put("login", "ok");
					map.put("name",rs.getString("mem_name"));
					map.put("auth",rs.getString("auth_name"));
					
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

	public String reginsert(Mem mem) {
	
		String sql = "call p_reginsert(?,?,?,?,?,?,?)";
		
		String in = "성공";
		
		try {
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, mem.getMemId());
			cstmt.setString(2, mem.getMemPw());
			cstmt.setString(3, mem.getMemTel());
			cstmt.setString(4, mem.getMemEmail());
			cstmt.setString(5, mem.getMemBirth());
			cstmt.setString(6, mem.getMemName());
			cstmt.setString(7, mem.getCheck());
			
			cstmt.executeQuery();
			
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return in;
	}

	public Mem mypage(String id) {
		
		Mem member = new Mem();
//		Address add = new Address();
		
		String sql = "call p_mypage(?,?)";
		try {
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, id);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			
			ResultSet rs = (ResultSet)cstmt.getObject(2);
			
			Address addr = null;
			
			if(rs.next()) {
				member.setMemId(rs.getString("mem_id"));
				member.setMemEmail(rs.getString("mem_email"));
				member.setMemTel(rs.getString("mem_tel"));
				member.setMemName(rs.getString("mem_name"));
				
				addr = new Address();
				addr.setAddAddress(rs.getString("add_address"));
			}
			member.setAddressSet(addr);

			sql = "select * from att where mem_id = ?";
			 stmt = conn.prepareStatement(sql);
			 stmt.setString(1, id);

			 rs = stmt.executeQuery();
			 
			 Att att = new Att();
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
			 
			 member.setAtt(att);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return member;
	}

	public Mem info(String id) {
		Mem member = new Mem();
		
		String sql = "call p_info(?,?)";

		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(2);
			
			if(rs.next()) {
				member.setMemName(rs.getString("mem_name"));
				member.setMemEmail(rs.getString("mem_email"));
				member.setMemTel(rs.getString("mem_tel"));
				member.setMemSnsinfo(rs.getString("mem_snsinfo"));
				member.setMemImg(rs.getString("mem_img"));
				
				Address a = new Address();
				a.setAddAddress(rs.getString("add_address"));
				a.setAddCategory(rs.getString("add_category"));
				a.setAddPhone(rs.getString("add_phone"));
				a.setAddPerson(rs.getString("add_person"));
				a.setAddetail(rs.getString("add_detail"));
				member.setAddressSet(a);
				
			}
			
			cstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return member;
	}

	public List<Pro> membuylist(String id) {
		List<Pro> pro = new ArrayList<Pro>();
		
		String sql = "call p_membuylist(?,?)";
		
		try {
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, id);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(2);
			
			while(rs.next()) {
				Pro p = new Pro();
				p.setProSeqno(rs.getInt("rownum"));
				
				Item i = new Item();
				i.setItemImg(rs.getString("item_img"));
				i.setItemName(rs.getString("item_name"));
				
				Orders o = new Orders();
				o.setOrderDate(rs.getDate("order_date"));
				o.setOrderTotalprice(rs.getInt("order_totalprice"));
				o.setOrderAmount(rs.getInt("order_amount"));
				
				p.setOrdersSet(o);
				p.setItem(i);
				pro.add(p);
			}
			cstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return pro;
	}

	public Map<String, List<Cart>> memauclist(String id) {
		Map<String, List<Cart>> auc = new HashMap<String, List<Cart>>();
		
		List<Cart> cart = new ArrayList<Cart>();
		List<Cart> cart2 = new ArrayList<Cart>();
		
		String sql = "call p_memauclist_end(?,?)";
		
		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(2);
			
			while(rs.next()) {
				Cart c = new Cart();
				c.setCartSeqno(rs.getInt("rownum"));
				
				Item i = new Item();
				i.setItemImg(rs.getString("item_img"));
				i.setItemName(rs.getString("item_name"));
				
				Orders o = new Orders();
				o.setOrderDate(rs.getDate("order_date"));
				o.setOrderTotalprice(rs.getInt("oreder_totalprice"));
				
				Auc a = new Auc();
				a.setAucStat(rs.getString("auc_stat"));

				Pro p = new Pro();
				p.setItem(i);
				c.setAuc(a);
				c.setPro(p);
				c.setOrdersSet(o);
				
				cart.add(c);
			}
			
			sql = "call p_memauclist_ing(?,?)";
			
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			rs = (ResultSet)cstmt.getObject(2);
			
			while(rs.next()) {
				Cart cc = new Cart();
				Auc a = new Auc();
				List<AucNowing> an = new ArrayList<AucNowing>();
				a.setAucSeqno(rs.getInt("rownum"));
				a.setAucImg(rs.getString("auc_img"));
				a.setAucCloseprice(rs.getInt("auc_closeprice"));
				a.setAucStat(rs.getString("auc_stat"));
				Item i = new Item();
				i.setItemName(rs.getString("auc_name"));
				
				AucNowing g = new AucNowing();
				g.setAucnowDate(rs.getDate("aucnow_date"));
				an.add(g);
				
				a.setItem(i);
				a.setAucNowingSet(an);
				
				cc.setAuc(a);
				cart2.add(cc);
			}
			
			auc.put("END", cart);
			auc.put("ING", cart2);
			
		cstmt.close();	
		} catch (SQLException e) {

			e.printStackTrace();
		
		}
		
		
		return auc;
	}

	public List<Pro> buystat(String id) {
		List<Pro> pro = new ArrayList<Pro>();
		
		String sql = "call buystat(?,?)";

		try {
			cstmt = conn.prepareCall(sql);
			cstmt.setString(1, id);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet)cstmt.getObject(2);
			
			while(rs.next()) {
				Pro p = new Pro();
				p.setProAmount(rs.getInt("rownum"));
				p.setProSeqno(rs.getInt("pro_seqno"));
				p.setProPrice(rs.getInt("pro_price"));
				
				Item i = new Item();
				i.setItemImg(rs.getString("item_img"));
				i.setItemName(rs.getString("item_name"));
				
				Orders o = new Orders();
				o.setOrderDate(rs.getDate("order_date"));
				
				Orderdetail d = new Orderdetail();
				d.setOrderdetailStat(rs.getString("orderdetail_stat"));
				
				o.setOrderdetailSet(d);
				p.setOrdersSet(o);
				p.setItem(i);
				
				pro.add(p);
			}
			
			
			cstmt.close();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pro;
	}

	public Map<String, List<Ship>> ordercheck(String id) {
		Map<String, List<Ship>> ship = new HashMap<String, List<Ship>>();
		
		List<Ship> detail = new ArrayList<Ship>();
		List<Ship> order = new ArrayList<Ship>();
		
		String sql = "SELECT *"
				+ " FROM("
				+ "    select o.order_seqno, d.orderdetail_way, o.order_date"
				+ "    from orders o, orderdetail d"
				+ "    where o.order_seqno=d.order_seqno"
				+ "    and o.mem_id = ?"
				+ "    order by o.order_date desc) a,"
				+ "    (select s.order_seqno, s.ship_seqno, w.waybill_name, w.waybill_number, s.add_address"
				+ "    from waybill w,"
				+ "                (select s.order_seqno, s.ship_seqno, a.add_address, a.add_num"
				+ "                 from ship s, address a"
				+ "                 where s.add_seqno=a.add_seqno) s"
				+ "    where w.ship_seqno=s.ship_seqno) s"
				+ " where a.order_seqno = s.order_seqno";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Ship s = new Ship();
				
				Orders o = new Orders();
				Orderdetail od = new Orderdetail();
				od.setOrderdetailWay(rs.getString("orderdetail_way"));
				o.setOrderdetailSet(od);
				s.setOrders(o);

				Address a = new Address();
				a.setAddAddress(rs.getString("add_address"));
				s.setAddress(a);
				
				Waybill w = new Waybill();
				w.setWaybillName(rs.getString("waybill_name"));
				w.setWaybillNumber(rs.getInt("waybill_number"));
				s.setWaybillSet(w);
				
				detail.add(s);
			}
			
			sql = "select rownum, o.order_seqno, o.order_date, o.order_totalprice, s.ship_status"
				+ " from orders o, ship s"
				+ " where o.order_seqno = s.order_seqno"
				+ " and o.mem_id = ?"
				+ " order by o.order_seqno desc";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			rs = stmt.executeQuery();

			while(rs.next()) {
				Ship s = new Ship();
				s.setShipStatus(rs.getString("ship_status"));
				
				Orders o = new Orders();
				o.setOrderSeqno(rs.getInt("order_seqno"));
				o.setOrderDate(rs.getDate("order_date"));
				o.setOrderTotalprice(rs.getInt("order_totalprice"));
				s.setOrders(o);
				
				order.add(s);
			}
			
			ship.put("order", order);
			ship.put("detail", detail);
			
			stmt.close();	

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ship;

	}

	public void infoinsert(Mem mem) {
		Address add = mem.getAddressSet();
		Att att = mem.getAtt();
		String sql = "update mem set mem_email = ?, mem_tel = ?,";
				sql += " mem_snsinfo = ? where mem_id = ?";
		
		try {
			stmt = conn.prepareStatement(sql);
		
			stmt.setString(1, mem.getMemEmail());
			stmt.setString(2, mem.getMemTel());
			
			if( mem.getMemSnsinfo() != null) { 
				stmt.setString(3, mem.getMemSnsinfo());
			} else { 
				stmt.setString(3, ""); 
			}
			
			stmt.setString(4, mem.getMemId());
		
			stmt.executeQuery();

			sql = "update address set add_category = ?, add_phone = ?,"
					+ " add_person = ?, add_address = ?, ADD_DETAIL = ?"
					+ " where mem_id = ?";
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, add.getAddCategory());
			stmt.setString(2, add.getAddPhone());
			stmt.setString(3, add.getAddPerson());
			stmt.setString(4, add.getAddAddress());
			stmt.setString(5, add.getAddetail());
			stmt.setString(6, mem.getMemId());
			
			stmt.executeQuery();

		 //첨부파일
			if(att != null) {
				
				sql = "INSERT INTO att(att_seqno, att_name, att_savename, att_size, att_type, att_path, mem_id)"
						+ " VALUES (att_seqno.NEXTVAL, ?,?,?,?,?,?)";
			
			PreparedStatement stmt;
			String attach_no = null;
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, att.getAttName());
				stmt.setString(2, att.getSavefilename());
				stmt.setString(3, att.getAttSize());
				stmt.setString(4, att.getAttType());
				stmt.setString(5, att.getAttPath());
				stmt.setString(6, mem.getMemId());
				stmt.executeQuery();
				
				sql = "SELECT max(att_seqno) FROM att";
				stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				attach_no = rs.getString(1);
				
				
				String fileType = att.getAttType();
				
				//썸네일
				if(fileType.substring(0, fileType.indexOf("/")).equals("image")) {
					sql = "INSERT INTO att_thumb (thumb_seqno, thumb_filename, thumb_filesize, thumb_filepath, att_seqno) "
							+ " VALUES (thumb_seqno.nextval, ?, ?, ?, ?)";
					Thumbnail thumb = att.getAttThumb();
						stmt = conn.prepareStatement(sql);
						stmt.setString(1, thumb.getFileName());
						stmt.setString(2, thumb.getFileSize());
						stmt.setString(3, thumb.getFilePath());
						stmt.setString(4, attach_no);
						stmt.executeQuery();
				}
			}
			
			stmt.close();	
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Map<String, String> changePw(String new_pw, String id, String now_pw) {

		Map<String, String> map = new HashMap<String, String>();
		
		String sql = "select mem_id, mem_pw from mem where mem_id = ? ";
		
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, id);

				ResultSet rs = stmt.executeQuery();
				
			
				
				if(rs.next()) {
					
					if(rs.getString("mem_pw").equals(now_pw)) {
						sql = "update mem set mem_pw = ? where mem_id = ?";
						
						stmt = conn.prepareStatement(sql);
						stmt.setString(1, new_pw);
						stmt.setString(2, id);
						
						stmt.executeQuery();
						
						//비밀번호 변경 성공
						map.put("change", "ok");
						
					} else {
						//비밀번호 변경 실패
						map.put("change", "pwfail");
					}
					
				} else {
					map.put("change", "no_member");
				}
				
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return map;

		
	}

	public Map<String, String> findId(String idemail) {
		Map<String, String> map = new HashMap<String, String>();
		
		String sql = "select mem_id,mem_email from mem where mem_email = ? ";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, idemail);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()) {
				
				if(rs.getString("mem_email").equals(idemail)) {
					//주소일치
					map.put("find", "ok");
					map.put("lostid", rs.getString("mem_id"));
				} else {
					//비밀번호 변경 실패
					map.put("find", "pwfail");
				}
				
			} else {
				map.put("find", "no_member");
			}
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return map;
	}

	public Map<String, String> findPw(String id, String email) {
		Map<String, String> map = new HashMap<String, String>();
		
		String sql = "select mem_id,mem_email,mem_pw from mem where mem_email = ? and mem_id = ?";
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, email);
			stmt.setString(2, id);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				
				if(rs.getString("mem_id").equals(id)) {
					
					if(rs.getString("mem_email").equals(email)) {
						
						map.put("find", "ok");
						map.put("lostpw", rs.getString("mem_pw"));
						
					} else {
						map.put("find", "pwfail");
						
					}
					
				} else {
					System.out.println("진입");
					map.put("find", "no_member");
				}
			}	else {
				
				map.put("find", "null");

			}
		

			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return map;
	}

	public int checkid(String id) {
		int rs = 0;
		String sql ="select mem_id from mem where mem_id = ?";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, id);
			
			ResultSet res = stmt.executeQuery();
			if(res.next()) {
				rs = 1;
			} else {
				rs = 0;
			}
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
		
	}
}
