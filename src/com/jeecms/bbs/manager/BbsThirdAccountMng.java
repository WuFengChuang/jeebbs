package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsThirdAccount;

public interface BbsThirdAccountMng {
	public Pagination getPage(String username,String source,int pageNo, int pageSize);

	public List<BbsThirdAccount> getList(String username,String source,Integer first,Integer count);
	
	public BbsThirdAccount findById(Long id);
	
	public BbsThirdAccount findByKey(String key);

	public BbsThirdAccount save(BbsThirdAccount bean);

	public BbsThirdAccount update(BbsThirdAccount bean);

	public BbsThirdAccount deleteById(Long id);
	
	public BbsThirdAccount[] deleteByIds(Long[] ids);
}