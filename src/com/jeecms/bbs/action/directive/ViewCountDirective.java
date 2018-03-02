package com.jeecms.bbs.action.directive;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.cache.TopicCountEhCache;
import com.jeecms.bbs.entity.BbsTopicCountEnum;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ViewCountDirective implements TemplateDirectiveModel {

	public static final String TOPIC_ID = "topicId";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer topicId = DirectiveUtils.getInt(TOPIC_ID, params);
		Long viewCount = topicCountEhCache.getViewCount(topicId);
		Long viewDayCount = topicCountEhCache.getViewCount(topicId,BbsTopicCountEnum.day);
		Long viewWeekCount = topicCountEhCache.getViewCount(topicId,BbsTopicCountEnum.week);
		Long viewMonthCount = topicCountEhCache.getViewCount(topicId,BbsTopicCountEnum.month);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put("viewCount", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(viewCount));
		paramWrap.put("viewDayCount", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(viewDayCount));
		paramWrap.put("viewWeekCount", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(viewWeekCount));
		paramWrap.put("viewMonthCount", DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(viewMonthCount));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

	@Autowired
	private TopicCountEhCache topicCountEhCache;
}
