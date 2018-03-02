package com.jeecms.bbs.action.directive;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class NewPostListDirective implements TemplateDirectiveModel {
	public static final String ORDERBY = "orderby";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		int count = FrontUtils.getCount(params);
		int orderby = getOrderby(params);
		List<BbsPost> postList = bbsPostMng.getList(count,orderby);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(postList));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

	}

	public static int getOrderby(Map<String, TemplateModel> params)
			throws TemplateException {
		Integer orderby = DirectiveUtils.getInt(ORDERBY, params);
		if(orderby==null){
			return 1;
		}
		return orderby;
	}

	@Autowired
	private BbsPostMng bbsPostMng;

}
