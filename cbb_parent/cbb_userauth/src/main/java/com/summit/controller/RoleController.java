package com.summit.controller;

import com.summit.domain.log.LogBean;
import com.summit.domain.role.RoleBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;
import com.summit.service.role.RoleService;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(description = "角色管理")
@Controller
@RequestMapping("role")
@Slf4j
public class RoleController {
	@Autowired
	private RoleService rs;
	@Autowired
	private FunctionService fs;
	@Autowired
	private SummitTools st;
	@Autowired
	ILogUtil logUtil;

    @ApiOperation(value = "新增角色", notes = "用于application/json格式")
    @PostMapping("/add")
	@ResponseBody
	public Map<String, Object> add(RoleBean roleBean, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理新增",userName);
			res = rs.add(roleBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@ApiOperation(value = "角色管理删除")
	@DeleteMapping("del")
	@ResponseBody
	public Map<String, Object> del(String codes, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理删除",userName);
			res = rs.del(codes);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@ApiOperation(value = "角色管理修改")
	@PutMapping("edit")
	@ResponseBody
	public Map<String, Object> edit(RoleBean roleBean, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理修改",userName);
			res = rs.edit(roleBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@ApiOperation(value = "角色管理按照编号查询")
	@GetMapping("queryByCode")
	@ResponseBody
	public Map<String, Object> queryByCode(String code, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理按照编号查询",userName);
			res = rs.queryByCode(code);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@ApiOperation(value = "角色管理分页查询")
	@GetMapping("queryByPage")
	@ResponseBody
	public Page<JSONObject> queryByPage(Integer start, Integer limit,
			RoleBean roleBean, HttpServletRequest request,String userName) {
		Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理分页查询",userName);
			start = (start == null) ? 1 : start;
			limit = (limit == null) ? SysConstants.PAGE_SIZE : limit;
			res = rs.queryByPage(start, limit, roleBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@ApiOperation(value = "角色管理查询所有数据")
	@GetMapping("queryAll")
	@ResponseBody
	public Page<JSONObject> queryAll(HttpServletRequest request,String userName) {
		Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "查询所有数据",userName);
			res = rs.queryAll();
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@ApiOperation(value = "角色管理查询角色权限")
	@GetMapping("getRoleFunInfo")
	@ResponseBody
	public Map<String, Object> getRoleFunInfo(String roleCode, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理查询角色权限",userName);
			m.put("treeNode", fs.queryAll(userName));
			m.put("funId", rs.queryFunIdByRoleCode(roleCode));
			res = st.success("", m);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@ApiOperation(value = "角色管理角色授权")
	@GetMapping("roleAuthorization")
	@ResponseBody
	public Map<String, Object> roleAuthorization(String roleCode, String funIds, HttpServletRequest request,String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理角色授权",userName);
			res = rs.roleAuthorization(roleCode, funIds);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}
}
