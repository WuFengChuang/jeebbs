package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestCmsSiteConfig {
	private static String base = "http://192.168.0.150:8080/jeebbs5/api/admin";
	private static String appId="7166912116544627";
	private static String appKey="vDnwyGf4Ej8eCcqLkhjaHSmav2TAXGVa";
	private static String sessionKey="DD5426B97D97DA4D46E6B237CCA2D235";
	private static String aesKey="wKIFyACLEUvHnSIT";
	private static String ivKey="1yTSp6TP47uP12RK";
	
	public static void main(String[] args) {
		System.out.println(testSsoUpd());
	}
	
	private static String testSystemGet(){
		String url = base+"/site_config/system_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testSystemUpd(){
		String url = base+"/site_config/system_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+1);//id 必填
		paramBuff.append("&contextPath="+"/jeebbs5");
		paramBuff.append("&port="+"81");
		paramBuff.append("&dbFileUri="+"/dbfile.svl?n=");
		paramBuff.append("&uploadToDb="+false);
		paramBuff.append("&defImg="+"/r/cms/www/no_picture.gif");
		paramBuff.append("&allowSuffix="+"7z,aiff,asf,avi,bmp,csv,doc,docx,fla,flv,gif,gz,gzip,jpeg,jpg,mid,mov,mp3,mp4,mpc,mpeg,mpg,ods,odt,pdf,png,ppt,pxd,qt,ram,rar,rm,rmi,rmvb,rtf,sdc,sitd,swf,sxc,sxw,tar,tgz,tif,tiff,vsd,wav,wma,wmv,xls,xlsx,txt,xml,zip");
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testBaseGet(){
		String url = base+"/site_config/base_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testBaseUpd(){
		String url = base+"/site_config/base_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+1);//id 必填
		paramBuff.append("&name="+"JEEBBS论坛1");
		paramBuff.append("&shortName="+"jeebbs");
		paramBuff.append("&domain="+"192.168.0.119");
		paramBuff.append("&path="+"www");
		paramBuff.append("&domainAlias="+"192.168.0.185");
		paramBuff.append("&domainRedirect="+"");
		paramBuff.append("&corsUrl="+"http://localhost:9535");
		paramBuff.append("&relativePath="+true);
		paramBuff.append("&protocol="+"http://");
		paramBuff.append("&dynamicSuffix="+".jhtml");
		paramBuff.append("&staticSuffix="+".html");
		paramBuff.append("&localeAdmin="+"zh_CN");
		paramBuff.append("&localeFront="+"zh_CN");
		paramBuff.append("&uploadFtpId=");
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testBbsConfigGet(){
		String url = base+"/bbs_config/get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testBbsConfigUpd(){
		String url = base+"/bbs_config/update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+1);//id 必填
		paramBuff.append("&keepMinute="+1);
		paramBuff.append("&defAvatar="+1);
		paramBuff.append("&picZoomDefWidth="+300);
		paramBuff.append("&avatarWidth="+160);
		paramBuff.append("&avatarHeight="+160);
		paramBuff.append("&topicCountPerPage="+20);
		paramBuff.append("&postCountPerPage="+10);
		paramBuff.append("&topicHotCount="+0);
		paramBuff.append("&keywords="+"JEEBBS");
		paramBuff.append("&description="+"JEEBBS");
		paramBuff.append("&registerStatus="+"1");
		paramBuff.append("&registerGroupId="+"1");
		paramBuff.append("&emailValidate="+false);
		paramBuff.append("&registerRule="+"sss");
		paramBuff.append("&defaultGroupId="+1);
		paramBuff.append("&defaultActiveLevel="+2);
		paramBuff.append("&sensitivityInputOn="+true);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	
	private static String testLoginConfigGet(){
		String url = base+"/bbs_config/login_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testLoginConfigUpd(){
		String url = base+"/bbs_config/login_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("errorTimes="+4);
		paramBuff.append("&errorInterval="+30);
		paramBuff.append("&host="+"smtp.qq.com");
		paramBuff.append("&port="+"");
		paramBuff.append("&username="+"221@qq.com");
		paramBuff.append("&password="+"");
		paramBuff.append("&encoding="+"utf-8");
		paramBuff.append("&personal="+"jeecms");
		paramBuff.append("&forgotPasswordSubject="+"JEECMS会员密码找回信息");
		paramBuff.append("&forgotPasswordText="+"asd");
		paramBuff.append("&registerSubject="+"JEECMS会员注册信息");
		paramBuff.append("&registerText="+"asd");
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testExchangeGet(){
		String url = base+"/bbs_config/creditExchange_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	private static String testExchangeUpd(){
		String url = base+"/bbs_config/creditExchange_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+1);//id 必填
		paramBuff.append("&expoint="+2);
		paramBuff.append("&exprestige="+10);
		paramBuff.append("&exmoney="+10);
		paramBuff.append("&pointoutavailable="+true);
		paramBuff.append("&pointinavailable="+true);
		paramBuff.append("&prestigeoutavailable="+true);
		paramBuff.append("&prestigeinavailable="+true);
		paramBuff.append("&exchangetax="+0.2);
		paramBuff.append("&miniBalance="+0);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testChargeGet(){
		String url = base+"/bbs_config/charge_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	private static String testChargeUpd(){
		String url = base+"/bbs_config/charge_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+1);//id 必填
		paramBuff.append("&weixinAppId="+"wxc596191fde33119f");
		paramBuff.append("&weixinSecret=");
		paramBuff.append("&weixinAccount="+"1264316901");
		paramBuff.append("&weixinPassword=");
		paramBuff.append("&transferApiPassword=");
		paramBuff.append("&payTransferPassword=");
		paramBuff.append("&rewardPattern="+false);
		paramBuff.append("&rewardMin="+0.01);
		paramBuff.append("&rewardMax="+1);
		paramBuff.append("&alipayPartnerId="+"2088421700024217");
		paramBuff.append("&alipayAccount="+"48955621@qq.com");
		paramBuff.append("&alipayKey=");
		paramBuff.append("&alipayAppId="+"2016090701863111");
		paramBuff.append("&alipayPublicKey=");
		paramBuff.append("&alipayPrivateKey=");
		paramBuff.append("&chargeRatio="+0.1);
		paramBuff.append("&minDrawAmount="+1);
		paramBuff.append("&giftChargeRatio="+0);
		paramBuff.append("&attr_reward_fix_1="+1);
		paramBuff.append("&attr_reward_fix_2="+2);
		paramBuff.append("&attr_reward_fix_3="+3);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testMessageGet(){
		String url = base+"/bbs_config/message_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testMessageUpd(){
		String url = base+"/bbs_config/message_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("reportMsgAuto="+false);//id 必填
		paramBuff.append("&reportMsgTxt="+"您的举报已受理!");
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	private static String testApiGet(){
		String url = base+"/bbs_config/api_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testApiUpd(){
		String url = base+"/bbs_config/api_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("&qqEnable="+true);
		paramBuff.append("&qqID="+"1011942041");
		paramBuff.append("&qqKey="+"");
		paramBuff.append("&sinaEnable="+false);
		paramBuff.append("&sinaID="+"11");
		paramBuff.append("&sinaKey="+"");
		paramBuff.append("&qqWeboEnable="+false);
		paramBuff.append("&qqWeboID="+"333");
		paramBuff.append("&qqWeboKey="+"");
		paramBuff.append("&weixinEnable="+true);
		paramBuff.append("&weixinLoginId="+"wx797f3f026a9fbe5b");
		paramBuff.append("&weixinLoginSecret="+"");
		paramBuff.append("&weixinAppId="+"12123");
		paramBuff.append("&weixinAppSecret="+"");
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testSsoGet(){
		String url = base+"/bbs_config/sso_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testSsoUpd(){
		String url = base+"/bbs_config/sso_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("&attr_ssoEnable="+false);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	
	private static String testItemList(){
		String url = base+"/bbs_config/item_list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testItemGet(){
		String url = base+"/bbs_config/item_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+6);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testItemSave(){
		String url = base+"/bbs_config/item_save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("field="+"123123");
		paramBuff.append("&label="+"123123");
		paramBuff.append("&dataType="+"1");
		paramBuff.append("&priority="+"10");
		paramBuff.append("&help="+"");
		paramBuff.append("&helpPosition="+"1");
		paramBuff.append("&defValue="+"");
		paramBuff.append("&size="+"");
		paramBuff.append("&optValue="+"");
		paramBuff.append("&rows="+"");
		paramBuff.append("&cols="+"");
		paramBuff.append("&required="+false);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testItemUdp(){
		String url = base+"/bbs_config/item_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+8);
		paramBuff.append("&field="+"8");
		paramBuff.append("&label="+"8");
		paramBuff.append("&dataType="+"1");
		paramBuff.append("&priority="+"90");
		paramBuff.append("&help="+"");
		paramBuff.append("&helpPosition="+"1");
		paramBuff.append("&defValue="+"");
		paramBuff.append("&size="+"");
		paramBuff.append("&optValue="+"");
		paramBuff.append("&rows="+"");
		paramBuff.append("&cols="+"");
		paramBuff.append("&required="+false);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testItemDel(){
		String url = base+"/bbs_config/item_delete";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("ids="+"7,8");
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testItemPriority(){
		String url = base+"/bbs_config/item_priority";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("ids="+"9,10");
		paramBuff.append("&priorities="+"6,7");
		paramBuff.append("&labels="+"111,222");
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testADGet(){
		String url = base+"/bbs_config/ad_get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	private static String testADUpd(){
		String url = base+"/bbs_config/ad_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("adDayCharge="+1.02);
		paramBuff.append("&adClickCharge="+10);
		paramBuff.append("&adDisplayCharge="+10);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
}
