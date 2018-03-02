package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsOrderDao;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsOrderDaoImpl extends HibernateBaseDao<BbsOrder, Long> implements BbsOrderDao {
	public Pagination getPage(String orderNum,Integer buyUserId,Integer authorUserId,
			Short payMode,Short dataType,Integer dataId,int pageNo, int pageSize) {
		Finder f=createFinder(orderNum, buyUserId, authorUserId, payMode,dataType,dataId);
		Pagination page = find(f, pageNo, pageSize);
		return page;
	}
	
	public List<BbsOrder> getList(String orderNum,Integer buyUserId,
			Integer authorUserId,Short payMode,Short dataType,
			Integer dataId,Integer first, Integer count){
		Finder f=createFinder(orderNum, buyUserId, authorUserId, payMode,dataType,dataId);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	public Pagination getPageByTopic(Integer topicId,
			Short payMode,int pageNo, int pageSize){
		Finder f=createFinder(topicId, payMode);
		Pagination page = find(f, pageNo, pageSize);
		return page;
	}
	
	public List<BbsOrder> getListByTopic(Integer topicId,
			Short payMode,Integer first, Integer count){
		Finder f=createFinder(topicId, payMode);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	private Finder createFinder(String orderNum,Integer buyUserId,
			Integer authorUserId,Short payMode,Short dataType,Integer dataId){
		String hql="from BbsOrder bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(orderNum)){
			f.append(" and bean.orderNumber like:num").setParam("num", "%"+orderNum+"%");
		}
		if(buyUserId!=null){
			f.append(" and bean.buyUser.id=:buyUserId").setParam("buyUserId", buyUserId);
		}
		if(authorUserId!=null){
			f.append(" and bean.authorUser.id=:authorUserId").setParam("authorUserId", authorUserId);
		}
		if(payMode!=null&&payMode!=0){
			f.append(" and bean.chargeReward=:payMode")
			.setParam("payMode", payMode);
		}
		if(dataType!=null&&dataType!=-1){
			f.append(" and bean.dataType=:dataType")
			.setParam("dataType", dataType);
		}
		if(dataId!=null&&dataId!=0){
			f.append(" and bean.dataId=:dataId")
			.setParam("dataId", dataId);
		}
		f.append(" order by bean.buyTime desc");
		f.setCacheable(true);
		return f;
	}
	
	private Finder createFinder(Integer topicId,Short payMode){
		String hql="from BbsOrder bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(topicId!=null){
			f.append(" and bean.dataType=:dataType and bean.dataId=:topicId")
			.setParam("dataType", BbsOrder.ORDER_TYPE_TOPIC).setParam("topicId", topicId);
		}
		if(payMode!=null&&payMode!=0){
			f.append(" and bean.chargeReward=:payMode")
			.setParam("payMode", payMode);
		}
		f.append(" order by bean.buyTime desc");
		f.setCacheable(true);
		return f;
	}

	public BbsOrder findById(Long id) {
		BbsOrder entity = get(id);
		return entity;
	}
	
	public BbsOrder findByOrderNumber(String orderNumber){
		String hql="from BbsOrder bean where bean.orderNumber=:orderNumber";
		Finder finder=Finder.create(hql).setParam("orderNumber", orderNumber);
		List<BbsOrder>list=find(finder);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public BbsOrder findByOutOrderNum(String orderNum,Integer payMethod){
		String hql;
		if(payMethod==BbsOrder.PAY_METHOD_WECHAT){
			hql="from BbsOrder bean where bean.orderNumWeixin=:orderNum";
		}else{
			hql="from BbsOrder bean where bean.orderNumAlipay=:orderNum";
		}
		Finder finder=Finder.create(hql).setParam("orderNum", orderNum);
		List<BbsOrder>list=find(finder);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public BbsOrder find(Integer buyUserId,Integer topicId){
		String hql="from BbsOrder bean where bean.dataType=:dataType and  "
				+ "bean.dataId=:topicId "
				+ "and bean.buyUser.id=:buyUserId";
		Finder finder=Finder.create(hql).setParam("dataType", BbsOrder.ORDER_TYPE_TOPIC)
				.setParam("topicId", topicId)
				.setParam("buyUserId", buyUserId);
		finder.setCacheable(true);
		List<BbsOrder>list=find(finder);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	

	public BbsOrder save(BbsOrder bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsOrder deleteById(Long id) {
		BbsOrder entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsOrder> getEntityClass() {
		return BbsOrder.class;
	}
}