package com.jeecms.bbs.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsMessageReplyDao;
import com.jeecms.bbs.entity.BbsMessageReply;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsMessageReplyDaoImpl extends
		HibernateBaseDao<BbsMessageReply, Integer> implements BbsMessageReplyDao {
	public BbsMessageReply findById(Integer id) {
		BbsMessageReply entity = get(id);
		return entity;
	}

	public BbsMessageReply save(BbsMessageReply bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsMessageReply deleteById(Integer id) {
		BbsMessageReply entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	public Pagination getPageByMsgId(Integer msgId, Integer pageNo,
			Integer pageSize) {
		String hql = "from BbsMessageReply bean where bean.message.id=:msgId order by bean.createTime asc";
		Finder f = Finder.create(hql).setParam("msgId", msgId);
		return find(f, pageNo, pageSize);
	}
	
	public Pagination getPage(Integer senderId,Integer receiverId,String content,
			Integer pageNo, Integer pageSize){
		String hql = "select bean  from BbsMessageReply bean "
				+ "where bean.status=0 ";
		Finder f = Finder.create(hql);
		if(senderId!=null&&senderId!=0){
			f.append(" and bean.sender.id=:senderId").setParam("senderId", senderId);
		}
		if(receiverId!=null&&receiverId!=0){
			f.append(" and bean.receiver.id=:receiverId").setParam("receiverId", receiverId);
		}
		if(StringUtils.isNotBlank(content)){
			f.append(" and bean.content like :content").setParam("content", "%"+content+"%");
		}
		f.append(" order by bean.createTime desc");
		f.setCacheable(true);
		Pagination page= find(f,pageNo,pageSize);
		return page;
	}
	
	public List<BbsMessageReply> getList(Integer msgId,Integer first,int count){
		String hql = "from BbsMessageReply bean where bean.message.id=:msgId order by bean.createTime desc";
		Finder f = Finder.create(hql).setParam("msgId", msgId);
		if(first!=null){
			f.setFirstResult(first);
		}
		f.setMaxResults(count);
		return find(f);
	}
	
	public List<BbsMessageReply> getList(String username){
		String hql = "from BbsMessageReply bean where bean.receiver.username=:username and bean.status=false and bean.isnotification=false order by bean.createTime desc";
		Finder f = Finder.create(hql).setParam("username", username);
		return find(f);
	}
	
	public int countByReceiver(String username) {
		String hql = "select count(*) from BbsMessageReply bean where bean.receiver.username=:username and bean.status=false";
		Query query = getSession().createQuery(hql);
		query.setParameter("username", username);
		return ((Number) query.iterate().next()).intValue();
	}

	@Override
	protected Class<BbsMessageReply> getEntityClass() {
		return BbsMessageReply.class;
	}
}