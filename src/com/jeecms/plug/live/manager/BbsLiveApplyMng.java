package com.jeecms.plug.live.manager;

import java.util.List;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveApply;

public interface BbsLiveApplyMng {
	public Pagination getPage(String mobile,Short status,
			Integer applyUserId,int pageNo, int pageSize);
	
	public List<BbsLiveApply> getList(String mobile,Short status,
			Integer applyUserId,Integer first, Integer count);
	
	public Long findLiveApplyCount(Short status,Integer applyUserId);
	
	public boolean haveApplied(Integer applyUserId);

	public BbsLiveApply findById(Integer id);

	public BbsLiveApply save(BbsLiveApply bean,String[] picPaths, String[] picDescs);

	public BbsLiveApply update(BbsLiveApply bean);
	
	public BbsLiveApply check(Integer id,BbsUser checkUser);
	
	public BbsLiveApply[] check(Integer[] ids,BbsUser checkUser);
	
	public BbsLiveApply cancel(Integer id,BbsUser checkUser);
	
	public BbsLiveApply[] cancel(Integer[] ids,BbsUser checkUser);
	
	public BbsLiveApply update(BbsLiveApply bean,String[] picPaths, String[] picDescs);

	public BbsLiveApply deleteById(Integer id);
	
	public BbsLiveApply[] deleteByIds(Integer[] ids);
}