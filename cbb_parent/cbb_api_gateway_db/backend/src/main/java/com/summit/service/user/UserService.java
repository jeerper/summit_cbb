package com.summit.service.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.config.UserContext;
import com.summit.domain.user.UserBean;
import com.summit.domain.user.UserBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SummitTools.DateTimeType;
import com.summit.util.SysConstants;

import net.sf.json.JSONObject;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserRepository ur;
	@Autowired
	private SummitTools st;
	
	@Autowired
	private UserBeanRowMapper ubr;

	public Map<String, Object> add(UserBean userBean) {
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "SELECT * FROM SYS_USER WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userBean.getUserName());
		if (st.collectionNotNull(l)) {
			return st.error("登陆名" + userBean.getUserName() + "已存在！");
		}
		sql = "INSERT INTO [SYS_USER] (USERNAME,NAME,PASSWORD,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,LAST_UPDATE_TIME) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ur.jdbcTemplate.update(sql, userBean.getUserName(), userBean.getName(),
				encoder.encode(userBean.getPassword()),
				userBean.getIsEnabled(), userBean.getEmail(), userBean
						.getPhoneNumber(), 1, userBean.getNote(), st.DTFormat(
						DateTimeType.dateTime, new Date()));
		return st.success("");
	}

	public Map<String, Object> del(String userNames) {
		userNames = userNames.replaceAll(",", "','");
		String sql = "UPDATE SYS_USER SET STATE = 0, LAST_UPDATE_TIME = ? WHERE USERNAME <> '"
				+ SysConstants.SUPER_USERNAME
				+ "' AND USERNAME IN ('"
				+ userNames + "')";
		ur.jdbcTemplate.update(sql, st.DTFormat(DateTimeType.dateTime,
				new Date()));
		delUserRoleByUserName(userNames);
		return st.success("");
	}

	public Map<String, Object> edit(UserBean userBean) {
		String sql = "UPDATE SYS_USER SET NAME = ?, EMAIL = ?, PHONE_NUMBER =?, NOTE = ?, IS_ENABLED = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
		ur.jdbcTemplate.update(sql, userBean.getName(), userBean.getEmail(),
				userBean.getPhoneNumber(), userBean.getNote(), userBean
						.getIsEnabled(), st.DTFormat(DateTimeType.dateTime,
						new Date()), userBean.getUserName());
		return st.success("");
	}

	public Map<String, Object> editPassword(String oldPassword,
			String password, String repeatPassword) {
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		UserBean ub = queryByUserName(UserContext.getUsername());
		if (ub == null) {
			return st.error("，请刷新页面重试");
		}
		
		if (!encoder.matches(oldPassword, ub.getPassword())) {
			return st.error("，密码错误");
		}
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
		ur.jdbcTemplate.update(sql, encoder.encode(password), st.DTFormat(DateTimeType.dateTime, new Date()), ub
				.getUserName());
		return st.success("");
	}

	public UserBean queryByUserName(String userName) {
		String sql = "SELECT USERNAME ,NAME ,PASSWORD ,IS_ENABLED ,EMAIL ,PHONE_NUMBER ,STATE ,NOTE ,LAST_UPDATE_TIME FROM SYS_USER WHERE STATE = 1 AND USERNAME = ?";
		List<UserBean> l = ur.queryAllCustom(sql, ubr, userName);
		if (st.collectionNotNull(l)) {
			return l.get(0);
		}
		return null;
	}

	public Page<JSONObject> queryByPage(int start, int limit, UserBean userBean) {
		StringBuilder sb = new StringBuilder(
				"SELECT USERNAME,NAME,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,LAST_UPDATE_TIME FROM SYS_USER WHERE USERNAME <> '"
						+ SysConstants.SUPER_USERNAME + "'");
		if (st.stringNotNull(userBean.getName())) {
			sb.append(" AND NAME LIKE '%").append(userBean.getName()).append(
					"%'");
		}
		if (st.stringNotNull(userBean.getUserName())) {
			sb.append(" AND USERNAME LIKE '%").append(userBean.getUserName())
					.append("%'");
		}
		if (userBean.getIsEnabled() != null) {
			sb.append(" AND IS_ENABLED = ").append(userBean.getIsEnabled());
		}

		if (userBean.getState() != null) {
			sb.append(" AND STATE = ").append(userBean.getState());
		}
		return ur.queryByCustomPage(sb.toString(), start, limit);
	}

	public Map<String, Object> resetPassword(String userNames) {
		userNames = userNames.replaceAll(",", "','");
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ?";
		ur.jdbcTemplate.update(sql, encoder.encode("888888"), st.DTFormat(DateTimeType.dateTime, new Date()),
				userNames);
		return st.success("");
	}

	public List<String> queryRoleByUserName(String userName) {
		List<String> list = new ArrayList<String>();
		if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
			list.add(SysConstants.SUROLE_CODE);
			return list;
		}
		String sql = "SELECT ROLE_CODE FROM SYS_USER_ROLE WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userName);
		for (JSONObject o : l) {
			list.add(st.objJsonGetString(o, "ROLE_CODE"));
		}
		return list;
	}

	public void delUserRoleByUserName(String userNames){
		String sql = "DELETE FROM SYS_USER_ROLE WHERE USERNAME IN ('"
				+ userNames + "')";
		ur.jdbcTemplate.update(sql);
	}
	
	public Map<String, Object> grantRole(String userName, String role) {
		delUserRoleByUserName(userName);
		if (st.stringIsNull(role)) {
			return st.success("");
		}
		String sql = "INSERT INTO [SYS_USER_ROLE] ([ID], [USERNAME], [ROLE_CODE]) VALUES (?, ?, ?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		String[] roleArr = role.split(",");
		for (String roleCode : roleArr) {
			batchArgs.add(new Object[] { st.getKey(), userName, roleCode });
		}
		ur.jdbcTemplate.batchUpdate(sql, batchArgs);
		return st.success("");
	}

	public Collection<GrantedAuthority> getGrantedAuthoritiesByUserName(
			String userName) {
		List<String> roles = queryRoleByUserName(userName);
		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			result.add(new SimpleGrantedAuthority(role));
		}
		result.add(new SimpleGrantedAuthority("ROLE_DEFAULT"));
		return result;
	}
}
