package com.summit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.summit.domain.log.LogBean;
import com.summit.domain.user.UserBean;
import com.summit.service.log.ILogUtil;
import com.summit.service.user.UserService;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;


@Api("user模块")
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService us;

	@Autowired
	private SummitTools st;
	@Autowired
	ILogUtil logUtil;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "新增用户", notes = "用于application/json格式")
	public Map<String, Object> add(
			@ApiParam(value = "用戶信息example:{\"USERNAME\":\"zhangsan\",\"PASSWORD\":\"888888\",\"EMAIL\":\"@qq.com\",\"NAME\":\"zhangsan\",\"PHONE_NUMBER\":\"123456\",\"NOTE\": \"备注 \"}", required = true) @RequestBody UserBean userBean,
			HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户新增");
			res = us.add(userBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}

	@ApiOperation(value = "删除用户信息")

	@RequestMapping("/del")
	@ResponseBody
	public Map<String, Object> del(String userNames, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "删除用户");
			res = us.del(userNames);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}

	@ApiOperation(value = "修改用户", notes = "用于application/json格式")
	@RequestMapping("/edit")
	@ResponseBody
	public Map<String, Object> edit(UserBean userBean, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "修改用户");
			if (st.stringEquals(SysConstants.SUPER_USERNAME, userBean.getUsername())) {
				res = st.success("", new ArrayList<Object>());
			} else {
				res = us.edit(userBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}

	@ApiOperation(value = "修改密码")
	@RequestMapping("/editPassword")
	@ResponseBody
	public Map<String, Object> editPassword(String oldPassword, String password, String repeatPassword,
			HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "修改密码");
			if (st.stringIsNull(oldPassword)) {
				res = st.error("，请输入旧密码");
			} else if (st.stringIsNull(password)) {
				res = st.error("，请输入新密码");
			} else if (st.stringIsNull(repeatPassword)) {
				res = st.error("，请输入确认密码");
			} else if (!st.stringEquals(password, repeatPassword)) {
				res = st.error("，两次输入的密码不一致");
			} else {
				res = us.editPassword(oldPassword, password, repeatPassword);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}

	@ApiOperation(value = "根据用户名查询用户信息")
	@RequestMapping("/queryByUserName")
	@ResponseBody
	public Map<String, Object> queryByUserName(String userName, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理根据用户名查询用户");
			if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
				return st.success("");
			}
			UserBean ub = us.queryByUserName(userName);
			if (ub == null) {
				return st.error("");
			}
			ub.setPassword(null);
			ub.setState(null);
			ub.setLastUpdateTime(null);
			res = st.success("", ub);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}

	@ApiOperation(value = "分页查询")
	@RequestMapping("/queryByPage")
	@ResponseBody
	public Page<JSONObject> queryByPage(Integer start, Integer limit, UserBean userBean, HttpServletRequest request) {
		Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理分页查询");
			start = (start == null) ? 1 : start;
			limit = (limit == null) ? SysConstants.PAGE_SIZE : limit;
			res = us.queryByPage(start, limit, userBean);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}

	@ApiOperation(value = "重置密码")
	@RequestMapping("/resetPassword")
	@ResponseBody
	public Map<String, Object> resetPassword(String userName, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理重置密码");
			if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
				res = st.success("");
			} else {
				res = us.resetPassword(userName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}

	@ApiOperation(value = "根据用户名查询权限")
	@RequestMapping("/queryRoleByUserName")
	@ResponseBody
	public Map<String, Object> queryRoleByUserName(String userName, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理查询用户角色");
			if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
				res = st.success("", new ArrayList<Object>());
			} else {
				res = st.success("", us.queryRoleByUserName(userName));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}

	@ApiOperation(value = "授权权限")
	@RequestMapping("/grantRole")
	@ResponseBody
	public Map<String, Object> grantRole(String userName, String role, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理授权");
			if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
				res = st.success("");
			} else {
				res = us.grantRole(userName, role);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}
}