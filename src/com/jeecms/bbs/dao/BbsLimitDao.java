package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsLimit;

public interface BbsLimitDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsLimit findById(Integer id);
	
	public List<BbsLimit> getList(String ip,Integer userId);

	public BbsLimit save(BbsLimit bean);

	public BbsLimit updateByUpdater(Updater<BbsLimit> updater);

	public BbsLimit deleteById(Integer id);
}