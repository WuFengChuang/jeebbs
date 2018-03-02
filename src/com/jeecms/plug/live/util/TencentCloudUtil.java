package com.jeecms.plug.live.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.web.HttpClientUtil;

public class TencentCloudUtil {
	private static final Logger log = LoggerFactory.getLogger(TencentCloudUtil.class);
	
	private final static String PROTOCOL_RTMP="rtmp://";
	private final static String PROTOCOL_HTTP="http://";
	//0表示禁用； 1表示允许推流；2表示断流
	private final static String LIVE_STATUS_DISABLED="0";
	private final static String LIVE_STATUS_CONNECT="1";
	private final static String LIVE_STATUS_RESET="2";
	private final static String CONTROL_URL="http://fcgi.video.qcloud.com/common_access?";
	private final static String STATUS_APINAME="Live_Channel_SetStatus";
	
	//禁用流
	public static int disableStram(String key,
			String appid, String streamId){
		return controlStram(key, appid, 
				streamId,  LIVE_STATUS_DISABLED);
	}
	
	//启用流
	public static int connectStram(String key,
			String appid, String streamId){
		return controlStram(key, appid, 
				streamId, LIVE_STATUS_CONNECT);
	}
	
	private static int controlStram(String key,
			String appid, String streamId,String status){
		String url=CONTROL_URL;
		StringBuffer buff=new StringBuffer();
		Long t=Calendar.getInstance().getTime().getTime()+60;
		buff.append(url);
		buff.append("appid="+appid);
		buff.append("&interface="+STATUS_APINAME);
		buff.append("&t="+t);
		buff.append("&sign="+getSign(key, t));
		buff.append("&Param.s.channel_id="+streamId);
		buff.append("&Param.n.status="+status);
		String result=HttpClientUtil.getInstance().getNoSsl(buff.toString());
		int ret=-1;
		try {
			JSONObject json=new JSONObject(result);
			ret=(int) json.get("ret");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e.getMessage());
		}
		return ret;
	}
	
	 public static String getPushUrl(String pushBaseUrl,
			 String bizId,String bizKey,Integer userId, Date endTime){
		 StringBuffer buff=new StringBuffer();
		 buff.append(PROTOCOL_RTMP);
		 buff.append(bizId).append(".").append(pushBaseUrl)
		 .append(bizId).append("_").append(userId)
		 .append("?bizid=").append(bizId).append("&")
		 .append(getSafeUrlByUser(bizKey, bizId, userId.toString(), endTime));
		 return buff.toString();
	 }
	 
	 public static String getRtmpPlayUrl(String playBaseUrl,
			 String bizId,Integer userId){
		 StringBuffer buff=new StringBuffer();
		 buff.append(PROTOCOL_RTMP);
		 buff.append(bizId).append(".").append(playBaseUrl)
		 .append(bizId).append("_").append(userId);
		 return buff.toString();
	 }
	 
	 public static String getPlayUrl(String playBaseUrl,
			 String bizId,Integer userId,String ext){
		 StringBuffer buff=new StringBuffer();
		 buff.append(PROTOCOL_HTTP);
		 buff.append(bizId).append(".").append(playBaseUrl)
		 .append(bizId).append("_").append(userId).append(".").append(ext);
		 return buff.toString();
	 }
	 
	 private static String getSafeUrlByUser(String key, String bizId,String userId, Date endTime){
		 return getSafeUrl(key, bizId+"_"+userId, endTime);
	 }

    /*
     * KEY
     */
    private static String getSign(String key,long t) {
    	long txTime=t;
        String input = new StringBuilder().
                append(key).
                append(txTime).toString();

        String sign = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            sign  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign ;
    }
    
    private static String getSafeUrl(String key, String streamId, Date endTime) {
    	long txTime=getExpireTime(endTime);
        String input = new StringBuilder().
                append(key).
                append(streamId).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
            new StringBuilder().
                append("txSecret=").
                append(txSecret).
                append("&").
                append("txTime=").
                append(Long.toHexString(txTime).toUpperCase()).
                toString();
    }
    
    private static long getExpireTime(Date endTime){
    	//延迟10分钟
    	return endTime.getTime()/1000+10*60;
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
    

    public static void main(String[] args) {
    	 System.out.println(getSafeUrlByUser("111", "2222", "5", null));
    }

	
    
    private static final char[] DIGITS_LOWER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

}