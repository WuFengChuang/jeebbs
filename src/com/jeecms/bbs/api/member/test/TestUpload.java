package com.jeecms.bbs.api.member.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.upload.FileUpload;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;

public class TestUpload {
	private static String base="http://192.168.0.150:8080/jeebbs5/api/member";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	
	public static void main(String[] args) {
		String filePath="d:\\test\\1.doc";
		uploadAttach(236,"[localimg]1[/localimg]","attach",filePath);
	}
	
	public static void uploadAttach(Integer postId,String code,String type,
			String filePath){
		String url=base+"/upload/o_upload?";
		FileUpload fileUpload = new FileUpload();
		Boolean mark=false;
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		StringBuffer paramBuff=new StringBuffer();
		Map<String, String>param=new HashMap<String, String>();
		paramBuff.append("postId="+postId);
		paramBuff.append("&code="+code);
		paramBuff.append("&type="+type);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		String result ="";
		try {
			result= fileUpload.uploadFileWithHttpMime(postId,  code,type,
					url, filePath, appId, nonce_str, sign);
			JSONObject json=new JSONObject(result);
			String status=(String) json.get("status");
			System.out.println("result->"+result);
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
				String attachId=(String) bodyJson.get("attachId");
				System.out.println("attachId->"+attachId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
