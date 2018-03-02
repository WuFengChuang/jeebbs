package com.jeecms.bbs.api.admin;

import java.io.OutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
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
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsIncomeStatistic;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.ExcelUtil;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsIncomeStatisticApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsIncomeStatisticApiAct.class);
	
	@Autowired
	private BbsIncomeStatisticMng manager;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/data/incomeStatistic_list")
	public void statisticList(Date begin,Date end,Integer pageNo,Integer pageSize
			,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Pagination page = manager.getPage(begin, end, pageNo, pageSize);
		List<BbsIncomeStatistic> list = (List<BbsIncomeStatistic>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null) {
			for(int i = 0 ; i < list.size(); i++){
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
	
	@RequestMapping(value = "/data/incomeStatistic_get")
	public void statisticGet(Integer id,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsIncomeStatistic bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new BbsIncomeStatistic();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				JSONObject json = bean.convertToJson();
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}else{
				code = ResponseCode.API_CODE_NOT_FOUND;
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/data/incomeStatistic_export")
	public void export(Date begin,Date end,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String fileName = "收益统计"+System.currentTimeMillis()+".xls";
		String sheetName = "收益统计";
		String[] title = new String[]{"日期","总收益","广告收益","礼物收益",
				"道具收益","直播收益","帖子收益"};
		Pagination page = manager.getPage(begin, end, pageNo, pageSize);
		List<BbsIncomeStatistic> list = (List<BbsIncomeStatistic>) page.getList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String [][]values = new String[list.size()][];
		for(int i = 0 ; i < list.size(); i++){
			values[i] = new String[title.length];
			//将对象内容转换成字符串
			BbsIncomeStatistic obj = list.get(i);
			values[i][0] = sdf.format(obj.getIncomeDate());
			values[i][1] = obj.getTotalIncomeAmount().toString();
			values[i][2] = obj.getAdIncomeAmount().toString();
			values[i][3] = obj.getGiftIncomeAmount().toString();
			values[i][4] = obj.getMagicIncomeAmount().toString();
			values[i][5] = obj.getLiveIncomeAmount().toString();
			values[i][6] = obj.getPostIncomeAmount().toString();
		}
		HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, values, null);
		//将文件存到指定位置
		try {
			ExcelUtil.setResponseHeader(request, response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			log.info("export incomeStatistic");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
}
