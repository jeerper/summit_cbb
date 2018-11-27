package com.summit.util;


import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author yt
 *
 */
public class StringUtil {


	public static boolean isInteger(String str) {
		return str.matches("^[+-]?\\d+$");
	}

	
	public static boolean isDouble(String str) {
		return str.matches("^[+-]?\\d+(\\.\\d+$)?");
	}

	
	public static boolean isSafe(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		return str.matches("[\\w]{4,}");
	}


	public static boolean isEmail(String email) {
		int limit = 256;
		if (email == null || email.length() < 1 || email.length() > limit) {
			return false;
		}
		return email.matches("^[-+.\\w]+@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	}


	public static boolean isChinese(String str) {
		return str.matches("^[\u0391-\uFFE5]+$");
	}


	public static boolean isPhoneNumber(String pn) {
		try {
			return pn.matches("^1[\\d]{10}$");

		} catch (RuntimeException e) {
			return false;
		}
	}

	
	public synchronized static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}


	public static String getNonceStr(int count) {
		return getHexString(NumberUtil.random((int) Math.pow(16, count)), count);
	}


	public static String getHexString(int number, int length) {
		String value = Integer.toHexString(number);
		return fullStringBefore(value, '0', length);
	}


	public static String fullStringBefore(String src,char full,int length){
		if (length>0) {
			if (src.length()>length) {
				return src.substring(src.length()-length);
			}else if(src.length()<length){
				return appendStringNotNull(null, getNString(full, length-src.length()),src);
			}
		}
		return src;
	}


	public static String fullStringAfter(String src,char full,int length){
		if (length>0) {
			if (src.length()>length) {
				return src.substring(src.length()-length);
			}else if(src.length()<length){
				return appendStringNotNull(null,src, getNString(full, length-src.length()));
			}
		}
		return src;
	}

	public static String getNString(char src, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(src);
		}
		return sb.toString();
	}
	
	public static String getNString(String src, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(src);
		}
		return sb.toString();
	}

	
	public static String[] indexOf(String str, String tar, int startIndex) {
		if (str != null) {
			int index = str.indexOf(tar, startIndex);
			if (index > 0) {
				return new String[] { str.substring(startIndex, index), str.substring(index + tar.length()) };
			}
		}
		return new String[] { "", str };
	}

	
	public static boolean inStrings(String str, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append(".+,");
		sb.append(str);
		sb.append(",.+|^");
		sb.append(str);
		sb.append(",.+|^");
		sb.append(str);
		sb.append("$|,.+");
		sb.append(str);
		sb.append("$");
		return content.matches(sb.toString());
	}


	public static String subString(String str, int start, int end) {
		if (StringUtils.isNotBlank(str) && start < end) {
			if (str.length() > end) {
				return str.substring(start, end);
			} else {
				if (str.length() > start) {
					return str.substring(start);
				} else {
					return "";
				}
			}
		}
		return str;
	}


	public static boolean checkNotEmpty(String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (StringUtils.isBlank(strings[i])) {
				return false;
			}
		}
		return true;
	}

	
	public static String appendStringByObject(String split, Object... strings) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strings.length;) {
			sb.append(strings[i]);
			if (++i < strings.length && StringUtils.isNotEmpty(split)) {
				sb.append(split);
			}
		}
		return sb.toString();
	}
	
	public static String appendStringNotNull(String split, String... strings) {
		if (checkNotEmpty(strings)) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < strings.length;) {
				sb.append(strings[i]);
				if (++i < strings.length && StringUtils.isNotEmpty(split)) {
					sb.append(split);
				}
			}
			return sb.toString();
		}
		return "";
	}


	public static String appendString(String split, String nullCharpter, String... strings) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strings.length;) {
			String str = strings[i];
			if (str != null) {
				sb.append(str);
			} else {
				sb.append(nullCharpter);
			}
			if (++i < strings.length && StringUtils.isNotEmpty(split)) {
				sb.append(split);
			}
		}
		return sb.toString();
	}

	
	public static String getOrderNum() {
		String now = DateUtil.format("yyyyMMddHHmmssSSS", new Date());
		int num = NumberUtil.random(6);
		return now + num;
	}
	

	public static String beanNameToTablleName(String beanName) {
		StringBuffer sb = new StringBuffer();
		for (char c : beanName.toCharArray()) {
			int code = (int) c;
			if (code >= 65 && code <= 90) {
				if (sb.length() > 0) {
					sb.append('_');
				}
				sb.append((char) (code + 32));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	

	public static boolean checkLength(String str,int minLength,int maxLength){
		if (str == null) {
			return false;
		}
		if (str.length() >= minLength && str.length() <= maxLength) {
			return true;
		}
		return false;
	}
	

	public static boolean isLoginName(String str){
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches("^[A-Za-z0-9_]{6,20}$");
	}
	
}

