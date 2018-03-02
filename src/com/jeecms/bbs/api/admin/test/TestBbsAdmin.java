package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsAdmin {
	private static String base = "http://192.168.0.173:8080/jeebbs5/api/admin";
	private static String appId="7166912116544627";
	private static String appKey="vDnwyGf4Ej8eCcqLkhjaHSmav2TAXGVa";
	private static String sessionKey="5DF7242082195984398F0DC985486C2A";
	private static String aesKey="wKIFyACLEUvHnSIT";
	private static String ivKey="1yTSp6TP47uP12RK";
	
	
	public static void main(String[] args) {
		//System.out.println(testAdminList());
		System.out.println(testAdminSave());
	}
	
	private static String testAdminList(){
		String url = base+"/admin/list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("pageNo="+1);
		paramBuff.append("&pageSize="+10);
		paramBuff.append("&queryUsername=");
		paramBuff.append("&queryEmail=");
		paramBuff.append("&queryDisabled="+false);
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
	
	//管理员与会员查询详情接口相同，通过查询后的admin参数可进行区分是否为管理员
	private static String testAdminGet(){
		String url = base+"/user/get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+5);
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
	
	private static String testAdminSave(){
		String url = base+"/admin/save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("username="+"ceshi4");
		paramBuff.append("&email="+"asd@qq.com");
		paramBuff.append("&password="+"5");
		paramBuff.append("&groupId="+5);
		paramBuff.append("&rank="+5);
		paramBuff.append("&realname="+5);
		paramBuff.append("&gender="+"true");
		paramBuff.append("&roleIds="+"1");
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
		//param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testAdminUpd(){
		String url = base+"/admin/update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+41);
		paramBuff.append("&email=");
		paramBuff.append("&password="+"6");
		paramBuff.append("&groupId="+5);
		paramBuff.append("&rank="+5);
		paramBuff.append("&disabled="+true);
		paramBuff.append("&realname="+5);
		paramBuff.append("&gender="+"true");
		paramBuff.append("&roleIds="+"1");
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
	
	private static String testAdminDel(){
		String url=base+"/user/delete";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"41,39");
		paramBuff.append("&appId="+appId);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
}
