package com.jeecms.core.entity;

import static com.jeecms.bbs.Constants.RES_PATH;
import static com.jeecms.bbs.Constants.TPL_BASE;
import static com.jeecms.bbs.Constants.UPLOAD_PATH;
import static com.jeecms.common.web.Constants.DEFAULT;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.core.entity.base.BaseCmsSite;

public class CmsSite extends BaseCmsSite {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name",getName());
		}else{
			json.put("name","");
		}
		if (StringUtils.isNotBlank(getShortName())) {
			json.put("shortName",getShortName());
		}else{
			json.put("shortName","");
		}
		if (StringUtils.isNotBlank(getDomain())) {
			json.put("domain",getDomain());
		}else{
			json.put("domain","");
		}
		if (StringUtils.isNotBlank(getPath())) {
			json.put("path",getPath());
		}else{
			json.put("path","");
		}
		if (StringUtils.isNotBlank(getDomainAlias())) {
			json.put("domainAlias",getDomainAlias());
		}else{
			json.put("domainAlias","");
		}
		if (StringUtils.isNotBlank(getDomainRedirect())) {
			json.put("domainRedirect",getDomainRedirect());
		}else{
			json.put("domainRedirect","");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("corsUrl",getName());
		}else{
			json.put("corsUrl","");
		}
		if (getRelativePath()!=null) {
			json.put("relativePath",getRelativePath());
		}else{
			json.put("relativePath","");
		}
		if (StringUtils.isNotBlank(getProtocol())) {
			json.put("protocol",getProtocol());
		}else{
			json.put("protocol","");
		}
		
		if (StringUtils.isNotBlank(getDynamicSuffix())) {
			json.put("dynamicSuffix",getDynamicSuffix());
		}else{
			json.put("dynamicSuffix","");
		}
		
		if (StringUtils.isNotBlank(getStaticSuffix())) {
			json.put("staticSuffix",getStaticSuffix());
		}else{
			json.put("staticSuffix","");
		}
		
		if (StringUtils.isNotBlank(getLocaleAdmin())) {
			json.put("localeAdmin",getLocaleAdmin());
		}else{
			json.put("localeAdmin","");
		}
		
		if (StringUtils.isNotBlank(getLocaleFront())) {
			json.put("localeFront",getLocaleFront());
		}else{
			json.put("localeFront","");
		}
		
		return json;
	}
	
	/**
	 * 获得站点url
	 * 
	 * @return
	 */
	public String getUrl() {
		if (getStaticIndex()) {
			return getUrlStatic();
		} else {
			return getUrlDynamic();
		}
	}
	
	public String getAppUrl(){
		String  path ="http://";
		path = path +   getDomain();
		if(getPort()!=null){
			path = path + ":" + getPort().toString();
		}
		if(getContextPath()!=null){
			path =path + getContextPath();
		}
		path = path +"/";
		return path;
	}

	/**
	 * 获得完整路径。在给其他网站提供客户端包含时有可以使用。
	 * 
	 * @return
	 */
	public String getUrlWhole() {
		if (getStaticIndex()) {
			return getUrlBuffer(false, true, false).append("/").toString();
		} else {
			return getUrlBuffer(true, true, false).append("/").toString();
		}
	}

	public String getUrlDynamic() {
		return getUrlBuffer(true, null, false).append("/").toString();
	}

	public String getUrlStatic() {
		return getUrlBuffer(false, null, true).append("/").toString();
	}
	
	public String getMagicShopUrl(){
		StringBuffer buff=new StringBuffer();
		buff.append(getProtocol()).append(getDomain());
		CmsConfig config=getConfig();
		if(config.getPort()!=null&&config.getPort()!=80){
			buff.append(config.getPort());
		}
		if(StringUtils.isNotBlank(config.getContextPath())){
			buff.append(config.getContextPath());
		}
		buff.append("/magic/mybox.jhtml");
		return buff.toString();
	}
	
	public String getGiftIndexUrl(){
		StringBuffer buff=new StringBuffer();
		buff.append(getProtocol()).append(getDomain());
		CmsConfig config=getConfig();
		if(config.getPort()!=null&&config.getPort()!=80){
			buff.append(config.getPort());
		}
		if(StringUtils.isNotBlank(config.getContextPath())){
			buff.append(config.getContextPath());
		}
		buff.append("/gift/index.jhtml");
		return buff.toString();
	}
	
	public String getMemberAdIndexUrl(){
		StringBuffer buff=new StringBuffer();
		buff.append(getProtocol()).append(getDomain());
		CmsConfig config=getConfig();
		if(config.getPort()!=null&&config.getPort()!=80){
			buff.append(config.getPort());
		}
		if(StringUtils.isNotBlank(config.getContextPath())){
			buff.append(config.getContextPath());
		}
		buff.append("/member/myAdvertises.jspx");
		return buff.toString();
	}

	public StringBuilder getUrlBuffer(boolean dynamic, Boolean whole,
			boolean forIndex) {
		boolean relative = whole != null ? !whole : getRelativePath();
		String ctx = getContextPath();
		StringBuilder url = new StringBuilder();
		if (!relative) {
			url.append(getProtocol()).append(getDomain());
			if (getPort() != null) {
				url.append(":").append(getPort());
			}
		}
		if (!StringUtils.isBlank(ctx)) {
			url.append(ctx);
		}
		if (dynamic) {
			String servlet = getServletPoint();
			if (!StringUtils.isBlank(servlet)) {
				url.append(servlet);
			}
		} else {
			if (!forIndex) {
				String staticDir = getStaticDir();
				if (!StringUtils.isBlank(staticDir)) {
					url.append(staticDir);
				}
			}
		}
		return url;
	}
	
	public StringBuilder getHttpsUrlBuffer(boolean dynamic, Boolean whole,
			boolean forIndex) {
		boolean relative = whole != null ? !whole : getRelativePath();
		String ctx = getContextPath();
		StringBuilder url = new StringBuilder();
		if (!relative) {
			url.append("https://").append(getDomain());
			if (getPort() != null&&getPort()!=80) {
				url.append(":").append(getPort());
			}
		}
		if (!StringUtils.isBlank(ctx)) {
			url.append(ctx);
		}
		if (dynamic) {
			String servlet = getServletPoint();
			if (!StringUtils.isBlank(servlet)) {
				url.append(servlet);
			}
		} else {
			if (!forIndex) {
				String staticDir = getStaticDir();
				if (!StringUtils.isBlank(staticDir)) {
					url.append(staticDir);
				}
			}
		}
		return url;
	}
	
	public String getHttpsUrlDynamic() {
		return getHttpsUrlBuffer(true, null, false).append("/").toString();
	}

	public String getHttpsUrlStatic() {
		return getHttpsUrlBuffer(false, null, true).append("/").toString();
	}
	
	public String getUrlPrefix() {
		StringBuilder url = new StringBuilder();
		url.append(getProtocol()).append(getDomain());
		if (getPort() != null) {
			url.append(":").append(getPort());
		}
		return url.toString();
	}
	
	public String getUrlPrefixWithNoDefaultPort() {
		StringBuilder url = new StringBuilder();
		url.append(getProtocol()).append(getDomain());
		if (getPort() != null&&getPort() != 80) {
			url.append(":").append(getPort());
		}
		return url.toString();
	}
	
	
	public String getSafeUrlPrefix() {
		StringBuilder url = new StringBuilder();
		url.append("https://").append(getDomain());
		if (getPort() != null&&getPort()!=80&&getPort()!=443) {
			url.append(":").append(getPort());
		}
		return url.toString();
	}


	/**
	 * 获得模板路径。如：/WEB-INF/t/cms/www
	 * 
	 * @return
	 */
	public String getTplPath() {
		return TPL_BASE + "/" + getPath();
	}

	/**
	 * 获得模板方案路径。如：/WEB-INF/t/cms/www/default
	 * 
	 * @return
	 */
	public String getSolutionPath() {
		return TPL_BASE + "/" + getPath() + "/" + getTplSolution();
	}
	
	public String getMobileSolutionPath() {
		return TPL_BASE + "/" + getPath() + "/" + getTplMobileSolution();
	}

	/**
	 * 获得模板资源路径。如：/r/cms/www
	 * 
	 * @return
	 */
	public String getResPath() {
		return RES_PATH + "/" + getPath();
	}

	/**
	 * 获得上传路径。如：/u/jeecms/path
	 * 
	 * @return
	 */
	public String getUploadPath() {
		return UPLOAD_PATH + getPath();
	}

	/**
	 * 获得上传访问前缀。
	 * 
	 * 根据配置识别上传至数据、FTP和本地
	 * 
	 * @return
	 */
	public String getUploadBase() {
		CmsConfig config = getConfig();
		String ctx = config.getContextPath();
		if (config.getUploadToDb()) {
			if (!StringUtils.isBlank(ctx)) {
				return ctx + config.getDbFileUri();
			} else {
				return config.getDbFileUri();
			}
		} else if (getUploadFtp() != null) {
			return getUploadFtp().getUrl();
		} else {
			if (!StringUtils.isBlank(ctx)) {
				return ctx;
			} else {
				return "";
			}
		}
	}

	public String getServletPoint() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getServletPoint();
		} else {
			return null;
		}
	}

	public String getContextPath() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getContextPath();
		} else {
			return null;
		}
	}

	public Integer getPort() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getPort();
		} else {
			return null;
		}
	}

	public String getDefImg() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getDefImg();
		} else {
			return null;
		}
	}

	public String getLoginUrl() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getLoginUrl();
		} else {
			return null;
		}
	}

	public String getProcessUrl() {
		CmsConfig config = getConfig();
		if (config != null) {
			return config.getProcessUrl();
		} else {
			return null;
		}
	}

	public static Integer[] fetchIds(Collection<CmsSite> sites) {
		if (sites == null) {
			return null;
		}
		Integer[] ids = new Integer[sites.size()];
		int i = 0;
		for (CmsSite s : sites) {
			ids[i++] = s.getId();
		}
		return ids;
	}
	
	public String getBaseDomain() {
		String domain = getDomain();
		if (domain.indexOf(".") > -1) {
			return domain.substring(domain.indexOf(".") + 1);
		}
		return domain;
	}

	public void init() {
		if (StringUtils.isBlank(getProtocol())) {
			setProtocol("http://");
		}
		if (StringUtils.isBlank(getTplSolution())) {
			setTplSolution(DEFAULT);
		}
		if (getFinalStep() == null) {
			byte step = 2;
			setFinalStep(step);
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsSite() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsSite(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsSite(java.lang.Integer id,
			com.jeecms.core.entity.CmsConfig config, java.lang.String domain,
			java.lang.String path, java.lang.String name,
			java.lang.String protocol, java.lang.String dynamicSuffix,
			java.lang.String staticSuffix, java.lang.Boolean indexToRoot,
			java.lang.Boolean staticIndex, java.lang.String localeAdmin,
			java.lang.String localeFront, java.lang.String tplSolution,
			java.lang.Byte finalStep, java.lang.Byte afterCheck,
			java.lang.Boolean relativePath, java.lang.Boolean resycleOn) {

		super(id, config, domain, path, name, protocol, dynamicSuffix,
				staticSuffix, indexToRoot, staticIndex, localeAdmin,
				localeFront, tplSolution, finalStep, afterCheck, relativePath,
				resycleOn);
	}

	/* [CONSTRUCTOR MARKER END] */

}