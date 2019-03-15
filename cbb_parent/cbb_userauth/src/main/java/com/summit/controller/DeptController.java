package com.summit.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.domain.dept.DeptBean;
import com.summit.domain.log.LogBean;
import com.summit.service.dept.DeptService;
import com.summit.service.log.ILogUtil;

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
	ILogUtil logUtil;
	/**
	 * 查询部门树
	 * @return
	 */
	@ApiOperation(value = "查询部门树")
	@RequestMapping(value = "/queryTree",method = RequestMethod.POST)
	public RestfulEntityBySummit<?> queryTree(String pid) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		//UserContextHolder.getUserName();
		LogBean logBean = new LogBean();
		//Map<String, Object> list = null;
	     try {
	           logBean = logUtil.insertLog(request, "1", "查询部门树", "");
	           //list = ds.queryDeptTree();
	           return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,ds.queryDeptTree(pid));
	     } catch (Exception e) {
	    	    logger.error("查询部门树失败：", e);
	    	    logUtil.updateLog(logBean, "1");
	            //e.printStackTrace();
	            logBean.setActionFlag("0");
	            logBean.setErroInfo(e.toString());
	            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
	     }
	    // logUtil.updateLog(logBean, "1");
		//return list;
	}
	
	/**
	 * 
	 * 根据id查询（分页）
	 * 
	 */
	@ApiOperation(value = "根据id查询")
	@RequestMapping(value = "/queryById",method = RequestMethod.POST)
	public RestfulEntityBySummit<?> queryById(@RequestParam(value = "id") String id) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据id查询分页","");
		try {
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,ds.queryById(id));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败：", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	
	@ApiOperation(value = "根据pid查询分页")
	@RequestMapping(value = "/queryByPidPage",method = RequestMethod.POST)
	public RestfulEntityBySummit<?> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "pid",required = false) String pid,
            @RequestParam(value = "deptcode",required = false) String deptcode,
            @RequestParam(value = "deptname",required = false) String deptname) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "根据pid查询分页","");
		//Page<JSONObject> list = null;
		try {
			JSONObject paramJson = new JSONObject();
			 paramJson.put("pid",pid);
			 paramJson.put("deptcode",deptcode);
			 paramJson.put("deptname",deptname);
			///list = ds.queryByPage(start, limit, pid);
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,ds.queryByPage(page, pageSize, paramJson));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("查询失败：", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	
	
	
	/**
	 * 新增
	 */
	@ApiOperation(value = "部门新增",notes="编码(deptCode),部门名称(deptName),上级部门(pid)都是必输项,没有上级部门为pid='-1'")
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	public RestfulEntityBySummit<?> add(@RequestBody  DeptBean deptBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "部门新增","");
		//Map<String, Object> list = null;
		try {
			//list = ds.add(deptBean);
			return new RestfulEntityBySummit<>(ds.add(deptBean));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败：", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	
	/**
	 * 编辑保存
	 */
	@ApiOperation(value = "部门编辑",notes="id,编码(deptCode),部门名称(deptName),上级部门(pid)都是必输项,没有上级部门为pid='-1'")
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	public RestfulEntityBySummit<?> edit(@RequestBody DeptBean deptBean) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "部门编辑","");
		Map<String, Object> list = null;
		try {
			//list = ds.edit(deptBean);
			return new RestfulEntityBySummit<>(ds.edit(deptBean));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败：", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	/**
	 * 删除
	 */
	@ApiOperation(value = "部门删除")
	@RequestMapping(value = "/del",method = RequestMethod.GET)
	public RestfulEntityBySummit<?> del(@RequestParam(value = "ids") String ids) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		LogBean logBean = logUtil.insertLog(request,"1", "部门删除","");
		Map<String, Object> list = null;
		try {
			//list = ds.del(ids);
			return new RestfulEntityBySummit<>(ds.del(ids));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败：", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		
		//return list;
	}
	
}
