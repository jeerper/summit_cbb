package com.summit.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.domain.dept.DeptBean;
import com.summit.domain.log.LogBean;
import com.summit.service.adcd.ADCDService;
import com.summit.service.dept.DeptService;
import com.summit.service.log.ILogUtil;
import com.summit.util.Page;
import com.summit.util.SummitTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 部门信息管理
 * @author liuyh
 *
 */
@Api(description = "部门管理")
@Controller
@RequestMapping("dept")
public class DeptController {
	@Autowired
	private DeptService ds;
	@Autowired
	private SummitTools st;
	@Autowired
	ILogUtil logUtil;
	/**
	 * 查询部门树
	 * @return
	 */
	@ApiOperation(value = "查询部门树", notes = "用于application/json格式")
	@RequestMapping(value = "/queryTree",method = RequestMethod.POST)
	@ResponseBody
	public RestfulEntityBySummit<?> queryTree() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//UserContextHolder.getUserName();
		LogBean logBean = new LogBean();
		//Map<String, Object> list = null;
	     try {
	           logBean = logUtil.insertLog(request, "1", "查询部门树", "");
	           //list = ds.queryDeptTree();
	           return new RestfulEntityBySummit<>(ds.queryDeptTree());
	     } catch (Exception e) {
	            e.printStackTrace();
	            logBean.setActionFlag("0");
	            logBean.setErroInfo(e.toString());
	            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
	     }
	    // logUtil.updateLog(logBean, "1");
		//return list;
	}
	
	/**
	 * 
	 * 根据id查询（分页）
	 * 
	 */
	@ApiOperation(value = "根据id查询分页")
	@RequestMapping(value = "/queryByIdPage",method = RequestMethod.POST)
	@ResponseBody
	public RestfulEntityBySummit<?> queryById(String id) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据id查询分页","");
		Map<String, Object> list = null;
		try {
			return new RestfulEntityBySummit<>(ds.queryById(id));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	
	@ApiOperation(value = "根据pid查询分页")
	@RequestMapping(value = "/queryByPidPage",method = RequestMethod.POST)
	@ResponseBody
	public RestfulEntityBySummit<?> queryByPage(int start, int limit, String pid) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据pid查询分页","");
		//Page<JSONObject> list = null;
		try {
			///list = ds.queryByPage(start, limit, pid);
			return new RestfulEntityBySummit<>(ds.queryByPage(start, limit, pid));
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
	@ApiOperation(value = "部门新增")
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public RestfulEntityBySummit<?> add(DeptBean deptBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "部门新增","");
		//Map<String, Object> list = null;
		try {
			//list = ds.add(deptBean);
			return new RestfulEntityBySummit<>(ds.add(deptBean));
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
	@ApiOperation(value = "部门编辑")
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	@ResponseBody
	public RestfulEntityBySummit<?> edit(DeptBean deptBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "部门编辑","");
		Map<String, Object> list = null;
		try {
			//list = ds.edit(deptBean);
			return new RestfulEntityBySummit<>(ds.edit(deptBean));
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
	@ApiOperation(value = "部门删除")
	@RequestMapping(value = "/del",method = RequestMethod.GET)
	@ResponseBody
	public RestfulEntityBySummit<?> del(String ids) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "部门删除","");
		Map<String, Object> list = null;
		try {
			//list = ds.del(ids);
			String message=ds.del(ids);
			if(message!=null && message.length()>0){
				
				return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9994);
			}else{
				return new RestfulEntityBySummit<>(ds.del(ids));	
			}
			
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
