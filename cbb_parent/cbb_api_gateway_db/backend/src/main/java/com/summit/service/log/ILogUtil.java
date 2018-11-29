package com.summit.service.log;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.summit.domain.log.LogBean;
/**
 * 日志记录工具
 * @param request:获取请求的相关信息（ip）
 * @param logType:日志类型  1：系统日志   2：运行日志
 * @param funName:访问模块的名称
 * 
 *
 */
@Component
public interface ILogUtil {
	
	public LogBean insertLog(HttpServletRequest request,String logType,String funName);
	
	public int updateLog(LogBean logBean,String type);

}
