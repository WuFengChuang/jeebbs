package com.jeecms.bbs.dao;

import java.util.List;

import net.sf.ehcache.Ehcache;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsSession;

public interface BbsSessionDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsSession> getList(Integer userId,Integer count);
	
	public Integer total(boolean member);
	
	public void freshCacheToDB(Ehcache cache);

	public BbsSession findById(Long id);
	
	public BbsSession findBySessionId(String sessionId);

	public BbsSession save(BbsSession bean);

	public BbsSession updateByUpdater(Updater<BbsSession> updater);

	public BbsSession deleteById(Long id);
}