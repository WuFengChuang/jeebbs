package com.jeecms.bbs.manager;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsSession;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserOnline;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsConfig;

public interface BbsUserOnlineMng {

	public List<BbsUserOnline> getList();

	public Pagination getPage(int pageNo, int pageSize);

	public BbsUserOnline findById(Integer id);

	public BbsUserOnline save(BbsUserOnline bean);
	
	public BbsUserOnline saveByUser(BbsUser user);

	public BbsUserOnline update(BbsUserOnline bean);
	
	public void clearCount(CmsConfig config);
	
	public void updateUserOnlineTime(BbsSession userSession,Date clearDate);

	public BbsUserOnline deleteById(Integer id);

	public BbsUserOnline[] deleteByIds(Integer[] ids);
}