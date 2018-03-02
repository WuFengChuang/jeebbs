package com.jeecms.bbs.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.bbs.dao.BbsWebserviceAuthDao;
import com.jeecms.bbs.entity.BbsWebserviceAuth;
import com.jeecms.bbs.manager.BbsWebserviceAuthMng;

@Service
@Transactional
public class BbsWebserviceAuthMngImpl implements BbsWebserviceAuthMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public boolean isPasswordValid(String username, String password){
		BbsWebserviceAuth auth=findByUsername(username);
		if(auth!=null&&auth.getEnable()){
			return pwdEncoder.isPasswordValid(auth.getPassword(), password);
		}else{
			return false;
		}
	}

	@Transactional(readOnly = true)
	public BbsWebserviceAuth findByUsername(String username) {
		BbsWebserviceAuth entity = dao.findByUsername(username);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsWebserviceAuth findById(Integer id) {
		BbsWebserviceAuth entity = dao.findById(id);
		return entity;
	}

	public BbsWebserviceAuth save(BbsWebserviceAuth bean) {
		dao.save(bean);
		return bean;
	}

	public BbsWebserviceAuth update(BbsWebserviceAuth bean) {
		Updater<BbsWebserviceAuth> updater = new Updater<BbsWebserviceAuth>(bean);
		BbsWebserviceAuth entity = dao.updateByUpdater(updater);
		return entity;
	}
	
	public BbsWebserviceAuth update(Integer id,String username,String password,String system,Boolean enable){
		BbsWebserviceAuth entity =findById(id);
		if(StringUtils.isNotBlank(username)){
			entity.setUsername(username);
		}
		if(StringUtils.isNotBlank(password)){
			entity.setPassword(pwdEncoder.encodePassword(password));
		}
		if(StringUtils.isNotBlank(system)){
			entity.setSystem(system);
		}
		if(enable!=null){
			entity.setEnable(enable);
		}
		return entity;
	}

	public BbsWebserviceAuth deleteById(Integer id) {
		BbsWebserviceAuth bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsWebserviceAuth[] deleteByIds(Integer[] ids) {
		BbsWebserviceAuth[] beans = new BbsWebserviceAuth[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	@Autowired
	private PwdEncoder pwdEncoder;
	private BbsWebserviceAuthDao dao;

	@Autowired
	public void setDao(BbsWebserviceAuthDao dao) {
		this.dao = dao;
	}
}