package com.summit.controller;

import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.ADCDBean;
import com.summit.common.entity.ADCDTreeBean;
import com.summit.common.entity.LogBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.service.adcd.ADCDService;
import com.summit.service.log.LogUtilImpl;

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
	private ADCDService adcdService;
	
	@Autowired
	LogUtilImpl logUtil;
	/**
	 * 查询adcd树
	 * @return
	 */
	@ApiOperation(value = "查询行政区划树", notes = "用于application/json格式")
	@GetMapping(value = "/queryAdTree")
	public RestfulEntityBySummit<ADCDBean> queryTree(@RequestParam(value = "pid",required = false)  String pid,
			@RequestParam(value = "isQueryAll",required = false,defaultValue="true")  boolean isQueryAll) {
	        try {
	           ADCDBean ADCDBean=adcdService.queryAdcdTree(pid,isQueryAll);
	           return ResultBuilder.buildSuccess(ADCDBean);
	          
	        } catch (Exception e) {
	            logger.error("数据查询失败！", e);
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
	        }
	}
	
	
	@ApiOperation(value = "查询行政区划树--JSON 数据直接生成树结构", notes = "用于application/json格式")
	@GetMapping(value = "/queryAdcdJsonTree")
	public RestfulEntityBySummit<ADCDTreeBean> queryJsonTree(@RequestParam(value = "pid",required = false)  String pid,
			@RequestParam(value = "isQueryAll",required = false,defaultValue="true")  boolean isQueryAll) {
		// HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	     try {
	           ADCDTreeBean adcdBean=adcdService.queryJsonAdcdTree(pid,isQueryAll);
	           return ResultBuilder.buildSuccess(adcdBean);
	     } catch (Exception e) {
	            logger.error("数据查询失败！", e);
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
	     }
	}
	
	@ApiOperation(value = "查询有多个行政区划树--JSON 数据直接生成树结构", notes = "用于application/json格式")
	@GetMapping(value = "/queryJsonTreeList")
	public RestfulEntityBySummit<List<ADCDTreeBean>> queryJsonTreeList(@RequestParam(value = "pid",required = false)  String pid,
			@RequestParam(value = "isQueryAll",required = false,defaultValue="true")  boolean isQueryAll) {
		// HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	     try {
	           List<ADCDTreeBean> adcdBean=adcdService.queryJsonAdcdTreeList(pid,isQueryAll);
	           return ResultBuilder.buildSuccess(adcdBean);
	     } catch (Exception e) {
	            logger.error("数据查询失败！", e);
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
	     }
	}
	
	@ApiOperation(value = "根据padcd查询下面所有的子节点")
	@RequestMapping(value = "/queryAllAdcdByPadcd",method = RequestMethod.GET)
	public RestfulEntityBySummit<List<String>> queryAllAdcdByPadcd(
			@RequestParam(value = "padcd",required = true) String padcd) {
		// HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		try {
			return ResultBuilder.buildSuccess(adcdService.getAdcdBean(padcd));
		} catch (Exception e) {
			 logger.error("数据查询失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	
	/**
	 * 
	 * 根据特定条件查询
	 * 
	 */
	@ApiOperation(value = "根据特定条件查询[padcd,level]")
	@RequestMapping(value = "/queryAdcd",method = RequestMethod.GET)
	public RestfulEntityBySummit<List<ADCDBean>> queryByPadcd(
			@RequestParam(value = "padcd",required = false) String padcd,
			@RequestParam(value = "level",required = false) String level) {
		try {
			JSONObject json =new JSONObject();
			json.put("padcd", padcd);
			json.put("level", level);
			return ResultBuilder.buildSuccess(adcdService.queryByPId(json));
		} catch (Exception e) {
			 logger.error("数据查询失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "根据编码查询不分页")
	@RequestMapping(value = "/queryByAdcds",method = RequestMethod.GET)
	public RestfulEntityBySummit<List<ADCDBean>> queryByAdcds(
            @RequestParam(value = "adcds") String adcds) {
		
		try {
			List<ADCDBean> list=adcdService.queryByAdcds(adcds);
			return ResultBuilder.buildSuccess(list);
		} catch (Exception e) {
			 logger.error("数据查询失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "根据编码查询不分页返回Map")
	@RequestMapping(value = "/queryAdcdMap",method = RequestMethod.GET)
	public RestfulEntityBySummit<Map<String,ADCDBean>> queryAdcdMap(
			@RequestParam(value = "adcds" ,required = false) String adcds,
            @RequestParam(value = "padcd" ,required = false) String padcd) {
		try {
			Map<String,ADCDBean> adcdsMap=adcdService.queryAdcdMap(adcds,padcd);
			return ResultBuilder.buildSuccess(adcdsMap);
		} catch (Exception e) {
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
		Page<ADCDBean> list = null;
		try {
			 list = adcdService.queryByPage(page, pageSize, padcd);
			return ResultBuilder.buildSuccess(list);
		} catch (Exception e) {
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
		try {
			//list = adcdService.add(adcdBean);
			ResponseCodeEnum responseCodeEnum=adcdService.add(adcdBean);
			//LogBean logBean = new LogBean("行政区划管理","共享用户组件","新增行政区划信息："+adcdBean.toString(),"1");
	        //logUtil.insertLog(logBean);
			if(responseCodeEnum!=null){
				return ResultBuilder.buildError(responseCodeEnum);
			}else{
			   return ResultBuilder.buildSuccess();
			}
		} catch (Exception e) {
			logger.error("行政区划新增失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	/**
	 * 编辑保存
	 */
	@ApiOperation(value = "行政区划编辑",notes="编码(ADCD),行政区划名称(ADNM),padcd(父节点)都是必输项")
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	public RestfulEntityBySummit<?> edit(@RequestBody ADCDBean adcdBean) {
		try {
			adcdService.edit(adcdBean);
			//LogBean logBean = new LogBean("行政区划管理","共享用户组件","修改行政区划信息："+adcdBean.toString(),"2");
	        //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("行政区划编辑失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	/**
	 * 删除
	 */
	@ApiOperation(value = "行政区划删除")
	@RequestMapping(value = "/del",method = RequestMethod.DELETE)
	public RestfulEntityBySummit<?> del(@RequestParam(value = "adcds") String adcds) {
		try {
			 adcdService.del(adcds);
			 //LogBean logBean = new LogBean("行政区划管理","共享用户组件","删除行政区划信息："+ids,"3");
		     //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("行政区划删除失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
}
