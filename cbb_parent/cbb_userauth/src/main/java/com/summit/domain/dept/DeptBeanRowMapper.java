package com.summit.domain.dept;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.summit.util.RowMapperUtil;


@Component
public class DeptBeanRowMapper implements RowMapper<DeptBean> {
	@Autowired
	RowMapperUtil rmu;

	public DeptBean mapRow(ResultSet rs, int arg1) throws SQLException {
		
		return new DeptBean(
				rmu.resultSetGetString(rs, "ID"),
				rmu.resultSetGetString(rs, "PID"),
				rmu.resultSetGetString(rs, "DEPTCODE"),
				rmu.resultSetGetString(rs, "DEPTNAME"),
				rmu.resultSetGetString(rs, "REMARK")
				);
	}



}
