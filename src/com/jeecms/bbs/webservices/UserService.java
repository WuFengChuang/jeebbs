/**
*	TOM
*/
package com.jeecms.bbs.webservices;



import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsWebserviceAuthMng;
import com.jeecms.bbs.manager.BbsWebserviceCallRecordMng;


public class UserService  extends SpringBeanAutowiringSupport{
	private static final String SERVICE_CODE_USER_DELETE="user_delete";
	private static final String SERVICE_CODE_USER_ADD="user_add";
	private static final String SERVICE_CODE_USER_UPDATE="user_update";
	private static final String RESPONSE_CODE_SUCCESS="100";
	private static final String RESPONSE_CODE_AUTH_ERROR="101";
	private static final String RESPONSE_CODE_PARAM_REQUIRED="102";
	private static final String RESPONSE_CODE_USER_NOT_FOUND="103";
	private static final String RESPONSE_CODE_USER_ADD_ERROR="104";
	private static final String RESPONSE_CODE_USER_UPDATE_ERROR="105";
	private static final String RESPONSE_CODE_USER_DELETE_ERROR="106";
	private static final String LOCAL_IP="127.0.0.1";
	
	public String addUser(String auth_username,String auth_password,String username,String password,String email,String realname,String sex,String tel) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}else{
				BbsUserGroup group = bbsConfigMng.get().getRegisterGroup();
				BbsUserExt userExt=new BbsUserExt();
				userExt.setRealname(realname);
				if(StringUtils.isNotBlank(sex)){
					if(sex.equals("true")){
						userExt.setGender(true);
					}else if(sex.equals("false")){
						userExt.setGender(false);
					}
				}
				userExt.setMoble(tel);
				try {
					bbsUserMng.registerMember(username, email,false, password, LOCAL_IP, group.getId(), userExt, null);
					responseCode=RESPONSE_CODE_SUCCESS;
					bbsWebserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_ADD);
				} catch (Exception e) {
					responseCode=RESPONSE_CODE_USER_ADD_ERROR;
				}
			}
		}
		return responseCode;
	}
	
	public String updateUser(String auth_username,String auth_password,String username,String password,String email,String realname,String sex,String tel) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isBlank(username)||StringUtils.isBlank(password)||StringUtils.isBlank(username)){
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}else{
				BbsUser user=bbsUserMng.findByUsername(username);
				if(user!=null){
					try {
						Boolean sexBoolean=null;
						if(StringUtils.isNotBlank(sex)){
							if(sex.equals("true")){
								sexBoolean=true;
							}else if(sex.equals("false")){
								sexBoolean=false;
							}
						}
						bbsUserMng.updateMember(user.getId(), email, password, realname, sexBoolean, tel);
						responseCode=RESPONSE_CODE_SUCCESS;
						bbsWebserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_UPDATE);
					} catch (Exception e) {
						e.printStackTrace();
						responseCode=RESPONSE_CODE_USER_UPDATE_ERROR;
					}
				}else{
					responseCode=RESPONSE_CODE_USER_NOT_FOUND;
				}
			}
		}
		return responseCode;
	}
	
	public String delUser(String auth_username,String auth_password,String username) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isNotBlank(username)){
				BbsUser user=bbsUserMng.findByUsername(username);
				if(user!=null){
					try{
						bbsUserMng.deleteById(user.getId());
						responseCode=RESPONSE_CODE_SUCCESS;
						bbsWebserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_DELETE);
					} catch (Exception e) {
						responseCode=RESPONSE_CODE_USER_DELETE_ERROR;
					}
				}else{
					responseCode=RESPONSE_CODE_USER_NOT_FOUND;
				}
			}else{
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}
		}
		return responseCode;
	}
	
	private boolean validate(String username,String password){
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
			return false;
		}else{
			return bbsWebserviceAuthMng.isPasswordValid(username, password);
		}
	}
	
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsWebserviceAuthMng bbsWebserviceAuthMng;
	@Autowired
	private BbsWebserviceCallRecordMng bbsWebserviceCallRecordMng;
}

