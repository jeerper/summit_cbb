package com.summit.service.log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.LogBean;
import com.summit.common.entity.QueryLogBean;
import com.summit.common.entity.UserInfo;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.repository.UserRepository;
import com.summit.util.SummitTools;

import net.sf.json.JSONObject;

@Component
@Service
public class LogUtilImpl  {
	private static final Logger logger = LoggerFactory.getLogger(LogUtilImpl.class);
	@Autowired
	public UserRepository ur;
	
	@Autowired
	private SummitTools st;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 新增日志方法
	 * @param logBean
	 * @return
	 */
	public  void insertLog(LogBean logBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String userName="";
		UserInfo userInfo=UserContextHolder.getUserInfo();
		if(userInfo!=null){
			   userName=userInfo.getUserName();
		}
		if(request !=null  && logBean.getFunName()!=null && logBean.getFunName().trim().length()>0){
			String id = SummitTools.getKey();
			String callerIP = "";
			if(SummitTools.stringIsNull(logBean.getCallerIP())){
				callerIP=getIPFromHttp(request);
			}else{
				callerIP=logBean.getCallerIP();
			}
			String insertLogSql = "INSERT INTO SYS_LOG(id,userName,callerIP,funName,stime,etime,actiontime,systemName,operInfo,actionFlag,operType,erroInfo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ";
			jdbcTemplate.update(insertLogSql,
					id,userName,callerIP,logBean.getFunName(),logBean.getStime(),
					logBean.getEtime(),logBean.getActiontime(),
					logBean.getSystemName(),logBean.getOperInfo(),
					logBean.getActionFlag(),logBean.getOperType(),logBean.getErroInfo());
				
		}
	}
	
	/**
	 * 
	 * 修改日志方法
	 * @param logType:日志类型  1：系统日志  2：运行日志
	 * @return int :更新成功的数量
	 * 
	 */
	public void updateLog(LogBean logBean) {
		if(logBean!=null && logBean.getId()!=null && logBean.getId().trim().length()>0){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
			
			String updateTime = sf.format(new Date());
			String updateLogSql = "UPDATE SYS_LOG SET updateTime=?,actionFlag = ?,erroInfo = ?,systemName=?,operInfo=? ,operType=? WHERE id = ? ";
			jdbcTemplate.update(
					updateLogSql,
					updateTime,
					logBean.getActionFlag(),
					logBean.getErroInfo(),
					logBean.getSystemName(),
					logBean.getOperInfo(),
					logBean.getOperType(),
					logBean.getId()
			);
		}
	}
	
	
	public void delLog(String startDate,String endDate) {
		if(startDate!=null &&  startDate.trim().length()>0 && endDate!=null &&  endDate.trim().length()>0){
			String delLogSql = "delete from SYS_LOG where  stime between ? and  ? ";
			jdbcTemplate.update(delLogSql,startDate,endDate
			);
		}
	}
	
	
	  public Page<QueryLogBean> queryByPage(int start, int limit, JSONObject paramJson) throws Exception {
			LinkedMap linkedMap=new LinkedMap();
			Integer index = 1;
			StringBuilder sb = new StringBuilder("select syslog.id,syslog.username,callerIP,funName,DATE_FORMAT(stime, '%Y-%m-%d %H:%i:%s')AS stime,erroInfo,");
			sb.append(" DATE_FORMAT(etime, '%Y-%m-%d %H:%i:%s')AS etime,actionFlag, user1.NAME,operType,operInfo,actiontime ");
			sb.append(" from sys_log syslog  inner join sys_user user1");
			sb.append(" on syslog.username=user1.username where 1=1 ");
            
			if (paramJson.containsKey("name")) {
				sb.append(" AND user1.NAME LIKE ? ");
				linkedMap.put(index,"%" + paramJson.get("name") + "%");
	    		index++;
			}
			if (paramJson.containsKey("systemName")) {
				sb.append(" AND systemName LIKE ? ");
				linkedMap.put(index,"%" + paramJson.get("systemName") + "%");
	    		index++;
			}
			if (paramJson.containsKey("funName")) {
				sb.append(" AND funName LIKE ? ");
				linkedMap.put(index, "%" + paramJson.get("funName")+ "%");
	    		index++;
			}
			if (paramJson.containsKey("startDate")) {
				sb.append(" AND stime >=  ? ");
				linkedMap.put(index, paramJson.get("startDate"));
	    		index++;
			}
			if (paramJson.containsKey("endDate")) {
				sb.append(" AND etime <=  ? ");
				linkedMap.put(index, paramJson.get("endDate"));
	    		index++;
			}
			if (paramJson.containsKey("operIp")) {
				sb.append(" AND callerIP like  ? ");
				linkedMap.put(index, "%" + paramJson.get("operIp")+ "%" );
	    		index++;
			}
			if (paramJson.containsKey("operType")) {
				sb.append(" AND operType =  ? ");
				linkedMap.put(index, paramJson.get("operType"));
	    		index++;
			}
			if (paramJson.containsKey("actionFlag")) {
				sb.append(" AND actionFlag =  ? ");
				linkedMap.put(index, paramJson.get("actionFlag"));
	    		index++;
			}
			sb.append(" order by stime desc");
			Page<Object> page= ur.queryByCustomPage(sb.toString(), start, limit,linkedMap);
			if(page!=null){
				List<QueryLogBean> queryLogBeanList=new ArrayList<QueryLogBean>();
				if(page.getContent()!=null && page.getContent().size()>0){
					  for(Object o:page.getContent()){
						 QueryLogBean queryLogBean=JSON.parseObject(o.toString(), new TypeReference<QueryLogBean>() {});
						 queryLogBeanList.add(queryLogBean);
					 }
				}
				return new Page<QueryLogBean>(queryLogBeanList,page.getPageable());
			}
			return null;
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
				logger.error("获取ip失败！"+e.getMessage());
				//e.printStackTrace();
			}
		 }
		 return ip;
		} 
	
	
	

}
