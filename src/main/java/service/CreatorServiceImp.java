package service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.CreatorDao;
import dto.Att;
import dto.Auc;
import dto.Creator;
import dto.Item;
import dto.Marketing;
import dto.Mem;
import dto.Pro;

public class CreatorServiceImp implements CreatorService{
	
	
//	private static final String CHARSET = "utf-8";
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
		
		DiskFileItemFactory factory = new DiskFileItemFactory(); 
//		factory.setDefaultCharset("CHARSET");//상수로 선언하는게 좋다.
		//factory form의 데이터를 가져와서 저장 utf8로 저장하는게 좋음
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		
		
		
		Pro pro = new Pro();
		Item proitem = new Item();
		Att attachfile = null;
		FileService fileService = new FileServiceImp();
		
		try {
			List<FileItem> items = upload.parseRequest(req);
			//멀티파트 확인법
			for(FileItem item : items) {
				if (item.isFormField()) {//2진데이터인지 텍스트인지 구별해줌
					pro =  fileService.getFormParameter(item,pro,proitem); 
				}else {
					attachfile = fileService.fileUpload(item);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pro.setAtt_file(attachfile);
	    String id = (String)req.getSession().getAttribute("sess_id");
	    
	    
	      return creatorDao.productadd(pro,id);
	   }
	
	@Override
	   public Map<String, String> cremodifyreg(HttpServletRequest req) {
	      return creatorDao.cremodifyreg(req);
	   }

}











