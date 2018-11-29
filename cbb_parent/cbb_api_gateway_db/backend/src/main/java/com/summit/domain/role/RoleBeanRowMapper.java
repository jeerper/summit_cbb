package com.summit.domain.role;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.summit.util.RowMapperUtil;


@Component
public class RoleBeanRowMapper implements RowMapper<RoleBean> {
	@Autowired
	RowMapperUtil rmu;

	public RoleBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new RoleBean(rmu.resultSetGetString(rs, "CODE"), rmu
				.resultSetGetString(rs, "NAME"), rmu.resultSetGetString(rs,
				"NOTE"));
	}
}
