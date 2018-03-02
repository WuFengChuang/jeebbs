package com.jeecms.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.jeecms.bbs.Constants;

/**
 * 字符串的帮助类，提供静态方法，不可以实例化。
 * 
 * @author tom
 * 
 */
public class StrUtils {
	/**
	 * 禁止实例化
	 */
	private StrUtils() {
	}

	/**
	 * 处理url
	 * 
	 * url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
	 * 
	 * @param url
	 * @return
	 */
	public static String handelUrl(String url) {
		if (url == null) {
			return null;
		}
		url = url.trim();
		if (url.equals("") || url.startsWith("http://")
				|| url.startsWith("https://")) {
			return url;
		} else {
			return "http://" + url.trim();
		}
	}

	/**
	 * 分割并且去除空格
	 * 
	 * @param str
	 *            待分割字符串
	 * @param sep
	 *            分割符
	 * @param sep2
	 *            第二个分隔符
	 * @return 如果str为空，则返回null。
	 */
	public static String[] splitAndTrim(String str, String sep, String sep2) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		if (!StringUtils.isBlank(sep2)) {
			str = StringUtils.replace(str, sep2, sep);
		}
		String[] arr = StringUtils.split(str, sep);
		// trim
		for (int i = 0, len = arr.length; i < len; i++) {
			arr[i] = arr[i].trim();
		}
		return arr;
	}

	/**
	 * 文本转html
	 * 
	 * @param txt
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		boolean doub = false;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if (c == ' ') {
				if (doub) {
					sb.append(' ');
					doub = false;
				} else {
					sb.append("&nbsp;");
					doub = true;
				}
			} else {
				doub = false;
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\n':
					sb.append("<br/>");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param keyword 源词汇
	 * @param smart 是否智能分词
	 * @return 分词词组(,拼接)
	 */
	public static String getKeywords(String keyword, boolean smart) {
		StringReader reader = new StringReader(keyword);
		IKSegmenter iks = new IKSegmenter(reader, smart);
		StringBuilder buffer = new StringBuilder();
		try {
			Lexeme lexeme;
			while ((lexeme = iks.next()) != null) {
				buffer.append(lexeme.getLexemeText()).append(Constants.COMMON_SPLIT);
			}
		} catch (IOException e) {
		}
		//去除最后一个,
		if (buffer.length() > 0) {
			buffer.setLength(buffer.length() - 1);
		}
		return buffer.toString();
	}

	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}

	public static String htmlCut(String s, int len, String append) {
		String text = html2Text(s, len * 2);
		return textCut(text, len, append);
	}

	public static String html2Text(String html, int len) {
		try {
			Lexer lexer = new Lexer(html);
			Node node;
			StringBuilder sb = new StringBuilder(html.length());
			while ((node = lexer.nextNode()) != null) {
				if (node instanceof TextNode) {
					sb.append(node.toHtml());
				}
				if (sb.length() > len) {
					break;
				}
			}
			return sb.toString();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static  List<String> getImageSrc(String htmlCode) {  
        List<String> imageSrcList = new ArrayList<String>();  
        String regular="<img(.*?)src=\"(.*?)\"";  
        String img_pre="(?i)<img(.*?)src=\"";
        String img_sub="\"";
        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);  
        String src = null;  
        while (m.find()) {  
        	src=m.group();
        	src=src.replaceAll(img_pre, "").replaceAll(img_sub, "").trim();
            imageSrcList.add(src);  
        }  
        return imageSrcList;  
    }
	
	public static  List<String> getLinkSrc(String htmlCode) {  
        List<String> imageSrcList = new ArrayList<String>();  
        String regular="<a(.*?)href=\"(.*?)\"(.*?)</a>";  
        String video_pre="<a(.*?)href=\"";
        String video_sub="\"(.*?)</a>";
        Pattern p=Pattern.compile(regular,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);  
        String src = null;  
        while (m.find()) {  
        	src=m.group();
        	src=src.replaceAll(video_pre, "").replaceAll(video_sub, "").trim();
            imageSrcList.add(src);  
        }  
        return imageSrcList;  
    }

	/**
	 * 检查字符串中是否包含被搜索的字符串。被搜索的字符串可以使用通配符'*'。
	 * 
	 * @param str
	 * @param search
	 * @return
	 */
	public static boolean contains(String str, String search) {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(search)) {
			return false;
		}
		String reg = StringUtils.replace(search, "*", ".*");
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}
	
	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符
	 * 
	 * @param s
	 * @return
	 */
	public static  String xssEncode(String s) {
		if (s == null || s.equals("")) {
			return s;
		}
		//< > ' " \ / # & 
//		s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");  
//		s = s.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");  
//		s = s.replaceAll("'", "&#39;");  
//		s = s.replaceAll("eval\\((.*)\\)", "");  
//		s = s.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");  
//		s = s.replaceAll("script", "");
//		s = s.replaceAll("#", "＃");
		//s = s.replaceAll("%", "％");
		/*
		try {
			s = URLDecoder.decode(s, UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		*/
		String result = stripXSS(s); 
		/*
        if (null != result) {  
            result = escape(result);  
        }  
        */
		return result;
	}
	
	 public static String escape(String s) {  
	        StringBuilder sb = new StringBuilder(s.length() + 16);  
	        for (int i = 0; i < s.length(); i++) {  
	            char c = s.charAt(i);  
	            switch (c) {  
	            case '>':  
	                sb.append('＞');// 全角大于号  
	                break;  
	            case '<':  
	                sb.append('＜');// 全角小于号  
	                break;  
	            case '\'':  
	                sb.append('‘');// 全角单引号  
	                break;  
	            case '\"':  
	                sb.append('“');// 全角双引号  
	                break;  
	            case '\\':  
	                sb.append('＼');// 全角斜线  
	                break;  
	            case '%':  
	                sb.append('％'); // 全角冒号  
	                break;  
	            case ';':  
	                sb.append('；'); // 全角分号  
	                break;  
	            default:  
	                sb.append(c);  
	                break;  
	            }  
	  
	        }  
	        return sb.toString();  
	    }  
	 private static String stripXSS(String value) {  
	        if (value != null) {  
	            // NOTE: It's highly recommended to use the ESAPI library and  
	            // uncomment the following line to  
	            // avoid encoded attacks.  
	            // value = ESAPI.encoder().canonicalize(value);  
	            // Avoid null characters  
	            value = value.replaceAll("", "");  
	            // Avoid anything between script tags  
	            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>",  
	                    Pattern.CASE_INSENSITIVE);  
	  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            // Avoid anything in a src='...' type of expression  
	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",  
	                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
	                            | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",  
	                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
	                            | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            // Remove any lonesome </script> tag  
	            scriptPattern = Pattern.compile("</script>",  
	                    Pattern.CASE_INSENSITIVE);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            // Remove any lonesome <script ...> tag  
	            scriptPattern = Pattern.compile("<script(.*?)>",  
	                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
	                            | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            // Avoid eval(...) expressions  
	            scriptPattern = Pattern.compile("eval\\((.*?)\\)",  
	                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
	                            | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            // Avoid expression(...) expressions  
	            scriptPattern = Pattern.compile("expression\\((.*?)\\)",  
	                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
	                            | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            // Avoid javascript:... expressions  
	            scriptPattern = Pattern.compile("javascript:",  
	                    Pattern.CASE_INSENSITIVE);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            // Avoid vbscript:... expressions  
	            scriptPattern = Pattern.compile("vbscript:",  
	                    Pattern.CASE_INSENSITIVE);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            // Avoid onload= expressions  
	            scriptPattern = Pattern.compile("onload(.*?)=",  
	                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
	                            | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            
	            scriptPattern = Pattern.compile("onmouseover(.*?)=",  
	                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
	                            | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	  
	            scriptPattern = Pattern.compile("<iframe>(.*?)</iframe>",  
	                    Pattern.CASE_INSENSITIVE);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	  
	            scriptPattern = Pattern.compile("</iframe>",  
	                    Pattern.CASE_INSENSITIVE);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	  
	            // Remove any lonesome <script ...> tag  
	            scriptPattern = Pattern.compile("<iframe(.*?)>",  
	                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE  
	                            | Pattern.DOTALL);  
	            value = scriptPattern.matcher(value).replaceAll("");  
	            value = value.replace(";", "");  
	            value = value.replace("<", "");  
	            value = value.replace(">", "");  
	        }  
	        return value;  
	    }  
	
	/**
	 * 保留两位小数（四舍五入）
	 * @param value
	 * @return
	 */
	public static Double retainTwoDecimal(double value){
		long l1 = Math.round(value*100); //四舍五入
		double ret = l1/100.0; //注意:使用 100.0 而不是 100
		return ret;
	}
}
