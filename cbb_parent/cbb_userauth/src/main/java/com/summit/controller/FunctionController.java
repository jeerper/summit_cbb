package com.summit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.domain.function.FunctionBean;
import com.summit.domain.log.LogBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "功能管理")
@Controller
@RequestMapping("function")
public class FunctionController {
	@Autowired
	private FunctionService fs;
	@Autowired
	private SummitTools st;
	@Autowired
	ILogUtil logUtil;

	@ApiOperation(value = "新增功能", notes = "用于application/json格式")
	@PostMapping("/add")
	@ResponseBody
	public RestfulEntityBySummit<?> add(FunctionBean functionBean, HttpServletRequest request, String userName) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理新增",userName);
			return new RestfulEntityBySummit<>(fs.add(functionBean));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理删除")
	@DeleteMapping("del")
	@ResponseBody
	public RestfulEntityBySummit<?> del(String ids, HttpServletRequest request,String userName) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理删除",userName);
			return new RestfulEntityBySummit<>(fs.del(ids));
			//res = fs.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理修改")
	@PutMapping("edit")
	@ResponseBody
	public RestfulEntityBySummit<?> edit(FunctionBean functionBean, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理修改",userName);
			//res = fs.edit(functionBean);
			return new RestfulEntityBySummit<>(fs.edit(functionBean));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理根据ID查询")
	@GetMapping("queryById")
	@ResponseBody
	public RestfulEntityBySummit<?> queryById(String id, String userName, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理根据ID查询",userName);
			//res = fs.queryById(id,userName);
			return new RestfulEntityBySummit<>(fs.queryById(id,userName));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理查询树形图")
	@GetMapping("queryTree")
	@ResponseBody
	public RestfulEntityBySummit<?> queryTree(String userName,HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理查询树形图",userName);
			res = st.success("", fs.queryAll(userName));
			return new RestfulEntityBySummit<>(fs.queryAll(userName));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "功能管理分页查询")
	@GetMapping("queryByPage")
	@ResponseBody
	public RestfulEntityBySummit<?> queryByPage(Integer start, Integer limit, String pId, String userName, HttpServletRequest request) {
		//Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理分页查询",userName);
			start = (start == null) ? 1 : start;
			limit = (limit == null) ? SysConstants.PAGE_SIZE : limit;
			//res = fs.queryByPage(start, limit, pId,userName);
			return new RestfulEntityBySummit<>(fs.queryByPage(start, limit, pId,userName));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}
}
