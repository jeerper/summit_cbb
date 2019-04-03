package com.summit.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.summit.common.entity.DictionaryBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.domain.log.LogBean;
import com.summit.service.dictionary.DictionaryService;
import com.summit.service.log.ILogUtil;
import com.summit.util.Page;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Api(description = "数据字典")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("dictionary")
@Slf4j
public class DictionaryController {
	private static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	ILogUtil logUtil;

	@ApiOperation(value = "新增数据字典", notes = "编码(code),名称(name)都是必输项")
    @PostMapping(value = "/add")
	public  RestfulEntityBySummit<String> add(@RequestBody DictionaryBean dictionaryBean, HttpServletRequest request) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典新增","");
			ResponseCodeEnum responseCodeEnum=dictionaryService.add(dictionaryBean);
			if(responseCodeEnum!=null){
				return ResultBuilder.buildError(responseCodeEnum);
			}
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
			
		}
	}

	@ApiOperation(value = "数据字典删除")
	@DeleteMapping("del")
	public RestfulEntityBySummit<String> del(
			@RequestParam(value = "codes") String codes, HttpServletRequest request ) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典删除","");
			dictionaryService.del(codes);
			return ResultBuilder.buildSuccess();
			//return new RestfulEntityBySummit<String>(,null);
		} catch (Exception e) {
			logger.error("操作失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "数据字典修改", notes = "编码(code),名称(name)都是必输项")
	@PutMapping("edit")
	public RestfulEntityBySummit<String> edit(@RequestBody DictionaryBean dictionaryBean, HttpServletRequest request) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典修改","");
			dictionaryService.edit(dictionaryBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "查询数据字典树")
	@RequestMapping(value = "/queryTree",method = RequestMethod.GET)
	public RestfulEntityBySummit<DictionaryBean> queryTree(@RequestParam(value = "pid",required = false) String pid) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		LogBean logBean = new LogBean();
	     try {
	           logBean = logUtil.insertLog(request, "1", "数据字典树", "");
	           return ResultBuilder.buildSuccess(dictionaryService.queryTree(pid));
	          // return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,dictionaryService.queryTree(pid));
	     } catch (Exception e) {
	    	    logger.error("查询数据字典树失败：", e);
	    	    logUtil.updateLog(logBean, "1");
	            logBean.setActionFlag("0");
	            logBean.setErroInfo(e.toString());
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
	     }
	}

	@ApiOperation(value = "数据字典按照编码查询")
	@GetMapping("queryByCode")
	public RestfulEntityBySummit<DictionaryBean> queryByCode(@RequestParam(value = "code")  String code) {
		//Map<String, Object> res = new HashMap<String, Object>();
		//LogBean logBean = new LogBean();
		try {
			//logBean = logUtil.insertLog(request,"1", "数据字典按照编码查询","");
			//res = dictionaryService.queryByCode(code);
			return ResultBuilder.buildSuccess(dictionaryService.queryByCode(code));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			//logBean.setActionFlag("0");
			//logBean.setErroInfo(e.toString());
			//logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典查询所有数据")
	@GetMapping("queryAll")
	public RestfulEntityBySummit<List<DictionaryBean>> queryAll(HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典查询全部数据","");
			return ResultBuilder.buildSuccess(dictionaryService.queryAll());
			//return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,dictionaryService.queryAll());
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "数据字典分页查询")
	@GetMapping("queryByPage")
	public RestfulEntityBySummit<Page<DictionaryBean>> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "pId",required = false) String pId, HttpServletRequest request ) {
		//Page<DictionaryBean> res = new Page<DictionaryBean>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典分页查询", "");
			page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
			//return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,dictionaryService.queryByPage(page, pageSize, pId));
			return ResultBuilder.buildSuccess(dictionaryService.queryByPage(page, pageSize, pId));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "数据字典按照父ID查询")
	@GetMapping("queryByPid")
	public RestfulEntityBySummit<List <DictionaryBean>> queryByPid(
			@RequestParam(value = "pId") String pId, HttpServletRequest request) {
		//List<DictionaryBean> res = new ArrayList<DictionaryBean>();
		LogBean logBean = new LogBean();
		//MsgBean mb = new MsgBean();
		try {
			logBean = logUtil.insertLog(request,"1", "数据字典按照父ID查询", "");
			//res = dictionaryService.queryByPid(pId);
			List <DictionaryBean> list=dictionaryService.queryByPid(pId);
			//JSONArray jsonArray= JSONArray.parseArray(JSON.toJSONString(list));
			logger.info("查询成功");
			return ResultBuilder.buildSuccess(list);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	@ApiOperation(value = "初始化字典缓存加载")
	@GetMapping("initSysDic")
	@PostConstruct
	public RestfulEntityBySummit<?> initSysDic() {
		//LogBean logBean = new LogBean();
		try {
			//logBean = logUtil.insertLog(request,"1", "初始化字典缓存加载", "");
			dictionaryService.initSysDic();
			logger.info("初始化字典缓存加载");
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("查询失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
}
