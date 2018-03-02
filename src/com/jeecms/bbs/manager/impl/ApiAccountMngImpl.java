package com.jeecms.bbs.manager.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.dao.ApiAccountDao;
import com.jeecms.bbs.entity.ApiAccount;
import com.jeecms.bbs.manager.ApiAccountMng;

@Service
@Transactional
public class ApiAccountMngImpl implements ApiAccountMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public ApiAccount findByAppId(String appId){
		return dao.findByAppId(appId);
	}
	
	@Transactional(readOnly = true)
	public ApiAccount getApiAccount(HttpServletRequest request){
		String appId=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_APPID);
		ApiAccount apiAccount = null;
		if(StringUtils.isNotBlank(appId)){
			apiAccount=findByAppId(appId);
		}
		return apiAccount;
	}

	@Transactional(readOnly = true)
	public ApiAccount findById(Integer id) {
		ApiAccount entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public ApiAccount findByDefault(){
		List<ApiAccount>list=dao.getList(0, 1);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public ApiAccount save(ApiAccount bean) {
		dao.save(bean);
		return bean;
	}

	public ApiAccount update(ApiAccount bean,String appKey,String aesKey,String ivKey) {
		Updater<ApiAccount> updater = new Updater<ApiAccount>(bean);
		if(StringUtils.isBlank(appKey)){
			updater.exclude("appKey");
		}
		if(StringUtils.isBlank(aesKey)){
			updater.exclude("aesKey");
		}
		if(StringUtils.isBlank(ivKey)){
			updater.exclude("ivKey");
		}
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public ApiAccount deleteById(Integer id) {
		ApiAccount bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiAccount[] deleteByIds(Integer[] ids) {
		ApiAccount[] beans = new ApiAccount[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ApiAccountDao dao;

	@Autowired
	public void setDao(ApiAccountDao dao) {
		this.dao = dao;
	}
}