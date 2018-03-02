package com.jeecms.bbs.manager;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.page.Pagination;

public interface BbsUserMng {
	
	public List<BbsUser> getList(String username, Integer groupId,
			Boolean disabled, Boolean admin,Boolean official,
			Integer first, Integer count);
	
	public Pagination getPage(String username, String email, Integer groupId,
			Boolean disabled, Boolean admin,Boolean official,Integer lastLoginDay,
			Integer rank,Integer orderBy, int pageNo, int pageSize);
	/**
	 * 查找我关注的人和关注我的人
	 * @param attent 0 我关注的  1关注我的
	 * @return
	 */
	public Pagination getPageByAttent(Integer attent, Integer userId,
			int pageNo, int pageSize);
	
	public List<BbsUser> getListByAttent(Integer attent, Integer userId,
			Integer first, Integer count);
	
	public List<BbsUser> getAdminList(Integer siteId, Boolean allChannel,
			Boolean disabled, Integer rank);

	public BbsUser findById(Integer id);

	public BbsUser findByUsername(String username);
	
	public BbsUser registerMember(String username, String email,Boolean official,
			String password, String ip, Integer groupId, BbsUserExt userExt,Map<String,String>attr) throws UnsupportedEncodingException, MessagingException;
	
	public BbsUser registerMember(String username, String email,
			String password, String ip, Integer groupId, BbsUserExt userExt,Map<String,String>attr, Boolean activation , EmailSender sender, MessageTemplate msgTpl) throws UnsupportedEncodingException, MessagingException ;

	public void updateLoginInfo(Integer userId, String ip,Date loginTime,String sessionId);

	public void updateUploadSize(Integer userId, Integer size);

	public void updatePwdEmail(Integer id, String password, String email);
	
	public void updateGroup(Integer id, Integer groupId);

	public boolean isPasswordValid(Integer id, String password);

	public BbsUser saveAdmin(String username, String email, String password,
			String ip, int rank, Integer groupId, Integer[] roleIds,
			BbsUserExt userExt) throws UnsupportedEncodingException, MessagingException;

	public BbsUser updateAdmin(BbsUser bean, BbsUserExt ext, String password,
			Integer groupId, Integer[] roleIds);

	public BbsUser updateMember(Integer id, String email, String password,
			Boolean isDisabled, String signed, String avatar, BbsUserExt ext,Map<String,String>attr,
			Integer groupId);
	
	public BbsUser updateMember(Integer id, String email, String password,String realname,Boolean gender,String tel);

	public BbsUser deleteById(Integer id);

	public BbsUser[] deleteByIds(Integer[] ids);

	public boolean usernameNotExist(String username);

	public boolean emailNotExist(String email);
	
	public List<BbsUser> getSuggestMember(String username, Integer count);
	
	public void updateActiveLevel();
	
	/**
	 * 
	 * @param userId 用户Id
	 * @param point 积分
	 * @param prestige 威望
	 * @param mid 道具标识
	 * @param num 道具数量
	 * @param operator 0出售道具，=1使用道具 =2丢弃道具 =3购买道具=4系统赠送道具
	 */
	public void updatePoint(Integer userId,Integer point,  Integer prestige,String mid,int num,int operator);
	
	public int attentUser(Integer userId,Integer attentUserId,Integer operate);
	
	public BbsUser update(BbsUser bean);
	
	public BbsUser forbidUser(Integer siteId,BbsUser user,boolean disabled);
	
}