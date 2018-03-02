package com.jeecms.bbs.manager;

import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsMagicLog;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.page.Pagination;

public interface BbsMagicLogMng {

	public Pagination getPage(Byte operator,Integer userId,int pageNo, int pageSize);

	public BbsMagicLog findById(Integer id);

	public BbsMagicLog save(BbsMagicLog bean);
	
	public void buyMagicLog(BbsCommonMagic magic, BbsUser user,
			Integer buynum, Byte Operate);

	public BbsMagicLog update(BbsMagicLog bean);

	public BbsMagicLog deleteById(Integer id);

	public BbsMagicLog[] deleteByIds(Integer[] ids);
}