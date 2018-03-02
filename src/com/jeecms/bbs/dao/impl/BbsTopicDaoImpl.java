package com.jeecms.bbs.dao.impl;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsTopicDao;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCountEnum;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsTopicDaoImpl extends HibernateBaseDao<BbsTopic, Integer>
		implements BbsTopicDao {
	@SuppressWarnings("unchecked")
	public List<BbsTopic> getTopTopic(Integer webId, Integer ctgId,
			Integer forumId) {
		Finder f = Finder.create("from BbsTopic bean where 1=1");
		f.append(" and bean.website.id=:webId").setParam("webId", webId);
		f.append(" and bean.status>=").append(String.valueOf(BbsTopic.NORMAL));
		f.append(" and (bean.topLevel=3 ");
		f.append(" or (bean.topLevel=2 and bean.category.id=:ctgId)");
		f.setParam("ctgId", ctgId);
		f.append(" or (bean.topLevel=1 and bean.forum.id=:forumId))");
		f.setParam("forumId", forumId);
		f.append(" order by bean.topLevel desc");
		return find(f);
	}
	
	public Pagination getForTag(Integer siteId, Integer forumId,
			Integer parentPostTypeId,
			Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel,Integer topicTypeId,Integer excludeId,
			Boolean checkStatus, int pageNo,int pageSize, String jinghua,Integer orderBy,Short recommend) {
		Finder f =createFinder(siteId, forumId, parentPostTypeId,
				postTypeId, status, primeLevel, keyWords, creater,
				createrId, topLevel, topicTypeId,excludeId,checkStatus,jinghua, orderBy,recommend);
		return find(f, pageNo, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsTopic> getListForTag(Integer siteId, Integer forumId,
			Integer parentPostTypeId,
			Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel,Integer topicTypeId,Integer excludeId,
			Boolean checkStatus, int first,int count,String jinghua, Integer orderBy,Short recommend) {
		Finder f =createFinder(siteId, forumId, parentPostTypeId,
				postTypeId, status, primeLevel, keyWords, creater,
				createrId, topLevel, topicTypeId,excludeId, checkStatus,jinghua, orderBy,recommend);
		f.setFirstResult(first);
		f.setMaxResults(count);
		return find(f);
	}
	
	private Finder createFinder(Integer siteId, Integer forumId,
			Integer parentPostTypeId,
			Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel,Integer topicTypeId,
			Integer excludeId,Boolean checkStatus,String jinghua, Integer orderBy,Short recommend){
		Finder f = Finder.create("select bean from BbsTopic bean ");
		if(topicTypeId!=null){
			f.append(" join bean.types type,BbsTopicType parent");
			f.append(" where (type.lft between parent.lft and parent.rgt");
			f.append(" and parent.id=:parentId)");
			f.setParam("parentId", topicTypeId);
		}else{
			f.append(" where 1=1 ");
		}
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		if (forumId != null&&forumId!=0) {
			f.append(" and bean.forum.id=:forumId")
					.setParam("forumId", forumId);
		}
		if(parentPostTypeId!=null){
			f.append(" and bean.postType.parent.id=:parentPostTypeId")
			.setParam("parentPostTypeId", parentPostTypeId);
		}
		if(postTypeId!=null){
			f.append(" and bean.postType.id=:postTypeId")
			.setParam("postTypeId", postTypeId);
		}
		if (status == null) {
			status = 0;
		}
		if (recommend!=null) {
			f.append(" and bean.recommend = :recommend").setParam("recommend", recommend);
		}
		f.append(" and bean.status>=:status").setParam("status", status);
		if (topLevel != null) {
			if (topLevel != 0) {
				f.append(" and bean.topLevel>=:topLevel").setParam("topLevel",
						topLevel);
			} else {
				f.append(" and bean.topLevel=0");
			}
		}
		if (primeLevel != null) {
			f.append(" and bean.primeLevel >=:primeLevel").setParam(
					"primeLevel", primeLevel);
		}
		if (!StringUtils.isBlank(keyWords)) {
			f.append(" and bean.topicText.title like :keyWords").setParam("keyWords",
					"%" + keyWords + "%");
		}
		if (!StringUtils.isBlank(creater)) {
			f.append(" and bean.creater.username like :creater").setParam(
					"creater", "%" + creater + "%");
		}
		if (createrId != null) {
			f.append(" and bean.creater.id =:createrId").setParam("createrId",
					createrId);
		}
		if("index_jing".equals(jinghua)){
			f.append(" and bean.primeLevel != 0");
		}
		if(checkStatus!=null){
			f.append(" and bean.checkStatus=:checkStatus").setParam("checkStatus", checkStatus);
		}
		if (excludeId != null) {
			f.append(" and bean.id<>:excludeId");
			f.setParam("excludeId", excludeId);
		}
		if(orderBy.equals(1)){
			f.append(" order by bean.topLevel desc,bean.lastTime desc");
		}else if(orderBy.equals(2)){
			f.append(" order by bean.viewsDay desc,bean.sortTime desc");
		}else if(orderBy.equals(3)){
			f.append(" order by bean.viewsWeek desc,bean.sortTime desc");
		}else if(orderBy.equals(4)){
			f.append(" order by bean.viewsMonth desc,bean.sortTime desc");
		}else if(orderBy.equals(5)){
			f.append(" order by bean.viewCount desc,bean.sortTime desc");
		}else if(orderBy.equals(6)){
			f.append(" order by bean.replyCount desc,bean.sortTime desc");
		}else if(orderBy.equals(7)){
			f.append(" order by bean.replyCountDay desc,bean.sortTime desc");
		}else if(orderBy.equals(8)){
			f.append(" order by bean.id desc");
		}else if(orderBy.equals(0)){
			f.append(" order by bean.lastTime desc,bean.id desc");
		}
		return f;
	}

	public Pagination getForSearchDate(Integer siteId, Integer forumId,
			Short primeLevel, Integer day, int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		if (forumId != null) {
			f.append(" and bean.forum.id=:forumId")
					.setParam("forumId", forumId);
		}
		f.append(" and bean.status>=0");
		if (day != null) {
			Calendar now = Calendar.getInstance();
			now.setTime(new Date(System.currentTimeMillis()));
			now.add(Calendar.DATE, -day);
			f.append(" and bean.createTime>:day")
					.setParam("day", now.getTime());
		}
		if (primeLevel != null) {
			f.append(" and bean.primeLevel >=:primeLevel").setParam(
					"primeLevel", primeLevel);
		}
		f.append(" and bean.checkStatus=true");
		f.append(" order by bean.topLevel desc,bean.sortTime desc");
		return find(f, pageNo, pageSize);
	}

	public Pagination getTopicByTime(Integer siteId, int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		f.append(" order by bean.lastTime desc");
		return find(f, pageNo, pageSize);
	}

	public Pagination getMemberTopic(Integer siteId, Integer userId,
			int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		f.append(" and bean.creater.id=:userId");
		f.setParam("userId", userId);
		f.append(" order by bean.lastTime desc");
		return find(f, pageNo, pageSize);
	}

	public Pagination getMemberReply(Integer siteId, Integer userId,
			int pageNo, int pageSize) {
		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		f.append(" and bean.haveReply like :userId").setParam("userId",
				"%," + userId + ",%");
		f.append(" order by bean.sortTime desc");
		return find(f, pageNo, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsTopic> getMemberReply(Integer siteId, Integer userId,
			Integer first,Integer count) {
		Finder f = Finder.create("select bean from BbsTopic bean where 1=1 ");
		f.append(" and bean.site.id=:siteId").setParam("siteId", siteId);
		f.append(" and bean.haveReply like :userId").setParam("userId",
				"%," + userId + ",%");
		f.append(" order by bean.sortTime desc");
		if(first==null){
			first=0;
		}
		f.setFirstResult(first);
		f.setMaxResults(count);
		return find(f);
	}

	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public BbsTopic findById(Integer id) {
		BbsTopic entity = get(id);
		return entity;
	}

	public BbsTopic save(BbsTopic bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsTopic deleteById(Integer id) {
		BbsTopic entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<BbsTopic> getEntityClass() {
		return BbsTopic.class;
	}

	@SuppressWarnings("unchecked")
	public List<BbsTopic> getList(Integer forumId,String keywords,Integer userId,
			Short topLevel,Integer first,Integer count) {
		String hql = "from BbsTopic bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(forumId!=null){
			f.append(" and bean.forum.id=:forumId").setParam("forumId", forumId);
		}
		if(StringUtils.isNotBlank(keywords)){
			f.append(" and bean.topicText.title like :keyWords").setParam("keyWords",
					"%" + keywords + "%");
		}
		if(userId!=null){
			f.append(" and bean.creater.id=:userId").setParam("userId", userId);
		}
		if(topLevel!=null){
			f.append(" and bean.topLevel>=:topLevel").setParam("topLevel", topLevel);
		}
		f.append(" order by bean.id desc");
		f.setCacheable(false);
		f.setFirstResult(first);
		f.setMaxResults(count);
		return find(f);
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsTopic> getNewList(Short topLevel,Integer first,Integer count,Integer orderby) {
		String hql = "from BbsTopic bean where bean.checkStatus=true ";
		if(topLevel!=null){
			hql+=(" and  bean.topLevel>="+topLevel);
		}
		if(orderby==null){
			hql+="order by bean.createTime desc";
		}
		if(orderby.equals(1)){
			hql+="order by bean.createTime desc";
		}else if(orderby.equals(2)){
			hql+="order by bean.viewsDay desc,bean.createTime desc";
		}else if(orderby.equals(3)){
			hql+="order by bean.viewsWeek desc,bean.createTime desc";
		}else if(orderby.equals(4)){
			hql+="order by bean.viewsMonth desc,bean.createTime desc";
		}else if(orderby.equals(5)){
			hql+="order by bean.viewCount desc,bean.createTime desc";
		}else if(orderby.equals(6)){
			hql+="order by bean.replyCount desc,bean.createTime desc";
		}else if(orderby.equals(7)){
			hql+="order by bean.replyCountDay desc,bean.createTime desc";
		}else if(orderby.equals(8)){
			hql+="order by bean.id desc";
		}
		return getSession().createQuery(hql).setFirstResult(first).setMaxResults(count).setCacheable(false).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsTopic> getTopList(Short topLevel,Integer count,Integer orderby){
		String hql = "from BbsTopic bean where bean.checkStatus=true ";
		Finder f=Finder.create(hql);
		if(topLevel!=null){
			f.append(" where bean.topLevel>=:topLevel").setParam("topLevel", topLevel);
		}
		if(orderby==null){
			f.append(" order by bean.topLevel desc");
		}
		if(orderby.equals(1)){
			f.append(" order by bean.topLevel desc");
		}else if(orderby.equals(2)){
			f.append(" order by bean.createTime desc");
		}else{
			f.append(" order by bean.id desc");
		}
		f.setMaxResults(count);
		return find(f);
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsTopic> getTopicList(Integer userId,Integer bigId,Integer smallId,Integer count) {
		Finder f = Finder.create("select bean from BbsTopic bean where 1=1");
		if(bigId!=null){
			f.append(" and bean.id>:bigId");
			f.setParam("bigId", bigId);
		}else if(smallId!=null){
			f.append(" and bean.id<:smallId");
			f.setParam("smallId",smallId);
		}
		if(userId!=null){
			f.append(" and bean.creater.id=:userId");
			f.setParam("userId", userId);
		}
		if(bigId!=null){
			f.append(" order by bean.id asc");
		}else{
			f.append(" order by bean.id desc");
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<BbsTopic> getTopicList(){
		String hql = "from BbsTopic bean order by bean.createTime desc";
		return getSession().createQuery(hql).setFirstResult(0).setCacheable(false).list();
	}
	
	
	
	public void updateAllTopicCount(BbsTopicCountEnum e){
		String hql="";
		String updateReplyCountDaySql="";
		if(e==null){
			hql = "update BbsTopic bean set bean.viewsDay=0";
		}
		if(e.equals(BbsTopicCountEnum.day)){
			hql = "update BbsTopic bean set bean.viewsDay=0";
			updateReplyCountDaySql="update BbsTopic bean set bean.replyCountDay=0";
		}else if(e.equals(BbsTopicCountEnum.week)){
			hql = "update BbsTopic bean set bean.viewsWeek=0";
		}else if(e.equals(BbsTopicCountEnum.month)){
			hql = "update BbsTopic bean set bean.viewsMonth=0";
		}
		if(StringUtils.isNotBlank(hql)){
			getSession().createQuery(hql).executeUpdate();
		}
		if(StringUtils.isNotBlank(updateReplyCountDaySql)){
			getSession().createQuery(updateReplyCountDaySql).executeUpdate();
		}
	}

	@Override
	public void updateAllTopTime() {
		String hql ="update BbsTopic bean set bean.topTime=:nullTime,bean.topLevel=:normal where bean.topTime<:topTime";
		Query query = getSession().createQuery(hql);
		query.setParameter("nullTime", null);
		query.setParameter("normal", BbsTopic.NORMAL);
		query.setParameter("topTime", new java.util.Date());
		query.executeUpdate();
	}
}