package com.summit.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * @author yt
 *
 */
@Component
public class ResultUtil {
	public  JSONObject responseMsg(String respCode) {
		return responseMsg(respCode, null);
	}

	public  JSONObject responseMsg(String respCode, Object obj) {
		JSONObject js = new JSONObject();
		if (StringUtils.isEmpty(respCode)) {
			js.put("code", ExceptionEnum._503);
			js.put("message", ExceptionEnum.getNameByIndex("503"));
		}else{
			js.put("code", respCode);
			js.put("message", ExceptionEnum.getNameByIndex(respCode));
			js.put("data", obj);
		}
		return js;
	}
}
