package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateFormatUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.BbsConfigAttr;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.bbs.cache.BbsUserCache;
import com.jeecms.bbs.dao.BbsSessionDao;
import com.jeecms.bbs.entity.BbsSession;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsSessionMng;
import com.jeecms.bbs.manager.BbsUserOnlineMng;
import com.jeecms.bbs.web.CmsUtils;

@Service
@Transactional
public class BbsSessionMngImpl implements BbsSessionMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsSession> getList(Integer userId,Integer count){
		return dao.getList(userId,count);
	}
	
	@Transactional(readOnly = true)
	public BbsSession getUserSession(Integer userId){
		List<BbsSession> list=getList(userId, 1);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public void freshCacheToDB(Ehcache cache){
		dao.freshCacheToDB(cache);
	}
	
	public void recordUserSession(HttpServletRequest request,HttpServletResponse response){
		BbsUser user=CmsUtils.getUser(request);
		Date now=Calendar.getInstance().getTime();
		int userOnlineKeepMinute=configMng.get().getKeepMinute();
		String userSessionId=session.getSessionId(request, response);
		BbsSession session=findBySessionId(userSessionId);
		CmsConfig config=CmsUtils.getSite(request).getConfig();
		Date clearDate=config.getCountClearTime();
		if(session==null){
			session=new BbsSession();
			session.setIp(RequestUtils.getIpAddr(request));
			session.setSessionId(userSessionId);
			session.setUser(user);
			session.setFirstActivetime(now);
			session.setLastActivetime(now);
			save(session);
		}else{
			//还在保持时间范围内
			if(user!=null&&session.getUser()==null){
				session.setUser(user);
				update(session);
			}
			bbsUserCache.view(session.getId(),now);
		}
		processExistSessions(userOnlineKeepMinute,clearDate);
	}
	
	
	private void processExistSessions(int userOnlineKeepMinute,Date clearDate){
		long time = System.currentTimeMillis();
		if (time > processTime + interval) {
			processTime = time;
			Date now=Calendar.getInstance().getTime();
			//更新最后活动时间
			bbsUserCache.refreshToDB();
			List<BbsSession>userSessions=getList(null,Integer.MAX_VALUE);
			CmsConfig config=configMng.get();
			bbsUserOnlineMng.clearCount(config);
			Integer topNum=config.getUserOnlineTopNum();
			int sessionTotal=userSessions.size();
			if(sessionTotal>topNum){
				BbsConfigAttr configAttr=new BbsConfigAttr();
				configAttr.setUserOnlineTopDay(DateFormatUtils.formatDate(now));
				configAttr.setUserOnlineTopNum(String.valueOf(sessionTotal));
				configMng.updateConfigAttr(configAttr);
			}
			for(BbsSession userSession:userSessions){
				bbsUserOnlineMng.updateUserOnlineTime(userSession,clearDate);
				//超出在线保持时间
				if(time-userSession.getLastActivetime().getTime()>userOnlineKeepMinute*60*1000){
					//更新在线时长数据
					deleteById(userSession.getId());
				}
			}
			bbsUserCache.removeCache();
		}
	}
	
	
	@Transactional(readOnly = true)
	public Integer total(boolean member){
		return dao.total(member);
	}

	@Transactional(readOnly = true)
	public BbsSession findById(Long id) {
		BbsSession entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsSession findBySessionId(String sessionId){
		return dao.findBySessionId(sessionId);
	}

	public BbsSession save(BbsSession bean) {
		dao.save(bean);
		return bean;
	}

	public BbsSession update(BbsSession bean) {
		Updater<BbsSession> updater = new Updater<BbsSession>(bean);
		BbsSession entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsSession deleteById(Long id) {
		BbsSession bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsSession[] deleteByIds(Long[] ids) {
		BbsSession[] beans = new BbsSession[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsSessionDao dao;
	@Autowired
	private CmsConfigMng configMng;
	@Autowired
	private BbsUserOnlineMng bbsUserOnlineMng;
	@Autowired
	private BbsUserCache bbsUserCache;
	@Autowired
	private SessionProvider session;

	// 间隔时间
	private int interval = 1 * 60 * 1000*5; // 1分钟
	// 最后处理时间
	private long processTime = System.currentTimeMillis();
		
	@Autowired
	public void setDao(BbsSessionDao dao) {
		this.dao = dao;
	}
}