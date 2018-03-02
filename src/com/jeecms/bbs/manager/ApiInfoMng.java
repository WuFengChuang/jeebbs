package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.ApiInfo;

public interface ApiInfoMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public ApiInfo findByUrl(String url);

	public ApiInfo findById(Integer id);

	public ApiInfo save(ApiInfo bean);

	public ApiInfo update(ApiInfo bean);

	public ApiInfo deleteById(Integer id);
	
	public ApiInfo[] deleteByIds(Integer[] ids);
}