package service;

import java.util.List;
import java.util.Map;

import dao.AucDao;
import dto.Auc;

public class AuctionServiceimp implements AuctionServeice {
	AucDao Dao = new AucDao();
	
	@Override
	public Map<String, List<Auc>> aucList() {
		return Dao.aucList();
	}


	@Override
	public Auc detailList(String seqno) {
		return Dao.detailList(seqno);
	}


	@Override
	public void aucnow(String srt, String seqno,String id) {
		Dao.aucnow(srt,seqno,id);
	}

}
