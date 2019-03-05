package com.summit.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.domain.adcd.ADCDBean;
import com.summit.domain.log.LogBean;
import com.summit.service.adcd.ADCDService;
import com.summit.service.log.ILogUtil;
import com.summit.util.Page;
import com.summit.util.SummitTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 行政区划信息管理
 * @author whh
 *
 */
@Api(description = "行政区划管理")
@Controller
@RequestMapping("adcd")
public class ADCDController {
	private static final Logger logger = LoggerFactory.getLogger(ADCDController.class);
	@Autowired
	private ADCDService ds;
	@Autowired
	private SummitTools st;
	@Autowired
	ILogUtil logUtil;
	/**
	 * 查询adcd树
	 * @return
	 */
	@ApiOperation(value = "查询行政区划树", notes = "用于application/json格式")
	@RequestMapping(value = "/queryAdTree",method = RequestMethod.POST)
	@ResponseBody
	public RestfulEntityBySummit<?> queryTree(String pid) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//UserContextHolder.getUserName();
		LogBean logBean = new LogBean();
		//Map<String, Object> list = null;
	     try {
	           logBean = logUtil.insertLog(request, "1", "查询adcd树", "");
	           //list = st.success("", ds.queryAdcdTree(pid));
	           RestfulEntityBySummit<?> info=new RestfulEntityBySummit<>(ds.queryAdcdTree(pid));
	           logger.debug("数据查询成功！"+info.getCode()+"==="+info.getData()); 
	           return info;
	     } catch (Exception e) {
	            e.printStackTrace();
	            logBean.setActionFlag("0");
	            logBean.setErroInfo(e.toString());
	            logger.debug("数据查询失败！"+e.toString()); 
	            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
	     }
	    // logUtil.updateLog(logBean, "1");
		
		//return list;
	}
	
	/**
	 * 
	 * 根据特定条件查询
	 * 
	 */
	@ApiOperation(value = "根据特定条件查询[padcd,level]")
	@RequestMapping(value = "/queryByPadcd",method = RequestMethod.GET)
	@ResponseBody
	public RestfulEntityBySummit<?> queryByPadcd(String padcd,String level) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据特定条件查询","");
		//Map<String, Object> list = null;
		try {
			JSONObject json =new JSONObject();
			json.put("padcd", padcd);
			json.put("level", level);
			//list = ds.queryByPId(json);
			return new RestfulEntityBySummit<>(ds.queryByPId(json));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return list;
	}
	
	@ApiOperation(value = "根据编码查询不分页")
	@RequestMapping(value = "/queryByAdcds",method = RequestMethod.GET)
	@ResponseBody
	public RestfulEntityBySummit<?> queryByAdcds(String adcds) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据adcds查询","");
		try {
			return new RestfulEntityBySummit<>(ds.queryByAdcds(adcds));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return list;
	}
	@ApiOperation(value = "根据父节点查询分页")
	@RequestMapping(value = "/queryByPadcdPage",method = RequestMethod.GET)
	@ResponseBody
	public RestfulEntityBySummit<?> queryByPage(int start, int limit, String padcd) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据PADCD查询分页","");
		Page<JSONObject> list = null;
		try {
			//list = ds.queryByPage(start, limit, padcd);
			return new RestfulEntityBySummit<>(ds.queryByPage(start, limit, padcd));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	/**
	 * 新增
	 */
	@ApiOperation(value = "行政区划新增")
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public RestfulEntityBySummit<?> add(ADCDBean adcdBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "行政区划新增","");
		Map<String, Object> list = null;
		try {
			//list = ds.add(adcdBean);
			return new RestfulEntityBySummit<>(ds.add(adcdBean));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	
	/**
	 * 编辑保存
	 */
	@ApiOperation(value = "行政区划编辑")
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@ResponseBody
	public RestfulEntityBySummit<?> edit(ADCDBean adcdBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "行政区划编辑","");
		//Map<String, Object> list = null;
		try {
			//list = ds.edit(adcdBean);
			return new RestfulEntityBySummit<>(ds.edit(adcdBean));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	/**
	 * 删除
	 */
	@ApiOperation(value = "行政区划删除")
	@RequestMapping(value = "/del",method = RequestMethod.DELETE)
	@ResponseBody
	public RestfulEntityBySummit<?> del(String ids) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "行政区划删除","");
		Map<String, Object> list = null;
		try {
			//list = ds.del(ids);
			return new RestfulEntityBySummit<>(ds.del(ids));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	
}
