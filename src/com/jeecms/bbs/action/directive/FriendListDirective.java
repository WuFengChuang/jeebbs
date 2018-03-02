package com.jeecms.bbs.action.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 *江西金磊科技发展有限公司jeecms研发
 */
public class FriendListDirective implements TemplateDirectiveModel{
	public static final String PARAM_USERNAME = "username";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String username = getUsername(params);
		List<BbsUser> list = bbsUserMng.getList(username, null, false, false, false, 0, Integer.MAX_VALUE);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
	/**
	 * 获取指定的字段值
	 * @param params
	 * @return
	 * @throws TemplateException
	 */
	private String getUsername(Map<String, TemplateModel> params)
			throws TemplateException {
		String username = DirectiveUtils.getString(PARAM_USERNAME, params);
		return username == null ? "" : username;
	}

	@Autowired
	private BbsUserMng bbsUserMng;
}
