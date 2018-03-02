package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.ExcelUtil;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsForumCount;
import com.jeecms.bbs.manager.BbsForumCountMng;
import com.jeecms.bbs.manager.BbsForumMng;

@Controller
public class BbsForumCountAct {
	private static final Logger log = LoggerFactory.getLogger(BbsForumCountAct.class);

	@RequiresPermissions("data:singleforumstatistic")
	@RequestMapping("/data/singleforumstatistic.do")
	public String singleForumCountList(Integer forumId,Date begin,Date end,
			Integer pageNo,HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(forumId,begin,end,cpn(pageNo), 
				CookieUtils.getPageSize(request));
		BbsForum forum = null;
		if(forumId!=null){
			forum=forumMng.findById(forumId);
		}
		model.addAttribute("pagination",pagination);
		model.addAttribute("forumId",forumId);
		model.addAttribute("forum",forum);
		model.addAttribute("begin",begin);
		model.addAttribute("end",end);
		return "data/singleforumstatistic";
	}
	
	@RequiresPermissions("data:forumstatistic")
	@RequestMapping("/data/forumstatistic.do")
	public String list(Date begin,Date end,Integer pageNo, 
			HttpServletRequest request, ModelMap model) {
		Date now=Calendar.getInstance().getTime();
		if(begin==null){
			begin=DateUtils.parseDateTimeToDay(now);
		}
		if(end==null){
			end=DateUtils.parseDateTimeToDay(now);
		}
		List<Object[]>list = manager.getList(begin, end, Integer.MAX_VALUE);
		model.addAttribute("pagination",list);
		model.addAttribute("begin",begin);
		model.addAttribute("end",end);
		return "data/forumstatistic";
	}
	
	@RequiresPermissions("data:forumCountExport")
	@RequestMapping("/data/forumCountExport.do")
	public void exportExcel(Date begin,Date end,
			HttpServletRequest request,HttpServletResponse response){
		String fileName = "板块统计"+System.currentTimeMillis()+".xls"; //文件名 
        String sheetName = "板块统计";//sheet名
        String []title = new String[]{"板块","贴子数","回复数","访问量"};//标题
        List<Object[]> list =manager.getList(begin, end, Integer.MAX_VALUE);
        
        String [][]values = new String[list.size()][];
        for(int i=0;i<list.size();i++){
            values[i] = new String[title.length];
            //将对象内容转换成string
            Object[] obj = list.get(i);
            values[i][0] = obj[1].toString();
            values[i][1] = obj[2].toString();
            values[i][2] = obj[3].toString();
            values[i][3] = obj[4].toString();
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
	
	@RequiresPermissions("data:singleForumCountExport")
	@RequestMapping("/data/singleForumCountExport.do")
	public void sigleForumExportExcel(Integer forumId,Date begin,Date end,
			HttpServletRequest request,HttpServletResponse response){
		BbsForum forum=null;
		if(forumId!=null){
			forum=forumMng.findById(forumId);
		}
		String fileName = "板块统计"+System.currentTimeMillis()+".xls"; //文件名 
        String sheetName = "板块统计";//sheet名
        if(forum!=null){
        	fileName=forum.getTitle()+fileName;
        	sheetName=forum.getTitle()+fileName;
        }
        String []title = new String[]{"日期","贴子数","回复数","访问量"};//标题
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BbsForumCount> list =manager.getList(forumId,begin, end, null,Integer.MAX_VALUE);
        
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
             os.flush();  
             os.close();  
        } catch (Exception e) {  
             e.printStackTrace();  
        }  
	}

	
	@Autowired
	private BbsForumCountMng manager;
	@Autowired
	private BbsForumMng forumMng;
}