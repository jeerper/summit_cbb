package com.summit.controller;

import java.util.List;

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
import com.summit.common.entity.DeptBean;
import com.summit.common.entity.DeptTreeBean;
import com.summit.common.entity.LogBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.service.dept.DeptService;
import com.summit.service.log.LogUtilImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 
 * 部门信息管理
 * @author liuyh
 *
 */
@Api(description = "部门管理")
@RestController  /* @Controller + @ResponseBody*/
@RequestMapping("dept")
public class DeptController {
	private static final Logger logger = LoggerFactory.getLogger(DeptController.class);
	@Autowired
	private DeptService ds;
	@Autowired
	LogUtilImpl logUtil;
	/**
	 * 查询部门树
	 * @return
	 */
	@ApiOperation(value = "查询部门树")
	@RequestMapping(value = "/queryTree",method = RequestMethod.GET)
	public RestfulEntityBySummit<DeptBean> queryTree(@RequestParam(value = "pid",required = false) String pid) {
	     try {
	           return ResultBuilder.buildSuccess(ds.queryDeptTree(pid));
	     } catch (Exception e) {
	    	    logger.error("查询部门树失败：", e);
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
	     }
	}
	
	@ApiOperation(value = "查询部门--JSON 数据直接生成树结构", notes = "用于application/json格式")
	@GetMapping(value = "/queryDeptJsonTree")
	public RestfulEntityBySummit<DeptTreeBean> queryJsonTree(@RequestParam(value = "pid",required = false)  String pid
		) {
	     try {
	           DeptTreeBean adcdBean=ds.queryJsonAdcdTree(pid);
	           return ResultBuilder.buildSuccess(adcdBean);
	     } catch (Exception e) {
	            logger.error("数据查询失败！", e);
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
	     }
	}
	
	/**
	 * 
	 * 根据id查询（分页）
	 * 
	 */
	@ApiOperation(value = "根据id查询")
	@RequestMapping(value = "/queryById",method = RequestMethod.GET)
	public RestfulEntityBySummit<DeptBean> queryById(@RequestParam(value = "id") String id) {
		try {
			return ResultBuilder.buildSuccess(ds.queryById(id));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败：", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	/**
	 * 
	 * 根据id查询（分页）
	 * 
	 */
	@ApiOperation(value = "根据id查询部门包含行政区划")
	@RequestMapping(value = "/queryDeptAdcdById",method = RequestMethod.GET)
	public RestfulEntityBySummit<DeptBean> queryDeptAdcdById(@RequestParam(value = "id") String id) {
		try {
			return ResultBuilder.buildSuccess(ds.queryDeptAdcdById(id));
		} catch (Exception e) {
			logger.error("查询失败：", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
		
		//return list;
	}
	
	@ApiOperation(value = "根据adcd查询部门信息")
	@RequestMapping(value = "/queryDeptByAdcd",method = RequestMethod.GET)
	public RestfulEntityBySummit<List<DeptBean>> queryDeptByAdcd(@RequestParam(value = "adcd",required = false)  String adcd){
		try {
			return ResultBuilder.buildSuccess(ds.queryDeptByAdcd(adcd));
		} catch (Exception e) {
			logger.error("查询失败：", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "根据pid查询分页")
	@RequestMapping(value = "/queryByPidPage",method = RequestMethod.GET)
	public RestfulEntityBySummit<Page<DeptBean>> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "pid",required = false) String pid,
            @RequestParam(value = "deptcode",required = false) String deptcode,
            @RequestParam(value = "deptname",required = false) String deptname,
            @RequestParam(value = "adcd",required = false) String adcd,
            @RequestParam(value = "adnm",required = false) String adnm) {
		//Page<JSONObject> list = null;
		try {
			JSONObject paramJson = new JSONObject();
			 paramJson.put("pid",pid);
			 paramJson.put("deptcode",deptcode);
			 paramJson.put("deptname",deptname);
			 paramJson.put("adcd",adcd);
			 paramJson.put("adnm",adnm);
			return ResultBuilder.buildSuccess(ds.queryByPage(page, pageSize, paramJson));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败：", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	
	
	/**
	 * 新增
	 */
	@ApiOperation(value = "部门新增",notes="部门名称(deptName),上级部门(pid)都是必输项,没有上级部门为pid=-1 ")
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	public RestfulEntityBySummit<String> add(@RequestBody  DeptBean deptBean) {
		try {
			//list = ds.add(deptBean);
			ResponseCodeEnum responseCodeEnum=ds.add(deptBean);
			//LogBean logBean = new LogBean("部门管理","共享用户组件","新增部门信息："+deptBean,"1");
		    //logUtil.insertLog(logBean);
			if(responseCodeEnum!=null){ 
				return ResultBuilder.buildError(responseCodeEnum);
			}else{
				return ResultBuilder.buildSuccess();
			}
		} catch (Exception e) {
			logger.error("操作失败：", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	/**
	 * 编辑保存
	 */
	@ApiOperation(value = "部门编辑",notes="id,部门名称(deptName),上级部门(pid)都是必输项,没有上级部门为pid=-1 ")
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	public RestfulEntityBySummit<String> edit(@RequestBody DeptBean deptBean) {
		try {
			//list = ds.edit(deptBean);
			ds.edit(deptBean);
			//LogBean logBean = new LogBean("部门管理","共享用户组件","部门编辑信息："+deptBean,"2");
		   // logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败：", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	/**
	 * 删除
	 */
	@ApiOperation(value = "部门删除")
	@RequestMapping(value = "/del",method = RequestMethod.DELETE)
	public RestfulEntityBySummit<String> del(@RequestParam(value = "ids") String ids) {
		try {
			ds.del(ids);
			//LogBean logBean = new LogBean("部门管理","共享用户组件","部门删除信息："+ids,"3");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败：", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
}
