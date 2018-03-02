package com.jeecms.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
	
	public static SimpleDateFormat common_format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dayFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat format1 = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss");

	/**
	 * 得到指定日期的一天的开始时刻00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDate(Date date) {
		String temp = format.format(date);
		temp += " 00:00:00";

		try {
			return format1.parse(temp);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 得到指定日期的一天的的最后时刻23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFinallyDate(Date date) {
		String temp = format.format(date);
		temp += " 23:59:59";

		try {
			return format1.parse(temp);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 是否是今天。根据System.currentTimeMillis() / 1000 / 60 / 60 / 24计算。
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		long day = date.getTime() / 1000 / 60 / 60 / 24;
		long currentDay = System.currentTimeMillis() / 1000 / 60 / 60 / 24;
		return day == currentDay;
	}
	
	public static boolean isInHour(Date date) {
		long hour = date.getTime() / 1000 / 60 / 60 ;
		long currentHour = System.currentTimeMillis() / 1000 / 60 / 60 ;
		return hour == currentHour;
	}
	

	/**
	 * 
	 * @param date
	 *            判断是否是本周，取出日期，依据日期取出该日所在周所有日期，在依据这些日期是否和本日相等
	 * @return
	 */
	public static boolean isThisWeek(Date date) {
		List<Date> dates = dateToWeek(date);
		Boolean flag = false;
		for (Date d : dates) {
			if (isToday(d)) {
				flag = true;
				break;
			} else {
				continue;
			}
		}
		return flag;
	}

	/**
	 * 
	 * @param date
	 *            判断是否是本月的日期
	 * @return
	 */
	public static boolean isThisMonth(Date date) {
		long year = date.getYear();
		long month = date.getMonth();
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime().getYear() == year
				&& calendar.getTime().getMonth() == month;
	}

	/**
	 * 
	 * @param date
	 *            判断是否是本年的日期
	 * @return
	 */
	public static boolean isThisYear(Date date) {
		long year = date.getYear();
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime().getYear() == year;
	}

	/**
	 * 
	 * @param mdate
	 *            取出改日的一周所有日期
	 * @return
	 */
	public static List<Date> dateToWeek(Date mdate) {
		int day = mdate.getDay();
		Date fdate;
		List<Date> list = new ArrayList();
		Long fTime = mdate.getTime() - day * 24 * 3600000;
		for (int i = 0; i < 7; i++) {
			fdate = new Date();
			fdate.setTime(fTime + ((i+1) * 24 * 3600000));
			list.add(i, fdate);
		}
		return list;
	}

	public static Double getDiffMinuteTwoDate(Date begin, Date end) {
		return (end.getTime() - begin.getTime()) / 1000 / 60.0;
	}
	
	public static Integer getDiffIntMinuteTwoDate(Date begin, Date end) {
		return (int) ((end.getTime() - begin.getTime()) / 1000 / 60);
	}
	
	public static Double getDiffHourTwoDate(Date begin, Date end) {
		return getDiffMinuteTwoDate(begin, end)/60;
	}
	
	public static Integer getDiffIntHourTwoDate(Date begin, Date end) {
		return  getDiffHourTwoDate(begin, end).intValue();
	}
	
	public static final int getDaysBetween(Date early, Date late) { 
        java.util.Calendar calst = java.util.Calendar.getInstance();   
        java.util.Calendar caled = java.util.Calendar.getInstance();   
        calst.setTime(early);   
        caled.setTime(late);   
        //设置时间为0时   
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);   
        calst.set(java.util.Calendar.MINUTE, 0);   
        calst.set(java.util.Calendar.SECOND, 0);   
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);   
        caled.set(java.util.Calendar.MINUTE, 0);   
        caled.set(java.util.Calendar.SECOND, 0);   
        //得到两个日期相差的天数   
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst   
                .getTime().getTime() / 1000)) / 3600 / 24;   
        return days;   
   }   
  

	public static String parseDate(Date date, String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		String dateString;
		dateString = formater.format(date);
		return dateString;
	}
	
	public static Date parseDateTimeToDay(Date date) {
		return parseDateFormat(date, "yyyy-MM-dd");
	}
	
	public static Date parseDateFormat(Date date, String format) {
		String dateString;
		dateString = common_format.format(date);
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		try {
			return dateformat.parse(dateString);
		} catch (ParseException e) {
			//e.printStackTrace();
		}
		return null;
	}

	public static Date afterDate(Date date, Integer after) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + after);
		return calendar.getTime();
	}
	
	public static String parseDateToTimeStr(Date  date){
		return common_format.format(date);
	}
	
	public static String parseDateToDateStr(Date  date){
		return dayFormat.format(date);
	}
	
	public static Date parseDayStrToDate(String dateStr){
		try {
			return dayFormat.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date parseTimeStrToDate(String dateStr){
		try {
			return common_format.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 获取date月后的amount月的第一天的开始时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficMonthStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, amount);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getStartDate(cal.getTime());
	}

	/**
	 * 获取当前自然月后的amount月的最后一天的终止时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficMonthEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getSpecficMonthStart(date, amount + 1));
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFinallyDate(cal.getTime());
	}
	
	/**
	 * 获取date年后的amount年的第一天的开始时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficYearStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, amount);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		return getStartDate(cal.getTime());
	}

	/**
	 * 获取date年后的amount年的最后一天的终止时间
	 * 
	 * @param amount
	 *            可正、可负
	 * @return
	 */
	public static Date getSpecficYearEnd(Date date, int amount) {
		Date temp = getStartDate(getSpecficYearStart(date, amount + 1));
		Calendar cal = Calendar.getInstance();
		cal.setTime(temp);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return getFinallyDate(cal.getTime());
	}
	
	public static Date getSpecficDateStart(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return getStartDate(cal.getTime());
	}
	
	public static Date getSpecficDateEnd(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return getFinallyDate(cal.getTime());
	}
	
	public static Date getSpecficDate(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		return cal.getTime();
	}
	
	public static Date getDateMinute(Date date, int minute) {
		long t = date.getTime();
		t +=minute*60*1000;
		Date d=new Date(t);
		return d;
	}
	
	

	public static void main(String arg[]) {
		String d="2017-9-24 11:13:45";
		//System.out.println(DateUtils.parseTimeStrToDate(d).getTime()/1000);
		//System.out.println(Long.toHexString(DateUtils.parseTimeStrToDate(d).getTime()/1000));
		Long l=1505145599l;
		//System.out.println(new Date(l));
		//System.out.println(isToday(DateUtils.parseTimeStrToDate(d)));
		//System.out.println(isThisWeek(DateUtils.parseTimeStrToDate(d)));
		List<Date>list=dateToWeek(new Date());
		for(Date s:list){
			System.out.println(s);
		}
	}
}
