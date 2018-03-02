package com.jeecms.bbs.entity;

import static com.jeecms.bbs.web.FrontUtils.replaceSensitivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.entity.base.BaseBbsPost;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.util.DateUtils;
import com.jeecms.core.bbcode.BbcodeHandler;
import com.jeecms.core.entity.CmsSite;

public class BbsPost extends BaseBbsPost {
	private static final long serialVersionUID = 1L;
	/**
	 * 正常状态
	 */
	public static final short NORMAL = 0;
	/**
	 * 屏蔽状态
	 */
	public static final short SHIELD = -1;
	/**
	 * 待审核状态
	 */
	public static final boolean CHECKING = false;
	/**
	 * 审核状态
	 */
	public static final boolean CHECKED = true;
	/**
	 * 帖子的锚点
	 */
	public static final String ANCHOR = "#pid";
	/**
	 * 查询 主题下所有贴
	 */
	public static final Integer OPT_ALL = 1;
	/**
	 * 查询主题下针对题主的贴
	 */
	public static final Integer OPT_TO_AUTHOR = 2;
	


	public JSONObject convertToJson(Integer https) throws JSONException {
		JSONObject json=new JSONObject();
		json.put("id", getId());
		json.put("title", getTitle());
		String urlPrefix="";
		CmsSite site=getSite();
		json.put("createTime", DateUtils.parseDateToTimeStr(getCreateTime()));
		json.put("posterIp", getPosterIp());
		json.put("position", getIndexCount());
		json.put("status", getStatus());
		json.put("hasAttach", getAffix());
		json.put("hidden", getHidden());
		json.put("anonymous", getAnonymous());
		//来源1 PC 2手机 3平板
		json.put("postSource", getPostSource());
		if(https==com.jeecms.bbs.api.Constants.URL_HTTP){
			json.put("url", getWholeUrl());
			urlPrefix=site.getUrlPrefixWithNoDefaultPort();
		}else{
			json.put("url", getWholeHttpsUrl());
			urlPrefix=site.getSafeUrlPrefix();
		}
		json.put("content", replaceTxt(getContentHtml(), urlPrefix));
		json.put("quoteContent", "[quote]"+replaceTxt(getQuoteContent(), urlPrefix)+"[/quote]");
		json.put("createrId", getCreater().getId());
		json.put("createrUsername", getCreater().getUsername());
		
		json.put("topicId", getTopic().getId());
		json.put("topicTitle", getTopic().getTitle());
		
		if(getParent()!=null){
			json.put("parentId", getParent().getId());
			json.put("parentTitle", getParent().getTitle());
		}else{
			json.put("parentId", "");
			json.put("parentTitle", "");
		}
		if(getPostLongitude()!=null){
			json.put("postLongitude", getPostLongitude());
		}else{
			json.put("postLongitude", "");
		}
		if(getPostLatitude()!=null){
			json.put("postLatitude", getPostLatitude());
		}else{
			json.put("postLatitude", "");
		}
		JSONArray gradeArray=new JSONArray();
		if(getGrade()!=null&&getGrade().size()>0){
			for(BbsGrade grade:getGrade()){
				gradeArray.put(grade.convertToJson());
			}
		}
		json.put("grades", gradeArray);
		json.put("upCount", getUps());
		json.put("replyCount", getReplys());
		return json;
	}
	
