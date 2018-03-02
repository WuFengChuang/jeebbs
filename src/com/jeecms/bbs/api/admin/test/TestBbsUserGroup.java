package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsUserGroup {
	private static String base = "http://192.168.0.150:8080/jeebbs5/api/admin";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="EBD6DFAABD5FBCB9BB90F3EF2757DB10";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		System.out.println(testGroupGet());
	}

	private static String testGroups(){
		String url = base+"/group/list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("groupType="+2);//会员组类型
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String,String> param = new HashMap<String,String>();
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
	
	private static String testGroupGet(){
		String url = base+"/group/get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+0);
		paramBuff.append("&type="+2);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey, ivKey);
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
	
	private static String testGroupSave(){
		String url = base+"/group/save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("groupType="+2);
		paramBuff.append("&name="+"修改测试");
		paramBuff.append("&point="+100);
		paramBuff.append("&imgPath="+"/jeebbs5/r/cms/www/blue/bbs_forum/img/level/1.gif");
		paramBuff.append("&forumIds="+"22");
		paramBuff.append("&views="+"22");
		paramBuff.append("&topics="+"22");
		paramBuff.append("&replies="+"22");
		paramBuff.append("&perm_super_moderator="+"true");
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
			String[] keyValue = p.split("=");
			if (keyValue.length == 2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println(param.toString());
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testGroupDel(){
		String url=base+"/group/delete";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"27");
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
	
	private static String testGroupUpd(){
		//groupType,bean.getId(),bean.getName(),bean.getPoint(),bean.getImgPath(),forumIds
		String url=base+"/group/update";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+7);
		paramBuff.append("&groupType="+1);
		paramBuff.append("&name="+"会元");
		paramBuff.append("&point="+8000);
		paramBuff.append("&imgPath="+"/jeebbs5/r/cms/www/blue/bbs_forum/img/level/7.gif");
		paramBuff.append("&forumIds="+"20");
		paramBuff.append("&views="+"20");
		paramBuff.append("&topics="+"");
		paramBuff.append("&replies="+"20");
//		paramBuff.append("&perm_allow_topic="+"false");
//		paramBuff.append("&perm_allow_reply="+"true");
//		paramBuff.append("&perm_allow_attach="+"true");
//		paramBuff.append("&perm_attach_type="+"jpg");
//		paramBuff.append("&perm_attach_max_size="+"20");
//		paramBuff.append("&perm_attach_per_day="+"100");
		//当groupType=2时需要使用到以下参数
		//--start
//		paramBuff.append("&perm_super_moderator="+"false");
//		paramBuff.append("&perm_post_limit="+"true");
//		paramBuff.append("&perm_topic_top="+"true");
//		paramBuff.append("&perm_topic_manage="+"true");
//		paramBuff.append("&perm_topic_edit="+"true");
//		paramBuff.append("&perm_topic_shield="+"true");
//		paramBuff.append("&perm_topic_delete="+"true");
//		paramBuff.append("&perm_view_ip="+"true");
//		paramBuff.append("&perm_member_prohibit="+"true");
		//--end
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
