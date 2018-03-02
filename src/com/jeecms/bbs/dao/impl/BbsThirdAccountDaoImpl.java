package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsThirdAccountDao;
import com.jeecms.bbs.entity.BbsThirdAccount;

@Repository
public class BbsThirdAccountDaoImpl extends HibernateBaseDao<BbsThirdAccount, Long> implements BbsThirdAccountDao {
	public Pagination getPage(String username,String source,int pageNo, int pageSize) {
		String hql="from BbsThirdAccount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(username)){
			f.append(" and bean.username like :username").setParam("username", "%"+username+"%");
		}
		if(StringUtils.isNotBlank(source)){
			f.append(" and bean.source=:source").setParam("source", source);
		}
		return find(f, pageNo, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BbsThirdAccount> getList(String username, String source, Integer first, Integer count) {
		String hql="from BbsThirdAccount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(username)){
			f.append(" and bean.username like :username").setParam("username", "%"+username+"%");
		}
		if(StringUtils.isNotBlank(source)){
			f.append(" and bean.source=:source").setParam("source", source);
		}
		if (first!=null) {
			f.setFirstResult(first);
		}
		if (count!=null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	public BbsThirdAccount findById(Long id) {
		BbsThirdAccount entity = get(id);
		return entity;
	}
	
	public BbsThirdAccount findByKey(String key){
		String hql="from BbsThirdAccount bean where bean.accountKey=:accountKey";
		Finder f=Finder.create(hql).setParam("accountKey", key);
		List<BbsThirdAccount>li= find(f);
		if(li.size()>0){
			return li.get(0);
		}else{
			return null;
		}
	}

	public BbsThirdAccount save(BbsThirdAccount bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsThirdAccount deleteById(Long id) {
		BbsThirdAccount entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsThirdAccount> getEntityClass() {
		return BbsThirdAccount.class;
	}
}