	private String replaceTxt(String txt,String urlPrefix){
		if(StringUtils.isNotBlank(txt)){
			//替换图片地址
			List<String>imgUrls=com.jeecms.common.util.StrUtils.getImageSrc(txt);
			for(String img:imgUrls){
				String imgRealUrl=img;
				if(!img.startsWith("http://")&&!img.startsWith("https://")&&!img.startsWith("ftp://")){
					imgRealUrl= urlPrefix+img;
				}
				txt=txt.replace(img, imgRealUrl);
			}
			//替换链接地址
			List<String>linkUrls=com.jeecms.common.util.StrUtils.getLinkSrc(txt);
			for(String linkUrl:linkUrls){
				String linkRealUrl=linkUrl;
				if(!linkUrl.startsWith("http://")&&!linkUrl.startsWith("https://")&&!linkUrl.startsWith("ftp://")){
					linkRealUrl= urlPrefix+linkUrl;
				}
				txt=txt.replace(linkUrl, linkRealUrl);
			}
			//图片替换特殊标记，客户端自己下载图片并替换标记
			//<img[^>]*/>
			String regular="<img(.*?)src=\"(.*?)/>";  
	        String img_pre="<img(.*?)src=\"";
	        String img_sub="\"(.*?)/>";
	        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
	        Matcher m = p.matcher(txt);  
	        String src = null;  
	        String newSrc=null;
	        while (m.find()) {  
	        	src=m.group();
	        	newSrc=src.replaceAll(img_pre, Constants.API_PLACEHOLDER_IMAGE_BEGIN)
	        			.replaceAll(img_sub,  Constants.API_PLACEHOLDER_IMAGE_END).trim();
	        	txt=txt.replace(src, newSrc);
	        }  
	        //原图
	        String originImgregular="<a class=\"zoomImg\"(.*?)href=\"(.*?)\"(.*?)</a>";  
	        Pattern originImgp=Pattern.compile(originImgregular,Pattern.CASE_INSENSITIVE);
	        Matcher originImgm = originImgp.matcher(txt);  
	        String originsrc = null;  
	        while (originImgm.find()) {  
	        	originsrc=originImgm.group();
	        	txt=txt.replace(originsrc, "");
	        }  
	        
	        String linkregular="<a(.*?)href=\"(.*?)\"(.*?)</a>";  
	        String link_pre="<a(.*?)href=\"";
	        String link_sub="\"(.*?)</a>";
	        Pattern linkp=Pattern.compile(linkregular,Pattern.CASE_INSENSITIVE);
	        Matcher linkm = linkp.matcher(txt);  
	        String linksrc = null;  
	        String linknewSrc=null;
	        while (linkm.find()) {  
	        	linksrc=linkm.group();
	        	linknewSrc=linksrc.replaceAll(link_pre, Constants.API_PLACEHOLDER_LINK_BEGIN)
	        			.replaceAll(link_sub,  Constants.API_PLACEHOLDER_LINK_END).trim();
	        	txt=txt.replace(linksrc, linknewSrc);
	        }  
		}
		return txt;
	}

	/**
	 * URL地址
	 * 
	 * @return
	 */
	public String getUrl() {
		Integer index = getIndexCount();
		if (index == 1) {
			// 第一个帖子和主题url相同
			return getTopic().getUrl();
		} else {
			StringBuilder buff = getTopic().getUrlPerfix();
			Integer pageSize = getConfig().getPostCountPerPage();
			int pageNo = (index - 1) / pageSize;
			if (pageNo > 0) {
				buff.append('_').append(pageNo + 1);
			}
			buff.append(getSite().getDynamicSuffix()).append(ANCHOR).append(
					getId());
			return buff.toString();
		}
	}
	
	public String getWholeUrl() {
		Integer index = getIndexCount();
		if (index == 1) {
			// 第一个帖子和主题url相同
			return getTopic().getWholeUrl();
		} else {
			StringBuilder buff = getTopic().getWholeUrlPerfix();
			Integer pageSize = getConfig().getPostCountPerPage();
			int pageNo = (index - 1) / pageSize;
			if (pageNo > 0) {
				buff.append('_').append(pageNo + 1);
			}
			buff.append(getSite().getDynamicSuffix()).append(ANCHOR).append(
					getId());
			return buff.toString();
		}
	}
	
	public String getWholeHttpsUrl() {
		Integer index = getIndexCount();
		if (index == 1) {
			// 第一个帖子和主题url相同
			return getTopic().getWholeHttpsUrl();
		} else {
			StringBuilder buff = getTopic().getWholeHttpsUrlPerfix();
			Integer pageSize = getConfig().getPostCountPerPage();
			int pageNo = (index - 1) / pageSize;
			if (pageNo > 0) {
				buff.append('_').append(pageNo + 1);
			}
			buff.append(getSite().getDynamicSuffix()).append(ANCHOR).append(
					getId());
			return buff.toString();
		}
	}

	public String getRedirectUrl() {
		String path = getTopic().getForum().getPath();
		String url = "/" + path + "/" + getTopic().getId()
				+ getSite().getDynamicSuffix();
		return url;
	}

	/**
	 * 是否楼主
	 * 
	 * @return
	 */
	public boolean isFirst() {
		return getTopic().getFirstPost().equals(this);
	}

