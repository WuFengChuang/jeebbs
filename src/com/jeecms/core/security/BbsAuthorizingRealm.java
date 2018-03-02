package com.jeecms.core.security;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.UnifiedUserMng;

/**
 * 自定义DB Realm
 * 
 */
public class BbsAuthorizingRealm extends AuthorizingRealm {

	/**
	 * 登录认证
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		BbsUser user = bbsUserMng.findByUsername(token.getUsername());
		if (user != null) {
			UnifiedUser unifiedUser = unifiedUserMng.findById(user.getId());
			return new SimpleAuthenticationInfo(user.getUsername(), 
					unifiedUser.getPassword(),getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		BbsUser user = bbsUserMng.findByUsername(username);
		SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
		if (user != null) {
			Set<String>viewPermissionSet=new HashSet<String>();
			Set<String> perms = user.getPerms();
			if (!CollectionUtils.isEmpty(perms)) {
				// 权限加入AuthorizationInfo认证对象
				auth.setStringPermissions(perms);
			}
		}
		return auth;
	}
	
	public void removeUserAuthorizationInfoCache(String username){
		  SimplePrincipalCollection pc = new SimplePrincipalCollection();
		  pc.add(username, super.getName()); 
		  super.clearCachedAuthorizationInfo(pc);
	}

	protected BbsUserMng bbsUserMng;
	protected UnifiedUserMng unifiedUserMng;

	@Autowired
	public void setBbsUserMng(BbsUserMng bbsUserMng) {
		this.bbsUserMng = bbsUserMng;
	}

	@Autowired
	public void setUnifiedUserMng(UnifiedUserMng unifiedUserMng) {
		this.unifiedUserMng = unifiedUserMng;
	}

}
