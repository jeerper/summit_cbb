package com.summit.controller;

import com.summit.domain.function.FunctionBean;
import com.summit.domain.log.LogBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("function")
public class FunctionController {
	@Autowired
	private FunctionService fs;
	@Autowired
	private SummitTools st;
	@Autowired
	ILogUtil logUtil;

	@RequestMapping("add")
	@ResponseBody
	public Map<String, Object> add(FunctionBean functionBean, HttpServletRequest request, String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理新增",userName);
			res = fs.add(functionBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("del")
	@ResponseBody
	public Map<String, Object> del(String ids, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理删除",userName);
			res = fs.del(ids);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("edit")
	@ResponseBody
	public Map<String, Object> edit(FunctionBean functionBean, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理修改",userName);
			res = fs.edit(functionBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("queryById")
	@ResponseBody
	public Map<String, Object> queryById(String id, String userName, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理根据ID查询",userName);
			res = fs.queryById(id,userName);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("queryTree")
	@ResponseBody
	public Map<String, Object> queryTree(String userName,HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理查询树形图",userName);
			res = st.success("", fs.queryAll(userName));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("queryByPage")
	@ResponseBody
	public Page<JSONObject> queryByPage(Integer start, Integer limit, String pId, String userName, HttpServletRequest request) {
		Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "功能管理分页查询",userName);
			start = (start == null) ? 1 : start;
			limit = (limit == null) ? SysConstants.PAGE_SIZE : limit;
			res = fs.queryByPage(start, limit, pId,userName);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}
}
