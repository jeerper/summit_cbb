package com.summit.controller;

import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.*;
import com.summit.common.util.ResultBuilder;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.service.function.FunctionService;
import com.summit.service.log.LogUtilImpl;
import com.summit.service.user.UserService;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(description = "功能管理")
@RestController
@RequestMapping("function")
public class FunctionController {
	private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);
	@Autowired
	private FunctionService fs;
	
	@Autowired
	private UserService userservice;
	
	@Autowired
	LogUtilImpl logUtil;

	@ApiOperation(value = "新增功能",  notes = "上级功能(pid),功能名称(name),功能排序(fdesc)都是必输项")
	@PostMapping("/add")
	public RestfulEntityBySummit<String> add(@RequestBody FunctionBean functionBean) {
		try {
			fs.add(functionBean);
			//LogBean logBean = new LogBean("功能管理","共享用户组件","修改功能信息："+functionBean,"1");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "功能管理删除")
	@DeleteMapping("del")
	public RestfulEntityBySummit<String> del(
			@RequestParam(value = "ids") String ids) {
		try {
			fs.del(ids);
			//LogBean logBean = new LogBean("功能管理","共享用户组件","删除功能信息："+ids,"3");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "功能管理修改" ,notes = "id,上级功能(pid),功能名称(name),功能排序(fdesc)都是必输项")
	@PutMapping("edit")
	public RestfulEntityBySummit<String> edit(@RequestBody FunctionBean functionBean) {
		try {
			fs.edit(functionBean);
			//LogBean logBean = new LogBean("功能管理","共享用户组件","修改功能信息："+functionBean,"2");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "功能管理根据ID查询")
	@GetMapping("queryById")
	public RestfulEntityBySummit<List<FunctionBean>> queryById(
			@RequestParam(value = "id")  String id) {
		try {
			String userName="";
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
				userName=userInfo.getUserName();
			}
			return ResultBuilder.buildSuccess(fs.queryById(id,userName));
		} catch (Exception e) {
			logger.error("查询失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "功能管理查询树形结构")
	@GetMapping("queryTree")
	public RestfulEntityBySummit<FunctionBean> queryTree(@RequestParam(value = "pid" ,required = false)  String pid) {
		try {
			return ResultBuilder.buildSuccess(fs.queryFunTree(pid));
		} catch (Exception e) {
			logger.error("查询失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	
	@ApiOperation(value = "功能管理查询树形结构-title-key")
	@GetMapping("queryRenderTree")
	public RestfulEntityBySummit<FunctionTreeBean> queryRenderTree(@RequestParam(value = "pid" ,required = false)  String pid) {
		try {
			return ResultBuilder.buildSuccess(fs.queryJsonFunctionTree(pid));
		} catch (Exception e) {
			logger.error("查询失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "功能管理分页查询")
	@GetMapping("queryByPage")
	public RestfulEntityBySummit<Page<FunctionBean>> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "pId",required = false) String pId) {
		try {
			String userName="";
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
				userName=userInfo.getUserName();
			}
			return ResultBuilder.buildSuccess(fs.queryByPage(page, pageSize, pId,userName));
		} catch (Exception e) {
			logger.error("查询失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "根据用户名查询分配的菜单-树展示")
	@GetMapping("getFunInfoByUserName")
	public RestfulEntityBySummit<List<FunctionBean>> getFunInfoByUserName(@RequestParam(value = "userName" ,required = true)  String userName) {
		try {
			//String userName="";
			List<String> roleList = userservice.queryRoleByUserName(userName);
			//UserInfo userInfo=UserContextHolder.getUserInfo();
//			if(userInfo!=null){
//				userName=userInfo.getUserName();
//			}
			boolean isSuroleCode=false; 
            if(roleList!=null && roleList.size()>0){
            	isSuroleCode=roleList.contains(SysConstants.SUROLE_CODE);
            }
			return ResultBuilder.buildSuccess(fs.getFunInfoByUserName(userName,isSuroleCode));
		} catch (Exception e) {
			logger.error("查询失败！", e);
			 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
}
