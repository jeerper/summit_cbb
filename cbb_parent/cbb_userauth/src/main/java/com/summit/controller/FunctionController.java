package com.summit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.domain.function.FunctionBean;
import com.summit.domain.log.LogBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "功能管理")
@Controller
@RequestMapping("function")
public class FunctionController {
	private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);
	@Autowired
	private FunctionService fs;
	
	@Autowired
	ILogUtil logUtil;

	@ApiOperation(value = "新增功能", notes = "用于application/json格式")
	@PostMapping("/add")
	@ResponseBody
	public RestfulEntityBySummit<?> add(FunctionBean functionBean, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理新增","");
			return new RestfulEntityBySummit<>(fs.add(functionBean));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理删除")
	@DeleteMapping("del")
	@ResponseBody
	public RestfulEntityBySummit<?> del(
			@RequestParam(value = "ids") String ids, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理删除","");
			return new RestfulEntityBySummit<>(fs.del(ids));
			//res = fs.del(ids);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理修改")
	@PutMapping("edit")
	@ResponseBody
	public RestfulEntityBySummit<?> edit(FunctionBean functionBean, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理修改","");
			//res = fs.edit(functionBean);
			return new RestfulEntityBySummit<>(fs.edit(functionBean));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理根据ID查询")
	@GetMapping("queryById")
	@ResponseBody
	public RestfulEntityBySummit<?> queryById(
			@RequestParam(value = "id")  String id,  HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理根据ID查询","");
			//res = fs.queryById(id,"");
			String userName="";
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
				userName=userInfo.getUserName();
			}
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,fs.queryById(id,userName));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理查询树形图")
	@GetMapping("queryTree")
	@ResponseBody
	public RestfulEntityBySummit<?> queryTree(HttpServletRequest request) {
		LogBean logBean = new LogBean();
		//Map<String, Object> res = new HashMap<String, Object>();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理查询树形图","");
			//res = st.success("", fs.queryAll(userName));
			String userName="";
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
				userName=userInfo.getUserName();
			}
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,fs.queryAll(userName));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理分页查询")
	@GetMapping("queryByPage")
	@ResponseBody
	public RestfulEntityBySummit<?> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "pId",required = false) String pId,HttpServletRequest request) {
		//Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理分页查询","");
			//res = fs.queryByPage(start, limit, pId,"");
			String userName="";
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
				userName=userInfo.getUserName();
			}
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,fs.queryByPage(page, pageSize, pId,userName));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}
}
