package com.summit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.summit.domain.log.LogBean;
import com.summit.domain.role.RoleBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;
import com.summit.service.role.RoleService;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("role")
public class RoleController {
	@Autowired
	private RoleService rs;
	@Autowired
	private FunctionService fs;
	@Autowired
	private SummitTools st;
	@Autowired
	ILogUtil logUtil;

	@RequestMapping("add")
	@ResponseBody
	public Map<String, Object> add(RoleBean roleBean, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理新增");
			res = rs.add(roleBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("del")
	@ResponseBody
	public Map<String, Object> del(String codes, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理删除");
			res = rs.del(codes);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("edit")
	@ResponseBody
	public Map<String, Object> edit(RoleBean roleBean, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理修改");
			res = rs.edit(roleBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("queryByCode")
	@ResponseBody
	public Map<String, Object> queryByCode(String code, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理按照编号查询");
			res = rs.queryByCode(code);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("queryByPage")
	@ResponseBody
	public Page<JSONObject> queryByPage(Integer start, Integer limit,
			RoleBean roleBean, HttpServletRequest request) {
		Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理分页查询");
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

	@RequestMapping("queryAll")
	@ResponseBody
	public Page<JSONObject> queryAll(HttpServletRequest request) {
		Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理分页查询");
			res = rs.queryAll();
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean,"1");
		return res;
	}

	@RequestMapping("getRoleFunInfo")
	@ResponseBody
	public Map<String, Object> getRoleFunInfo(String roleCode, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		Map<String, Object> m = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理查询角色权限");
			m.put("treeNode", fs.queryAll());
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

	@RequestMapping("roleAuthorization")
	@ResponseBody
	public Map<String, Object> roleAuthorization(String roleCode, String funIds, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request,"1", "角色管理角色授权");
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
