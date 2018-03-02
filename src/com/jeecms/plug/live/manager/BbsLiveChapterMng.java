package com.jeecms.plug.live.manager;

import java.util.List;

import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveChapter;

public interface BbsLiveChapterMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsLiveChapter> getTopList(Integer userId);
	
	public List<BbsLiveChapter> getChildList(Integer userId,Integer parentId);

	public BbsLiveChapter findById(Integer id);

	public BbsLiveChapter findByPath(Integer userId,String path);

	public BbsLiveChapter save(BbsLiveChapter bean,Integer root);

	public BbsLiveChapter update(BbsLiveChapter bean,Integer root);

	public BbsLiveChapter deleteById(Integer id);
	
	public BbsLiveChapter[] deleteByIds(Integer[] ids);
	
	public String checkDelete(Integer id);

	public BbsLiveChapter[] updatePriority(Integer[] ids, Integer[] priority);
}