package com.summit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.domain.log.LogBean;
import com.summit.domain.role.FunctionListBean;
import com.summit.domain.role.RoleBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;
import com.summit.service.role.RoleService;
import com.summit.util.Page;

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
	ILogUtil logUtil;

    @ApiOperation(value = "新增角色", notes = "角色名称(name)都是必输项")
    @PostMapping("/add")
	public RestfulEntityBySummit<String> add(@RequestBody RoleBean roleBean, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			
			logBean = logUtil.insertLog(request,"1", "角色管理新增","");
			return new RestfulEntityBySummit<String>(rs.add(roleBean),null);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<String>(ResponseCodeBySummit.CODE_9999,null);
		}
	}

	@ApiOperation(value = "角色管理删除")
	@DeleteMapping("del")
	public RestfulEntityBySummit<String> del(
			@RequestParam(value = "codes") String codes,HttpServletRequest request) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理删除","");
			//res = rs.del(codes);
			return new RestfulEntityBySummit<String>(rs.del(codes),null);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<String>(ResponseCodeBySummit.CODE_9999,null);
		}
	}

	@ApiOperation(value = "角色管理修改", notes = "code,角色名称(name)都是必输项")
	@PutMapping("edit")
	public RestfulEntityBySummit<String> edit(@RequestBody RoleBean roleBean, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理修改","");
			return new RestfulEntityBySummit<String>(rs.edit(roleBean),null);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
		    return new RestfulEntityBySummit<String>(ResponseCodeBySummit.CODE_9999,null);
		}
	}

	@ApiOperation(value = "角色管理按照编号查询")
	@GetMapping("queryByCode")
	public RestfulEntityBySummit<RoleBean> queryByCode(
			@RequestParam(value = "code") String code, HttpServletRequest request) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理按照编号查询","");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,rs.queryByCode(code));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<RoleBean>(ResponseCodeBySummit.CODE_9999,null);
		}
	}

	@ApiOperation(value = "角色管理分页查询")
	@GetMapping("queryByPage")
	public RestfulEntityBySummit<Page<RoleBean>> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "name",required = false) String name, HttpServletRequest request) {
		//Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理分页查询","");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,rs.queryByPage(page, pageSize, name));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
		    return new RestfulEntityBySummit<Page<RoleBean>>(ResponseCodeBySummit.CODE_9999,null);
		}
	}

	@ApiOperation(value = "角色管理查询所有数据")
	@GetMapping("queryAll")
	public RestfulEntityBySummit<List<RoleBean>> queryAll(HttpServletRequest request) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "查询所有数据","");
			//res = rs.queryAll();
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,rs.queryAll());
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<List<RoleBean>>(ResponseCodeBySummit.CODE_9999,null);
		}
	}

	@ApiOperation(value = "角色管理查询角色权限")
	@GetMapping("getRoleFunInfo")
	public RestfulEntityBySummit<FunctionListBean> getRoleFunInfo(
		@RequestParam(value = "roleCode") String roleCode, HttpServletRequest request) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理查询角色权限","");
			String userName="";
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
				userName=userInfo.getUserName();
			}
			FunctionListBean functionListBean=new FunctionListBean();
			functionListBean.setFunctionList(fs.queryAll(userName));
			functionListBean.setFunctionIdList(rs.queryFunIdByRoleCode(roleCode));
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,functionListBean);
		} catch (Exception e) {
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<FunctionListBean>(ResponseCodeBySummit.CODE_9999,null);
		}
	}

	@ApiOperation(value = "角色管理角色授权")
	@GetMapping("roleAuthorization")
	public RestfulEntityBySummit<String> roleAuthorization(
			@RequestParam(value = "roleCode") String roleCode,
			@RequestParam(value = "funIds") String funIds, HttpServletRequest request) {
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理角色授权","");
			//res = rs.roleAuthorization(roleCode, funIds);
			return new RestfulEntityBySummit<String>(rs.roleAuthorization(roleCode, funIds),null);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<String>(ResponseCodeBySummit.CODE_9999,null);
		}
	}
}