	public boolean isShield() {
		if (getStatus() == SHIELD) {
			return true;
		}
		return false;
	}

	/**
	 * 获得标题
	 * 
	 * @return
	 */
	public String getTitle() {
		BbsPostText text = getPostText();
		if (text == null) {
			return null;
		} else {
			return replaceSensitivity(text.getTitle());
		}
	}

	/**
	 * 获得内容
	 * 
	 * @return
	 */
	public String getContent() {
		BbsPostText text = getPostText();
		if (text == null) {
			return null;
		} else {
			return replaceSensitivity(text.getContent());
		}
	}
	
	/**
	 * 获得转换后的内容
	 * 
	 * @return
	 */
	public String getContentHtml() {
		String s = getContent();
		if (StringUtils.isBlank(s)) {
			return "";
		} else {
			if (getAffix()) {
				Set<Attachment> att = getAttachments();
				for (Attachment t : att) {
					String oldcontent = "[attachment]" + t.getId()
							+ "[/attachment]";
					if (t.getPicture()) {
						String newcontent = "[img]" + getSite().getUrl()
								+ t.getFilePath().substring(1) + "[/img]";
						/*
						String newcontent ="[zoomImg]" + getSite().getUrl()
								+ t.getFilePath().substring(1) + "[/zoomImg]"
								+"[img]" + getSite().getUrl()
								+ t.getZoomPicPath().substring(1) + "[/img]";
								*/
						s = StrUtils.replace(s, oldcontent, newcontent);
					} else {
						String newcontent = "[url=" + getSite().getUrl()
								+ t.getFilePath().substring(1) + "]"
								+ t.getFileName() + "[/url]";
						s = StrUtils.replace(s, oldcontent, newcontent);
					}
				}
			}
			if (getHidden()) {
				List<String> list = getHideContent(s);
				for (String str : list) {
					s = StrUtils.replace(s, "[hide]" + str + "[/hide]",
							"[quote]" + str + "[/quote]");
				}
			}
			return BbcodeHandler.toHtml(s);
		}
	}
	
	public String getAppContentHtml() {
		String s = getContent();
		if (StringUtils.isBlank(s)) {
			return "";
		} else {
			if (getAffix()) {
				Set<Attachment> att = getAttachments();
				for (Attachment t : att) {
					String oldcontent = "[attachment]" + t.getId()+ "[/attachment]";
					if (t.getPicture()) {
						//String newcontent = "[img]" + getSite().getAppUrl()+ t.getFilePath().substring(1) + "[/img]";
						String newcontent = "[img]" + getSite().getAppUrl()
								+ t.getZoomPicPath().substring(1) + "[/img]";
						s = StrUtils.replace(s, oldcontent, newcontent);
					} else {
						String newcontent = "[url=" + getSite().getAppUrl() + t.getFilePath().substring(1) + "]" + t.getFileName() + "[/url]";
						s = StrUtils.replace(s, oldcontent, newcontent);
					}
				}
			}
			if (getHidden()) {
				List<String> list = getHideContent(s);
				for (String str : list) {
					s = StrUtils.replace(s, "[hide]" + str + "[/hide]","[quote]" + str + "[/quote]");
				}
			}
			return BbcodeHandler.toHtml(s);
		}
	}
	
	public String getShortContentHtml() {
		String s = getContent();
		if (StringUtils.isBlank(s)) {
			return "";
		} else {
			if (getAffix()) {
				Set<Attachment> att = getAttachments();
				for (Attachment t : att) {
					String oldcontent = "[attachment]" + t.getId()
							+ "[/attachment]";
					s = StrUtils.replace(s, oldcontent, "");
				}
			}
			if(isShield()){
				return getShieldContent();
			}
			if (getHidden()) {
				List<String> list = getHideContent(s);
				for (String str : list) {
					s = StrUtils.replace(s, "[hide]" + str + "[/hide]",
							"这是隐藏内容.需要回复才能浏览");
				}
			}
			return BbcodeHandler.toHtml(s);
			//return s;
		}
	}

	/**
	 * 获取引用内容
	 * 
	 * @return
	 */
	public String getQuoteContent() {
		String s = getContent();
		if (getHidden()) {
			List<String> list = getHideContent(s);
			if (list != null) {
				for (String str : list) {
					String newcontent = "[color=red]此处是被引用的隐藏帖[/color]";
					s = StrUtils.replace(s, "[hide]" + str + "[/hide]",
							newcontent);
				}
			}
		}
		if (haveQuote(s)) {
			s = s.substring(s.lastIndexOf("[/quote]") + 8);
		}
		return s;
	}
	
