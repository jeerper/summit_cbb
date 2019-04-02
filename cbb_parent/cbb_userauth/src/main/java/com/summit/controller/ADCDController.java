package com.summit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.domain.adcd.ADCDBean;
import com.summit.domain.log.LogBean;
import com.summit.service.adcd.ADCDService;
import com.summit.service.log.ILogUtil;
import com.summit.util.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 
 * 行政区划信息管理
 * @author whh
 *
 */
@Api(description = "行政区划管理")
@RestController
@RequestMapping("adcd")
public class ADCDController {
	private static final Logger logger = LoggerFactory.getLogger(ADCDController.class);
	@Autowired
	private ADCDService ds;
	
	@Autowired
	ILogUtil logUtil;
	/**
	 * 查询adcd树
	 * @return
	 */
	@ApiOperation(value = "查询行政区划树", notes = "用于application/json格式")
	@GetMapping(value = "/queryAdTree")
	public RestfulEntityBySummit<ADCDBean> queryTree(@RequestParam(value = "pid",required = false)  String pid) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//UserContextHolder.getUserName();
		LogBean logBean = new LogBean();
		//Map<String, Object> list = null;
	     try {
	           logBean = logUtil.insertLog(request, "1", "查询adcd树", "");
	           //list = st.success("", ds.queryAdcdTree(pid));
	           ADCDBean ADCDBean=ds.queryAdcdTree(pid);
	           return ResultBuilder.buildSuccess(ADCDBean);
	           //logger.debug("数据查询成功！"+info.getCode()+"==="+info.getData()); 
	          
	     } catch (Exception e) {
	            //e.printStackTrace();
	            logBean.setActionFlag("0");
	            logBean.setErroInfo(e.toString());
	            logger.error("数据查询失败！", e);
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
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
	public RestfulEntityBySummit<List<ADCDBean>> queryByPadcd(
			@RequestParam(value = "padcd",required = false) String padcd,
			@RequestParam(value = "level",required = false) String level) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据特定条件查询","");
		//Map<String, Object> list = null;
		try {
			JSONObject json =new JSONObject();
			json.put("padcd", padcd);
			json.put("level", level);
			return ResultBuilder.buildSuccess(ds.queryByPId(json));
		} catch (Exception e) {
			//e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			 logger.error("数据查询失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return list;
	}
	
	@ApiOperation(value = "根据编码查询不分页")
	@RequestMapping(value = "/queryByAdcds",method = RequestMethod.GET)
	public RestfulEntityBySummit<List<ADCDBean>> queryByAdcds(
            @RequestParam(value = "adcds") String adcds) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据adcds查询","");
		try {
			List<ADCDBean> list=ds.queryByAdcds(adcds);
			return ResultBuilder.buildSuccess(list);
		} catch (Exception e) {
			//e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			 logger.error("数据查询失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	@ApiOperation(value = "根据父节点查询分页")
	@RequestMapping(value = "/queryByPadcdPage",method = RequestMethod.GET)
	public RestfulEntityBySummit<Page<ADCDBean>> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "padcd",required = false) String padcd) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据PADCD查询分页","");
		Page<ADCDBean> list = null;
		try {
			list = ds.queryByPage(page, pageSize, padcd);
			return ResultBuilder.buildSuccess(list);
		} catch (Exception e) {
			//e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			logger.error("数据查询失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	/**
	 * 新增
	 */
	@ApiOperation(value = "行政区划新增",notes="编码(ADCD),行政区划名称(ADNM),padcd(父节点)都是必输项")
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	public RestfulEntityBySummit<String> add(@RequestBody ADCDBean adcdBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "行政区划新增","");
		try {
			//list = ds.add(adcdBean);
			ResponseCodeEnum responseCodeEnum=ds.add(adcdBean);
			if(responseCodeEnum!=null){
				return ResultBuilder.buildError(responseCodeEnum);
			}else{
			   return ResultBuilder.buildSuccess();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("行政区划新增失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	/**
	 * 编辑保存
	 */
	@ApiOperation(value = "行政区划编辑",notes="编码(ADCD),行政区划名称(ADNM),padcd(父节点)都是必输项")
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	public RestfulEntityBySummit<?> edit(@RequestBody ADCDBean adcdBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "行政区划编辑","");
		//Map<String, Object> list = null;
		try {
			ds.edit(adcdBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("行政区划编辑失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	/**
	 * 删除
	 */
	@ApiOperation(value = "行政区划删除")
	@RequestMapping(value = "/del",method = RequestMethod.DELETE)
	public RestfulEntityBySummit<?> del(@RequestParam(value = "ids") String ids) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "行政区划删除","");
		try {
			 ds.del(ids);
			return ResultBuilder.buildSuccess();
			//return new RestfulEntityBySummit<String>(ds.del(ids),null);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("行政区划删除失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	
}
