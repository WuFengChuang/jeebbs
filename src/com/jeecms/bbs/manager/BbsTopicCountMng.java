package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCount;

public interface BbsTopicCountMng {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsTopicCount findById(Integer id);
	
	public int topicUp(Integer id);
	
	public int topicCollect(Integer id);
	
	public int topicReward(Integer id);
	
	public int topicAttent(Integer id);
	
	public int topicCancelUp(Integer id);
	
	public int topicCancelCollect(Integer id);
	
	public int topicCancelAttent(Integer id);

	public BbsTopicCount save(BbsTopicCount count, BbsTopic topic);

	public BbsTopicCount update(BbsTopicCount bean);

	public BbsTopicCount deleteById(Integer id);
	
	public BbsTopicCount[] deleteByIds(Integer[] ids);
}