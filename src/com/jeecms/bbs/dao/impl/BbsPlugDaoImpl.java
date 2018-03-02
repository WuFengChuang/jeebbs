package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsPlugDao;
import com.jeecms.bbs.entity.BbsPlug;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsPlugDaoImpl extends HibernateBaseDao<BbsPlug, Integer> implements BbsPlugDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsPlug> getList(String author,Boolean used){
		Finder f=Finder.create("from BbsPlug plug where 1=1 ");
		if(StringUtils.isNotBlank(author)){
			f.append("and plug.author=:author").setParam("author", author);
		}
		if(used!=null){
			if(used){
				f.append("and plug.used=true");
			}else{
				f.append("and plug.used=false");
			}
		}
		return find(f);
	}

	public BbsPlug findById(Integer id) {
		BbsPlug entity = get(id);
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public BbsPlug findByPath(String plugPath){
		Finder f=Finder.create("from BbsPlug plug where plug.path=:path").setParam("path", plugPath);
		List<BbsPlug>list=find(f);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public BbsPlug save(BbsPlug bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsPlug deleteById(Integer id) {
		BbsPlug entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsPlug> getEntityClass() {
		return BbsPlug.class;
	}
}