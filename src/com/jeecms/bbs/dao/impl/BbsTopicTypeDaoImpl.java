package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsTopicTypeDao;
import com.jeecms.bbs.entity.BbsTopicType;

@Repository
public class BbsTopicTypeDaoImpl extends HibernateBaseDao<BbsTopicType, Integer> implements BbsTopicTypeDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public Pagination getTopPage(Boolean displayOnly, Boolean cacheable, Integer orderBy, Integer pageNo,
			Integer pageSize) {
		Finder finder = createFinder(null, displayOnly, cacheable, orderBy);
		return find(finder, pageNo, pageSize);
	}

	
	
	public List<BbsTopicType> getTopList(Boolean displayOnly,
			Integer count, Boolean cacheable,Integer orderBy,Integer first){
		Finder finder=createFinder(null, displayOnly,cacheable,orderBy);
		if(count!=null){
			finder.setMaxResults(count);
		}
		if (first!=null) {
			finder.setFirstResult(first);
		}
		return find(finder);
	}
	
	public List<BbsTopicType> getChildList(Integer parentId, Boolean displayOnly,
			Integer count,Boolean cacheable,Integer orderBy,Integer first){
		Finder finder=createFinder(parentId, displayOnly,cacheable,orderBy);
		if(count!=null){
			finder.setMaxResults(count);
		}
		if (first!=null) {
			finder.setFirstResult(first);
		}
		return find(finder);
	}
	
	public Pagination getChildList(Integer parentId, Boolean displayOnly, Boolean cacheable, Integer orderBy,
			Integer pageNo, Integer pageSize) {
		Finder finder = createFinder(parentId, displayOnly, cacheable, orderBy);
		return find(finder, pageNo, pageSize);
	}
	
	private Finder createFinder(Integer parentId,Boolean displayOnly
			 , Boolean cacheable,Integer orderBy) {
		Finder f = Finder.create("from BbsTopicType bean ");
		if(parentId!=null){
			f.append(" where bean.parent.id=:parentId");
			f.setParam("parentId", parentId);
		}else{
			f.append(" where bean.parent.id is null ");
		}
		
		if (displayOnly) {
			f.append(" and bean.display=true");
		}
		if(orderBy==1){
			f.append(" order by bean.priority desc ");
		}else if(orderBy==2){
			f.append(" order by bean.priority asc ");
		}else if(orderBy==3){
			f.append(" order by bean.topicCount desc ");
		}else if(orderBy==4){
			f.append(" order by bean.topicCount asc ");
		}else if(orderBy==5){
			f.append(" order by bean.topicEssenceCount desc ");
		}else if(orderBy==6){
			f.append(" order by bean.topicEssenceCount asc ");
		}else if(orderBy==7){
			f.append(" order by bean.subscribeCount desc ");
		}else if(orderBy==8){
			f.append(" order by bean.subscribeCount asc ");
		}
		
		f.setCacheable(cacheable);
		return f;
	}

	public BbsTopicType findById(Integer id) {
		BbsTopicType entity = get(id);
		return entity;
	}
	
	public BbsTopicType findByName(String name){
		Finder f = Finder.create("from BbsTopicType bean  where bean.name=:name")
				.setParam("name", name);
		List<BbsTopicType>list=find(f);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public BbsTopicType findByPath(String path){
		Finder f = Finder.create("from BbsTopicType bean  where bean.path=:path")
				.setParam("path", path);
		List<BbsTopicType>list=find(f);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public BbsTopicType save(BbsTopicType bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsTopicType deleteById(Integer id) {
		BbsTopicType entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsTopicType> getEntityClass() {
		return BbsTopicType.class;
	}

}