package com.summit.service.log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.summit.common.entity.UserInfo;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.domain.log.LogBean;
import com.summit.repository.UserRepository;
import com.summit.util.SummitTools;

@Component
@Service
public class LogUtilImpl implements ILogUtil {
	private static final Logger logger = LoggerFactory.getLogger(LogUtilImpl.class);
	@Autowired
	public UserRepository ur;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * 新增日志方法
	 * @param request
	 * @param logType:日志类型  1：系统日志  2：运行日志
	 * @param funName:访问的模块名称
	 * @return LogBean:日志实体类
	 * 
	 */
	public  LogBean insertLog(HttpServletRequest request, String logType,String funName,String userName) {
		if(userName==null || "".equals(userName)){
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
			   userName=userInfo.getUserName();
			}
		}
		LogBean lg = null;
		if(request !=null && logType!=null && logType.trim().length()>0 && funName!=null && funName.trim().length()>0){
			String id = new SummitTools().getKey();
			String callerIP = "";
			String sTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date());
			String insertLogSql = "";
			String dbName = "";
			if("1".equals(logType)){
				callerIP = getIPFromHttp(request);
				dbName = "WF_SYS_LOG";
			}else if("2".equals(logType)){
				// webservice 调用获取IP
				dbName = "WF_DATA_SERVICESLOG";
			}
			insertLogSql = "INSERT INTO "+dbName+"(id,userName,callerIP,funName,sTime) VALUES (?,?,?,?,?) ";
			int insertLogCount = jdbcTemplate.update(insertLogSql, id,userName,callerIP,funName,sTime);
			if(insertLogCount>0){
				logger.debug("插入日志记录成功！");
				lg = new LogBean();
				lg.setId(id);
				lg.setUserName(userName);
				lg.setsTime(sTime);
				lg.setCallerIP(callerIP);
				lg.setFunName(funName);
				// 默认成功，当失败时将该值修改为0
				lg.setActionFlag("1");
			}else{
				logger.debug("插入日志记录失败！");
			}
		}
		return lg;
	}
	
	/**
	 * 
	 * 修改日志方法
	 * @param logType:日志类型  1：系统日志  2：运行日志
	 * @return int :更新成功的数量
	 * 
	 */
	public int updateLog(LogBean logBean,String logType) {
		if(logBean!=null && logType!=null && logType.trim().length()>0){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
			String sTime = logBean.getsTime();
			String eTime = sf.format(new Date());
			String dbName = "";
			int actionTime=  0;
			if("1".equals(logType)){
				dbName = "WF_SYS_LOG";
			}else if("2".equals(logType)){
				dbName = "WF_DATA_SERVICESLOG";
			}
			try {
			    actionTime = (int) (sf.parse(eTime).getTime()-sf.parse(sTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("记录日志功能时间转换失败！");
			}
			logBean.seteTime(eTime);
			logBean.setActionTime(actionTime+"");
			String updateLogSql = "UPDATE "+dbName+" SET eTime=?,actionTime =?,actionFlag = ?,erroInfo = ? WHERE id = ? ";
			int upfateoLgCount = jdbcTemplate.update(
					updateLogSql,
					eTime,
					actionTime,
					logBean.getActionFlag(),
					logBean.getErroInfo(),
					logBean.getId()
			);
			if(upfateoLgCount>0){
				logger.debug("更新日志记录成功！");
			}else{
				logger.error("更新日志记录失败！");
			}
		}
		
		return 0;
	}
	
	
	/**
	 * 
	 * 获取IP(.do请求)
	 * 
	 */
	private String getIPFromHttp(HttpServletRequest request) {     
		 String ip = request.getHeader("x-forwarded-for");   
		  if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip))    {     
		    ip = request.getHeader("Proxy-Client-IP");  
		 }  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)   || "null".equalsIgnoreCase(ip)) {    
		  ip = request.getHeader("WL-Proxy-Client-IP");  
		 }  
		 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)    || "null".equalsIgnoreCase(ip)) {  
		  ip = request.getRemoteAddr();   
		 }  
		
		 if("0:0:0:0:0:0:0:1".equals(ip)){
			 try {
				 ip = InetAddress.getLocalHost().toString();
				 //DESKTOP-KH1EMEE/192.168.56.1
				 int computNameIndex = ip.indexOf("/");
				if(computNameIndex!=-1){
					ip = ip.substring(computNameIndex+1);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		 }
		 return ip;
		} 
	
	
	

}
