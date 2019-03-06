package com.summit.controller;

import com.alibaba.fastjson.JSONArray;
import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.domain.dictionary.DictionaryBean;
import com.summit.domain.log.LogBean;
import com.summit.service.dictionary.DictionaryService;
import com.summit.service.log.ILogUtil;
import com.summit.util.Page;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Api(description = "数据字典")
@Controller
@RequestMapping("dictionary")
public class DictionaryController {
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	ILogUtil logUtil;

	@ApiOperation(value = "新增数据字典", notes = "用于application/json格式")
    @PostMapping
	@ResponseBody
	public  RestfulEntityBySummit<?> add(DictionaryBean dictionaryBean, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		//LogBean logBean = new LogBean();
		try {
			//logBean = logUtil.insertLog(request,"1", "数据字典新增");
			//res = dictionaryService.add(dictionaryBean);
			return new RestfulEntityBySummit<>(dictionaryService.add(dictionaryBean));
		} catch (Exception e) {
			e.printStackTrace();
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
			//logBean.setActionFlag("0");
			//logBean.setErroInfo(e.toString());
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典删除")
	@DeleteMapping("del")
	@ResponseBody
	public RestfulEntityBySummit<?> del(String codes, HttpServletRequest request ) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典删除","");
			//res = dictionaryService.del(codes);
			return new RestfulEntityBySummit<>(dictionaryService.del(codes));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典修改")
	@PutMapping("edit")
	@ResponseBody
	public RestfulEntityBySummit<?> edit(DictionaryBean dictionaryBean, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典修改","");
			return new RestfulEntityBySummit<>(dictionaryService.edit(dictionaryBean));
			//res = dictionaryService.edit(dictionaryBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典按照编码查询")
	@GetMapping("queryByCode")
	@ResponseBody
	public RestfulEntityBySummit<?> queryByCode(String code, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典按照编码查询","");
			//res = dictionaryService.queryByCode(code);
			JSONArray jsonArray = new JSONArray();
	        jsonArray.add(dictionaryService.queryByCode(code));
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典查询所有数据")
	@GetMapping("queryTree")
	@ResponseBody
	public RestfulEntityBySummit<?> queryTree(HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典查询全部数据","");
			//res = dictionaryService.queryTree();
			JSONArray jsonArray = new JSONArray();
	        jsonArray.add(dictionaryService.queryTree());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典分页查询")
	@GetMapping("queryByPage")
	@ResponseBody
	public RestfulEntityBySummit<?> queryByPage(Integer start, Integer limit,
			String pId, HttpServletRequest request ) {
		//Page<DictionaryBean> res = new Page<DictionaryBean>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典分页查询", "");
			start = (start == null) ? 1 : start;
			limit = (limit == null) ? SysConstants.PAGE_SIZE : limit;
			//res = dictionaryService.queryByPage(start, limit, pId);
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,dictionaryService.queryByPage(start, limit, pId));
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}
	
	@ApiOperation(value = "数据字典按照父ID查询")
	@GetMapping("queryByPid")
	@ResponseBody
	public RestfulEntityBySummit<List <DictionaryBean>> queryByPid(String pId, HttpServletRequest request) {
		//List<DictionaryBean> res = new ArrayList<DictionaryBean>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典按照父ID查询", "");
			//res = dictionaryService.queryByPid(pId);
			List <DictionaryBean> list=dictionaryService.queryByPid(pId);
			//JSONArray jsonArray= JSONArray.parseArray(JSON.toJSONString(list));
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,list);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999,null);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}
	@ApiOperation(value = "初始化字典缓存加载")
	@PostMapping("initSysDic")
	@ResponseBody
	public RestfulEntityBySummit<?> initSysDic(HttpServletRequest request) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "初始化字典缓存加载", "");
			dictionaryService.initSysDic();
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
	}
	
}
