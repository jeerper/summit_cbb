package com.summit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.summit.domain.dictionary.DictionaryBean;
import com.summit.domain.log.LogBean;
import com.summit.service.dictionary.DictionaryService;
import com.summit.service.log.ILogUtil;
import com.summit.util.Page;
import com.summit.util.SysConstants;


@Controller
@RequestMapping("dictionary")
public class DictionaryController {
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	ILogUtil logUtil;

	@RequestMapping("add")
	@ResponseBody
	public Map<String, Object> add(DictionaryBean dictionaryBean, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典新增");
			res = dictionaryService.add(dictionaryBean);
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
	public Map<String, Object> del(String codes, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典删除");
			res = dictionaryService.del(codes);
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
	public Map<String, Object> edit(DictionaryBean dictionaryBean, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典修改");
			res = dictionaryService.edit(dictionaryBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("queryByCode")
	@ResponseBody
	public Map<String, Object> queryByCode(String code, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典按照编码查询");
			res = dictionaryService.queryByCode(code);
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
	public Map<String, Object> queryTree(HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典查询树形结构");
			res = dictionaryService.queryTree();
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
	public Page<DictionaryBean> queryByPage(Integer start, Integer limit,
			String pId, HttpServletRequest request) {
		Page<DictionaryBean> res = new Page<DictionaryBean>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典分页查询");
			start = (start == null) ? 1 : start;
			limit = (limit == null) ? SysConstants.PAGE_SIZE : limit;
			res = dictionaryService.queryByPage(start, limit, pId);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}
	
	@RequestMapping("queryByPid")
	@ResponseBody
	public List<DictionaryBean> queryByPid(String pId, HttpServletRequest request) {
		List<DictionaryBean> res = new ArrayList<DictionaryBean>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典按照父ID查询");
			res = dictionaryService.queryByPid(pId);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}
	
}
