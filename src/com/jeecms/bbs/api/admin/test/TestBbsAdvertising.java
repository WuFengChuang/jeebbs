package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsAdvertising {
	private static String base = "http://192.168.0.150:8080/jeebbs5/api/admin";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="632D1E3D53CD5690C30AE1C58EA2464B";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		System.out.println(testAdvertisingList());
	}
	
	private static String testAdvertisingList(){
		String url = base+"/advertising/list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("pageNo="+1);
		paramBuff.append("&pageSize="+10);
		paramBuff.append("&queryChargeMode=");//Integer
		paramBuff.append("&queryTitle=");//String
		paramBuff.append("&orderBy=");//Integer
		paramBuff.append("&https=");//Integer
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
	
	private static String testAdvertisingGet(){
		String url = base+"/advertising/get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+38);
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
	
	
	private static String testAdvertisingSave(){
		String url = base+"/advertising/save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("name="+"广告测试1");//名称 必填
		paramBuff.append("&username="+"hq");//广告主
		paramBuff.append("&adspaceId="+5);//版位编号 必填 1 2 3 4
		paramBuff.append("&category="+"code");//类型 image/flash/text/code
		
		//category=image
		paramBuff.append("&attr_image_url="+"");//图片地址
		paramBuff.append("&attr_image_width="+"");//图片宽
		paramBuff.append("&attr_image_height="+"");//图片高
		paramBuff.append("&attr_image_link="+"");//链接地址
		paramBuff.append("&attr_image_title="+"");//链接提示
		paramBuff.append("&attr_image_target="+"");//链接目标 新窗口(_blank)/原窗口(_self)
		
		//category=flash
		paramBuff.append("&attr_flash_url="+"");//FLASH地址
		paramBuff.append("&attr_flash_width="+"");//FLASH宽
		paramBuff.append("&attr_flash_height="+"");//FLASH高
		
		
		//category=text
		paramBuff.append("&attr_text_title="+"");//文字内容
		paramBuff.append("&attr_text_link="+"");//文字链接
		paramBuff.append("&attr_text_color="+"");//文字颜色 
		paramBuff.append("&attr_text_font="+"");//文字大小 12px
		paramBuff.append("&attr_text_target="+"");//链接目标
		
		//category=code
		paramBuff.append("&code="+"321321");//代码内容
		
		paramBuff.append("&chargeMode="+1);//计费方式 0周期计费 1点击量计费 2展现量收费
		paramBuff.append("&chargeDay=");//广告天数
		paramBuff.append("&startTime="+"2017-09-06");//开始时间
		paramBuff.append("&chargeAmount="+2000);//费用
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
	
	private static String testAdvertisingUdp(){
		String url = base+"/advertising/update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+"19");//id 必填
		paramBuff.append("&name="+"hq");//名称 必填
		paramBuff.append("&adspaceId="+1);//版位编号 必填 1 2 3 4 ----不可改
		paramBuff.append("&category="+"code");//类型 image/flash/text/code
		
		//category=image
		paramBuff.append("&attr_image_url="+"");//图片地址
		paramBuff.append("&attr_image_width="+"");//图片宽
		paramBuff.append("&attr_image_height="+"");//图片高
		paramBuff.append("&attr_image_link="+"");//链接地址
		paramBuff.append("&attr_image_title="+"");//链接提示
		paramBuff.append("&attr_image_target="+"");//链接目标 新窗口(_blank)/原窗口(_self)
		
		//category=flash
		paramBuff.append("&attr_flash_url="+"");//FLASH地址
		paramBuff.append("&attr_flash_width="+"");//FLASH宽
		paramBuff.append("&attr_flash_height="+"");//FLASH高
		
		
		//category=text
		paramBuff.append("&attr_text_title="+"");//文字内容
		paramBuff.append("&attr_text_link="+"");//文字链接
		paramBuff.append("&attr_text_color="+"");//文字颜色 
		paramBuff.append("&attr_text_font="+"");//文字大小 12px
		paramBuff.append("&attr_text_target="+"");//链接目标
		
		//category=code
		paramBuff.append("&code="+"1231");//代码内容
		
		paramBuff.append("&chargeDay="+12);//广告天数   ----不可改
		paramBuff.append("&startTime="+"2017-09-06");//开始时间   ----不可改
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
	
	private static String testAdvertisingDel(){
		String url = base+"/advertising/delete";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("ids="+"19,18");//id 必填
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
	private static String testAdvertisingGetCharge(){
		String url = base+"/advertising/get_charge_amount";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("chargeDay="+10);//id 必填
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
	private static String testAdvertisingCheckCharge(){
		String url = base+"/advertising/check_ad_amount";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("amount="+10.2);//id 必填
		paramBuff.append("&username="+"hq");
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
