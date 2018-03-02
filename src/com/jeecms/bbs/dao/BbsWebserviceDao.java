package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsWebservice;

public interface BbsWebserviceDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsWebservice> getList(String type);

	public BbsWebservice findById(Integer id);

	public BbsWebservice save(BbsWebservice bean);

	public BbsWebservice updateByUpdater(Updater<BbsWebservice> updater);

	public BbsWebservice deleteById(Integer id);
}