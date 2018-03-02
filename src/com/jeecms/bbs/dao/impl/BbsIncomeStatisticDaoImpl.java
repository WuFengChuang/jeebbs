package com.jeecms.bbs.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.bbs.dao.BbsIncomeStatisticDao;
import com.jeecms.bbs.entity.BbsIncomeStatistic;

@Repository
public class BbsIncomeStatisticDaoImpl extends HibernateBaseDao<BbsIncomeStatistic, Integer> implements BbsIncomeStatisticDao {
	public Pagination getPage(Date begin,Date end,int pageNo, int pageSize) {
		String hql=" from  BbsIncomeStatistic bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(begin!=null){
			f.append(" and bean.incomeDate>=:begin").setParam("begin", begin);
		}
		if(end!=null){
			f.append(" and bean.incomeDate<=:end").setParam("end", end);
		}
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}
	
	public List<BbsIncomeStatistic> getList(Date begin,Date end,Integer first,Integer count){
		String hql=" from  BbsIncomeStatistic bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(begin!=null){
			f.append(" and bean.incomeDate>=:begin").setParam("begin", begin);
		}
		if(end!=null){
			f.append(" and bean.incomeDate<=:end").setParam("end", end);
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
	
	public List<Object[]> findIncomeStatisticCount(Date begin,Date end,int groupBy){
		String hql=" select sum(bean.totalIncomeAmount),sum(bean.adIncomeAmount)"
				+ ",sum(bean.giftIncomeAmount),sum(bean.magicIncomeAmount)"
				+ ",sum(bean.liveIncomeAmount),sum(bean.postIncomeAmount)";
		Finder f=Finder.create(hql);
		//1月统计  2今年统计   3年度统计
		if(groupBy==1||groupBy==0){
			f.append(" ,day(bean.incomeDate) ");
		}else if(groupBy==2){
			f.append(" ,month(bean.incomeDate) ");
		}else if(groupBy==3){
			f.append(" ,year(bean.incomeDate) ");
		}
		f.append("  from  BbsIncomeStatistic bean where 1=1 ");
		//3年度统计则查询所有
		if(begin!=null&&groupBy!=3){
			f.append(" and bean.incomeDate>=:begin").setParam("begin", begin);
		}
		if(end!=null){
			f.append(" and bean.incomeDate<=:end").setParam("end", end);
		}
		if(groupBy==1||groupBy==0){
			f.append(" group by bean.incomeDate ");
		}else if(groupBy==2){
			f.append("  group by  month(bean.incomeDate) ");
		}else if(groupBy==3){
			f.append("  group by  year(bean.incomeDate) ");
		}
		f.setCacheable(true);
		return find(f);
	}

	public BbsIncomeStatistic findById(Integer id) {
		BbsIncomeStatistic entity = get(id);
		return entity;
	}
	
	public BbsIncomeStatistic findByDate(Date date){
		String hql=" from BbsIncomeStatistic bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(date!=null){
			f.append(" and bean.incomeDate>=:incomeDate and "
					+ "bean.incomeDate<=:incomeDateEnd")
			.setParam("incomeDate", date).setParam("incomeDateEnd", DateUtils.getFinallyDate(date));
		}
		f.setMaxResults(1);
		List<BbsIncomeStatistic>list=find(f);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public BbsIncomeStatistic save(BbsIncomeStatistic bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsIncomeStatistic deleteById(Integer id) {
		BbsIncomeStatistic entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsIncomeStatistic> getEntityClass() {
		return BbsIncomeStatistic.class;
	}
}