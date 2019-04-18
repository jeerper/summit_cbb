package com.summit.service.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.ResponseCodeEnum;
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


	public ResponseCodeEnum add(UserInfo userInfo) {
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "SELECT * FROM SYS_USER WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userInfo.getUserName());
		if (st.collectionNotNull(l)) {
			return ResponseCodeEnum.CODE_4022;
		}
		sql = "INSERT INTO SYS_USER (USERNAME,NAME,PASSWORD,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,LAST_UPDATE_TIME) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,now())";
		jdbcTemplate.update(sql,
				userInfo.getUserName(),
				userInfo.getName(),
				encoder.encode(userInfo.getPassword()),
				1,
				userInfo.getEmail(),
				userInfo.getPhoneNumber(),
				1,
				userInfo.getNote()
				);
		return null;
	}

	public void  del(String userNames) {
		userNames = userNames.replaceAll(",", "','");
		String sql = "UPDATE SYS_USER SET STATE = 0, LAST_UPDATE_TIME = ? WHERE USERNAME <> '"
				+ SysConstants.SUPER_USERNAME
				+ "' AND USERNAME IN ('"
				+ userNames + "')";
		jdbcTemplate.update(sql,new Date());
		delUserRoleByUserName(userNames);
	}

	public void edit(UserInfo userInfo) {
		String sql = "UPDATE SYS_USER SET NAME = ?, EMAIL = ?, PHONE_NUMBER =?, NOTE = ?, IS_ENABLED = ?, LAST_UPDATE_TIME = now() WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, userInfo.getName(), userInfo.getEmail(),
				userInfo.getPhoneNumber(), userInfo.getNote(), userInfo
						.getIsEnabled(), userInfo.getUserName());
	
	}

	public void editPassword(String userName, String oldPassword,
			String password, String repeatPassword) {
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        UserInfo ub = queryByUserName(userName);
		
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, encoder.encode(password), new Date(), ub
				.getUserName());
	}

	public UserInfo queryByUserName(String userName) {
		String sql = "SELECT USERNAME ,NAME ,PASSWORD ,IS_ENABLED ,EMAIL ,PHONE_NUMBER ,STATE ,NOTE ,LAST_UPDATE_TIME FROM SYS_USER WHERE STATE = 1 AND USERNAME = ?";
		List<UserInfo> l = ur.queryAllCustom(sql, ubr, userName);
		if (st.collectionNotNull(l)) {
			return l.get(0);
		}
		return null;
	}

	public Page<UserInfo> queryByPage(int start, int limit, JSONObject paramJson) throws SQLException {
		StringBuilder sb = new StringBuilder(
				"SELECT USERNAME,NAME,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE FROM SYS_USER WHERE USERNAME <> '"
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
		Page<UserInfo> pageUserInfo=new Page<UserInfo>();
		Page <JSONObject> page= ur.queryByCustomPage(sb.toString(), start, limit);
		if(page!=null){
			 ArrayList<UserInfo> students = JSON.parseObject(page.getContent().toString(), new TypeReference<ArrayList<UserInfo>>() {});
			 pageUserInfo.setContent(students);
			 pageUserInfo.setTotalElements(page.getTotalElements());
			 return pageUserInfo;
		}
		
		return null;
	}

	public List<UserInfo> queryByPage(JSONObject paramJson) throws Exception {
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
		List<Object> userInfoList= ur.queryAllCustom(sb.toString(), new LinkedMap());
		if(userInfoList!=null){
			 ArrayList<UserInfo> userInfo = JSON.parseObject(userInfoList.toString(), new TypeReference<ArrayList<UserInfo>>() {});
			 return userInfo;
		}
		return null;
	}
	
	public void resetPassword(String userNames) {
		userNames = userNames.replaceAll(",", "','");
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ?";
		jdbcTemplate.update(sql, encoder.encode("888888"),  new Date(),
				userNames);
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

	public void delUserRoleByUserName(String userNames){
		String sql = "DELETE FROM SYS_USER_ROLE WHERE USERNAME IN ('"
				+ userNames + "')";
		jdbcTemplate.update(sql);
	}
	
	public void grantRole(String userName, String role) {
		delUserRoleByUserName(userName);
		String sql = "INSERT INTO SYS_USER_ROLE (ID, USERNAME, ROLE_CODE) VALUES (?, ?, ?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		String[] roleArr = role.split(",");
		for (String roleCode : roleArr) {
			batchArgs.add(new Object[] { SummitTools.getKey(), userName, roleCode });
		}
		jdbcTemplate.batchUpdate(sql, batchArgs);
	}


	public List<String> getFunByUserName(String userName){
		List<String> list = new ArrayList<String>();
		String sql = "SELECT DISTINCT SF.ID FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userName);
		for (JSONObject o : l) {
			list.add(st.objJsonGetString(o, "ID"));
		}
		return list;
	}

	public List<FunctionBean> getFunInfoByUserName(String userName){
		String sql = "SELECT  SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ? ORDER BY FDESC";
		List<JSONObject>list= ur.queryAllCustom(sql, userName);
		if(list!=null){
			 ArrayList<FunctionBean> functionBeans = JSON.parseObject(list.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			 return functionBeans;
		}
		return null;
	}
}
