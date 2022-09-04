package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

public class MemberDao {
	private final Connection conn = OracleConn.getInstance().getConn();
	PreparedStatement stmt;
	public Map<String, String> longinProc(String id, String pw) {

		Map<String, String> map = new HashMap<String, String>();
		
		String sql = "select m.mem_id, m.mem_pw, m.mem_name, a.auth_name "
				+ " from mem m , mem_auth a "
				+ " where a.mem_id = m.mem_id and m.mem_id = ?";
		
		try {
			stmt = conn.prepareStatement(sql);
		
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			
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
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		
		return map;
	}

	public String reginsert(Mem mem) {
	
		String sql = "insert into mem (mem_id, mem_pw, mem_tel, mem_email, mem_birth, mem_name, mem_check)";
				sql+= " values (?, ?, ?, ?, ?, ?, ?)";
		
		String in = "성공";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			
			stmt.setString(1, mem.getMemId());
			stmt.setString(2, mem.getMemPw());
			stmt.setString(3, mem.getMemTel());
			stmt.setString(4, mem.getMemEmail());
			stmt.setString(5, mem.getMemBirth());
			stmt.setString(6, mem.getMemName());
			stmt.setString(7, mem.getCheck());
			
			stmt.executeQuery();
			
			sql = "insert into mem_auth (auth_seqno, mem_id) values (auth_seqno.nextval, ?)";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, mem.getMemId());

			stmt.executeQuery();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return in;
	}

	public Mem mypage(String id) {
		
		Mem member = new Mem();
//		Address add = new Address();
		
		String sql = "select m.mem_id, m.mem_email, m.mem_tel, m.mem_name, ";
				sql += " nvl(a.add_address, '주소를 입력하세요') as add_address";
				sql += " from mem m, address a";
				sql += " where m.mem_id = a.mem_id(+)"; 
				sql += " and m.mem_id = ?";
		try {
			stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
											  	  ResultSet.CONCUR_UPDATABLE);
			
			stmt.setString(1, id);

			ResultSet rs = stmt.executeQuery();
			
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
			 
			 List<Att> fileList = new ArrayList<Att>();
			 
			 while(rs.next()) {
				 Att att = new Att();
				 
				 att.setAttSeqno(rs.getInt("att_seqno"));
				 att.setAttName(rs.getString("att_filename"));
				 att.savefilename(rs.getString("att_savefilename"));
				 att.setAttSize(rs.getString("att_filesize"));
				 att.setAttType(rs.getString("att_filetype"));
				 att.setAttPath(rs.getString("att_filepath"));

				 if(rs.getString("filetype").contains("image")) {
				 
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
						 att.setThumbnail(th);
						 
					 }
					 
				 }
				 fileList.add(att);
			 }
			 
			 member.setAtt(fileList);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return member;
	}

	public Mem info(String id) {
		Mem member = new Mem();
		
		String sql = "select a.add_detail, m.mem_name, m.mem_email, m.mem_tel,"
				+ " m.mem_snsinfo, m.mem_img, a.add_category,"
				+ " a.add_phone,a.add_person,a.add_address"
				+ " from mem m, address a"
				+ " where m.mem_id = a.mem_id(+) and m.mem_id = ?";

		try {
			stmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
				  	  ResultSet.CONCUR_UPDATABLE);
		
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			
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
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return member;
	}

	public List<Pro> membuylist(String id) {
		List<Pro> pro = new ArrayList<Pro>();
		
		String sql = "select rownum, a.*"
				+ " from("
				+ " select (select item_img from item i where i.item_seqno = p.item_seqno) item_img,"
				+ " (select item_name from item i where i.item_seqno = p.item_seqno) item_name,"
				+ " o.order_date order_date, o.amount order_amount, o.order_totalprice order_totalprice"
				+ " from pro p,"
				+ " ("
				+ " select order_totalprice, o.mem_id, pro_seqno, order_date, m.amount"
				+ " from orders o, mini_order m"
				+ " where o.order_seqno = m.order_seqno"
				+ " ) o"
				+ " where p.pro_seqno = o.pro_seqno and o.mem_id = ?"
				+ " order by order_date desc) a";
		
		try {
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			
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
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return pro;
	}

	public Map<String, List<Cart>> memauclist(String id) {
		Map<String, List<Cart>> auc = new HashMap<String, List<Cart>>();
		
		List<Cart> cart = new ArrayList<Cart>();
		List<Cart> cart2 = new ArrayList<Cart>();
		
		String sql = "select rownum, a.*"
				+ " from("
				+ " select (select item_img from item i where i.item_seqno = p.item_seqno) item_img,"
				+ " (select item_name from item i where i.item_seqno = p.item_seqno) item_name,"
				+ " o.order_date order_date, o.amount order_amount, o.order_totalprice oreder_totalprice, p.auc_stat"
				+ " from auc p,"
				+ " ("
				+ " select order_totalprice, o.mem_id, auc_seqno, order_date, m.amount"
				+ " from orders o, mini_order m"
				+ " where o.order_seqno = m.order_seqno"
				+ " ) o"
				+ " where p.auc_seqno = o.auc_seqno and o.mem_id = ?"
				+ " order by order_date desc) a";
		
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
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
			
			sql = "select rownum, a.*"
				+ " from("
				+ " select a.auc_seqno, aucnow_date, (select item_name from item i where i.item_seqno = a.item_seqno) auc_name,"
				+ " (select item_img from item i where i.item_seqno = a.item_seqno) auc_img,"
				+ " a.auc_stat, a.auc_closeprice"
				+ " from (select auc_seqno, Max(aucnow_date) as aucnow_date"
				+ " from auc_nowing"
				+ " where mem_id = ?"
				+ " group by auc_seqno) an, auc a"
				+ " where a.auc_seqno = an.auc_seqno"
				+ " order by aucnow_date desc) a";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			
			rs = stmt.executeQuery();
			
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
			
		stmt.close();	
		} catch (SQLException e) {

			e.printStackTrace();
		
		}
		
		
		return auc;
	}

	public List<Pro> buystat(String id) {
		List<Pro> pro = new ArrayList<Pro>();
		
		String sql = "select rownum, (select item_img from item i where i.item_seqno = p.item_seqno) item_img,"
				+ " (select item_name from item i where i.item_seqno = p.item_seqno) item_name,"
				+ " p.pro_price, a.order_date, a.orderdetail_stat, a.pro_seqno"
				+ " from pro p,"
				+ " ("
				+ "select o.order_seqno, o.order_date, od.orderdetail_stat, o.pro_seqno"
				+ " from (select o.order_seqno, o.order_date, m.pro_seqno from orders o, mini_order m "
				+ " where m.order_seqno = o.order_seqno and o.mem_id = ?) o,"
				+ " ("
				+ " select order_seqno, orderdetail_stat"
				+ " from orderdetail"
				+ " where orderdetail_stat not in 'END'"
				+ " ) od"
				+ " where o.order_seqno = od.order_seqno"
				+ " order by o.order_date desc"
				+ " ) a"
				+ " where p.pro_seqno = a.pro_seqno";

		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			
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
			
			
			stmt.close();	
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
		
		String sql = "update mem set mem_email = ?, mem_tel = ?,";
				sql += " mem_snsinfo = ? where mem_id = ?";
		
		try {
			conn.prepareStatement(sql);
		
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
