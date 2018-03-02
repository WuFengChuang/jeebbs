package com.jeecms.plug.live.directive;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.manager.BbsLiveMng;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 活动live列表标签
 * 
 * @author tom
 * 
 */
public class LiveListDirective implements TemplateDirectiveModel {
	/**
	 * 输入参数，用户ID。
	 */
	public static final String PARAM_USER_ID = "userId";
	/**
	 * 输入参数，章节ID。
	 */
	public static final String PARAM_CHAPTER_ID = "chapterId";
	/**
	 * 输入参数，标题。可以为null。
	 */
	public static final String PARAM_TITLE = "title";
	/**
	 * 状态      0未开始   1进行中   2已结束
	 */
	public static final String PARAM_STATUS = "status";
	/**
	 * 输入参数，排序 1开始时间降序 2总收益降序 3ID降序 4参与人数降序 5限制人数降序
	 * 					 6开始时间升序 7总收益升序 8ID升序 9参与人数升序 10限制人数升序
	 */
	public static final String PARAM_ORDER_BY = "orderBy";
	
	public static final Integer STATUS_NOTBEGIN=0;
	public static final Integer STATUS_BEGINING=1;
	public static final Integer STATUS_END=2;
	

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer userId = getUserId(params);
		Integer chapterId = getChapterId(params);
		Integer orderBy = getOrderBy(params);
		Integer status = getStatus(params);
		String title=getTitle(params);
		Date now=Calendar.getInstance().getTime();
		Date queryStartBegin = null ,
			 queryStartEnd = null,
			 queryFinishBegin = null,
			 queryFinishEnd=null;
		if(status!=null){
			if(status.equals(STATUS_NOTBEGIN)){
				queryStartBegin=now;
			}else if(status.equals(STATUS_BEGINING)){
				queryFinishBegin=now;
				queryStartEnd=now;
			}else if(status.equals(STATUS_END)){
				queryFinishEnd=now;
			}
		}
		List<BbsLive> list = liveMng.getList(chapterId, title,
				userId, BbsLive.CHECKED, 
				queryStartBegin, queryStartEnd,
				queryFinishBegin,queryFinishEnd, 
				orderBy, FrontUtils.getFirst(params), 
				FrontUtils.getCount(params));
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	private Integer getUserId(Map<String, TemplateModel> params)
			throws TemplateException {
		return DirectiveUtils.getInt(PARAM_USER_ID, params);
	}

	private Integer getChapterId(Map<String, TemplateModel> params)
			throws TemplateException {
		return DirectiveUtils.getInt(PARAM_CHAPTER_ID, params);
	}
	
	private Integer getOrderBy(Map<String, TemplateModel> params)
			throws TemplateException {
		return DirectiveUtils.getInt(PARAM_CHAPTER_ID, params);
	}
	
	private Integer getStatus(Map<String, TemplateModel> params)
			throws TemplateException {
		return DirectiveUtils.getInt(PARAM_STATUS, params);
	}
	
	private String getTitle(Map<String, TemplateModel> params)
			throws TemplateException {
		return DirectiveUtils.getString(PARAM_TITLE, params);
	}

	@Autowired
	private BbsLiveMng liveMng;
}
