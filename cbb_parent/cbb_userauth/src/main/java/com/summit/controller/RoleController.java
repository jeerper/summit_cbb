package com.summit.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.AntdJsonBean;
import com.summit.common.entity.LogBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.RoleBean;
import com.summit.common.entity.UserInfo;
import com.summit.common.util.ResultBuilder;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.domain.role.FunctionListBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.LogUtilImpl;
import com.summit.service.role.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

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
	LogUtilImpl logUtil;

    @ApiOperation(value = "新增角色", notes = "角色名称(name)都是必输项")
    @PostMapping("/add")
	public RestfulEntityBySummit<String> add(@RequestBody RoleBean roleBean) {
		try {
			ResponseCodeEnum responseCodeEnum=rs.add(roleBean);
			//LogBean logBean = new LogBean("角色管理","共享用户组件","修改角色信息："+roleBean,"1");
		    //logUtil.insertLog(logBean);
			if(responseCodeEnum!=null){
				return ResultBuilder.buildError(responseCodeEnum);
			}
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理删除")
	@DeleteMapping("del")
	public RestfulEntityBySummit<String> del(
			@RequestParam(value = "codes") String codes) {
		try {
			rs.del(codes);
			//LogBean logBean = new LogBean("角色管理","共享用户组件","删除角色信息："+codes,"3");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理修改", notes = "code,角色名称(name)都是必输项")
	@PutMapping("edit")
	public RestfulEntityBySummit<String> edit(@RequestBody RoleBean roleBean) {
		try {
			rs.edit(roleBean);
			//LogBean logBean = new LogBean("角色管理","共享用户组件","修改角色信息："+roleBean,"2");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("操作失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理按照编号查询")
	@GetMapping("queryByCode")
	public RestfulEntityBySummit<RoleBean> queryByCode(
			@RequestParam(value = "code") String code) {
		try {
			return ResultBuilder.buildSuccess(rs.queryByCode(code));
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理分页查询")
	@GetMapping("queryByPage")
	public RestfulEntityBySummit<Page<RoleBean>> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "name",required = false) String name) {
		try {
			return ResultBuilder.buildSuccess(rs.queryByPage(page, pageSize, name));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色管理查询所有数据")
	@GetMapping("queryAll")
	public RestfulEntityBySummit<List<RoleBean>> queryAll() {
		try {
			return ResultBuilder.buildSuccess(rs.queryAll());
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	

	@ApiOperation(value = "角色管理查询所有数据--基于antd：Transfer穿梭框")
	@GetMapping("queryRoleAllAntd")
	public RestfulEntityBySummit<List<AntdJsonBean>> queryRoleAllAntd() {
		try {
			return ResultBuilder.buildSuccess(rs.queryRoleAntdJsonAll());
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
	
	@ApiOperation(value = "角色管理查询角色权限")
	@GetMapping("getRoleFunInfo")
	public RestfulEntityBySummit<FunctionListBean> getRoleFunInfo(
		@RequestParam(value = "roleCode") String roleCode) {
		try {
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
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}

	@ApiOperation(value = "角色授权")
	@GetMapping("roleAuthorization")
	public RestfulEntityBySummit<String> roleAuthorization(
			@RequestParam(value = "roleCode") String roleCode,
			@RequestParam(value = "funIds") String funIds) {
		try {
			rs.roleAuthorization(roleCode, funIds);
			//LogBean logBean = new LogBean("角色管理","共享用户组件","角色授权："+roleCode+",funIds"+funIds,"4");
		    //logUtil.insertLog(logBean);
			return ResultBuilder.buildSuccess();
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
		}
	}
}