	public String getShieldContent(){
		if(isShield()){
			return "该贴已经被屏蔽，仅版主可见";
		}else{
			return getContentHtml();
		}
	} 

	/**
	 * 分离隐藏内容
	 * 
	 * @param s
	 * 
	 * @return
	 */
	private List<String> getHideContent(String content) {
		String ems = "\\[hide\\]([\\s\\S]*?)\\[/hide\\]";
		Matcher matcher = Pattern.compile(ems).matcher(content);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			String url = matcher.group(1);
			list.add(url);
		}
		return list;
	}

	private boolean haveQuote(String content) {
		String ems = "\\[quote]([\\s\\S]*)\\[/quote\\]";
		Matcher matcher = Pattern.compile(ems).matcher(content);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 未回复的隐藏内容
	 * 
	 * @return
	 */
	public String getHideContent() {
		String s = getContent();
		if (StringUtils.isBlank(s)) {
			return "";
		} else {
			if (getAffix()) {
				Set<Attachment> att = getAttachments();
				for (Attachment t : att) {
					String oldcontent = "[attachment]" + t.getId()
							+ "[/attachment]";
					if (t.getPicture()) {
						String newcontent = "[img]" + getSite().getUrl()
								+ t.getFilePath().substring(1) + "[/img]";
						/*
						String newcontent = "[zoomImg]" + getSite().getUrl()
								+ t.getFilePath().substring(1) + "[/zoomImg]"
								+"[img]" + getSite().getUrl()
								+ t.getZoomPicPath().substring(1) + "[/img]";
								*/
						s = StrUtils.replace(s, oldcontent, newcontent);
					} else {
						String newcontent = "[url=" + getSite().getUrl()
								+ t.getFilePath().substring(1) + "]"
								+ t.getFileName() + "[/url]";
						s = StrUtils.replace(s, oldcontent, newcontent);
					}
				}
			}
			if (getHidden()) {
				List<String> list = getHideContent(s);
				for (String str : list) {
					s = StrUtils.replace(s, "[hide]" + str + "[/hide]",
							"[quote]这是隐藏内容.需要回复才能浏览[/quote]");
				}
			}
			return BbcodeHandler.toHtml(s);
		}
	}

	/**
	 * 覆盖父类同名方法。增加反向引用
	 */
	public void setPostText(BbsPostText text) {
		if (text != null) {
			text.setPost(this);
		}
		super.setPostText(text);
	}
	
	public Integer getUps(){
		BbsPostCount count = getPostCount();
		if (count == null) {
			return 0;
		} else {
			return count.getUps();
		}
	}
	
	public Integer getReplys(){
		BbsPostCount count = getPostCount();
		if (count == null) {
			return 0;
		} else {
			return count.getReplys();
		}
	}
	
	public String getCreateTimeHtml(){
		Date time=getCreateTime();
		Date now=Calendar.getInstance().getTime();
		if(DateUtils.isInHour(time)){
			int minute=DateUtils.getDiffIntMinuteTwoDate(time, now);
			if(minute>10){
				return minute+"分钟前";
			}else{
				return "刚刚";
			}
		}else if(DateUtils.isToday(time)){
			int hour=DateUtils.getDiffIntHourTwoDate(time, now);
			if(hour<=0){
				hour=1;
			}
			return hour+"小时前"; 
		}else{
			return DateUtils.parseDateToDateStr(time);
		}
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsPost () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsPost (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsPost (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		com.jeecms.bbs.entity.BbsConfig config,
		com.jeecms.bbs.entity.BbsTopic topic,
		com.jeecms.bbs.entity.BbsUser creater,
		java.lang.String title,
		java.util.Date createTime,
		java.lang.String posterIp,
		java.lang.Integer editCount,
		java.lang.Integer indexCount,
		java.lang.Short status,
		java.lang.Boolean affix,
		java.lang.Boolean hidden) {

		super (
			id,
			site,
			config,
			topic,
			creater,
			title,
			createTime,
			posterIp,
			editCount,
			indexCount,
			status,
			affix,
			hidden);
	}

	/* [CONSTRUCTOR MARKER END] */

}