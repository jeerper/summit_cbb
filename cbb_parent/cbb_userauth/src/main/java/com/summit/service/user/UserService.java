package com.summit.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.domain.user.UserInfoRowMapper;
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
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private UserInfoRowMapper ubr;


	public ResponseCodeBySummit add(UserInfo userInfo) {
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "SELECT * FROM SYS_USER WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userInfo.getUserName());
		if (st.collectionNotNull(l)) {
			return ResponseCodeBySummit.CODE_4033;
		}
		sql = "INSERT INTO SYS_USER (USERNAME,NAME,PASSWORD,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,LAST_UPDATE_TIME) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql,
				userInfo.getUserName(),
				userInfo.getName(),
				encoder.encode(userInfo.getPassword()),
				0,
				userInfo.getEmail(),
				userInfo.getPhoneNumber(),
				1,
				userInfo.getNote(),
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

	public ResponseCodeBySummit edit(UserInfo userInfo) {
		String sql = "UPDATE SYS_USER SET NAME = ?, EMAIL = ?, PHONE_NUMBER =?, NOTE = ?, IS_ENABLED = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, userInfo.getName(), userInfo.getEmail(),
				userInfo.getPhoneNumber(), userInfo.getNote(), userInfo
						.getIsEnabled(), st.DTFormat(DateTimeType.dateTime,
						new Date()), userInfo.getUserName());
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit editPassword(String userName, String oldPassword,
			String password, String repeatPassword) {
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        UserInfo ub = queryByUserName(userName);
		
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, encoder.encode(password), st.DTFormat(DateTimeType.dateTime, new Date()), ub
				.getUserName());
		return ResponseCodeBySummit.CODE_0000;
	}

	public UserInfo queryByUserName(String userName) {
		String sql = "SELECT USERNAME ,NAME ,PASSWORD ,IS_ENABLED ,EMAIL ,PHONE_NUMBER ,STATE ,NOTE ,LAST_UPDATE_TIME FROM SYS_USER WHERE STATE = 1 AND USERNAME = ?";
		List<UserInfo> l = ur.queryAllCustom(sql, ubr, userName);
		if (st.collectionNotNull(l)) {
			return l.get(0);
		}
		return null;
	}

	public Page<JSONObject> queryByPage(int start, int limit, JSONObject paramJson) {
		StringBuilder sb = new StringBuilder(
				"SELECT USERNAME,NAME,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,LAST_UPDATE_TIME FROM SYS_USER WHERE USERNAME <> '"
						+ SysConstants.SUPER_USERNAME + "'");
		if (paramJson.containsKey("name")) {
			sb.append(" AND NAME LIKE '%"+paramJson.get("name")+"%'");
		}
		if (paramJson.containsKey("userName")) {
			sb.append(" AND USERNAME LIKE '%"+paramJson.get("userName")+"%'");
		}
		if (paramJson.containsKey("isEnabled")) {
			sb.append(" AND IS_ENABLED = '"+paramJson.get("isEnabled")+"'");
		}
		if (paramJson.containsKey("state")) {
			sb.append(" AND STATE = '"+paramJson.get("state")+"'");
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

	public List<JSONObject> getFunInfoByUserName(String userName){
		String sql = "SELECT  SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ? ORDER BY FDESC";
		return ur.queryAllCustom(sql, userName);
	}
}
