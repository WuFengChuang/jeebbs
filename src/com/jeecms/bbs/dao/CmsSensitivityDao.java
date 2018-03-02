package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface CmsSensitivityDao {
	public List<CmsSensitivity> getList(Integer siteId, boolean cacheable);
	
	public List<CmsSensitivity> getApiList(Integer first, Integer count, Integer siteId, Boolean cacheable);

	public CmsSensitivity findById(Integer id);
	
	public CmsSensitivity findByWord(Integer siteId,String word);

	public CmsSensitivity save(CmsSensitivity bean);

	public CmsSensitivity updateByUpdater(Updater<CmsSensitivity> updater);

	public CmsSensitivity deleteById(Integer id);
	
	public void clear(Integer siteId);

	public Pagination getPage(Integer pageNo, Integer pageSize, Integer siteId, Boolean cacheable);
}