package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsWebserviceAuth;

public interface BbsWebserviceAuthMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public boolean isPasswordValid(String username, String password);
	
	public BbsWebserviceAuth findByUsername(String username);

	public BbsWebserviceAuth findById(Integer id);

	public BbsWebserviceAuth save(BbsWebserviceAuth bean);

	public BbsWebserviceAuth update(BbsWebserviceAuth bean);
	
	public BbsWebserviceAuth update(Integer id,String username,String password,String system,Boolean enable);

	public BbsWebserviceAuth deleteById(Integer id);
	
	public BbsWebserviceAuth[] deleteByIds(Integer[] ids);

}