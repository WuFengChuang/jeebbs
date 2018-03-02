package com.jeecms.bbs.dao;


import java.util.List;

import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsFriendShipDao {
	public BbsFriendShip findById(Integer id);

	public BbsFriendShip save(BbsFriendShip bean);

	public BbsFriendShip updateByUpdater(Updater<BbsFriendShip> updater);

	public BbsFriendShip deleteById(Integer id);

	public BbsFriendShip getFriendShip(Integer userId, Integer friendId);

	public Pagination getPageByUserId(Integer userId, Integer pageNo, Integer pageSize);
	
	public List<BbsFriendShip> getListByUserId(Integer userId,Integer first,Integer count);

	public Pagination getApplyByUserId(Integer userId, Integer pageNo,
			Integer pageSize);
	
	public List<BbsFriendShip> getApplyList(Integer userId,Integer first,Integer count);
}