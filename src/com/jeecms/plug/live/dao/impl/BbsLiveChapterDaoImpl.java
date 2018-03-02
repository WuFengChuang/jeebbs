package com.jeecms.plug.live.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveChapterDao;
import com.jeecms.plug.live.entity.BbsLiveChapter;

@Repository
public class BbsLiveChapterDaoImpl extends HibernateBaseDao<BbsLiveChapter, Integer> implements BbsLiveChapterDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public List<BbsLiveChapter> getTopList(Integer userId, boolean cacheable){
		Finder f = Finder.create("from BbsLiveChapter bean");
		f.append(" where bean.user.id=:userId and bean.parent.id is null");
		f.setParam("userId", userId);
		f.append(" order by bean.priority asc,bean.id asc");
		f.setCacheable(cacheable);
		return find(f);
	}

	public List<BbsLiveChapter> getChildList(Integer userId,Integer parentId, boolean cacheable){
		Finder f = Finder.create("from BbsLiveChapter bean");
		f.append(" where bean.user.id=:userId and bean.parent.id=:parentId");
		f.setParam("userId", userId).setParam("parentId", parentId);
		f.append(" order by bean.priority asc,bean.id asc");
		f.setCacheable(cacheable);
		return find(f);
	}


	public BbsLiveChapter findById(Integer id) {
		BbsLiveChapter entity = get(id);
		return entity;
	}
	
	public BbsLiveChapter findByPath(Integer userId,String path,boolean cacheable){
		String hql = "from BbsLiveChapter bean where bean.path=? and bean.user.id=?";
		Query query = getSession().createQuery(hql);
		query.setParameter(0, path).setParameter(1, userId);
		// 做一些容错处理，因为毕竟没有在数据库中限定path是唯一的。
		query.setMaxResults(1);
		return (BbsLiveChapter) query.setCacheable(cacheable).uniqueResult();
	}

	public BbsLiveChapter save(BbsLiveChapter bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLiveChapter deleteById(Integer id) {
		BbsLiveChapter entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsLiveChapter> getEntityClass() {
		return BbsLiveChapter.class;
	}
}