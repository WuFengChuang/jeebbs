package com.jeecms.bbs.api.front.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestTopic {

	public static void main(String[] args) {
//		testTopicSave();
		//testVoteTopicSave();
		//testTopicVote();
		//testTopicGet();
		//testTopicTop();
		//testTopicPrime();
		//testTopicLight();
		//锁定
		//testTopicOpe(1);
		//解锁
		//testTopicOpe(2);
		//移动
		//testTopicOpe(3);
		//屏蔽
		//testTopicOpe(4);
		//解开屏蔽
		//testTopicOpe(5);
		//提升下沉
		//testTopicOpe(6);
		//删除帖子
		//testTopicDelete();
		//购买
		//testTopicBuy();
		//打赏
		//testTopicReward();
		// 0点赞 1收藏 2关注  3取消点赞 4取消收藏 5取消关注 
		//testTopicOperate(0);
		//testTopicOperate(3);
		//testTopicOperate(1);
		//testTopicOperate(4);
		//testTopicOperate(2);
		//testTopicOperate(5);
		testNesTopicList();
	}
	
	private static String testTopicSave(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/save";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("forumId="+1);
		paramBuff.append("&title="+"测试API主题10");
		//是否包含附件
		paramBuff.append("&hasAttach="+false);
		/* 上传附件
		String att1="[localimg]1[/localimg]";//附件占位符
		String att2="[localimg]2[/localimg]";//附件占位符
		String att1Path="d:\\test\\1.doc";//附件路径
		String att2Path="d:\\test\\1.jpg";//附件路径
		paramBuff.append("&content="+"测试API主题内容9"+att1+"sdfadf"+att2);
		*/
		paramBuff.append("&content="+"测试API主题内容10");
		paramBuff.append("&postLatitude="+"28.67");
		paramBuff.append("&postLongitude="+"115.87");
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
		System.out.println(param);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		/*
		 * 上传附件
		JSONObject json;
		try {
			json = new JSONObject(res);
			String status=(String) json.get("status");
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
				String firstPostId=(String) bodyJson.get("firstPostId");
				TestUpload.uploadAttach(Integer.parseInt(firstPostId), att1, "attach", att1Path);
				TestUpload.uploadAttach(Integer.parseInt(firstPostId), att2, "image", att2Path);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		*/
		
		return res;
	}
	
	private static String testVoteTopicSave(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/save";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("forumId="+1);
		paramBuff.append("&title="+"您的爱好有哪些?");
		paramBuff.append("&content="+"您的爱好有哪些?");
		//是否包含附件
		paramBuff.append("&hasAttach="+false);
		//投票贴
		paramBuff.append("&category="+101);
		//1多选 2单选
		paramBuff.append("&categoryType="+1);
		paramBuff.append("&items="+"玩游戏,旅游,看书,打球");
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
	
	private static String testTopicVote(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/vote";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+93);
		paramBuff.append("&itemIds="+"5,6");
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
	
	private static String testTopicGet(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/get";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+93);
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
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testTopicTop(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/top";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+93);
		paramBuff.append("&forumId="+1);
		paramBuff.append("&topLevel="+1);
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
	
	private static String testTopicPrime(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/prime";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"91,90");
		paramBuff.append("&forumId="+1);
		paramBuff.append("&primeLevel="+1);
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
	
	private static String testTopicLight(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/light";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"91,90");
		paramBuff.append("&forumId="+1);
		//颜色
		paramBuff.append("&color="+"FF0000");
		//加粗
		paramBuff.append("&bold="+true);
		//斜体
		paramBuff.append("&italic="+true);
		//有效时间
		paramBuff.append("&time="+"2017-02-25 13:56:08");
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
	
	private static String testTopicOpe(Integer operate){
		String url="";
		if(operate==1){
			url="http://192.168.0.173:8080/jeebbs5/api/front/topic/lock";
		}else if(operate==2){
			url="http://192.168.0.173:8080/jeebbs5/api/front/topic/open";
		}else if(operate==3){
			url="http://192.168.0.173:8080/jeebbs5/api/front/topic/move";
		}else if(operate==4){
			url="http://192.168.0.173:8080/jeebbs5/api/front/topic/shield";
		}else if(operate==5){
			url="http://192.168.0.173:8080/jeebbs5/api/front/topic/shieldOpen";
		}else if(operate==6){
			url="http://192.168.0.173:8080/jeebbs5/api/front/topic/upOrDown";
		}
		
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"91,90");
		if(operate.equals(3)){
			paramBuff.append("&forumId="+2);
		}else{
			paramBuff.append("&forumId="+1);
		}
		
		if(operate==6){
			//提升或下沉时间
			paramBuff.append("&time="+"2017-02-20 13:56:08");
		}
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
	
	private static String testTopicDelete(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/delete";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"73");
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
	
	private static String testTopicBuy(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/buy";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+"67");
		paramBuff.append("&outOrderNum="+"4007062001201702160192676013");
		//1微信支付 2支付宝支付
		paramBuff.append("&orderType="+1);
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
	
	private static String testTopicReward(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/reward";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+"67");
		paramBuff.append("&outOrderNum="+"2017020821001004960205652832");
		//1微信支付 2支付宝支付
		paramBuff.append("&orderType="+2);
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
	
	private static String testTopicOperate(Integer operate){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/topic/operate";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("topicId="+"94");
		paramBuff.append("&operate="+operate);
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
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testNesTopicList(){
		String url=base+"/topic/list";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("topLevel="+1);
		paramBuff.append("&statu="+-1);
		paramBuff.append("&orderBy="+8);
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
			param.put(keyValue[0], keyValue[1]);
		}
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	private static String base="http://192.168.0.150:8080/jeebbs5/api/front";
	
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="CD214C0B91775FB8B028920BE4D43AAB";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
}
