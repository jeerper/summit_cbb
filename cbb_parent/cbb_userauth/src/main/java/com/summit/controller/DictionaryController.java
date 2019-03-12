package com.summit.controller;

import java.util.List;

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

import com.alibaba.fastjson.JSONArray;
import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.domain.dictionary.DictionaryBean;
import com.summit.domain.log.LogBean;
import com.summit.service.dictionary.DictionaryService;
import com.summit.service.log.ILogUtil;
import com.summit.util.MsgBean;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(description = "数据字典")
@Controller
@RequestMapping("dictionary")
public class DictionaryController {
	private static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	ILogUtil logUtil;

	@ApiOperation(value = "新增数据字典", notes = "编码(code),名称(name)都是必输项")
    @PostMapping
	@ResponseBody
	public  RestfulEntityBySummit<?> add(DictionaryBean dictionaryBean, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典新增","");
			//res = dictionaryService.add(dictionaryBean);
			return new RestfulEntityBySummit<>(dictionaryService.add(dictionaryBean));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
			
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典删除")
	@DeleteMapping("del")
	@ResponseBody
	public RestfulEntityBySummit<?> del(
			@RequestParam(value = "codes") String codes, HttpServletRequest request ) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典删除","");
			//res = dictionaryService.del(codes);
			return new RestfulEntityBySummit<>(dictionaryService.del(codes));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典修改", notes = "编码(code),名称(name)都是必输项")
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
			//e.printStackTrace();
			logger.error("操作失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典按照编码查询")
	@GetMapping("queryByCode")
	@ResponseBody
	public RestfulEntityBySummit<?> queryByCode(@RequestParam(value = "code")  String code, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典按照编码查询","");
			//res = dictionaryService.queryByCode(code);
			JSONArray jsonArray = new JSONArray();
	        jsonArray.add(dictionaryService.queryByCode(code));
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,jsonArray);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
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
			//e.printStackTrace();
			logger.error("查询失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典分页查询")
	@GetMapping("queryByPage")
	@ResponseBody
	public RestfulEntityBySummit<?> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "pId",required = false) String pId, HttpServletRequest request ) {
		//Page<DictionaryBean> res = new Page<DictionaryBean>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典分页查询", "");
			page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
			//res = dictionaryService.queryByPage(start, limit, pId);
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,dictionaryService.queryByPage(page, pageSize, pId));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
	}
	
	@ApiOperation(value = "数据字典按照父ID查询")
	@GetMapping("queryByPid")
	@ResponseBody
	public MsgBean queryByPid(
			@RequestParam(value = "pId") String pId, HttpServletRequest request) {
		//List<DictionaryBean> res = new ArrayList<DictionaryBean>();
		LogBean logBean = new LogBean();
		MsgBean mb = new MsgBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典按照父ID查询", "");
			//res = dictionaryService.queryByPid(pId);
			List <DictionaryBean> list=dictionaryService.queryByPid(pId);
			//JSONArray jsonArray= JSONArray.parseArray(JSON.toJSONString(list));
			//return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,list);
			if(null != list){
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(list);
                mb.setCode("CODE_0000");
                mb.setMessage("数据查询成功!");
                mb.setData(jsonArray);
            }
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			//return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999,null);
		}
		return mb;
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
			//e.printStackTrace();
			logger.error("查询失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
	}
	
}
