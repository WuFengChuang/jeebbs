package com.jeecms.plug.live.manager.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveApplyDao;
import com.jeecms.plug.live.entity.BbsLiveApply;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;
import com.jeecms.plug.live.manager.BbsLiveApplyMng;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;

@Service
@Transactional
public class BbsLiveApplyMngImpl implements BbsLiveApplyMng {
	@Transactional(readOnly = true)
	public Pagination getPage(String mobile,Short status,
			Integer applyUserId,int pageNo, int pageSize) {
		Pagination page = dao.getPage(mobile,status,applyUserId,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsLiveApply> getList(String mobile,Short status,
			Integer applyUserId,Integer first, Integer count){
		return dao.getList(mobile,status,applyUserId,first,count);
	}
	
	@Transactional(readOnly = true)
	public Long findLiveApplyCount(Short status,Integer applyUserId){
		return dao.findLiveApplyCount(status,applyUserId);
	}
	
	@Transactional(readOnly = true)
	public boolean haveApplied(Integer applyUserId){
		List<BbsLiveApply> list=getList(null, null, applyUserId, 0, 1);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}

	@Transactional(readOnly = true)
	public BbsLiveApply findById(Integer id) {
		BbsLiveApply entity = dao.findById(id);
		return entity;
	}

	public BbsLiveApply save(BbsLiveApply bean,
			String[] picPaths, String[] picDescs) {
		bean=dao.save(bean);
		// 保存图片集
		if (picPaths != null && picPaths.length > 0) {
			for (int i = 0, len = picPaths.length; i < len; i++) {
				if (!StringUtils.isBlank(picPaths[i])) {
					bean.addToPictures(picPaths[i], picDescs[i]);
				}
			}
		}
		return bean;
	}
	
	public BbsLiveApply check(Integer id,BbsUser checkUser){
		BbsLiveApply apply=findById(id);
		apply.setStatus(BbsLiveApply.STATUS_CHECKED);
		apply.setCheckUser(checkUser);
		apply.setCheckTime(Calendar.getInstance().getTime());
		apply=update(apply);
		BbsUser applyUser=apply.getApplyUser();
		applyUser.setLiveHost(true);
		BbsLiveUserAccount userAccount=liveUserAccountMng.findById(applyUser.getId());
		if(userAccount!=null){
			userAccount.setCheckTime(Calendar.getInstance().getTime());
			liveUserAccountMng.update(userAccount);
		}else{
			BbsLiveUserAccount account=new BbsLiveUserAccount();
			account.init();
			account.setCheckTime(Calendar.getInstance().getTime());
			liveUserAccountMng.save(account, applyUser);
		}
		userMng.update(applyUser);
		return apply;
	}
	
	public BbsLiveApply[] check(Integer[] ids,BbsUser checkUser){
		BbsLiveApply[] beans = new BbsLiveApply[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = check(ids[i],checkUser);
		}
		return beans;
	}
	
	public BbsLiveApply cancel(Integer id,BbsUser checkUser){
		BbsLiveApply apply=findById(id);
		apply.setStatus(BbsLiveApply.STATUS_REBACK);
		apply.setCheckUser(checkUser);
		apply.setCheckTime(Calendar.getInstance().getTime());
		apply=update(apply);
		BbsUser applyUser=apply.getApplyUser();
		applyUser.setLiveHost(false);
		userMng.update(applyUser);
		return apply;
	}
	
	public BbsLiveApply[] cancel(Integer[] ids,BbsUser checkUser){
		BbsLiveApply[] beans = new BbsLiveApply[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = cancel(ids[i],checkUser);
		}
		return beans;
	}
	
	public BbsLiveApply update(BbsLiveApply bean){
		Updater<BbsLiveApply> updater = new Updater<BbsLiveApply>(bean);
		BbsLiveApply entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsLiveApply update(BbsLiveApply bean,
			String[] picPaths, String[] picDescs) {
		Updater<BbsLiveApply> updater = new Updater<BbsLiveApply>(bean);
		BbsLiveApply entity = dao.updateByUpdater(updater);
		// 更新图片集
		entity.getPictures().clear();
		if (picPaths != null && picPaths.length > 0) {
			for (int i = 0, len = picPaths.length; i < len; i++) {
				if (!StringUtils.isBlank(picPaths[i])) {
					entity.addToPictures(picPaths[i], picDescs[i]);
				}
			}
		}
		return entity;
	}

	public BbsLiveApply deleteById(Integer id) {
		BbsLiveApply bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLiveApply[] deleteByIds(Integer[] ids) {
		BbsLiveApply[] beans = new BbsLiveApply[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsLiveApplyDao dao;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private BbsLiveUserAccountMng liveUserAccountMng;

	@Autowired
	public void setDao(BbsLiveApplyDao dao) {
		this.dao = dao;
	}
}