package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsPostCount;

public interface BbsPostCountMng {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsPostCount findById(Integer id);

	public BbsPostCount save(BbsPostCount count, BbsPost post);
	
	public int postUp(Integer id);
	
	public int postCancelUp(Integer id);
	
	public int postReply(Integer id);

	public BbsPostCount update(BbsPostCount bean);

	public BbsPostCount deleteById(Integer id);
	
	public BbsPostCount[] deleteByIds(Integer[] ids);
}