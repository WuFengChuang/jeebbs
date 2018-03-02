package com.jeecms.bbs.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Ehcache;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsSession;

public interface BbsSessionMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsSession> getList(Integer userId,Integer count);

	
	public BbsSession getUserSession(Integer userId);
	
	public void recordUserSession(HttpServletRequest request,HttpServletResponse response);
	
	public Integer total(boolean member);
	
	public void freshCacheToDB(Ehcache cache);

	public BbsSession findById(Long id);
	
	public BbsSession findBySessionId(String sessionId);

	public BbsSession save(BbsSession bean);

	public BbsSession update(BbsSession bean);

	public BbsSession deleteById(Long id);
	
	public BbsSession[] deleteByIds(Long[] ids);
}