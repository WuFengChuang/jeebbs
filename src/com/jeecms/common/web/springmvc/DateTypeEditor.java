package com.jeecms.common.web.springmvc;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * 日期编辑器
 * 
 * 根据日期字符串长度判断是长日期还是短日期。只支持yyyy-MM-dd，yyyy-MM-dd HH:mm:ss两种格式。
 * 
 * @author tom
 * 
 */
public class DateTypeEditor extends PropertyEditorSupport {
	public static final DateFormat DF_LONG = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final DateFormat DF_SHORT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final DateFormat DF_MINUTE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	/**
	 * 短类型日期长度
	 */
	public static final int SHORT_DATE = 10;
	/**
	 * 分钟时间长度
	 */
	public static final int MINUTE_DATE = 16;
	//throws IllegalArgumentException
	public void setAsText(String text)  {
		text = text.trim();
		if (!StringUtils.hasText(text)) {
			setValue(null);
			return;
		}
		try {
			if (text.length() <= SHORT_DATE) {
				//setValue(new java.sql.Date(DF_SHORT.parse(text).getTime()));
				setValue(new java.util.Date(DF_SHORT.parse(text).getTime()));
			}else if (text.length() == MINUTE_DATE) {
				//setValue(new java.sql.Date(DF_MINUTE_FORMAT.parse(text).getTime()));
				setValue(new java.util.Date(DF_MINUTE_FORMAT.parse(text).getTime()));
			}else {
				//setValue(new java.sql.Timestamp(DF_LONG.parse(text).getTime()));
				setValue(new java.util.Date(DF_LONG.parse(text).getTime()));
			}
		} catch (ParseException ex) {
			IllegalArgumentException iae = new IllegalArgumentException(
					"Could not parse date: " + ex.getMessage());
			iae.initCause(ex);
			throw iae;
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? DF_LONG.format(value) : "");
	}
}
