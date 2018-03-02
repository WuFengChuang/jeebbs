package com.jeecms.plug.live.dao;

import java.util.Date;
import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLive;

import net.sf.ehcache.Ehcache;

public interface BbsLiveDao {
	public Pagination getPage(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,
			Integer orderBy,int pageNo, int pageSize);
	
	public List<BbsLive> getList(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,
			Integer orderBy,Integer first, Integer count);
	
	public Long findLiveCount(Integer cid,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd);

	public BbsLive findById(Integer id);

	public BbsLive save(BbsLive bean);

	public BbsLive updateByUpdater(Updater<BbsLive> updater);

	public BbsLive deleteById(Integer id);
	
	public int countByChapterId(int chapterId);

	public void freshCacheToDB(Ehcache cache);
	
	public void clearLiveUserNum();
}