package service;

import java.util.List;

import dto.Pro;

public interface ProService {

	List<Pro> proList_clothes();

	List<Pro> proList_furn();

	List<Pro> proList_cos();

	List<Pro> proList_interior();

	List<Pro> proList_tra();

	public Pro detailList(String seqno);


}
