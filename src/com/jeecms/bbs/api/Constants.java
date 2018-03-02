package com.jeecms.bbs.api;

public class Constants {
	
	public static final String COMMON_PARAM_APPID="appId";
	
	public static final String COMMON_PARAM_SESSIONKEY="sessionKey";
	
	public static final String COMMON_PARAM_SIGN="sign";
	
	
	public static final String THIRD_SOURCE_WEIXIN_APP="weixinApp";
	
	//内容地址返回http
	public static final int URL_HTTP=1;
	//内容地址返回https
	public static final int URL_HTTPS=0;
	//超时时间
	public static final int USER_OVER_TIME=20;
	/**
	 * 图片占位符开始
	 */
	public static final String 	API_PLACEHOLDER_IMAGE_BEGIN="_img_start_";
	/**
	 * 图片占位符结束
	 */
	public static final String 	API_PLACEHOLDER_IMAGE_END="_img_end_";
	/**
	 * 视频占位符开始
	 */
	public static final String 	API_PLACEHOLDER_LINK_BEGIN="_link_start_";
	/**
	 * 视频占位符结束
	 */
	public static final String 	API_PLACEHOLDER_LINK_END="_link_end_";
	/**
	 * API接口调用状态-成功
	 */
	public static final String 	API_STATUS_SUCCESS="\"true\"";
	public static final String 	G_API_STATUS_SUCCESS="true";
	/**
	 * API接口调用状态失败
	 */
	public static final String 	API_STATUS_FAIL="\"false\"";
	public static final String 	G_API_STATUS_FAIL="false";
	/**
	 * API接口消息-成功
	 */
	public static final String 	API_MESSAGE_SUCCESS="\"success\"";
	public static final String 	G_API_MESSAGE_SUCCESS="success";
	/**
	 * API接口消息-缺少参数
	 */
	public static final String 	API_MESSAGE_PARAM_REQUIRED="\"param required\"";
	public static final String 	G_API_MESSAGE_PARAM_REQUIRED="param required";
	/**
	 * API接口消息-参数错误
	 */
	public static final String 	API_MESSAGE_PARAM_ERROR="\"param error\"";
	public static final String 	G_API_MESSAGE_PARAM_ERROR="param error";
	
