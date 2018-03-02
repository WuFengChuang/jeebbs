package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.CmsSensitivityDao;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class CmsSensitivityDaoImpl extends
		HibernateBaseDao<CmsSensitivity, Integer> implements CmsSensitivityDao {
	@SuppressWarnings("unchecked")
	public List<CmsSensitivity> getList(Integer siteId, boolean cacheable) {
		String hql = "from CmsSensitivity bean where bean.site.id=:siteId order by bean.id desc";
		return getSession().createQuery(hql).setParameter("siteId", siteId).setCacheable(cacheable).list();
	}

	@Override
	public List<CmsSensitivity> getApiList(Integer first, Integer count, Integer siteId, Boolean cacheable) {
		Finder f = Finder.create("from CmsSensitivity bean");
		f.append(" where 1=1");
		if (siteId!=null) {
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		f.append(" order by bean.id desc");
		if (first!=null) {
			f.setFirstResult(first);
		}
		if (count!=null) {
			f.setMaxResults(count);
		}
		f.setCacheable(cacheable);
		return find(f);
	}
	
	public CmsSensitivity findById(Integer id) {
		CmsSensitivity entity = get(id);
		return entity;
	}
	
	public CmsSensitivity findByWord(Integer siteId,String word){
		String hql = "from CmsSensitivity bean where bean.site.id=:siteId and bean.search=:word order by bean.id desc";
		List<CmsSensitivity>list= getSession().createQuery(hql).setParameter("siteId", siteId).
				setParameter("word", word).setCacheable(true).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public CmsSensitivity save(CmsSensitivity bean) {
		getSession().save(bean);
		return bean;
	}

	public CmsSensitivity deleteById(Integer id) {
		CmsSensitivity entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	public void clear(Integer siteId){
		String hql = "delete from CmsSensitivity bean where bean.site.id=:siteId";
		getSession().createQuery(hql).setParameter("siteId", siteId).executeUpdate();
	}

	@Override
	protected Class<CmsSensitivity> getEntityClass() {
		return CmsSensitivity.class;
	}

	@Override
	public Pagination getPage(Integer pageNo, Integer pageSize, Integer siteId, Boolean cacheable) {
		Finder f = Finder.create("from CmsSensitivity bean");
		f.append(" where 1=1");
		if (siteId!=null) {
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		f.append(" order by bean.id desc");
		return find(f, pageNo, pageSize);
	}
}