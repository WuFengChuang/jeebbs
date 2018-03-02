package com.jeecms.core.manager.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.core.dao.CmsRoleDao;
import com.jeecms.core.entity.CmsRole;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.core.manager.CmsRoleMng;
import com.jeecms.bbs.manager.BbsUserMng;

@Service
@Transactional
public class CmsRoleMngImpl implements CmsRoleMng {
	@Transactional(readOnly = true)
	public List<CmsRole> getList(Integer level) {
		return dao.getList(level);
	}

	@Transactional(readOnly = true)
	public CmsRole findById(Integer id) {
		CmsRole entity = dao.findById(id);
		return entity;
	}

	public CmsRole save(CmsRole bean, Set<String> perms) {
		bean.setPerms(perms);
		dao.save(bean);
		return bean;
	}

	public CmsRole update(CmsRole bean, Set<String> perms) {
		Updater<CmsRole> updater = new Updater<CmsRole>(bean);
		bean = dao.updateByUpdater(updater);
		bean.setPerms(perms);
		return bean;
	}

	public CmsRole deleteById(Integer id) {
		CmsRole bean = dao.deleteById(id);
		bean.getUsers().clear();
		return bean;
	}

	public CmsRole[] deleteByIds(Integer[] ids) {
		CmsRole[] beans = new CmsRole[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public void deleteMembers(CmsRole role, Integer[] userIds) {
		Updater<CmsRole> updater = new Updater<CmsRole>(role);
		role = dao.updateByUpdater(updater);
		if (userIds != null) {
			BbsUser user;
			for (Integer uid : userIds) {
				user = userMng.findById(uid);
				role.delFromUsers(user);
			}
		}
	}

	private CmsRoleDao dao;
	@Autowired
	private BbsUserMng userMng;

	@Autowired
	public void setDao(CmsRoleDao dao) {
		this.dao = dao;
	}
}