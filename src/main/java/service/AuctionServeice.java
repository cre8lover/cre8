package service;

import java.util.List;
import java.util.Map;

import dto.Auc;

public interface AuctionServeice {

	public Map<String, List<Auc>> aucList();

	public Auc detailList(String seqno);

	public void aucnow(String srt, String seqno, String id);
	
	
}
