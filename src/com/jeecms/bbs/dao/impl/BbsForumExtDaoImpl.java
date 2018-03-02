package com.jeecms.bbs.dao.impl;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsForumExtDao;
import com.jeecms.bbs.entity.BbsForumExt;
import com.jeecms.common.hibernate4.HibernateBaseDao;

@Repository
public class BbsForumExtDaoImpl extends HibernateBaseDao<BbsForumExt, Integer>
		implements BbsForumExtDao {
	public BbsForumExt save(BbsForumExt bean) {
		getSession().save(bean);
		return bean;
	}

	@Override
	protected Class<BbsForumExt> getEntityClass() {
		return BbsForumExt.class;
	}
}