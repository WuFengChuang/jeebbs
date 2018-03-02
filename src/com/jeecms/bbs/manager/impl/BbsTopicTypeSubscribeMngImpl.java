package com.jeecms.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsTopicTypeSubscribeDao;
import com.jeecms.bbs.entity.BbsTopicType;
import com.jeecms.bbs.entity.BbsTopicTypeSubscribe;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsTopicTypeMng;
import com.jeecms.bbs.manager.BbsTopicTypeSubscribeMng;
import com.jeecms.bbs.manager.BbsUserMng;

@Service
@Transactional
public class BbsTopicTypeSubscribeMngImpl implements BbsTopicTypeSubscribeMng {
	@Transactional(readOnly = true)
	public Pagination getPage(Integer userId,int pageNo, int pageSize) {
		Pagination page = dao.getPage(userId,pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsTopicTypeSubscribe findById(Integer id) {
		BbsTopicTypeSubscribe entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsTopicTypeSubscribe find(Integer typeId,Integer userId) {
		BbsTopicTypeSubscribe entity = dao.find(typeId,userId);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public List<BbsTopicTypeSubscribe> getList(Integer typeId,Integer userId){
		return dao.getList(typeId, userId);
	}
	
	public BbsTopicTypeSubscribe subscribe(Integer typeId,Integer userId,Integer operate){
		BbsTopicType type=bbsTopicTypeMng.findById(typeId);
		BbsUser user=bbsUserMng.findById(userId);
		BbsTopicTypeSubscribe sub=find(typeId, userId);
		if(sub==null){
			//订阅
			if(operate.equals(BbsTopicTypeSubscribe.SUBSCRIBE_OK)){
				sub=new BbsTopicTypeSubscribe();
				type.setSubscribeCount(type.getSubscribeCount()+1);
				sub.setType(type);
				sub.setUser(user);
				sub=save(sub);
			}
		}else{
			//取消订阅
			if(operate.equals(BbsTopicTypeSubscribe.SUBSCRIBE_CANCEL)){
				if (type.getSubscribeCount().equals(0)) {
					type.setSubscribeCount(0);
				}else{
					type.setSubscribeCount(type.getSubscribeCount()-1);
				}
				deleteById(sub.getId());
			}
		}
		return sub;
	}

	public BbsTopicTypeSubscribe save(BbsTopicTypeSubscribe bean) {
		dao.save(bean);
		return bean;
	}

	public BbsTopicTypeSubscribe update(BbsTopicTypeSubscribe bean) {
		Updater<BbsTopicTypeSubscribe> updater = new Updater<BbsTopicTypeSubscribe>(bean);
		BbsTopicTypeSubscribe entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsTopicTypeSubscribe deleteById(Integer id) {
		BbsTopicTypeSubscribe bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsTopicTypeSubscribe[] deleteByIds(Integer[] ids) {
		BbsTopicTypeSubscribe[] beans = new BbsTopicTypeSubscribe[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsTopicTypeSubscribeDao dao;
	@Autowired
	private BbsTopicTypeMng bbsTopicTypeMng;
	@Autowired
	private BbsUserMng bbsUserMng;

	@Autowired
	public void setDao(BbsTopicTypeSubscribeDao dao) {
		this.dao = dao;
	}
}