package com.jeecms.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.manager.BbsLiveChargeMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;
import com.jeecms.bbs.dao.BbsGiftUserDao;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsGiftUser;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsGiftMng;
import com.jeecms.bbs.manager.BbsGiftUserMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;

@Service
@Transactional
public class BbsGiftUserMngImpl implements BbsGiftUserMng {
	@Transactional(readOnly = true)
	public Pagination getPage(Integer giftId,Integer userId,
			int pageNo, int pageSize) {
		Pagination page = dao.getPage(giftId,userId,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsGiftUser> getList(Integer giftId,Integer userId,
			Integer first, Integer count){
		return dao.getList(giftId, userId, first, count);
	}
	
	@Transactional(readOnly = true)
	public BbsGiftUser getUserGift(Integer giftId,Integer userId){
		List<BbsGiftUser>list=getList(giftId, userId, 0, 1);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Transactional(readOnly = true)
	public BbsGiftUser findById(Integer id) {
		BbsGiftUser entity = dao.findById(id);
		return entity;
	}

	//购买礼物
	public BbsGiftUser addUserGift(Integer giftId,Integer userId,Integer num){
		BbsGift gift = null;
		BbsUser user=null;
		if(giftId!=null){
			gift=bbsGiftMng.findById(giftId);
		}
		if(userId!=null){
			user=bbsUserMng.findById(userId);
		}
		BbsGiftUser g=null;
		if(gift!=null&&user!=null&&num!=null){
			g=getUserGift(giftId, userId);
			if(g==null){
				g=new BbsGiftUser();
				g.setGift(gift);
				g.setUser(user);
				g.setNum(num);
				save(g);
			}else{
				g.setNum(num+g.getNum());
				update(g);
			}
		}
		return g;
	}
	
	//赠送礼物
	public int giveUserGift(BbsGift gift,BbsUser giveUser,
			BbsUser receiverUser,Integer liveId,Integer num){
		int status=0;
		if(giveUser!=null&&num>0){
			if(giveUser!=null){
				BbsGiftUser g=getUserGift(gift.getId(), giveUser.getId());
				if(g!=null&&g.getNum()>=num){
					//减少赠送者礼物数
					addUserGift(gift.getId(), giveUser.getId(), -num);
					//增加接受者礼物收益
					if(receiverUser!=null){
						userAccountMng.userReceiveGift(gift.getPrice()*num, receiverUser);
					}
					//live礼物数统计和主播礼物数统计
					if(liveId!=null){
						liveChargeMng.afterReceiveGift(liveMng.findById(liveId), num);
						liveUserAccountMng.afterReceiveGift(receiverUser, num);
					}
					status=1;
				}
			}
		}
		return status;
	}
	
	public BbsGiftUser save(BbsGiftUser bean) {
		dao.save(bean);
		return bean;
	}

	public BbsGiftUser update(BbsGiftUser bean) {
		Updater<BbsGiftUser> updater = new Updater<BbsGiftUser>(bean);
		BbsGiftUser entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsGiftUser deleteById(Integer id) {
		BbsGiftUser bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsGiftUser[] deleteByIds(Integer[] ids) {
		BbsGiftUser[] beans = new BbsGiftUser[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsGiftUserDao dao;
	@Autowired
	private BbsGiftMng bbsGiftMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsUserAccountMng userAccountMng;
	@Autowired
	private BbsLiveChargeMng liveChargeMng;
	@Autowired
	private BbsLiveMng liveMng;
	@Autowired
	private BbsLiveUserAccountMng liveUserAccountMng;

	@Autowired
	public void setDao(BbsGiftUserDao dao) {
		this.dao = dao;
	}
}