package com.jeecms.bbs.manager;


import java.util.List;

import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.page.Pagination;


public interface BbsFriendShipMng {
	public BbsFriendShip findById(Integer id);

	public BbsFriendShip save(BbsFriendShip bean);

	public BbsFriendShip update(BbsFriendShip bean);

	public BbsFriendShip deleteById(Integer id);

	public BbsFriendShip[] deleteByIds(Integer[] ids);
	
	public BbsFriendShip getFriendShip(Integer userId, Integer friendId);

	public void apply(BbsUser user, BbsUser friend);
	
	public void accept(BbsFriendShip friendShip);
	
	public void refuse(BbsFriendShip friendShip);
	
	public Pagination getPageByUserId(Integer userId, Integer pageNo, Integer pageSize);
	
	public List<BbsFriendShip> getListByUserId(Integer userId,Integer first,Integer count);
	
	public Pagination getApplyByUserId(Integer userId, Integer pageNo, Integer pageSize);
	
	public List<BbsFriendShip> getApplyList(Integer userId,Integer first,Integer count);
}