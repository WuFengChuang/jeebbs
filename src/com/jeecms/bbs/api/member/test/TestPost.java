package com.jeecms.bbs.api.member.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestPost {
	private static String base="http://192.168.0.150:8080/jeebbs5/api/member";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="375F2649FEA8BD78E9B81615ED93CEDD";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		testPostlist();
	}
	
	private static String testPostCheck(){
		String url=base+"/post/check";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("postId="+1);
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
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	private static String testPostlist(){
		String url=base+"/post/list";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("checkStatus="+false);
		paramBuff.append("&https=");
		paramBuff.append("&pageNo="+1);
		paramBuff.append("&pageSize="+10);
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
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testPostSave(){
		String url=base+"/post/save";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("topicId="+100);
		paramBuff.append("&title="+"测试API帖子2");
		//是否包含附件
		paramBuff.append("&hasAttach="+false);
		//上传附件
		String att1="[localimg]1[/localimg]";//附件占位符
		String att2="[localimg]2[/localimg]";//附件占位符
		String att1Path="d:\\test\\1.doc";//附件路径
		String att2Path="d:\\test\\1.jpg";//附件路径
		paramBuff.append("&content="+"测试API帖子2"+att1+"sdfadf"+att2);
		paramBuff.append("&postLatitude="+"27.67");
		paramBuff.append("&postLongitude="+"114.87");
		//paramBuff.append("&content="+"测试API帖子1");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
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
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		/*
		 * 上传附件
		*/
		JSONObject json;
		try {
			json = new JSONObject(res);
			String status=(String) json.get("status");
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
				String postId=(String) bodyJson.get("id");
				TestUpload.uploadAttach(Integer.parseInt(postId), att1, "attach", att1Path);
				TestUpload.uploadAttach(Integer.parseInt(postId), att2, "image", att2Path);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private static String testPostUpdate(){
		String url=base+"/post/update";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("postId="+144);
		paramBuff.append("&title="+"测试API帖子33333");
		//是否包含附件
		paramBuff.append("&hasAttach="+false);
		//上传附件
		String att1="[localimg]1[/localimg]";//附件占位符
		String att2="[localimg]2[/localimg]";//附件占位符
		String att1Path="d:\\test\\1.doc";//附件路径
		String att2Path="d:\\test\\1.jpg";//附件路径
		paramBuff.append("&content="+"测试API帖子33333"+att1+"sdfadf"+att2+"3333333333333");
		//paramBuff.append("&content="+"测试API帖子1");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
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
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		/*
		 * 上传附件
		*/
//		JSONObject json;
//		try {
//			json = new JSONObject(res);
//			String status=(String) json.get("status");
//			if(status.equals("true")){
//				JSONObject bodyJson= (JSONObject) json.get("body");
//				String postId=(String) bodyJson.get("id");
//				TestUpload.uploadAttach(Integer.parseInt(postId), att1, "attach", att1Path);
//				TestUpload.uploadAttach(Integer.parseInt(postId), att2, "image", att2Path);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		return res;
	}
	
	private static String testPostGrade(){
		String url=base+"/post/grade";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("postId="+70);
		paramBuff.append("&score="+2);
		paramBuff.append("&reason="+"非常好");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
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
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testPostShield(){
		String url=base+"/post/shield";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("postId="+144);
		paramBuff.append("&statu="+1);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
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
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testPostUp(){
		String url=base+"/post/up";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("postId="+236);
		//0点赞 3取消
		paramBuff.append("&operate="+3);
		//paramBuff.append("&operate="+0);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
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
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testPostDelete(){
		String url=base+"/post/delete";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("forumId="+2);
		paramBuff.append("&ids="+"227,228");
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
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
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
}
