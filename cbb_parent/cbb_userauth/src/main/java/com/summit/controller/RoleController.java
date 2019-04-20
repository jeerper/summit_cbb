package com.summit.controller;

import com.summit.common.entity.AntdJsonBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.RoleBean;
import com.summit.common.entity.UserInfo;
import com.summit.common.util.ResultBuilder;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.domain.log.LogBean;
import com.summit.domain.role.FunctionListBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;
import com.summit.service.role.RoleService;
import com.summit.util.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "角色管理")
@RestController
@RequestMapping("role")
@Slf4j
public class RoleController {
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	@Autowired
	private RoleService rs;
	@Autowired
	private FunctionService fs;
	@Autowired
	ILogUtil logUtil;

    @ApiOperation(value = "新增角色", notes = "角色名称(name)都是必输项")
    @PostMapping("/add")
	public RestfulEntityBySummit<String> add(@RequestBody RoleBean roleBean) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			logBean = logUtil.insertLog(request,"1", "角色管理新增","");
			ResponseCodeEnum responseCodeEnum=rs.add(roleBean);
			if(responseCodeEnum!=null){
				return ResultBuilder.buildError(responseCodeEnum);
			}
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理删除")
	@DeleteMapping("del")
	public RestfulEntityBySummit<String> del(
			@RequestParam(value = "codes") String codes) {
		LogBean logBean = new LogBean();
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			logBean = logUtil.insertLog(request,"1", "角色管理删除","");
			rs.del(codes);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理修改", notes = "code,角色名称(name)都是必输项")
	@PutMapping("edit")
	public RestfulEntityBySummit<String> edit(@RequestBody RoleBean roleBean) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			logBean = logUtil.insertLog(request,"1", "角色管理修改","");
			rs.edit(roleBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理按照编号查询")
	@GetMapping("queryByCode")
	public RestfulEntityBySummit<RoleBean> queryByCode(
			@RequestParam(value = "code") String code) {
		LogBean logBean = new LogBean();
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			logBean = logUtil.insertLog(request,"1", "角色管理按照编号查询","");
			return ResultBuilder.buildSuccess(rs.queryByCode(code));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理分页查询")
	@GetMapping("queryByPage")
	public RestfulEntityBySummit<Page<RoleBean>> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "name",required = false) String name) {
		//Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			logBean = logUtil.insertLog(request,"1", "角色管理分页查询","");
			return ResultBuilder.buildSuccess(rs.queryByPage(page, pageSize, name));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理查询所有数据")
	@GetMapping("queryAll")
	public RestfulEntityBySummit<List<RoleBean>> queryAll() {
		LogBean logBean = new LogBean();
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			logBean = logUtil.insertLog(request,"1", "查询所有数据","");
			//res = rs.queryAll();
			return ResultBuilder.buildSuccess(rs.queryAll());
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	

	@ApiOperation(value = "角色管理查询所有数据--基于antd：Transfer穿梭框")
	@GetMapping("queryRoleAllAntd")
	public RestfulEntityBySummit<List<AntdJsonBean>> queryRoleAllAntd() {
		//LogBean logBean = new LogBean();
		try {
			//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			//logBean = logUtil.insertLog(request,"1", "查询所有数据","");
			//res = rs.queryAll();
			return ResultBuilder.buildSuccess(rs.queryRoleAntdJsonAll());
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			//logBean.setActionFlag("0");
			//logBean.setErroInfo(e.toString());
			//logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "角色管理查询角色权限")
	@GetMapping("getRoleFunInfo")
	public RestfulEntityBySummit<FunctionListBean> getRoleFunInfo(
		@RequestParam(value = "roleCode") String roleCode) {
		LogBean logBean = new LogBean();
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			logBean = logUtil.insertLog(request,"1", "角色管理查询角色权限","");
			String userName="";
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
				userName=userInfo.getUserName();
			}
			FunctionListBean functionListBean=new FunctionListBean();
			functionListBean.setFunctionList(fs.queryAll(userName));
			functionListBean.setFunctionIdList(rs.queryFunIdByRoleCode(roleCode));
			return ResultBuilder.buildSuccess(functionListBean);
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理角色授权")
	@GetMapping("roleAuthorization")
	public RestfulEntityBySummit<String> roleAuthorization(
			@RequestParam(value = "roleCode") String roleCode,
			@RequestParam(value = "funIds") String funIds) {
		LogBean logBean = new LogBean();
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			logBean = logUtil.insertLog(request,"1", "角色管理角色授权","");
			//res = rs.roleAuthorization(roleCode, funIds);
			rs.roleAuthorization(roleCode, funIds);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
}
