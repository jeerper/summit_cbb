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
import com.summit.common.entity.LogBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.service.dictionary.DictionaryService;
import com.summit.service.log.LogUtilImpl;

import org.springframework.data.domain.Page;
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
	LogUtilImpl logUtil;

	@ApiOperation(value = "新增数据字典", notes = "编码(code),名称(name)都是必输项")
    @PostMapping(value = "/add")
	public  RestfulEntityBySummit<String> add(@RequestBody DictionaryBean dictionaryBean) {
		try {
			//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			
			ResponseCodeEnum responseCodeEnum=dictionaryService.add(dictionaryBean);
			if(responseCodeEnum!=null){
				return ResultBuilder.buildError(responseCodeEnum);
			}
			//LogBean logBean = new LogBean("数据字典管理","共享用户组件","新增数据字典："+dictionaryBean,"1");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
			
		}
	}

	@ApiOperation(value = "数据字典删除")
	@DeleteMapping("del")
	public RestfulEntityBySummit<String> del(
			@RequestParam(value = "codes") String codes ) {
		try {
			dictionaryService.del(codes);
			//LogBean logBean = new LogBean("数据字典管理","共享用户组件","删除数据字典："+codes,"3");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "数据字典修改", notes = "编码(code),名称(name)都是必输项")
	@PutMapping("edit")
	public RestfulEntityBySummit<String> edit(@RequestBody DictionaryBean dictionaryBean) {
		try {
			dictionaryService.edit(dictionaryBean);
			//LogBean logBean = new LogBean("数据字典管理","共享用户组件","修改数据字典信息："+dictionaryBean,"2");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "查询数据字典树")
	@RequestMapping(value = "/queryTree",method = RequestMethod.GET)
	public RestfulEntityBySummit<DictionaryBean> queryTree(@RequestParam(value = "pid",required = false) String pid) {
		 try {
	           return ResultBuilder.buildSuccess(dictionaryService.queryTree(pid));
	     } catch (Exception e) {
	    	    logger.error("查询数据字典树失败：", e);
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
	     }
	}

	@ApiOperation(value = "数据字典按照编码查询")
	@GetMapping("queryByCode")
	public RestfulEntityBySummit<DictionaryBean> queryByCode(@RequestParam(value = "code")  String code) {
		try {
			return ResultBuilder.buildSuccess(dictionaryService.queryByCode(code));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "数据字典查询所有数据")
	@GetMapping("queryAll")
	public RestfulEntityBySummit<List<DictionaryBean>> queryAll() {
		try {
			return ResultBuilder.buildSuccess(dictionaryService.queryAll());
		} catch (Exception e) {
			logger.error("查询失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "数据字典分页查询")
	@GetMapping("queryByPage")
	public RestfulEntityBySummit<Page<DictionaryBean>> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "pId",required = false) String pId ) {
		try {
			page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
			return ResultBuilder.buildSuccess(dictionaryService.queryByPage(page, pageSize, pId));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "数据字典按照父ID查询")
	@GetMapping("queryByPid")
	public RestfulEntityBySummit<List <DictionaryBean>> queryByPid(
			@RequestParam(value = "pId") String pId) {
		try {
			List <DictionaryBean> list=dictionaryService.queryByPid(pId);
			logger.info("查询成功");
			return ResultBuilder.buildSuccess(list);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "根据多个pcode查询子集数据,pcode以,分割")
	@GetMapping("queryDictionaryByPcodes")
	public RestfulEntityBySummit<List <DictionaryBean>> queryDictionaryByPcodes(
			@RequestParam(value = "pcodes") String pcodes) {
		try {
			List <DictionaryBean> list=dictionaryService.queryDictionaryByPcodes(pcodes);
			return ResultBuilder.buildSuccess(list);
		} catch (Exception e) {
			logger.error("查询失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	
	@ApiOperation(value = "初始化字典缓存加载")
	@GetMapping("initSysDic")
	@PostConstruct
	public RestfulEntityBySummit<?> initSysDic() {
		try {
			dictionaryService.initSysDic();
			logger.info("初始化字典缓存加载");
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("查询失败", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
}
