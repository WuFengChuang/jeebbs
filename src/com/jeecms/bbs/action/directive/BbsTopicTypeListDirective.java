package com.jeecms.bbs.action.directive;

import static com.jeecms.common.web.freemarker.DirectiveUtils.OUT_LIST;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeecms.bbs.entity.BbsTopicType;
import com.jeecms.bbs.manager.BbsTopicTypeMng;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.freemarker.DefaultObjectWrapperBuilderFactory;
import com.jeecms.common.web.freemarker.DirectiveUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 主题分类列表标签
 * 
 * @author tom
 * 
 */
public class BbsTopicTypeListDirective implements TemplateDirectiveModel {
	
	/**
	 * 输入参数，父类目ID。
	 */
	public static final String PARAM_PARENT_ID = "parentId";
	/**
	 * 输入参数，排序。
	 * 1 排序降序2 排序升序3主题数降序4主题数升序
	 * 5 精华主题数降序  6精华主题数升序7 订阅数降序 8  订阅数升序
	 */
	public static final String PARAM_ORDER_BY = "orderBy";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Integer parentId = getParentId(params);
		List<BbsTopicType> list;
		Integer count=FrontUtils.getCount(params);
		if(parentId!=null){
			list=bbsTopicTypeMng.getChildList(parentId,true,
					true,getOrderBy(params),null,count);
		}else{
			list=bbsTopicTypeMng.getTopList(true, 
					true,getOrderBy(params),null,count);
		}
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, DefaultObjectWrapperBuilderFactory.getDefaultObjectWrapper().wrap(list));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		body.render(env.getOut());
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
	
	private Integer getParentId(Map<String, TemplateModel> params)
			throws TemplateException {
		return DirectiveUtils.getInt(PARAM_PARENT_ID, params);
	}
	
	private Integer getOrderBy(Map<String, TemplateModel> params)
			throws TemplateException {
		Integer orderBy= DirectiveUtils.getInt(PARAM_ORDER_BY, params);
		if(orderBy==null){
			orderBy=1;
		}
		return orderBy;
	}

	@Autowired
	private BbsTopicTypeMng bbsTopicTypeMng;
}
