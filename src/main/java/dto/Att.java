package dto;

import java.io.Serializable;
import java.util.Date;

public class Att implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer attSeqno;
	private String savefilename;
	private String attType;
	private String attName;
	private String attPath;
	private String attSize;
	private String attThumb;
	private Date attSavedate;
	private Item item;
	private Marketing marketing;
	private Artdetail artdetail;
	private Mem mem;

	public Att() {
	}

	
	
	public String getAttSize() {
		return attSize;
	}



	public void setAttSize(String attSize) {
		this.attSize = attSize;
	}



	public String getAttThumb() {
		return attThumb;
	}



	public void setAttThumb(String attThumb) {
		this.attThumb = attThumb;
	}



	public void setAttSeqno(Integer attSeqno) {
		this.attSeqno = attSeqno;
	}

	public Integer getAttSeqno() {
		return this.attSeqno;
	}

	public void savefilename(String savefilename) {
		this.savefilename = savefilename;
	}

	public String savefilename() {
		return this.savefilename;
	}

	public void setAttType(String attType) {
		this.attType = attType;
	}

	public String getAttType() {
		return this.attType;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public String getAttName() {
		return this.attName;
	}

	public void setAttPath(String attPath) {
		this.attPath = attPath;
	}

	public String getAttPath() {
		return this.attPath;
	}

	public void setAttSavedate(Date attSavedate) {
		this.attSavedate = attSavedate;
	}

	public Date getAttSavedate() {
		return this.attSavedate;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return this.item;
	}

	public void setMarketing(Marketing marketing) {
		this.marketing = marketing;
	}

	public Marketing getMarketing() {
		return this.marketing;
	}

	public void setArtdetail(Artdetail artdetail) {
		this.artdetail = artdetail;
	}

	public Artdetail getArtdetail() {
		return this.artdetail;
	}

	public void setMem(Mem mem) {
		this.mem = mem;
	}

	public Mem getMem() {
		return this.mem;
	}

}