	/**
	 * API接口消息-APPID错误或被禁用
	 */
	public static final String 	API_MESSAGE_APP_PARAM_ERROR="\"appId not exist or appId disabled\"";
	/**
	 * API接口消息-签名认证错误
	 */
	public static final String 	API_MESSAGE_SIGN_VALIDATE_ERROR="\"sign validate error\"";
	/**
	 * API接口消息-用户未找到
	 */
	public static final String 	API_MESSAGE_USER_NOT_FOUND="\"user not found\"";
	/**
	 * API接口消息-主题未找到
	 */
	public static final String 	API_MESSAGE_TOPIC_NOT_FOUND="\"topic not found\"";
	/**
	 * API接口消息-帖子未找到
	 */
	public static final String 	API_MESSAGE_POST_NOT_FOUND="\"post not found\"";
	/**
	 * API接口消息 -不能删除
	 */
	public static final String 	API_MESSAGE_DELETE_ERROR="\"delete error\"";
	/**
	 * API接口消息 -消息未找到
	 */
	public static final String 	API_MESSAGE_MSG_NOT_FOUND="\"message not found\"";
	/**
	 * API接口消息-用户未登录
	 */
	public static final String 	API_MESSAGE_USER_NOT_LOGIN="\"user not login\"";
	/**
	 * API接口消息-道具功能已关闭
	 */
	public static final String 	API_MESSAGE_MAGIC_CLOSE="\"magic close\"";
	/**
	 * API接口消息-SESSION错误
	 */
	public static final String 	API_MESSAGE_SESSION_ERROR="\"session error\"";
	/**
	 * API接口消息-密码错误
	 */
	public static final String 	API_MESSAGE_PASSWORD_ERROR="\"password error\"";
	/**
	 * API接口消息-用户名已存在
	 */
	public static final String 	API_MESSAGE_USERNAME_EXIST="\"username exist\"";
	/**
	 * API接口消息-原密码错误
	 */
	public static final String 	API_MESSAGE_ORIGIN_PWD_ERROR="\"origin password invalid\"";
	/**
	 * API接口消息-上传错误
	 */
	public static final String 	API_MESSAGE_UPLOAD_ERROR="\"upload file error!\"";
	/**
	 * API接口消息-订单编号已经使用
	 */
	public static final String 	API_MESSAGE_ORDER_NUMBER_USED="\"order number used\"";
	/**
	 * API接口消息-订单编号错误
	 */
	public static final String 	API_MESSAGE_ORDER_NUMBER_ERROR="\"order number error\"";
	/**
	 * API接口消息-订单金额不足
	 */
	public static final String 	API_MESSAGE_ORDER_AMOUNT_NOT_ENOUGH="\"order amount not enough\"";
	/**
	 * API接口消息-重复请求API
	 */
	public static final String 	API_MESSAGE_REQUEST_REPEAT="\"request api repeat\"";
	/**
	 * API接口消息-礼物数量不足
	 */
	public static final String 	API_MESSAGE_GIFT_NOT_ENOUGH="\"gift not enough\"";
	/**
	 * API接口消息-用户没有权限
	 */
	public static final String 	API_MESSAGE_USER_NOT_HAS_PERM="\"user has not perm\"";
	/**
	 * API接口消息-用户超时
	 */
	public static final String 	API_MESSAGE_USER_OVER_TIME="\"user over time\"";
	/**
	 * API接口消息-用户身份伪造
	 */
	public static final String 	API_MESSAGE_USER_FORGERY="\"user identity forgery\"";
	/**
	 * API接口消息 -对象未找到
	 */
	public static final String 	API_MESSAGE_OBJECT_NOT_FOUND="\"object not found\"";
	/**
	 * API接口调用服务器响应错误
	 */
	public static final String 	API_STATUS_APPLICATION_ERROR="\"application error\"";
	/**
	 * API接口消息-文件不存在
	 */
	public static final String API_MESSAGE_FILE_NOT_FOUNT="\"file not found\"";
	/**
	 * API接口消息-插件被使用中
	 */
	public static final String API_MESSAGE_PLUG_IN_USED="\"plug in used\"";
	/**
	 * API接口消息-插件新功能冲突
	 */
	public static final String API_MESSAGE_PLUG_CONFLICT="\"plug conflict\"";
	/**
	 * API接口消息-不能卸载
	 */
	public static final String API_MESSAGE_CANNOT_UNINSTALL="\"can not uninstall\"";
	/**
	 * API接口消息-非版主
	 */
	public static final String 	API_MESSAGE_NOT_MODERATOR="\"you are not moderator\"";
	/**
	 * API接口消息-用户未设置邮箱
	 */
	public static final String API_MESSAGE_EMAIL_NOT_SET = "\"user email not set\"";
	/**
	 * API接口消息-用户输入错误
	 */
	public static final String API_MESSAGE_INPUT_ERROR = "\"input error\"";
	/**
	 * API接口消息-服务器邮箱设置错误
	 */
	public static final String API_MESSAGE_EMAIL_SETTING_ERROR="\"server mailbox setting error\"";
	/**
	 * API接口消息-服务器邮箱模板错误
	 */
	public static final String API_MESSAGE_EMAIL_TEMPLATE_ERROR="\"server mailbox template error\"";
	/**
	 * API接口消息-发送邮件异常
	 */
	public static final String API_MESSAGE_EMAIL_SEND_EXCEPTION="\"email send exception\"";
	/**
	 * API接口消息-板块地址已存在
	 */
	public static final String API_MESSAGE_FORUM_PATH_EXIST="\"forum path exist\"";
	/**
	 * API接口消息-live 审核中
	 */
	public static final String API_MESSAGE_LIVE_ERROR_CHECKING="\"live.error.checking\"";
	/**
	 * API接口消息-live 审核未通过
	 */
	public static final String API_MESSAGE_LIVE_ERROR_REJECT="\"live.error.reject\"";
	/**
	 * API接口消息-live 已关停
	 */
	public static final String API_MESSAGE_LIVE_ERROR_STOP="\"live.error.stop\"";
	
	/**
	 * API接口消息-非您的live订单
	 */
	public static final String API_MESSAGE_LIVE_ERROR_NOTYOURORDER="\"live.error.notYourOrder\"";
	/**
	 * API接口消息-live票不足
	 */
	public static final String API_MESSAGE_LIVE_ERROR_HASNOTENOUGHTICKET="\"live.error.hasNotEnoughTicket\"";
	
	public static final String API_MESSAGE_ACCOUNT_DISABLED="\"\"";
	
	public static final String 	API_ARRAY_SPLIT_STR=",";
	public static final String 	API_LIST_SPLIT_STR=";";
}
