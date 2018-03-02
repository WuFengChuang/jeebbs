package com.jeecms.bbs.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsUserAccountDao;
import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
@Repository
public class BbsUserAccountDaoImpl extends HibernateBaseDao<BbsUserAccount, Integer> implements BbsUserAccountDao {
	
	public Pagination getPage(String username,Date drawTimeBegin,Date drawTimeEnd,
			int orderBy,int pageNo,int pageSize){
		String hql=" select bean from BbsUserAccount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(username)){
			f.append(" and bean.user.username=:username").setParam("username", username);
		}
		if(drawTimeBegin!=null){
			f.append(" and bean.lastDrawTime>=:drawTimeBegin")
			.setParam("drawTimeBegin", DateUtils.getStartDate(drawTimeBegin));
		}
		if(drawTimeEnd!=null){
			f.append(" and bean.lastDrawTime<=:drawTimeEnd")
			.setParam("drawTimeEnd", DateUtils.getFinallyDate(drawTimeEnd));
		}
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
		}
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}
	

	@Override
	public List<BbsUserAccount> getList(String username, Date drawTimeBegin, Date drawTimeEnd, Integer orderBy,
			Integer first, Integer count) {
		String hql=" select bean from BbsUserAccount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(username)){
			f.append(" and bean.user.username=:username").setParam("username", username);
		}
		if(drawTimeBegin!=null){
			f.append(" and bean.lastDrawTime>=:drawTimeBegin")
			.setParam("drawTimeBegin", DateUtils.getStartDate(drawTimeBegin));
		}
		if(drawTimeEnd!=null){
			f.append(" and bean.lastDrawTime<=:drawTimeEnd")
			.setParam("drawTimeEnd", DateUtils.getFinallyDate(drawTimeEnd));
		}
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
	
	public BbsUserAccount findById(Integer id) {
		BbsUserAccount entity = get(id);
		return entity;
	}

	public BbsUserAccount save(BbsUserAccount bean) {
		getSession().save(bean);
		return bean;
	}
	
	@Override
	protected Class<BbsUserAccount> getEntityClass() {
		return BbsUserAccount.class;
	}

}