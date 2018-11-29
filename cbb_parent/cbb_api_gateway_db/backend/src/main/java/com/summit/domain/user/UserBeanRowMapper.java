package com.summit.domain.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.summit.util.RowMapperUtil;


@Component
public class UserBeanRowMapper implements RowMapper<UserBean> {
	@Autowired
	RowMapperUtil rmu;

	public UserBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new UserBean(rmu.resultSetGetString(rs, "NAME"), rmu
				.resultSetGetString(rs, "USERNAME"), rmu.resultSetGetString(rs,
				"PASSWORD"), rmu.resultSetGetString(rs, "EMAIL"), rmu
				.resultSetGetString(rs, "PHONE_NUMBER"), rmu.resultSetGetInt(
				rs, "IS_ENABLED"), rmu.resultSetGetInt(rs, "STATE"), rmu
				.resultSetGetStringTimestamp(rs, "LAST_UPDATE_TIME"), rmu
				.resultSetGetString(rs, "NOTE"));
	}
}
