package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.bbs.entity.BbsPlug;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsPlugDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsPlug> getList(String author,Boolean used);

	public BbsPlug findById(Integer id);
	
	public BbsPlug findByPath(String plugPath);

	public BbsPlug save(BbsPlug bean);

	public BbsPlug updateByUpdater(Updater<BbsPlug> updater);

	public BbsPlug deleteById(Integer id);
}