package com.jeecms.common.email;

/**
 * 邮件模板
 * 
 * @author tom
 * 
 */
public interface MessageTemplate {
	/**
	 * 找回密码主题
	 * 
	 * @return
	 */
	public String getForgotPasswordSubject();

	/**
	 * 找回密码内容
	 * 
	 * @return
	 */
	public String getForgotPasswordText();
	
	/**
	 * 会员注册主题
	 * 
	 * @return
	 */
	public String getRegisterSubject();

	/**
	 * 会员注册内容
	 * 
	 * @return
	 */
	public String getRegisterText();

	/**
	 * 服务到期主题
	 * 
	 * @return
	 */
	public String getServiceExpirationSubject();

	/**
	 * 服务到期内容
	 * 
	 * @return
	 */
	public String getServiceExpirationText();
}
