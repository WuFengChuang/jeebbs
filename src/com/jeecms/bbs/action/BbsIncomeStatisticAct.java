package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.ExcelUtil;
import com.jeecms.bbs.entity.BbsIncomeStatistic;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;
import com.jeecms.common.web.CookieUtils;

@Controller
public class BbsIncomeStatisticAct {
	private static final Logger log = LoggerFactory.getLogger(BbsIncomeStatisticAct.class);

	@RequiresPermissions("data:incomestatistic")
	@RequestMapping("/data/incomestatistic.do")
	public String statistic(Date begin,Date end,Integer pageNo, 
			HttpServletRequest request,
			ModelMap model) {
		Pagination pagination = manager.getPage(begin,end,cpn(pageNo), 
				CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("begin",begin);
		model.addAttribute("end",end);
		return "data/incomestatistic";
	}
	
	@RequiresPermissions("data:incomeExport")
	@RequestMapping("/data/incomeExport.do")
	public void exportExcel(Date begin,Date end,
			HttpServletRequest request,HttpServletResponse response){
		String fileName = "收益统计"+System.currentTimeMillis()+".xls"; //文件名 
        String sheetName = "收益统计";//sheet名
        String []title = new String[]{"日期","总收益","广告收益","礼物收益","道具收益","直播收益","帖子收益"};//标题
        
        List<BbsIncomeStatistic> list =manager.getList(begin, end, null,Integer.MAX_VALUE);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        String [][]values = new String[list.size()][];
        for(int i=0;i<list.size();i++){
            values[i] = new String[title.length];
            //将对象内容转换成string
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
        	 ExcelUtil.setResponseHeader(request,response, fileName);  
             OutputStream os = response.getOutputStream();  
             wb.write(os);  
             os.flush();  
             os.close();  
        } catch (Exception e) {  
             e.printStackTrace();  
        }  
	}
	
	
	@Autowired
	private BbsIncomeStatisticMng manager;
}