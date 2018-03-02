package com.jeecms.bbs.manager;

import java.util.List;

import com.jeecms.bbs.entity.BbsPlug;
import com.jeecms.common.page.Pagination;

public interface BbsPlugMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsPlug> getList(String author,Boolean used);

	public BbsPlug findById(Integer id);
	
	public BbsPlug findByPath(String plugPath);

	public BbsPlug save(BbsPlug bean);

	public BbsPlug update(BbsPlug bean);

	public BbsPlug deleteById(Integer id);
	
	public BbsPlug[] deleteByIds(Integer[] ids);
}