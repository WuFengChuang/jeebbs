package com.jeecms.bbs.manager.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.CmsSensitivityDao;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.manager.CmsSiteMng;

@Service
@Transactional
public class CmsSensitivityMngImpl implements CmsSensitivityMng {
	@Transactional(readOnly = true)
	public String replaceSensitivity(Integer siteId, String s) {
		if (StringUtils.isBlank(s)) {
			return s;
		}
		List<CmsSensitivity> list = getList(siteId, true);
		for (CmsSensitivity sen : list) {
			s = StringUtils.replace(s, sen.getSearch(), sen.getReplacement());
		}
		return s;
	}
	
	@Transactional(readOnly = true)
	public boolean txtHasSensitivity(Integer siteId, String txt){
		if (StringUtils.isBlank(txt)) {
			return false;
		}
		List<CmsSensitivity> list = getList(siteId, true);
		boolean hasSen=false;
		for (CmsSensitivity sen : list) {
			if(txt.contains(sen.getSearch())){
				hasSen=true;
				break;
			}
		}
		return hasSen;
	}

	@Transactional(readOnly = true)
	public List<CmsSensitivity> getList(Integer siteId, boolean cacheable) {
		return dao.getList(siteId, cacheable);
	}
	
	@Override
	public List<CmsSensitivity> getApiList(Integer first, Integer count, Integer siteId, Boolean cacheable) {
		return dao.getApiList(first, count, siteId, cacheable);
	}
	
	

	@Transactional(readOnly = true)
	public CmsSensitivity findById(Integer id) {
		CmsSensitivity entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public CmsSensitivity findByWord(Integer siteId,String word){
		return dao.findByWord(siteId,word);
	}
	
	public void batchSave(List<String>wordList,Integer siteId,Integer type){
		if(wordList.size()>0){
			if(type.equals(CmsSensitivity.TYPE_UPDATE)){
				//不替换已经存在的敏感词
				for(String w:wordList){
					String []sp=w.split("=");
					if(sp!=null&&sp.length==2){
						CmsSensitivity dbSen=findByWord(siteId, sp[0]);
						if(dbSen==null){
							CmsSensitivity s=new CmsSensitivity();
							s.setReplacement(sp[1]);
							s.setSearch(sp[0]);
							save(s, siteId);
						}
					}
				}
			}else if(type.equals(CmsSensitivity.TYPE_REPLACE)){
				//替换全部敏感词
				for(String w:wordList){
					String []sp=w.split("=");
					if(sp!=null&&sp.length==2){
						CmsSensitivity dbSen=findByWord(siteId, sp[0]);
						if(dbSen==null){
							CmsSensitivity s=new CmsSensitivity();
							s.setReplacement(sp[1]);
							s.setSearch(sp[0]);
							save(s, siteId);
						}else{
							dbSen.setReplacement(sp[1]);
							update(dbSen);
						}
					}
				}
			}else if(type.equals(CmsSensitivity.TYPE_CLEARSAVE)){
				//清空当前敏感词并导入新敏感词
				clear(siteId);
				for(String w:wordList){
					String []sp=w.split("=");
					if(sp!=null&&sp.length==2){
						CmsSensitivity s=new CmsSensitivity();
						s.setReplacement(sp[1]);
						s.setSearch(sp[0]);
						save(s, siteId);
					}
				}
			}
		}
	}

	public CmsSensitivity save(CmsSensitivity bean, Integer siteId) {
		bean.setSite(cmsSiteMng.findById(siteId));
		dao.save(bean);
		return bean;
	}
	
	public CmsSensitivity update(CmsSensitivity bean) {
		Updater<CmsSensitivity> updater = new Updater<CmsSensitivity>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public void updateEnsitivity(Integer[] ids, String[] searchs,
			String[] replacements) {
		CmsSensitivity ensitivity;
		for (int i = 0, len = ids.length; i < len; i++) {
			ensitivity = findById(ids[i]);
			ensitivity.setSearch(searchs[i]);
			ensitivity.setReplacement(replacements[i]);
		}
	}

	public CmsSensitivity deleteById(Integer id) {
		CmsSensitivity bean = dao.deleteById(id);
		return bean;
	}

	public CmsSensitivity[] deleteByIds(Integer[] ids) {
		CmsSensitivity[] beans = new CmsSensitivity[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public void clear(Integer siteId){
		dao.clear(siteId);
	}

	private CmsSensitivityDao dao;
	
	private CmsSiteMng cmsSiteMng;

	@Autowired
	public void setDao(CmsSensitivityDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}

	@Override
	public Pagination getPage(Integer pageNo, Integer pageSize, Integer siteId, Boolean cacheable) {
		return dao.getPage(pageNo, pageSize, siteId, cacheable);
	}
}