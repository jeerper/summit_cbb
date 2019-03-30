package com.summit.domain.user;

import com.summit.common.entity.UserInfo;
import com.summit.util.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class UserInfoRowMapper implements RowMapper<UserInfo> {
	@Autowired
	RowMapperUtil rmu;

	public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserInfo userInfo=new UserInfo();
		userInfo.setName(rmu.resultSetGetString(rs, "NAME"));
		userInfo.setUserName(rmu.resultSetGetString(rs, "USERNAME"));
		userInfo.setPassword(rmu.resultSetGetString(rs, "PASSWORD"));
		userInfo.setEmail(rmu.resultSetGetString(rs, "EMAIL"));
		userInfo.setPhoneNumber(rmu.resultSetGetString(rs, "PHONE_NUMBER"));
		userInfo.setIsEnabled(rmu.resultSetGetInt(rs, "IS_ENABLED"));
		userInfo.setState(rmu.resultSetGetInt(rs, "STATE"));
		userInfo.setLastUpdateTime(rmu.resultSetGetString(rs, "LAST_UPDATE_TIME"));
		userInfo.setNote(rmu.resultSetGetString(rs, "NOTE"));
		return userInfo;
	}
}
