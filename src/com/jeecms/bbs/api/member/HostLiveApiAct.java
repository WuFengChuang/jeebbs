package com.jeecms.bbs.api.member;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.action.front.BbsHostLiveAct;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveChapter;
import com.jeecms.plug.live.entity.BbsLiveRate;
import com.jeecms.plug.live.entity.BbsLiveUser;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;
import com.jeecms.plug.live.manager.BbsLiveChapterMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveRateMng;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;
import com.jeecms.plug.live.manager.BbsLiveUserMng;
import com.jeecms.plug.live.util.BaiduCloudUtil;
import com.jeecms.plug.live.util.TencentCloudUtil;

@Controller
public class HostLiveApiAct {

	private static final Logger log = LoggerFactory
			.getLogger(HostLiveApiAct.class);

	/**
	 * 直播管理-我的直播列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/host/list")
	public void myList(Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		CmsSite site = CmsUtils.getSite(request);
		CmsConfig config = site.getConfig();
		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			boolean liveHost = user.getLiveHost();
			// 判断是否有主讲人资质
			if (!liveHost) {
				message = "\"message.info.youAreNoHost\"";
				status = Constants.API_STATUS_FAIL;
			} else {
				Pagination page = liveMng.getPage(null, null, user.getId(),
						null, null, null, null, null, null, pageNo, pageSize);
				Map<String, String> pushUrlMap = new HashMap<>();
				List<BbsLive> lives = (List<BbsLive>) page.getList();
				int totalCount = page.getTotalCount();
				// 需要判断当前平台选择的视频云平台类型
				initTencentPushBaseUrl();
				for (BbsLive live : lives) {
					if (live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)) {
						String url = TencentCloudUtil.getPushUrl(
								getTencentPushBaseUrl(),
								config.getTencentBizId(),
								config.getTencentPushFlowKey(), user.getId(),
								live.getEndTime());
						pushUrlMap.put(live.getId().toString(), url);
					} else if (live.getLivePlat().equals(
							BbsLive.LIVE_PLAT_BAIDU)) {
						String url = BaiduCloudUtil.getPushUrl(
								config.getBaiduPushDomain(), user.getId(),
								live.getEndTime(),
								config.getBaiduStreamSafeKey());
						pushUrlMap.put(live.getId().toString(), url);
					}
				}
				JSONArray jsonArray = new JSONArray();
				if (lives != null && lives.size() > 0) {
					for (int i = 0; i < lives.size(); i++) {
						jsonArray.put(i, convertToJson(lives.get(i)/*
																	 * ,
																	 * pushUrlMap
																	 */));
						status = Constants.API_STATUS_SUCCESS;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
						body = jsonArray.toString() + ",\"totalCount\":"
								+ totalCount;
					}
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);

	}

	/**
	 * 直播管理-创建
	 * 
	 * @param cid非必填
	 * @throws JSONException
	 */
	@RequestMapping(value = "/liveHost/add")
	public void add(Integer cid, HttpServletRequest request,
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
				BbsLiveChapter c;
				if (cid != null) {
					c = liveChapterMng.findById(cid);
				} else {
					c = null;
				}
				List<BbsLiveChapter> chapterList;
				if (c != null) {
					chapterList = c.getListForSelect();
				} else {
					List<BbsLiveChapter> topList = liveChapterMng
							.getTopList(user.getId());
					chapterList = BbsLiveChapter
							.getListForSelect(topList, null);
				}
				JSONArray jsonArray = new JSONArray();
				if (chapterList != null && chapterList.size() > 0) {
					for (int i = 0; i < chapterList.size(); i++) {
						jsonArray.put(i, convertToJson1(chapterList.get(i), c));
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
	 * 直播管理-save chapterId 章节id title 标题 description 描述 liveLogo log图 beginTime
	 * 开始时间 endTime 结束时间 beginPrice 价格 limitUserNum 限制人数
	 */
	@SignValidate
	@RequestMapping(value = "/liveHost/save")
	public void save(Integer chapterId, String title, String description,
			String liveLogo, Date beginTime, Date endTime, Double beginPrice,
			Integer limitUserNum, String sign, String appId,
			HttpServletRequest request, HttpServletResponse response) {

		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);

		errors = ApiValidate.validateRequiredParams(errors, sign, appId, title,
				beginTime, endTime, chapterId);
		if (!errors.hasErrors()) {
			BbsUser user = CmsUtils.getUser(request);
			CmsSite site = CmsUtils.getSite(request);
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
						CmsConfig config = cmsConfigMng.get();
						Boolean liveNeedCheck = config.getLiveCheck();
						BbsLive live = new BbsLive();
						live.setAfterPrice(beginPrice);
						live.setBeginPrice(beginPrice);
						live.setBeginTime(beginTime);
						if (liveNeedCheck) {
							live.setCheckStatus(BbsLive.CHECKING);
						} else {
							live.setCheckStatus(BbsLive.CHECKED);
						}
						live.setCreateTime(Calendar.getInstance().getTime());
						live.setDescription(description);
						live.setEndTime(endTime);
						live.setLimitUserNum(limitUserNum);
						live.setLiveLogo(liveLogo);
						live.setTitle(title);
						live.init();
						live.setUser(user);
						String liveUrl = "", demandUrl = "", liveMobileUrl = "", demandImageUrl = "";
						String livePlatStr = config.getLivePlat();
						if (StringUtils.isNotBlank(livePlatStr)) {
							if (livePlatStr
									.equals(BbsLive.LIVE_PLAT_STR_TENCENT)) {
								live.setLivePlat(BbsLive.LIVE_PLAT_TENCENT);
							} else if (livePlatStr
									.equals(BbsLive.LIVE_PLAT_STR_BAIDU)) {
								live.setLivePlat(BbsLive.LIVE_PLAT_BAIDU);
							}
						} else {
							live.setLivePlat(BbsLive.LIVE_PLAT_TENCENT);
						}
						// 需要判断当前平台选择的视频云平台类型
						if (live.getLivePlat()
								.equals(BbsLive.LIVE_PLAT_TENCENT)) {
							initTencentPushBaseUrl();
							initTencentPlayBaseUrl();
							liveUrl = TencentCloudUtil.getRtmpPlayUrl(
									getTencentPlayBaseUrl(),
									config.getTencentBizId(), user.getId());
							demandUrl = TencentCloudUtil.getPlayUrl(
									getTencentPlayBaseUrl(),
									config.getTencentBizId(), user.getId(),
									"flv");
							liveMobileUrl = TencentCloudUtil.getPlayUrl(
									getTencentPlayBaseUrl(),
									config.getTencentBizId(), user.getId(),
									"m3u8");
						} else if (live.getLivePlat().equals(
								BbsLive.LIVE_PLAT_BAIDU)) {
							liveUrl = BaiduCloudUtil.getRtmpPlayUrl(
									config.getBaiduPlayDomain(), user.getId(),
									live.getEndTime(),
									config.getBaiduStreamSafeKey());
							demandUrl = BaiduCloudUtil.getPlayUrl(
									config.getBaiduPlayDomain(), user.getId(),
									live.getEndTime(),
									config.getBaiduStreamSafeKey(), "flv");
							liveMobileUrl = BaiduCloudUtil.getPlayUrl(
									config.getBaiduPlayDomain(), user.getId(),
									live.getEndTime(),
									config.getBaiduStreamSafeKey(), "m3u8");
						}
						live.setLiveUrl(liveUrl);
						live.setDemandUrl(demandUrl);
						if (StringUtils.isNotBlank(liveMobileUrl)) {
							live.setLiveMobileUrl(liveMobileUrl);
						}
						if (StringUtils.isNotBlank(demandImageUrl)) {
							live.setDemandImageUrl(demandImageUrl);
						}
						live.setChapter(liveChapterMng.findById(chapterId));
						live.setSite(site);
						if (limitUserNum != null) {
							BbsLiveRate rate = liveRateMng
									.findByNearLimitNum(limitUserNum);
							if (rate != null) {
								live.setCommissionRate(rate.getRate());
							}
						}
						liveMng.save(live);
						apiRecordMng.callApiRecord(
								RequestUtils.getIpAddr(request), appId,
								"/liveHost/save", sign);
						body = "{\"id\":" + "\"" + live.getId() + "\"}";
						log.info("save chapter id={}", live.getId());
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
	 * 直播管理-修改
	 *  id 必填
	 * 
	 */
	@RequestMapping(value = "/liveHost/edit")
	public void edit(Integer id, Integer cid, String qtitle, String qusername,
			Short qstatus, Date qtimeBegin, Date qtimeEnd, Integer qorderBy,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
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
				List<BbsLiveChapter> topList = liveChapterMng.getTopList(user
						.getId());
				List<BbsLiveChapter> chapterList = BbsLiveChapter
						.getListForSelect(topList, null);
				JSONArray jsonArray = new JSONArray();
				if (id != null) {
					if (chapterList != null && chapterList.size() > 0) {
						for (int i = 0; i < chapterList.size(); i++) {
							jsonArray.put(
									i,
									convertToJson2(chapterList.get(i),
											liveMng.findById(id), cid, qtitle,
											qusername, qstatus, qtimeBegin,
											qtimeEnd, qorderBy));
							status = Constants.API_STATUS_SUCCESS;
							message = Constants.API_MESSAGE_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
							body = jsonArray.toString();
						}
					}
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping(value = "/liveHost/update")
	public void update(Integer id, Integer chapterId, String title,
			String description, String liveLogo, Date beginTime, Date endTime,
			Date qFinishTimeBegin, Date qFinishTimeEnd, Double beginPrice,
			Double afterPrice, Integer limitUserNum, String livePlatKey,
			Short livePlat, Integer cid, String qtitle, String qusername,
			Short qstatus, Date qtimeBegin, Date qtimeEnd, Integer qorderBy,
			String sign, String appId, HttpServletRequest request,
			HttpServletResponse response) {

		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, appId, sign, title,
				beginTime, endTime, chapterId);
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
						updateLive(id, chapterId, title, description, liveLogo,
								beginTime, endTime, beginPrice, afterPrice,
								limitUserNum, livePlatKey, livePlat, null, cid);
						log.info("update live id={}", id);
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
	 * 直播管理-删除
	 * 
	 */
	@SignValidate
	@RequestMapping(value = "/liveHost/delete")
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
						BbsLive[] beans = liveMng.deleteByIds(ids);
						for (BbsLive bean : beans) {
							log.info("delete BbsLiveChapter id={}",
									bean.getId());
						}
						apiRecordMng.callApiRecord(
								RequestUtils.getIpAddr(request), appId,
								"/liveHost/delete", sign);
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
	 * 直播管理-查看拒绝原因
	 * 
	 * @throws JSONException
	 * 
	 */
	@RequestMapping(value = "/liveHost/viewReason")
	public void viewReason(Integer id, Integer cid, String qtitle,
			String qusername, Short qstatus, Date qtimeBegin, Date qtimeEnd,
			Integer qorderBy, HttpServletRequest request,
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
				JSONArray json = new JSONArray();
				if (id != null) {
					BbsLive live = liveMng.findById(id);
					json.put(convertToJson3(live, cid, qtitle, qusername,
							qstatus, qtimeBegin, qtimeEnd, qorderBy));
					message = Constants.API_MESSAGE_SUCCESS;
					status = Constants.API_STATUS_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}

			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 直播管理-查看参与者
	 * 
	 * @throws JSONException
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/liveHost/tickList")
	public void tickList(Integer liveId, Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			boolean liveHost = user.getLiveHost();
			// 判断是否有主讲人资质
			if (!liveHost) {
				message = "\"message.info.youAreNoHost\"";
				status = Constants.API_STATUS_FAIL;
			} else {
				if (liveId != null) {
					BbsLive live = liveMng.findById(liveId);
					if (!live.getUser().equals(user)) {
						message = "\"live.error.notYourLive\"";
						status = Constants.API_STATUS_FAIL;
					}
				}
				Pagination page = liveUserMng.getPage(null, null, liveId,
						pageNo, pageSize);
				List<BbsLiveUser> lives = (List<BbsLiveUser>) page.getList();
				int totalCount = page.getTotalCount();
				JSONArray jsonArray = new JSONArray();
				if (lives != null && lives.size() > 0) {
					for (int i = 0; i < lives.size(); i++) {
						jsonArray.put(i, convertToJson4(lives.get(i), liveId));
						status = Constants.API_STATUS_SUCCESS;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
						body = jsonArray.toString() + ",\"totalCount\":"
								+ totalCount;
					}
				} else {
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					status = Constants.API_STATUS_FAIL;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 申请提现
	 * 
	 * @param drawAmount
	 *            提现金额
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/live/host/draw")
	public void drawSubmit(Double drawAmount, HttpServletRequest request,
			HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, drawAmount);
		if (!errors.hasErrors()) {
			BbsLiveUserAccount account = liveUserAccountMng.findById(user
					.getId());
			if (account == null) {
				errors.addError(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
				code = ResponseCode.API_CODE_NOT_FOUND;
			} else {
				BbsConfigCharge config = configChargeMng.getDefault();
				if (drawAmount > account.getNoPayAmount()) {
					errors.addError("\"account balance not enough\"");
					code = ResponseCode.API_CODE_DRAW_BALANCE_NOT_ENOUGH;
				} else if (drawAmount < config.getMinDrawAmount()) {
					errors.addError("\"account draw less min amount\"");
					code = ResponseCode.API_CODE_DRAW_LESS;
				}
			}
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
			} else {
				accountDrawMng.draw(user, drawAmount, null,
						BbsAccountDraw.APPLY_TYPE_LIVE);
				log.info("draw submit success. id={}", user.getId());
				message = Constants.API_MESSAGE_SUCCESS;
				status = Constants.API_STATUS_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 删除直播提现申请
	 * 
	 * @param ids
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/live/host/delete")
	public void drawDel(String ids, HttpServletRequest request,
			HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				for (Integer id : idArray) {
					BbsAccountDraw d = accountDrawMng.findById(id);
					// 数据不存在
					if (errors.ifNotExist(d, BbsAccountDraw.class, id)) {
						errors.addError(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
						code = ResponseCode.API_CODE_NOT_FOUND;
						break;
					}
					// 非本用户数据
					if (!d.getDrawUser().getId().equals(user.getId())) {
						errors.noPermission(BbsAccountDraw.class, id);
						errors.addError("\"not your data\"");
						code = ResponseCode.API_CODE_DELETE_ERROR;
						break;
					}
					// 提现申请状态是申请成功待支付和提现成功
					if (d.getApplyStatus() == BbsAccountDraw.CHECKED_SUCC
							|| d.getApplyStatus() == BbsAccountDraw.DRAW_SUCC) {
						errors.addError("\"error.account.draw.hasChecked\"");
						code = ResponseCode.API_CODE_DELETE_ERROR;
						break;
					}
				}
				if (!errors.hasErrors()) {
					BbsAccountDraw[] deleteByIds = accountDrawMng
							.deleteByIds(idArray);
					for (int i = 0; i < deleteByIds.length; i++) {
						log.info("delete live draw id={}",
								deleteByIds[i].getId());
					}
					message = Constants.API_MESSAGE_SUCCESS;
					status = Constants.API_STATUS_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				} else {
					message = errors.getErrors().get(0);
				}
			} catch (Exception e) {
				status = Constants.API_MESSAGE_DELETE_ERROR;
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 直播-提现申请列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/live/host/draw_list")
	public void DrawGlyphList(Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		BbsUser user = CmsUtils.getUser(request);
		BbsLiveUserAccount account = liveUserAccountMng.findById(user.getId());
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = "\"\"";
		if (account != null) {
			Pagination page = accountDrawMng.getPage(user.getId(), null, null,
					null, BbsAccountDraw.APPLY_TYPE_LIVE, pageNo, pageSize);
			List<BbsAccountDraw> list = (List<BbsAccountDraw>) page.getList();
			int totalCount = page.getTotalCount();
			JSONArray jsonArray = new JSONArray();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			body = jsonArray.toString() + ",\"totalCount\":" + totalCount;
		} else {
			message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
			status = Constants.API_STATUS_FAIL;
			code = ResponseCode.API_CODE_NOT_FOUND;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	private JSONObject convertToJson(BbsLive lives/*
												 * , Map<String, String>
												 * pushUrlMap
												 */) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", lives.getId());
		json.put("title", lives.getTitle());// 标题
		json.put("createTime", lives.getCreateTime());// 创建时间
		json.put("beginTime", lives.getBeginTime());// 开始时间
		json.put("beginPrice", lives.getBeginPrice());// 未开始价格
		json.put("limitUserNum", lives.getLimitUserNum());// 限制用户数量,0无限制
		json.put("joinUserNum", lives.getJoinUserNum());// 参与人数
		json.put("totalAmount", lives.getTotalAmount());// 总收益
		json.put("yearAmount", lives.getYearAmount());// 年收益
		json.put("monthAmount", lives.getMonthAmount());// 月收益
		json.put("dayAmount", lives.getDayAmount());// 日收益
		json.put("checkStatus", lives.getCheckStatus());// 状态(0待审 1审核通过 2未通过
														// 3违规关停 )
		json.put("hasOver", lives.getHasOver());
		// json.put("pushUrlMap", pushUrlMap);
		return json;
	}

	private JSONObject convertToJson1(BbsLiveChapter chapter, BbsLiveChapter c)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", chapter.getId());
		json.put("chapter", c);
		json.put("child", chapter.getChild());
		return json;
	}

	private JSONObject convertToJson2(BbsLiveChapter chapter, BbsLive live,
			Integer cid, String qtitle, String qusername, Short qstatus,
			Date qtimeBegin, Date qtimeEnd, Integer qorderBy)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", chapter.getId());
		json.put("child", chapter.getChild());
		json.put("liveId", live.getId());// id
		json.put("title", live.getTitle());// 标题
		json.put("beginTime", live.getBeginTime());// 开始时间
		json.put("endTime", live.getEndTime());// 结束时间
		json.put("description", live.getDescription());// 描述
		json.put("beginPrice", live.getBeginPrice());// 未开始价格
		json.put("limitUserNum", live.getLimitUserNum());// 限制用户数量,0无限制
		json.put("commissionRate", live.getCommissionRate());// 佣金比例
		json.put("liveLogo", live.getLiveLogo());// logo图

		json.put("cid", cid);
		json.put("qtitle", qtitle);
		json.put("qusername", qusername);
		json.put("qstatus", qstatus);
		json.put("qtimeBegin", qtimeBegin);
		json.put("qtimeEnd", qtimeEnd);
		json.put("qorderBy", qorderBy);

		return json;
	}

	private JSONObject convertToJson3(BbsLive live, Integer cid, String qtitle,
			String qusername, Short qstatus, Date qtimeBegin, Date qtimeEnd,
			Integer qorderBy) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("reason", live.getReason());// 拒绝原因
		json.put("cid", cid);
		json.put("qtitle", qtitle);
		json.put("qusername", qusername);
		json.put("qstatus", qstatus);
		json.put("qtimeBegin", qtimeBegin);
		json.put("qtimeEnd", qtimeEnd);
		json.put("qorderBy", qorderBy);
		return json;
	}

	private JSONObject convertToJson4(BbsLiveUser liveUser, Integer liveId)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("liveId", liveId);// 参与者id
		json.put("username", liveUser.getJoinUser().getUsername());// 用户
		json.put("buyTime", liveUser.getBuyTime());// 获取票时间
		return json;

	}

	private void initTencentPushBaseUrl() {
		if (getTencentPushBaseUrl() == null) {
			setTencentPushBaseUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver
							.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),
					BbsHostLiveAct.TENCENT_PUSH_BASE_URL));
		}
	}

	private void initTencentPlayBaseUrl() {
		if (getTencentPlayBaseUrl() == null) {
			setTencentPlayBaseUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver
							.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),
					BbsHostLiveAct.TENCENT_PUSH_BASE_URL));
		}
	}

	private BbsLive updateLive(Integer id, Integer chapterId, String title,
			String description, String liveLogo, Date beginTime, Date endTime,
			Double beginPrice, Double afterPrice, Integer limitUserNum,
			String livePlatKey, Short livePlat, Short checkStatus, Integer cid) {
		BbsLive live = liveMng.findById(id);
		if (live != null) {
			CmsConfig config = cmsConfigMng.get();
			Integer hostId = live.getUser().getId();
			if (afterPrice != null) {
				live.setAfterPrice(afterPrice);
			}
			if (beginPrice != null) {
				live.setBeginPrice(beginPrice);
			}
			if (beginTime != null) {
				live.setBeginTime(beginTime);
			}
			if (StringUtils.isNotBlank(description)) {
				live.setDescription(description);
			}
			if (endTime != null) {
				live.setEndTime(endTime);
			}
			if (limitUserNum != null) {
				live.setLimitUserNum(limitUserNum);
			}
			if (StringUtils.isNotBlank(liveLogo)) {
				live.setLiveLogo(liveLogo);
			}
			if (StringUtils.isNotBlank(title)) {
				live.setTitle(title);
			}
			if (StringUtils.isNotBlank(livePlatKey)) {
				live.setLivePlatKey(livePlatKey);
			}
			if (livePlat != null) {
				live.setLivePlat(livePlat);
			} else {
				live.setLivePlat(BbsLive.LIVE_PLAT_BAIDU);
			}
			live.setChapter(liveChapterMng.findById(chapterId));
			if (limitUserNum != null) {
				BbsLiveRate rate = liveRateMng.findByNearLimitNum(limitUserNum);
				if (rate != null) {
					live.setCommissionRate(rate.getRate());
				}
			}
			if (checkStatus != null) {
				live.setCheckStatus(checkStatus);
			}
			String liveUrl = live.getLiveUrl(), demandUrl = live.getDemandUrl();
			String liveMobileUrl = live.getLiveMobileUrl();
			// 需要判断当前平台选择的视频云平台类型
			if (live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)) {
				initTencentPushBaseUrl();
				initTencentPlayBaseUrl();
				liveUrl = TencentCloudUtil.getRtmpPlayUrl(
						getTencentPlayBaseUrl(), config.getTencentBizId(),
						hostId);
				demandUrl = TencentCloudUtil.getPlayUrl(
						getTencentPlayBaseUrl(), config.getTencentBizId(),
						hostId, "flv");
				liveMobileUrl = TencentCloudUtil.getPlayUrl(
						getTencentPlayBaseUrl(), config.getTencentBizId(),
						hostId, "m3u8");
			} else if (live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)) {
				liveUrl = BaiduCloudUtil.getRtmpPlayUrl(
						config.getBaiduPlayDomain(), hostId, live.getEndTime(),
						config.getBaiduStreamSafeKey());
				demandUrl = BaiduCloudUtil.getPlayUrl(
						config.getBaiduPlayDomain(), hostId, live.getEndTime(),
						config.getBaiduStreamSafeKey(), "flv");
				liveMobileUrl = BaiduCloudUtil.getPlayUrl(
						config.getBaiduPlayDomain(), hostId, live.getEndTime(),
						config.getBaiduStreamSafeKey(), "m3u8");
			}
			if (StringUtils.isNotBlank(liveUrl)) {
				live.setLiveUrl(liveUrl);
			}
			if (StringUtils.isNotBlank(demandUrl)) {
				live.setDemandUrl(demandUrl);
			}
			if (StringUtils.isNotBlank(liveMobileUrl)) {
				live.setLiveMobileUrl(liveMobileUrl);
			}
			live = liveMng.update(live);
		}
		return live;
	}

	@Autowired
	private BbsLiveMng liveMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsLiveChapterMng liveChapterMng;
	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private BbsLiveRateMng liveRateMng;
	@Autowired
	private BbsLiveUserMng liveUserMng;
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsLiveUserAccountMng liveUserAccountMng;

	private String tencentPushBaseUrl;
	private String tencentPlayBaseUrl;

	public String getTencentPushBaseUrl() {
		return tencentPushBaseUrl;
	}

	public void setTencentPushBaseUrl(String tencentPushBaseUrl) {
		this.tencentPushBaseUrl = tencentPushBaseUrl;
	}

	public String getTencentPlayBaseUrl() {
		return tencentPlayBaseUrl;
	}

	public void setTencentPlayBaseUrl(String tencentPlayBaseUrl) {
		this.tencentPlayBaseUrl = tencentPlayBaseUrl;
	}

}
