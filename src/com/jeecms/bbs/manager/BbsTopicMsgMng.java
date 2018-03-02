package com.jeecms.bbs.manager;

import java.util.List;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsTopicMsg;

public interface BbsTopicMsgMng {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsTopicMsg findById(Integer id);

	public BbsTopicMsg save(BbsTopicMsg bean);

	public BbsTopicMsg update(BbsTopicMsg bean);

	public BbsTopicMsg deleteById(Integer id);
	
	public BbsTopicMsg[] deleteByIds(Integer[] ids);
	
	public List<BbsTopicMsg> getTopicList(Integer userId,Integer count) ;
}