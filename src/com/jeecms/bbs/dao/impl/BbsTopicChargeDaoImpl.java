package com.jeecms.bbs.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsTopicChargeDao;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;

@Repository
public class BbsTopicChargeDaoImpl extends HibernateBaseDao<BbsTopicCharge, Integer>
		implements BbsTopicChargeDao {
	
	public List<BbsTopicCharge> getList(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy,Integer first,Integer count){
		Finder finder=getFinder(title,authorUserId,
				buyTimeBegin,buyTimeEnd,orderBy);
		if(first!=null){
			finder.setFirstResult(first);
		}
		if(count!=null){
			finder.setMaxResults(count);
		}
		return find(finder);
	}
	
	public Pagination getPage(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy,
			int pageNo,int pageSize){
		Finder finder=getFinder(title,authorUserId,
				buyTimeBegin,buyTimeEnd,orderBy);
		return find(finder, pageNo, pageSize);
	}
	
	private Finder getFinder(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy){
		String hql="select bean from BbsTopicCharge bean where 1=1 ";
		Finder finder=Finder.create(hql);
		if(StringUtils.isNotBlank(title)){
			finder.append(" and bean.topic.title like :title")
			.setParam("title", "%"+title+"%");
		}
		if(authorUserId!=null){
			if(authorUserId==0){
				//未找到用户情况下不显示任何记录
				finder.append(" and 1!=1 ");
			}else{
				finder.append(" and bean.topic.creater.id=:authorUserId")
				.setParam("authorUserId", authorUserId);
			}
		}
		if(buyTimeBegin!=null){
			finder.append(" and bean.lastBuyTime>=:buyTimeBegin")
			.setParam("buyTimeBegin", DateUtils.getStartDate(buyTimeBegin));
		}
		if(buyTimeEnd!=null){
			finder.append(" and bean.lastBuyTime<=:buyTimeEnd")
			.setParam("buyTimeEnd", DateUtils.getFinallyDate(buyTimeEnd));
		}
		if(orderBy==1){
			finder.append(" order by bean.totalAmount desc ");
		}else if(orderBy==2){
			finder.append(" order by bean.totalAmount asc ");
		}else if(orderBy==3){
			finder.append(" order by bean.yearAmount desc ");
		}else if(orderBy==4){
			finder.append(" order by bean.yearAmount asc ");
		}else if(orderBy==5){
			finder.append(" order by bean.monthAmount desc ");
		}else if(orderBy==6){
			finder.append(" order by bean.monthAmount asc ");
		}else if(orderBy==7){
			finder.append(" order by bean.dayAmount desc ");
		}else if(orderBy==8){
			finder.append(" order by bean.dayAmount asc ");
		}else if(orderBy==9){
			finder.append(" order by bean.chargeAmount desc ");
		}else if(orderBy==10){
			finder.append(" order by bean.chargeAmount asc ");
		}
		return finder;
	}
	
	public BbsTopicCharge findById(Integer id) {
		BbsTopicCharge entity = get(id);
		return entity;
	}

	public BbsTopicCharge save(BbsTopicCharge bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<BbsTopicCharge> getEntityClass() {
		return BbsTopicCharge.class;
	}
}