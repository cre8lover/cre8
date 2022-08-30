package service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import dao.AdminDao;
import dto.AdminKeyWord;
import dto.Cat;
import dto.Item;
import dto.Marketing;
import dto.Mem;
import dto.Pro;

public class AdminServiceImp implements AdminService {
	AdminDao dao = new AdminDao();

	@Override
	public Map<String, String> login(String id, String pw) {

		return dao.longinProc(id, pw);
	}

	@Override
	public List<Cat> catelist(AdminKeyWord adkey) {
		
		return dao.categorylist(adkey);
	}
/*
	@Override
	public List<Pro> itemlist() {
		
		return dao.itemlist();
	}
*/
	@Override
	public List<Marketing> marketinglist(AdminKeyWord adkey) {
		
		return dao.marketinglist(adkey);
	}

	@Override
	public List<Marketing> month() {
		
		return dao.monthlist();
	}

	@Override
	public List<Marketing> year() {
		return dao.yearlist();
	}

	@Override
	public void reg(HttpServletRequest req) {
		Marketing market = new Marketing();
		String cate = req.getParameter("marcate");
		String name = req.getParameter("name");
		String price = req.getParameter("price");
		String company = req.getParameter("company");
		String start = req.getParameter("start");
		String finish = req.getParameter("finish");
		String comment = req.getParameter("comment");
		String phone = req.getParameter("phone");
		String ceo = req.getParameter("ceo");
		String regnum = req.getParameter("regnum");
		
		market.setMarCategory(cate);
		market.setMarProduct(name);
		market.setMarPrice(price);
		market.setMarCompany(company);
		market.setMarOpendate(start);
		market.setMarClosedate(finish);
		market.setMarDetail(comment);
		market.setMarPhone(phone);
		market.setMarCeo(ceo);
		market.setMarRegnum(regnum);
		
		dao.marketReg(market);
	}

	   @Override
	   public List<Mem> memberlist(AdminKeyWord adkey) {

	      return dao.memberlist(adkey);
	   }

	@Override
	public List<Marketing> purchase(AdminKeyWord adkey) {

		return dao.buylist(adkey);
	}

	@Override
	public Marketing modify(String seqno) {
		
		return dao.modify(seqno);
	}

	public void update(HttpServletRequest req) {
		Marketing market = new Marketing();
		
		String o = req.getParameter("seqno");
		System.out.println(o);
		int seqno = Integer.parseInt(o);
		
		String cate = req.getParameter("marcate");
		String name = req.getParameter("name");
		String price = req.getParameter("price");
		String company = req.getParameter("company");
		String start = req.getParameter("start");
		String finish = req.getParameter("finish");
		String comment = req.getParameter("comment");
		String phone = req.getParameter("phone");
		String ceo = req.getParameter("ceo");
		String regnum = req.getParameter("regnum");

		market.setMarCategory(cate);
		market.setMarProduct(name);
		market.setMarPrice(price);
		market.setMarCompany(company);
		market.setMarOpendate(start);
		market.setMarClosedate(finish);
		market.setMarDetail(comment);
		market.setMarPhone(phone);
		market.setMarCeo(ceo);
		market.setMarRegnum(regnum);
		market.setMarSeqno(seqno);
		dao.update(market);
	}

}
