package com.jeecms.bbs.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.bbs.dao.BbsAccountPayDao;
import com.jeecms.bbs.entity.BbsAccountPay;

@Repository
public class BbsAccountPayDaoImpl extends HibernateBaseDao<BbsAccountPay, Long> 
	implements BbsAccountPayDao {
	
	public Pagination getPage(String drawNum,Integer payUserId,Integer drawUserId,
			Date payTimeBegin,Date payTimeEnd,int pageNo, int pageSize) {
		String hql="from BbsAccountPay bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(drawNum)){
			f.append(" and bean.drawNum=:drawNum").setParam("drawNum", drawNum);
		}
		if(payUserId!=null){
			if(payUserId==0){
				f.append(" and 1!=1");
			}else{
				f.append(" and bean.payUser.id=:payUserId")
				.setParam("payUserId", payUserId);
			}
		}
		if(drawUserId!=null){
			if(drawUserId==0){
				f.append(" and 1!=1");
			}else{
				f.append(" and bean.drawUser.id=:drawUserId")
				.setParam("drawUserId", drawUserId);
			}
		}
		if(payTimeBegin!=null){
			f.append(" and bean.payTime>=:payTimeBegin")
			.setParam("payTimeBegin", DateUtils.getStartDate(payTimeBegin));
		}
		if(payTimeEnd!=null){
			f.append(" and bean.payTime<=:payTimeEnd")
			.setParam("payTimeEnd", DateUtils.getFinallyDate(payTimeEnd));
		}
		return find(f, pageNo, pageSize);
	}
	
	@Override
	public List<BbsAccountPay> getList(String drawNum, Integer payUserId, Integer drawUserId, Date payTimeBegin,
			Date payTimeEnd, Integer first, Integer count) {
		String hql="from BbsAccountPay bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(drawNum)){
			f.append(" and bean.drawNum=:drawNum").setParam("drawNum", drawNum);
		}
		if(payUserId!=null){
			if(payUserId==0){
				f.append(" and 1!=1");
			}else{
				f.append(" and bean.payUser.id=:payUserId")
				.setParam("payUserId", payUserId);
			}
		}
		if(drawUserId!=null){
			if(drawUserId==0){
				f.append(" and 1!=1");
			}else{
				f.append(" and bean.drawUser.id=:drawUserId")
				.setParam("drawUserId", drawUserId);
			}
		}
		if(payTimeBegin!=null){
			f.append(" and bean.payTime>=:payTimeBegin")
			.setParam("payTimeBegin", DateUtils.getStartDate(payTimeBegin));
		}
		if(payTimeEnd!=null){
			f.append(" and bean.payTime<=:payTimeEnd")
			.setParam("payTimeEnd", DateUtils.getFinallyDate(payTimeEnd));
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

	public BbsAccountPay findById(Long id) {
		BbsAccountPay entity = get(id);
		return entity;
	}

	public BbsAccountPay save(BbsAccountPay bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsAccountPay deleteById(Long id) {
		BbsAccountPay entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsAccountPay> getEntityClass() {
		return BbsAccountPay.class;
	}

}