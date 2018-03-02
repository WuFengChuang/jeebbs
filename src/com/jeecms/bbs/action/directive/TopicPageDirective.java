package com.jeecms.bbs.action.directive;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_PAGINATION;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.jeecms.bbs.action.directive.abs.AbstractTopicPageDirective;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.common.web.freemarker.DirectiveUtils.InvokeType;
import com.jeecms.core.entity.CmsSite;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TopicPageDirective extends AbstractTopicPageDirective {
	public static final String RECOMMEND = "recommend";
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "topic_page";
	/**
	 * 模板名称
	 */
	public static final String TPL_PAG = "topic";
	/**
	 * 我的主题
	 */
	public static final String TPL_MY_TOPIC = "mytopic_page";
	/**
	 * 我的主题
	 */
	public static final String TPL_MY_POST = "mypost_page";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		InvokeType type = DirectiveUtils.getInvokeType(params);
		String jinghua = getFindType(params);
		Boolean checkStatus=true;
		Short s=getCheckStatus(params);
		if(s!=null&&s==0){
			checkStatus=false;
		}
		Integer pageNo=1;
		try {
			pageNo=FrontUtils.getPageNo(env);
		} catch (Exception e) {
		}
		if(getPageNo(params)!=null){
			pageNo=getPageNo(params);
		}
		Pagination page = bbsTopicMng.getForTag(site.getId(),
				getForumId(params),getParentPostTypeId(params),getPostTypeId(params), getStatus(params), getPrimeLevel(params),
				getKeyWords(params), getCreater(params), getCreaterId(params),
				getTopLevel(params),getTopicTypeId(params),null,checkStatus,
				pageNo,FrontUtils.getCount(params), 
				jinghua,getOrderBy(params),getRecommend(params));
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_PAGINATION, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(page));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		if (InvokeType.custom == type) {
			FrontUtils.includeTpl(TPL_NAME, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.sysDefined == type) {
			FrontUtils.includeTpl(TPL_MY_TOPIC, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.userDefined == type) {
			FrontUtils.includeTpl(TPL_MY_POST, site, params, env);
			FrontUtils.includePagination(site, params, env);
		} else if (InvokeType.body == type) {
			body.render(env.getOut());
			FrontUtils.includePagination(site, params, env);
		} else {
			throw new RuntimeException("invoke type not handled: " + type);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
	
	public static Short getRecommend(Map<String, TemplateModel> params)
			throws TemplateException {
		Short recommend = DirectiveUtils.getShort(RECOMMEND, params);
		if (recommend==null) {
			return 0;
		}
		return recommend;
	}

}
