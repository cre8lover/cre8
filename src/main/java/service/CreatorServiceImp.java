package service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import dao.CreatorDao;
import dto.Auc;
import dto.Creator;
import dto.Marketing;
import dto.Mem;
import dto.Pro;

public class CreatorServiceImp implements CreatorService{
	
	CreatorDao creatorDao = new CreatorDao();
	Mem mem = new Mem();

	@Override
	public void Creatoradd(HttpServletRequest request) {
		creatorDao.Creatoradd(request);
	}

	@Override
	public List<Creator> Creatorpage() {
		return creatorDao.Creatorpage();
	}

	@Override
	public List<Pro> salesHistory(String id) {
		return creatorDao.salesHistory(id);
	}

	public void CreatorName(String id) {
		creatorDao.CreatorName(id);
	}

	@Override
	public List<Marketing> mk() {
		return creatorDao.mk();
	}

	@Override
	public List<Pro> Prolist(String id) {
		return creatorDao.Prolist(id);
	}

	@Override
	public List<Auc> Auclist(String id) {
		return creatorDao.Auclist(id);
	}

	
	@Override
	   public Creator infomodify(String id) {
	      return creatorDao.infomodify(id);
	}
	@Override
	public String totalmoney(String id) {
		return creatorDao.totalmoney(id);
	}

	@Override
	public Pro productdetail(String seqno) {
	  
		return creatorDao.productdetail(seqno);
	}
	
	@Override
	public String productmodify(HttpServletRequest req) {
	      
		return creatorDao.productmodify(req);
	   
	}
	
	 @Override
	   public String aucadd(HttpServletRequest req) {
	      return creatorDao.aucadd(req);
	   }
	   @Override
	   public Auc aucdetail(String seqno) {
	      return creatorDao.aucdetail(seqno);
	   }
	   @Override
	   public void aucmodify(HttpServletRequest req) {
	      creatorDao.aucmodify(req);
	   }

	@Override
	public Marketing mkk(int seqno) {
		return creatorDao.mkk(seqno);
	}
	
	@Override
	   public String productadd(HttpServletRequest req) {
	      return creatorDao.productadd(req);
	   }
	
	@Override
	   public Map<String, String> cremodifyreg(HttpServletRequest req) {
	      return creatorDao.cremodifyreg(req);
	   }

}











