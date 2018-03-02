package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsTopicTypeSubscribe;

public interface BbsTopicTypeSubscribeMng {
	public Pagination getPage(Integer userId,int pageNo, int pageSize);

	public BbsTopicTypeSubscribe findById(Integer id);
	
	public BbsTopicTypeSubscribe subscribe(Integer typeId,Integer userId,Integer operate);

	public BbsTopicTypeSubscribe save(BbsTopicTypeSubscribe bean);

	public BbsTopicTypeSubscribe update(BbsTopicTypeSubscribe bean);

	public BbsTopicTypeSubscribe deleteById(Integer id);
	
	public BbsTopicTypeSubscribe[] deleteByIds(Integer[] ids);
}