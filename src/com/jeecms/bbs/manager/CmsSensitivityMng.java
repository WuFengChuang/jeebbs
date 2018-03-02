package com.jeecms.bbs.manager;

import java.util.List;

import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.common.page.Pagination;

public interface CmsSensitivityMng {

	public String replaceSensitivity(Integer siteId, String s);
	
	public boolean txtHasSensitivity(Integer siteId, String txt);

	public List<CmsSensitivity> getList(Integer siteId, boolean cacheable);
	
	public List<CmsSensitivity> getApiList(Integer first,Integer count,Integer siteId,Boolean cacheable);

	public Pagination getPage(Integer pageNo,Integer pageSize,Integer siteId,Boolean cacheable);
	
	public CmsSensitivity findById(Integer id);
	
	public CmsSensitivity findByWord(Integer siteId,String word);
	
	public void batchSave(List<String>wordList,Integer siteId,Integer type);

	public CmsSensitivity save(CmsSensitivity bean, Integer siteId);
	
	public CmsSensitivity update(CmsSensitivity bean);

	public void updateEnsitivity(Integer[] ids, String[] searchs,
			String[] replacements);

	public CmsSensitivity deleteById(Integer id);

	public CmsSensitivity[] deleteByIds(Integer[] ids);
	
	public void clear(Integer siteId);
}