package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsTopicCount;

public interface BbsTopicCountDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsTopicCount findById(Integer id);

	public BbsTopicCount save(BbsTopicCount bean);

	public BbsTopicCount updateByUpdater(Updater<BbsTopicCount> updater);

	public BbsTopicCount deleteById(Integer id);
}