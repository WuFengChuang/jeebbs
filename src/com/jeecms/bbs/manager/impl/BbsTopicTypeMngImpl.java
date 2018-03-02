package com.jeecms.bbs.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.StrUtils;
import com.jeecms.bbs.Constants;
import com.jeecms.bbs.dao.BbsTopicTypeDao;
import com.jeecms.bbs.entity.BbsTopicType;
import com.jeecms.bbs.manager.BbsTopicTypeMng;

@Service
@Transactional
public class BbsTopicTypeMngImpl implements BbsTopicTypeMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsTopicType> getTopList(Boolean displayOnly
			,  Boolean cacheable,Integer orderBy,Integer first,Integer count){
		return dao.getTopList(displayOnly,count,cacheable,orderBy,first);
	}
	
	@Transactional(readOnly = true)
	public List<BbsTopicType> getChildList(Integer parentId, Boolean displayOnly, Boolean cacheable,Integer orderBy,Integer first,Integer count){
		return dao.getChildList(parentId, displayOnly,count, cacheable,orderBy,first);
	}

	@Transactional(readOnly = true)
	public BbsTopicType findById(Integer id) {
		BbsTopicType entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public List<BbsTopicType>getTopicTypeListFromTitle(String title){
		String tags="";
		List<BbsTopicType>list=new ArrayList<BbsTopicType>();
		if(StringUtils.isNotBlank(title)){
			tags=StrUtils.getKeywords(title, true);
			String tagArray[]=tags.split(Constants.COMMON_SPLIT);
			if(tagArray!=null&&tagArray.length>0){
				for(int i=0;i<tagArray.length;i++){
					String tag=tagArray[i];
					BbsTopicType type=findByName(tag);
					if(type!=null){
						list.add(type);
					}
				}
			}
		}
		return list;
	}
	
	@Transactional(readOnly = true)
	public BbsTopicType findByName(String name){
		return dao.findByName(name);
	}
	
	@Transactional(readOnly = true)
	public boolean typeNameExist(String name){
		BbsTopicType t=findByName(name);
		if(t!=null){
			return true;
		}else{
			return false;
		}
	}
	
	@Transactional(readOnly = true)
	public BbsTopicType findByPath(String path){
		return dao.findByPath(path);
	}

	public BbsTopicType save(BbsTopicType bean) {
		dao.save(bean);
		return bean;
	}

	public BbsTopicType update(BbsTopicType bean,Integer parentId) {
		Updater<BbsTopicType> updater = new Updater<BbsTopicType>(bean);
		BbsTopicType entity = dao.updateByUpdater(updater);
		if(parentId!=null){
			entity.setParent(findById(parentId));
		}else{
			entity.setParent(null);
		}
		return entity;
	}

	public BbsTopicType deleteById(Integer id) {
		BbsTopicType bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsTopicType[] deleteByIds(Integer[] ids) {
		BbsTopicType[] beans = new BbsTopicType[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsTopicTypeDao dao;

	@Autowired
	public void setDao(BbsTopicTypeDao dao) {
		this.dao = dao;
	}

	@Override
	public Pagination getTopPage(Boolean displayOnly, Boolean cacheable, Integer orderBy, Integer pageNo,
			Integer pageSize) {
		return dao.getTopPage(displayOnly,cacheable,orderBy,pageNo,pageSize);
	}

	@Override
	public Pagination getChildPage(Integer parentId,Boolean displayOnly, Boolean cacheable, Integer orderBy, Integer pageNo,
			Integer pageSize) {
		return dao.getChildList(parentId, displayOnly, cacheable, orderBy, pageNo,pageSize);
	}
}