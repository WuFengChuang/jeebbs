package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;

import com.jeecms.common.upload.FileUpload;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsTemplate {

	public static void main(String[] args) {
		//testTemplateTree();
		//testTemplateList();
		//testTemplateCreateDir();
		//testTemplateGet();
		//testSaveTpl();
		//testUpdateTpl();
		//testDelTpls();
		//testRenameTpl();
		//testGetSolutions();
		//testSolutionupdate();
		//testTplImport();
		testUploadTpl();
	}
	
	private static String testUploadTpl(){
		String url="http://192.168.0.173:8080/jeebbs5/api/admin/template/upload";
		FileUpload fileUpload = new FileUpload();
		String filePath="D:\\test\\1.jpg";
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		StringBuffer paramBuff=new StringBuffer();
		Map<String, String>param=new HashMap<String, String>();
		paramBuff.append("appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		String root="/WEB-INF/t/cms/www";
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		String result ="";
		try {
			result= fileUpload.uploadFile(root,
					url, filePath, appId, encryptSessionKey,nonce_str, sign);
			JSONObject json=new JSONObject(result);
			System.out.println(json);
			String status=(String) json.get("status");
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String testTemplateTree(){
		String url="http://192.168.0.173:8080/jeebbs5/api/admin/template/tree";
		StringBuffer paramBuff=new StringBuffer();
		//paramBuff.append("root=/WEB-INF/t/cms/www/blue/common");
		paramBuff.append("appId="+appId);
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
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testTemplateList(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/list";
		StringBuffer paramBuff=new StringBuffer();
		//paramBuff.append("root=/WEB-INF/t/cms/www/blue/common");
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
		System.out.println("encryptSessionKey->"+encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testTemplateCreateDir(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/dir_save";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("root=/WEB-INF/t/cms/www/blue/");
		//paramBuff.append("dirName=test");
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
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println("encryptSessionKey->"+encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testTemplateGet(){
		String url="http://192.168.0.173:8080/jeebbs5/api/admin/template/get";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("root=/WEB-INF/t/cms/www/blue/index");
		paramBuff.append("&name=/WEB-INF/t/cms/www/blue/index/首页.html");
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
		//String sign=PayUtil.createSign(param, appKey);
		//param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	
	private static String testSaveTpl(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/save";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("root=/WEB-INF/t/cms/www/blue/common");
		paramBuff.append("&filename=test");
		paramBuff.append("&source=test11");
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
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testUpdateTpl(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/update";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("root=/WEB-INF/t/cms/www/blue/common");
		paramBuff.append("&filename=test");
		paramBuff.append("&source=test112222");
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
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	
	private static String testDelTpls(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/delete";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("names=/WEB-INF/t/cms/www/blue/common/test2.html");
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
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println("encryptSessionKey->"+encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testRenameTpl(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/rename";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("origName=/WEB-INF/t/cms/www/blue/common/test.html");
		paramBuff.append("&distName=/WEB-INF/t/cms/www/blue/common/test2.html");
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
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testGetSolutions(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/getSolutions";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("appId="+appId);
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
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testSolutionupdate(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/solutionupdate";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("solution=blue");
		paramBuff.append("&mobile=true");
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
		System.out.println(encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testTplImport(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/template/importTpl";
		FileUpload fileUpload = new FileUpload();
		String filePath="D:\\test\\1.zip";
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		StringBuffer paramBuff=new StringBuffer();
		Map<String, String>param=new HashMap<String, String>();
		paramBuff.append("appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		String result ="";
		try {
			result= fileUpload.uploadFileWithHttpMime(null,
					url, filePath, appId, encryptSessionKey,nonce_str, sign);
			JSONObject json=new JSONObject(result);
			String status=(String) json.get("status");
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="675C1096CB0CC563EC6A5DE51BD012F9";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
}
