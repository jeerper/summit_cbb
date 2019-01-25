package com.summit.service.user;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.domain.user.UserBean;
import com.summit.domain.user.UserBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SummitTools.DateTimeType;
import com.summit.util.SysConstants;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository ur;
	@Autowired
	private SummitTools st;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private UserBeanRowMapper ubr;


	public ResponseCodeBySummit add(UserInfo userBean) {
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "SELECT * FROM SYS_USER WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userBean.getUserName());
		if (st.collectionNotNull(l)) {
			return ResponseCodeBySummit.CODE_4033;
		}
		sql = "INSERT INTO SYS_USER (USERNAME,NAME,PASSWORD,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,LAST_UPDATE_TIME) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql,
				userBean.getUserName(),
				userBean.getName(),
				encoder.encode(userBean.getPassword()),
				userBean.getIsEnabled(),
				userBean.getEmail(),
				userBean.getPhoneNumber(),
				1,
				userBean.getNote(),
				st.DTFormat(DateTimeType.dateTime, new Date()));
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit  del(String userNames) {
		userNames = userNames.replaceAll(",", "','");
		String sql = "UPDATE SYS_USER SET STATE = 0, LAST_UPDATE_TIME = ? WHERE USERNAME <> '"
				+ SysConstants.SUPER_USERNAME
				+ "' AND USERNAME IN ('"
				+ userNames + "')";
		jdbcTemplate.update(sql, st.DTFormat(DateTimeType.dateTime,
				new Date()));
		delUserRoleByUserName(userNames);
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit edit(UserInfo userBean) {
		String sql = "UPDATE SYS_USER SET NAME = ?, EMAIL = ?, PHONE_NUMBER =?, NOTE = ?, IS_ENABLED = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, userBean.getName(), userBean.getEmail(),
				userBean.getPhoneNumber(), userBean.getNote(), userBean
						.getIsEnabled(), st.DTFormat(DateTimeType.dateTime,
						new Date()), userBean.getUserName());
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit editPassword(String userName, String oldPassword,
			String password, String repeatPassword) {
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		UserBean ub = queryByUserName(userName);
		
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, encoder.encode(password), st.DTFormat(DateTimeType.dateTime, new Date()), ub
				.getUserName());
		return ResponseCodeBySummit.CODE_0000;
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

	public ResponseCodeBySummit resetPassword(String userNames) {
		userNames = userNames.replaceAll(",", "','");
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ?";
		jdbcTemplate.update(sql, encoder.encode("888888"), st.DTFormat(DateTimeType.dateTime, new Date()),
				userNames);
		return ResponseCodeBySummit.CODE_0000;
	}

	public List<String> queryRoleByUserName(String userName) {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT ROLE_CODE FROM SYS_USER_ROLE WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userName);
		for (JSONObject o : l) {
			list.add(st.objJsonGetString(o, "ROLE_CODE"));
		}
		return list;
	}

	public ResponseCodeBySummit delUserRoleByUserName(String userNames){
		String sql = "DELETE FROM SYS_USER_ROLE WHERE USERNAME IN ('"
				+ userNames + "')";
		jdbcTemplate.update(sql);
		return ResponseCodeBySummit.CODE_0000;
	}
	
	public ResponseCodeBySummit grantRole(String userName, String role) {
		delUserRoleByUserName(userName);
		if (st.stringIsNull(role)) {
			return ResponseCodeBySummit.CODE_4025;
		}
		String sql = "INSERT INTO SYS_USER_ROLE (ID, USERNAME, ROLE_CODE) VALUES (?, ?, ?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		String[] roleArr = role.split(",");
		for (String roleCode : roleArr) {
			batchArgs.add(new Object[] { st.getKey(), userName, roleCode });
		}
		jdbcTemplate.batchUpdate(sql, batchArgs);
		return ResponseCodeBySummit.CODE_0000;
	}


	public List<String> getFunByUserName(String userName){
		List<String> list = new ArrayList<String>();
		String sql = "SELECT DISTINCT SF.ID FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ? ORDER BY FDESC";
		List<JSONObject> l = ur.queryAllCustom(sql, userName);
		for (JSONObject o : l) {
			list.add(st.objJsonGetString(o, "ID"));
		}
		return list;
	}

}
