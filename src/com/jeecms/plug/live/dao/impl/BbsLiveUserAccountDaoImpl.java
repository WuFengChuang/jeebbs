package com.jeecms.plug.live.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveUserAccountDao;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;

@Repository
public class BbsLiveUserAccountDaoImpl extends HibernateBaseDao<BbsLiveUserAccount, Integer> implements BbsLiveUserAccountDao {
	public Pagination getPage(Integer userId,
			Integer orderBy,int pageNo, int pageSize) {
		Finder f=createFinder(userId,orderBy);
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}
	
	public List<BbsLiveUserAccount> getList(Integer userId,
			Integer orderBy,int  count) {
		Finder f=createFinder(userId,orderBy);
		f.setMaxResults(count);
		f.setCacheable(true);
		return find(f);
	}

	public BbsLiveUserAccount findById(Integer id) {
		BbsLiveUserAccount entity = get(id);
		return entity;
	}

	public BbsLiveUserAccount save(BbsLiveUserAccount bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLiveUserAccount deleteById(Integer id) {
		BbsLiveUserAccount entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	private Finder createFinder(Integer userId,Integer orderBy){
		String hql="select bean from BbsLiveUserAccount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(userId!=null){
			f.append(" and bean.user.id=:userId").setParam("userId", userId);
		}
		if(orderBy!=null){
			if(orderBy==1){
				f.append(" order by bean.totalAmount desc ");
			}else if(orderBy==2){
				f.append(" order by bean.totalAmount asc ");
			}else if(orderBy==3){
				f.append(" order by bean.yearAmount desc ");
			}else if(orderBy==4){
				f.append(" order by bean.yearAmount asc ");
			}else if(orderBy==5){
				f.append(" order by bean.monthAmount desc ");
			}else if(orderBy==6){
				f.append(" order by bean.monthAmount asc ");
			}else if(orderBy==7){
				f.append(" order by bean.dayAmount desc ");
			}else if(orderBy==8){
				f.append(" order by bean.dayAmount asc ");
			}else if(orderBy==9){
				f.append(" order by bean.buyCount desc ");
			}else if(orderBy==10){
				f.append(" order by bean.buyCount asc ");
			}else if(orderBy==11){
				f.append(" order by bean.noPayAmount desc ");
			}else if(orderBy==12){
				f.append(" order by bean.noPayAmount asc ");
			}else if(orderBy==13){
				f.append(" order by bean.ticketNum desc ");
			}else if(orderBy==14){
				f.append(" order by bean.ticketNum asc ");
			}else if(orderBy==15){
				f.append(" order by bean.giftNum desc ");
			}else if(orderBy==16){
				f.append(" order by bean.giftNum asc ");
			}else if(orderBy==17){
				f.append(" order by bean.topPriority desc ");
			}else if(orderBy==18){
				f.append(" order by bean.topPriority asc ");
			}
		}else{
			f.append(" order by bean.topPriority desc ");
		}
		return f;
	}
	
	@Override
	protected Class<BbsLiveUserAccount> getEntityClass() {
		return BbsLiveUserAccount.class;
	}
}