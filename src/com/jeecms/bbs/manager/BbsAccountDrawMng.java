package com.jeecms.bbs.manager;

import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;

public interface BbsAccountDrawMng {
	
	public BbsAccountDraw draw(BbsUser user,Double amount,
			String applyAccount,Short applyType);
	
	public Double getAppliedSum(Integer userId,Short applyType);
	
	public Pagination getPage(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,
			Short applyType,int pageNo, int pageSize);
	
	public List<BbsAccountDraw> getList(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,
			Short applyType,Integer first,Integer count);
	
	public Long findAccountDrawCount(Integer userId,Short applyStatus,Short applyType);

	public BbsAccountDraw findById(Integer id);

	public BbsAccountDraw save(BbsAccountDraw bean);

	public BbsAccountDraw update(BbsAccountDraw bean);

	public BbsAccountDraw deleteById(Integer id);
	
	public BbsAccountDraw[] deleteByIds(Integer[] ids);
}