package com.summit.controller;

import com.summit.common.entity.UserInfo;
import com.summit.domain.log.LogBean;
import com.summit.domain.user.UserBean;
import com.summit.service.log.ILogUtil;
import com.summit.service.user.UserService;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("user模块")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService us;

	@Autowired
	private SummitTools st;
	@Autowired
	ILogUtil logUtil;

	@PostMapping("/add")
	@ApiOperation(value = "新增用户", notes = "用于application/json格式")
	public Map<String, Object> add(UserBean userBean, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户新增",userBean.getUserName());
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
    @DeleteMapping("/del")
	public Map<String, Object> del(String userNames, HttpServletRequest request, String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "删除用户",userName);
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
	@PutMapping("/edit")
	public Map<String, Object> edit(UserBean userBean, HttpServletRequest request, String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "修改用户",userName);
			if (st.stringEquals(SysConstants.SUPER_USERNAME, userBean.getUserName())) {
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
	@PutMapping("/editPassword")
	public Map<String, Object> editPassword(String oldPassword, String password, String repeatPassword,
			HttpServletRequest request, String userName) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "修改密码",userName);
			if (st.stringIsNull(oldPassword)) {
				res = st.error("，请输入旧密码");
			} else if (st.stringIsNull(password)) {
				res = st.error("，请输入新密码");
			} else if (st.stringIsNull(repeatPassword)) {
				res = st.error("，请输入确认密码");
			} else if (!st.stringEquals(password, repeatPassword)) {
				res = st.error("，两次输入的密码不一致");
			} else {
				res = us.editPassword(oldPassword, password, repeatPassword,userName);
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
	@GetMapping("/queryByUserName")
	public Map<String, Object> queryByUserName(String userName, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理根据用户名查询用户",userName);
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
	@GetMapping("/queryByPage")
	public Page<JSONObject> queryByPage(Integer start, Integer limit, UserBean userBean, HttpServletRequest request, String userName) {
		Page<JSONObject> res = new Page<JSONObject>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理分页查询",userName);
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
	@PutMapping("/resetPassword")
	public Map<String, Object> resetPassword(String userName, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理重置密码",userName);
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
	@GetMapping("/queryRoleByUserName")
	public Map<String, Object> queryRoleByUserName(String userName, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理查询用户角色",userName);
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
	@PutMapping("/grantRole")
	public Map<String, Object> grantRole(String userName, String role, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "用户管理授权",userName);
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

	@ApiOperation(value = "根据用户名查询用户权限信息")
	@GetMapping("/queryUserRoleByUserName")
	public Map<String, Object> queryUserRoleByUserName(String userName, HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		LogBean logBean = new LogBean();
		try {
			logBean = logUtil.insertLog(request, "1", "根据用户名查询用户权限信息",userName);
			UserBean ub = us.queryByUserName(userName);
			List<String> roleList =  us.queryRoleByUserName(userName);
			List<String> funList = us.getFunByUserName(userName);
			UserInfo ui = new UserInfo();
			BeanUtils.copyProperties(ub, ui);
			String[] tmpStrRole = new String[roleList.size()];
			tmpStrRole = roleList.toArray(tmpStrRole);

			String[] tmpStrFun = new String[funList.size()];
			tmpStrFun = funList.toArray(tmpStrFun);
			ui.setPermissions(tmpStrFun);
			ui.setRoles(tmpStrRole);
			if (ui == null) {
				return st.error("");
			}
			ub.setPassword(null);
			ub.setState(null);
			ub.setLastUpdateTime(null);
			res = st.success("success", ui);
		} catch (Exception e) {
			e.printStackTrace();
			logBean.setActionFlag("0");
			logBean.setErroInfo(e.toString());
		}
		logUtil.updateLog(logBean, "1");
		return res;
	}
}
