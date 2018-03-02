package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsUser {
	private static String base = "http://192.168.0.150:8080/jeebbs5/api/admin";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="356FEA16E1D5F431D7F9BD9A29646E64";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		System.out.println(testUserGet());
	}
	
	private static String testUserList(){
		String url = base+"/user/list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("pageNo="+1);
		paramBuff.append("&pageSize="+4);
		paramBuff.append("&username=");
		paramBuff.append("&groupId=");
		paramBuff.append("&lastLoginDay=");
		paramBuff.append("&orderBy=");
		paramBuff.append("&all=");
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
	
	private static String testUserGet(){
		String url = base+"/user/get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+0);
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
	
	//官网账户添加将official属性更改为true
	private static String testUserSave(){
		String url = base+"/user/save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("username="+"ceshi999");
		paramBuff.append("&email="+"123@163.com");
		paramBuff.append("&password="+"123");
		paramBuff.append("&groupId="+4);
		paramBuff.append("&realname="+"ceshimame");
		paramBuff.append("&gender="+false);
		paramBuff.append("&birthday="+"2017-9-2");
		paramBuff.append("&comefrom="+"123123");
		paramBuff.append("&qq="+"363535358");
		paramBuff.append("&msn="+"23131");
		paramBuff.append("&official="+false);
		paramBuff.append("&phone="+"13564658545");
		paramBuff.append("&moble="+"13546854575");
		paramBuff.append("&intro="+"ceshi");
		paramBuff.append("&attr_test="+"attr");
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
	
	private static String testUserUpd(){
		String url = base+"/user/update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+"43");
		paramBuff.append("&email="+"123@163.com");
		paramBuff.append("&password="+"123");
		paramBuff.append("&groupId="+4);
		paramBuff.append("&realname="+"ceshimame");
		paramBuff.append("&gender="+false);
		paramBuff.append("&birthday="+"2017-09-03");
		paramBuff.append("&comefrom="+"123123");
		paramBuff.append("&qq="+"363535358");
		paramBuff.append("&msn="+"23131");
		paramBuff.append("&phone="+"13564658545");
		paramBuff.append("&moble="+"13546854575");
		paramBuff.append("&intro="+"ceshi");
		paramBuff.append("&attr_tests="+"sss");
		paramBuff.append("&disabled="+false);
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
	
	private static String testUserDel(){
		String url=base+"/admin/delete";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"77,78");
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
	
	private static String testOfficialList(){
		String url = base+"/user/official_list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("pageNo="+1);
		paramBuff.append("&pageSize="+10);
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
	
	private static String testOfficialSave(){
		String url = base+"/user/save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("username="+"official");
		paramBuff.append("&email="+"123@163.com");
		paramBuff.append("&password="+"123");
		paramBuff.append("&groupId="+4);
		paramBuff.append("&realname="+"ceshimame");
		paramBuff.append("&gender="+false);
		paramBuff.append("&birthday="+"2017-9-2");
		paramBuff.append("&comefrom="+"123123");
		paramBuff.append("&qq="+"363535358");
		paramBuff.append("&msn="+"23131");
		paramBuff.append("&official="+true);
		paramBuff.append("&phone="+"13564658545");
		paramBuff.append("&moble="+"13546854575");
		paramBuff.append("&intro="+"ceshi");
		paramBuff.append("&attr_test="+"attr");
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
	
	private static String testUserFindByUsername(){
		String url = base+"/user/comparison_username";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("username="+"jiasudu");
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
}
