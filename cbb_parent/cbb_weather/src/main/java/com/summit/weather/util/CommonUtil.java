package com.summit.weather.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * @Title:：CommonUtil.java
 * @Package ：com.summit.homs.util @Description： TODO @author： hyn @date：
 *          2018年9月9日 上午9:46:24
 * @version ： 1.0
 */
public class CommonUtil {

	static Pattern pattern = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");

	public static String getCdata(String cdataStr) {
		Matcher m = pattern.matcher(cdataStr);
		if (m.matches()) {
			return m.group(1);
		} else {
			return cdataStr;
		}
	}
}
