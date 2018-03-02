package com.jeecms.bbs.action.directive;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeecms.bbs.action.directive.abs.AbstractTopicPageDirective;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;
import com.jeecms.core.entity.CmsSite;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class TopicListDirective extends AbstractTopicPageDirective {
	public static final String RECOMMEND = "recommend";
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSite site = FrontUtils.getSite(env);
		String jinghua = getFindType(params);
		Boolean checkStatus=true;
		Short s=getCheckStatus(params);
		if(s!=null&&s==0){
			checkStatus=false;
		}
		Integer excludeId = DirectiveUtils.getInt(PARAM_EXCLUDE_ID, params);
		List<BbsTopic> list = bbsTopicMng.getListForTag(site.getId(),
				getForumId(params),getParentPostTypeId(params),getPostTypeId(params),
				getStatus(params), getPrimeLevel(params),
				getKeyWords(params), getCreater(params), getCreaterId(params),
				getTopLevel(params),getTopicTypeId(params),excludeId,checkStatus,
				FrontUtils.getPageNo(env),FrontUtils.getCount(params), 
				jinghua,getOrderBy(params),getRecommend(params));
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
	
	public static Short getRecommend(Map<String, TemplateModel> params)
			throws TemplateException {
		Short recommend = DirectiveUtils.getShort(RECOMMEND, params);
		return recommend;
	}
}
