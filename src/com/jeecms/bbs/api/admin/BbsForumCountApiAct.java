package com.jeecms.bbs.api.admin;

import java.util.Date;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsForumCount;
import com.jeecms.bbs.manager.BbsForumCountMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.ExcelUtil;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsForumCountApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsForumCountApiAct.class);
	
	@Autowired
	private BbsForumCountMng manager;
	@Autowired
	private BbsForumMng forumMng;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/data/forumstatistic_single_list")
	public void singleCountList(Integer forumId,Date begin,Date end,Integer pageNo,
			Integer pageSize,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Pagination page = manager.getPage(forumId, begin, end, pageNo, pageSize);
		List<BbsForumCount> list = (List<BbsForumCount>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null) {
			for(int i = 0 ; i < list.size() ; i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/data/forumstatistic_list")
	public void countList(Date begin,Date end,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Date now=Calendar.getInstance().getTime();
		if (begin==null) {
			begin = DateUtils.parseDateTimeToDay(now);
		}
		if (end==null) {
			end = DateUtils.parseDateTimeToDay(now);
		}
		List<Object[]> list = manager.getList(begin, end, Integer.MAX_VALUE);
		JSONArray jsonArray = new JSONArray();
		if (list!=null) {
			for(int i = 0 ; i < list.size() ; i++){
				Object[] objects = list.get(i);
				JSONObject json = convertToObject(objects);
				jsonArray.put(i,json);
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	@RequestMapping(value = "/data/forumstatistic_export")
	public void export(Date begin,Date end
			,HttpServletRequest request,HttpServletResponse response) throws ParseException{
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, begin,end);
		if (!errors.hasErrors()) {
			List<Object[]> list = manager.getList(begin, end, Integer.MAX_VALUE);
			String fileName = "板块统计"+System.currentTimeMillis()+".xls"; //文件名 
	        String sheetName = "板块统计";//sheet名
	        String []title = new String[]{"板块","贴子数","回复数","访问量"};//标题
	        String [][] values = new String[list.size()][];
	        for (int i = 0; i < list.size(); i++) {
				values[i] = new String[title.length];
				//将对象内容转换成String
				Object[] obj = list.get(i);
				values[i][0] = obj[1].toString();
				values[i][1] = obj[2].toString();
				values[i][2] = obj[3].toString();
				values[i][3] = obj[4].toString();
			}
	        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, values, null);
	        //将文件存到指定位置
	        try {
				ExcelUtil.setResponseHeader(request, response, fileName);
				OutputStream os = response.getOutputStream();
				wb.write(os);
				log.info("export forumstatistic");
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				os.flush();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/data/forumstatistic_single_export")
	public void singleExport(Integer forumId,Date begin,Date end,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, forumId);
		if (!errors.hasErrors()) {
			BbsForum forum = forumMng.findById(forumId);
			String fileName = "板块统计"+System.currentTimeMillis()+".xls"; //文件名 
	        String sheetName = "板块统计";//sheet名
	        if(forum!=null){
	        	fileName=forum.getTitle()+fileName;
	        	sheetName=forum.getTitle()+fileName;
	        }
	        String []title = new String[]{"日期","贴子数","回复数","访问量"};//标题
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Pagination page = manager.getPage(forumId, begin, end, pageNo, pageSize);
	        List<BbsForumCount> list = (List<BbsForumCount>) page.getList();
	        String [][]values = new String[list.size()][];
	        for(int i=0;i<list.size();i++){
	            values[i] = new String[title.length];
	            //将对象内容转换成string
	            BbsForumCount obj = list.get(i);
	            values[i][0] = sdf.format(obj.getCountDate());
	            values[i][1] = obj.getTopicCount().toString();
	            values[i][2] = obj.getPostCount().toString();
	            values[i][3] = obj.getVisitCount().toString();
	        }
	        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, values, null);
	        //将文件存到指定位置  
	        try {  
	        	ExcelUtil.setResponseHeader(request,response, fileName);  
	            OutputStream os = response.getOutputStream();  
	            wb.write(os);
	            log.info("export forumstatistic single");
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
	            os.flush();  
	            os.close();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	private JSONObject convertToObject(Object[] objects) throws JSONException{
		JSONObject json = new JSONObject();
		if (objects!=null&&objects.length==5) {
			if (objects[0]!=null) {
				json.put("forumId", objects[0]);
			}else{
				json.put("forumId", "");
			}
			if (objects[1]!=null) {
				json.put("forumTitle", objects[1]);
			}else{
				json.put("forumTitle", "");
			}
			if (objects[2]!=null) {
				json.put("sumTopicCount", objects[2]);
			}else{
				json.put("sumTopicCount", "");
			}
			if (objects[3]!=null) {
				json.put("sumPostCount", objects[3]);
			}else{
				json.put("sumPostCount", "");
			}
			if (objects[4]!=null) {
				json.put("sumVisitCount", objects[4]);
			}else{
				json.put("sumVisitCount", "");
			}
		}else{
			json.put("forumId", "");
			json.put("forumTitle", "");
			json.put("sumTopicCount", "");
			json.put("sumPostCount", "");
			json.put("sumVisitCount", "");
		}
		return json;
	}
}
