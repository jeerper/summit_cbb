package com.summit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.domain.log.LogBean;
import com.summit.domain.role.RoleBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;
import com.summit.service.role.RoleService;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(description = "角色管理")
@Controller
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
	public RestfulEntityBySummit<?> add(@RequestBody RoleBean roleBean, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			
			logBean = logUtil.insertLog(request,"1", "角色管理新增","");
			return new RestfulEntityBySummit<>(rs.add(roleBean));
			//res = rs.add(roleBean);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "角色管理删除")
	@DeleteMapping("del")
	@ResponseBody
	public RestfulEntityBySummit<?> del(
			@RequestParam(value = "codes") String codes,HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理删除","");
			//res = rs.del(codes);
			return new RestfulEntityBySummit<>(rs.del(codes));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "角色管理修改", notes = "code,角色名称(name)都是必输项")
	@PutMapping("edit")
	public RestfulEntityBySummit<?> edit(@RequestBody RoleBean roleBean, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理修改","");
			//res = rs.edit(roleBean);
			return new RestfulEntityBySummit<>(rs.edit(roleBean));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("操作失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
		    return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "角色管理按照编号查询")
	@GetMapping("queryByCode")
	@ResponseBody
	public RestfulEntityBySummit<?> queryByCode(
			@RequestParam(value = "code") String code, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理按照编号查询","");
			//res = rs.queryByCode(code);
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,rs.queryByCode(code));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "角色管理分页查询")
	@GetMapping("queryByPage")
	@ResponseBody
	public RestfulEntityBySummit<?> queryByPage(
			@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "name",required = false) String name, HttpServletRequest request) {
		//Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理分页查询","");
			//res = rs.queryByPage(start, limit, roleBean);
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,rs.queryByPage(page, pageSize, name));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
		    return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "角色管理查询所有数据")
	@GetMapping("queryAll")
	@ResponseBody
	public RestfulEntityBySummit<?> queryAll(HttpServletRequest request) {
		//Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "查询所有数据","");
			//res = rs.queryAll();
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,rs.queryAll());
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "角色管理查询角色权限")
	@GetMapping("getRoleFunInfo")
	@ResponseBody
	public RestfulEntityBySummit<?> getRoleFunInfo(
		@RequestParam(value = "roleCode") String roleCode, HttpServletRequest request) {
		//Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理查询角色权限","");
			String userName="";
			UserInfo userInfo=UserContextHolder.getUserInfo();
			if(userInfo!=null){
				userName=userInfo.getUserName();
			}
			m.put("treeNode", fs.queryAll(userName));
			m.put("funId", rs.queryFunIdByRoleCode(roleCode));
			//res = st.success("", m);
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,m);
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}

	@ApiOperation(value = "角色管理角色授权")
	@GetMapping("roleAuthorization")
	@ResponseBody
	public RestfulEntityBySummit<?> roleAuthorization(
			@RequestParam(value = "roleCode") String roleCode,
			@RequestParam(value = "funIds") String funIds, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理角色授权","");
			//res = rs.roleAuthorization(roleCode, funIds);
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,rs.roleAuthorization(roleCode, funIds));
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("数据查询失败！", e);
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
			logUtil.updateLog(logBean,"1");
			return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
		}
		//logUtil.updateLog(logBean,"1");
		//return res;
	}
}
