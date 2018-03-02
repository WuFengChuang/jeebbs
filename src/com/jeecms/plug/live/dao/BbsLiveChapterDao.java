package com.jeecms.plug.live.dao;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveChapter;

public interface BbsLiveChapterDao {
	public Pagination getPage(int pageNo, int pageSize);

	public List<BbsLiveChapter> getTopList(Integer userId, boolean cacheable);

	public List<BbsLiveChapter> getChildList(Integer userId,Integer parentId, boolean cacheable);

	public BbsLiveChapter findById(Integer id);
	
	public BbsLiveChapter findByPath(Integer userId,String path,boolean cacheable);

	public BbsLiveChapter save(BbsLiveChapter bean);

	public BbsLiveChapter updateByUpdater(Updater<BbsLiveChapter> updater);

	public BbsLiveChapter deleteById(Integer id);
}