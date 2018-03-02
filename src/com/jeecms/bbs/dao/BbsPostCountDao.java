package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsPostCount;

public interface BbsPostCountDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsPostCount findById(Integer id);

	public BbsPostCount save(BbsPostCount bean);

	public BbsPostCount updateByUpdater(Updater<BbsPostCount> updater);

	public BbsPostCount deleteById(Integer id);
}