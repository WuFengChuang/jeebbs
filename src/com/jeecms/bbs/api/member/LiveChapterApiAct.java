package com.jeecms.bbs.api.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.ApiRecord;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLiveChapter;
import com.jeecms.plug.live.manager.BbsLiveChapterMng;

@Controller
public class LiveChapterApiAct {

	private static final Logger log = LoggerFactory
			.getLogger(LiveChapterApiAct.class);

	/**
	 * 直播管理-章节列表
	 * 
	 * @param root
	 *            节点
	 */
	@RequestMapping(value = "/liveChapter/list")
	public void chapterList(Integer root, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";

		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			boolean liveHost = user.getLiveHost();
			// 判断是否有主讲人资质
			if (!liveHost) {
				message = "\"message.info.youAreNoHost\"";
				status = Constants.API_STATUS_FAIL;
			} else {
				List<BbsLiveChapter> list;
				if (root == null) {
					list = chapterMng.getTopList(user.getId());
				} else {
					Integer rootId = Integer.valueOf(root);
					list = chapterMng.getChildList(user.getId(), rootId);
				}
				JSONArray jsonArray = new JSONArray();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						jsonArray.put(i, convertToJson(list.get(i), root));
						status = Constants.API_STATUS_SUCCESS;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
						body = jsonArray.toString();
					}
				}

			}

		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 直播管理-章节树
	 * 
	 * @param root
	 *            节点
	 */
	@RequestMapping(value = "/liveChapter/tree")
	public void tree(String root, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";

		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			boolean liveHost = user.getLiveHost();
			// 判断是否有主讲人资质
			if (!liveHost) {
				message = "\"message.info.youAreNoHost\"";
				status = Constants.API_STATUS_FAIL;
			} else {
				boolean isRoot;
				// jquery treeview的根请求为root=source
				if (StringUtils.isBlank(root) || "source".equals(root)) {
					isRoot = true;
				} else {
					isRoot = false;
				}
				List<BbsLiveChapter> list;
				Integer userId = CmsUtils.getUserId(request);
				if (isRoot) {
					list = chapterMng.getTopList(userId);
				} else {
					Integer rootId = Integer.valueOf(root);
					list = chapterMng.getChildList(userId, rootId);
				}
				JSONArray jsonArray = new JSONArray();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						jsonArray.put(i, convertToJson1(list.get(i), isRoot));
						status = Constants.API_STATUS_SUCCESS;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
						body = jsonArray.toString();
					}
				}

			}

		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);

	}

	/**
	 * 直播管理-章节添加
	 * 
	 * @param root
	 *            节点
	 */
	@RequestMapping(value = "/liveChapter/add")
	public void add(Integer root, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";

		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			boolean liveHost = user.getLiveHost();
			// 判断是否有主讲人资质
			if (!liveHost) {
				message = "\"message.info.youAreNoHost\"";
				status = Constants.API_STATUS_FAIL;
			} else {
				BbsLiveChapter parent = null;
				JSONObject json = new JSONObject();
				if (root != null) {
					parent = chapterMng.findById(root);
					json.put("root", root);
					json.put("name", parent.getName());

				}
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);

	}

	/**
	 * 直播管理-章节添加数据保存
	 * 
	 * @param name名称
	 * @param root节点
	 * @param path路径
	 * @param priority排序
	 */
	@SignValidate
	@RequestMapping(value = "/liveChapter/save")
	public void save(String name, Integer priority, String path, Integer root,
			String sign, String appId, HttpServletRequest request,
			HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);

		errors = ApiValidate.validateRequiredParams(errors, sign, appId, name,
				path, priority);
		if (!errors.hasErrors()) {
			BbsUser user = CmsUtils.getUser(request);
			// 签名数据不可重复利用
			ApiRecord record = apiRecordMng.findBySign(sign, appId);
			if (record == null) {
				message = Constants.API_MESSAGE_REQUEST_REPEAT;
				code = ResponseCode.API_CODE_REQUEST_REPEAT;
			} else {
				if (user != null) {
					boolean liveHost = user.getLiveHost();
					// 判断是否有主讲人资质
					if (!liveHost) {
						message = "\"message.info.youAreNoHost\"";
						status = Constants.API_STATUS_FAIL;
					} else {
						BbsLiveChapter bean = new BbsLiveChapter();
						bean.setUser(user);
						bean.setName(name);
						bean.setPath(path);
						bean.setPriority(priority);
						bean.init();
						chapterMng.save(bean, root);
						apiRecordMng.callApiRecord(
								RequestUtils.getIpAddr(request), appId,
								"/liveChapter/save", sign);
						body = "{\"id\":" + "\"" + bean.getId() + "\"}";
						log.info("save chapter id={}", bean.getId());
						message = Constants.API_MESSAGE_SUCCESS;
						status = Constants.API_STATUS_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}
				}

			}
		} else {
			message = errors.getErrors().get(0);
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 直播管理-章节修改 章节信息 id 章节id root
	 * 
	 * @throws JSONException
	 * 
	 */
	@RequestMapping(value = "/liveChapter/edit")
	public void edit(Integer id, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";

		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			boolean liveHost = user.getLiveHost();
			// 判断是否有主讲人资质
			if (!liveHost) {
				message = "\"message.info.youAreNoHost\"";
				status = Constants.API_STATUS_FAIL;
			} else {
				// 章节
				BbsLiveChapter chapter = chapterMng.findById(id);
				// 章节列表
				List<BbsLiveChapter> topList = chapterMng.getTopList(user
						.getId());
				List<BbsLiveChapter> chapterList = BbsLiveChapter
						.getListForSelect(topList, chapter);
				JSONArray jsonArray = new JSONArray();

				if (chapterList != null && chapterList.size() > 0) {

					for (int i = 0; i < chapterList.size(); i++) {
						jsonArray.put(i, convertToJson2(chapterList.get(i)));
					}
					status = Constants.API_STATUS_SUCCESS;
					message = convertToJson3(chapter).toString();
					code = ResponseCode.API_CODE_CALL_SUCCESS;
					body = jsonArray.toString();
				}
			}
		}

		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 直播管理-章节修改 章节信息 id 章节id root
	 * 
	 * @throws JSONException
	 * 
	 */
	@SignValidate
	@RequestMapping(value = "/liveChapter/update")
	public void update(Integer id, String path, Integer priority,
			Integer parentId, String sign, String appId,
			HttpServletRequest request, HttpServletResponse response) {

		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, appId, sign, id,
				parentId, path);
		if (!errors.hasErrors()) {
			BbsUser user = CmsUtils.getUser(request);
			// 签名数据不可重复利用
			ApiRecord record = apiRecordMng.findBySign(sign, appId);
			if (record == null) {
				message = Constants.API_MESSAGE_REQUEST_REPEAT;
				code = ResponseCode.API_CODE_REQUEST_REPEAT;
			} else {
				if (user != null) {
					boolean liveHost = user.getLiveHost();
					// 判断是否有主讲人资质
					if (!liveHost) {
						message = "\"message.info.youAreNoHost\"";
						status = Constants.API_STATUS_FAIL;
					} else {
						updateChapter(id, parentId, path, priority);
						log.info("BbsLiveChapter chapter id={}", id);
						body = "{\"id\":" + "\"" + id + "\"}";
						message = Constants.API_MESSAGE_SUCCESS;
						status = Constants.API_STATUS_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}
				}
			}
		} else {
			message = errors.getErrors().get(0);
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 直播管理-章节删除 * @param ids 章节id ,逗号分隔 必选
	 * 
	 * @param appId
	 *            appId 必选
	 * @param sign
	 *            签名 必选
	 * @param sessionKey
	 *            会话标识必选
	 */
	@SignValidate
	@RequestMapping(value = "/liveChapter/delete")
	public void delete(Integer[] ids, String sign, String appId,
			HttpServletRequest request, HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, appId, sign, ids);
		if (!errors.hasErrors()) {
			BbsUser user = CmsUtils.getUser(request);
			// 签名数据不可重复利用
			ApiRecord record = apiRecordMng.findBySign(sign, appId);
			if (record == null) {
				message = Constants.API_MESSAGE_REQUEST_REPEAT;
				code = ResponseCode.API_CODE_REQUEST_REPEAT;
			} else {
				if (user != null) {
					boolean liveHost = user.getLiveHost();
					// 判断是否有主讲人资质
					if (!liveHost) {
						message = "\"message.info.youAreNoHost\"";
						status = Constants.API_STATUS_FAIL;
					} else {
						BbsLiveChapter[] beans = chapterMng.deleteByIds(ids);
						for (BbsLiveChapter bean : beans) {
							log.info("delete BbsLiveChapter id={}",
									bean.getId());
						}
						apiRecordMng.callApiRecord(
								RequestUtils.getIpAddr(request), appId,
								"/BbsLiveChapter/delete", sign);
						message = Constants.API_MESSAGE_SUCCESS;
						status = Constants.API_STATUS_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}
				}
			}
		} else {
			message = errors.getErrors().get(0);
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	private JSONObject convertToJson(BbsLiveChapter chapter, Integer root)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("root", root);
		json.put("id", chapter.getId());
		json.put("name", chapter.getName());// 名称
		json.put("path", chapter.getPath());// 章节路径
		json.put("priority", chapter.getPriority());// 排序

		return json;
	}

	private JSONObject convertToJson1(BbsLiveChapter chapter, Boolean isRoot)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", chapter.getId());// 章节id
		json.put("isRoot", isRoot);// root状态true/false
		json.put("name", chapter.getName());// 名称
		json.put("child", chapter.getChild());// 子章节
		return json;
	}

	private JSONObject convertToJson2(BbsLiveChapter chapter)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("parentId", chapter.getParentId());// 章节id
		json.put("parentName", chapter.getParentName());// 章节名称
		return json;
	}

	private JSONObject convertToJson3(BbsLiveChapter chapter)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", chapter.getId());
		json.put("path", chapter.getPath());// 路径
		json.put("priority", chapter.getPriority());// 排序
		return json;
	}

	private BbsLiveChapter updateChapter(Integer id, Integer parentId,
			String path, Integer priority) {
		BbsLiveChapter chapter = chapterMng.findById(id);
		if (chapter != null) {

			if (path != null) {
				chapter.setPath(path);
			}
			if (priority != null) {
				chapter.setPriority(priority);
			}
			chapter = chapterMng.update(chapter, parentId);
		}
		return chapter;
	}

	@Autowired
	private BbsLiveChapterMng chapterMng;
	@Autowired
	private ApiRecordMng apiRecordMng;

}
