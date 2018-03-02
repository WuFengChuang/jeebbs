package com.jeecms.bbs.api.front.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestUser {

	public static void main(String[] args) {
		//testLogin();
		//testGetUserStatus();
		//testSaveUser();
		//testUpdateUser();
		//testPasswdEdit();
		//testUserGet();
		testForgotPWD();
		//testGetUserPerms();
	}
	
	private static String testForgotPWD(){
		String url="http://192.168.0.150:80/jeebbs5/api/front/user/forgot_password";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"123");
		paramBuff.append("&email="+"123");
		paramBuff.append("&captcha="+"1234");
		paramBuff.append("&appId="+appId);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println(param);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testGetUserPerms(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/user/getPerms";
		StringBuffer paramBuff=new StringBuffer();
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println(param);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testLogin(){
		String url="http://192.168.0.140:8080/jeebbs5/api/front/user/login?";
		StringBuffer paramBuff=new StringBuffer();
		String password="password";
		paramBuff.append("username="+"admin");
		try {
			paramBuff.append("&aesPassword="+AES128Util.encrypt(password, aesKey,ivKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println(res);
		JSONObject json;
		try {
			json = new JSONObject(res);
			String sessionKey=(String) json.get("body");
			try {
				String descryptKey=AES128Util.decrypt(sessionKey, aesKey,ivKey);
				System.out.println(descryptKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private static String testLogout(){
		String url="http://192.168.0.173:8080/jeebbs5/api/member/user/logout?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"jiasudu");
		try {
			paramBuff.append("&sessionKey="+AES128Util.encrypt(sessionKey, aesKey,ivKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res-->"+res);
		return res;
	}
	
	private static String testGetUserStatus(){
		String url="http://192.168.0.140:8080/jeebbs5/api/front/user/getStatus?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"admin");
		try {
			paramBuff.append("&sessionKey="+AES128Util.encrypt(sessionKey, aesKey,ivKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println(res);
		JSONObject json;
		try {
			json = new JSONObject(res);
			String message=(String) json.get("message");
			System.out.println(message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(res);
		return res;
	}
	
	private static String testUserGet(){
		String url="http://192.168.0.140:8080/jeebbs5/api/front/user/get";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"admin");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		//String nonce_str="ofIcgEJdPN7FoGVY";
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
	//	url=url+paramBuff.toString();
		//String res=HttpClientUtil.getInstance().get(url);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		JSONObject json;
		try {
			json = new JSONObject(res);
			/*
			String sessionKey=(String) json.get("body");
			try {
				String descryptKey=AES128Util.decrypt(sessionKey, aesKey);
				System.out.println(descryptKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			System.out.println(json.get("body"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(res);
		return res;
	}
	
	private static String testSaveUser(){
		String url="http://192.168.0.140:8080/jeebbs5/api/front/user/add?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"test1112");
		paramBuff.append("&email="+"test1@qq.com");
		paramBuff.append("&loginPassword="+"password");
		paramBuff.append("&realname="+"realname");
		paramBuff.append("&gender="+true);
		paramBuff.append("&birthdayStr="+"1982-05-09");
		paramBuff.append("&phone=0791-88888888");
		paramBuff.append("&mobile=13888888888");
		paramBuff.append("&qq=123456");
		paramBuff.append("&userImg=/user/1.png");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testUpdateUser(){
		String url="http://192.168.0.140:8080/jeebbs5/api/front/user/edit?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"test1112");
		paramBuff.append("&realname="+"realname1");
		paramBuff.append("&gender="+false);
		paramBuff.append("&birthdayStr="+"1983-06-10");
		paramBuff.append("&phone=0791-77777777");
		paramBuff.append("&mobile=13899999999");
		paramBuff.append("&qq=1234561");
		paramBuff.append("&userImg=/user/2.png");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testPasswdEdit(){
		String url="http://192.168.0.140:8080/jeebbs5/api/front/user/pwd?";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("username="+"test1112");
		paramBuff.append("&origPwd="+"password");
		paramBuff.append("&newPwd=123456");
		paramBuff.append("&email="+"112@qq.com");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		url=url+paramBuff.toString();
		String res=HttpClientUtil.getInstance().get(url);
		System.out.println("res->"+res);
		return res;
	}

	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="698BA37BC12E153B61D943E4DD6EB388";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";

}
