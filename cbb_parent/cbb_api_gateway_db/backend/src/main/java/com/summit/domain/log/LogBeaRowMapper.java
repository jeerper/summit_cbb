package com.summit.domain.log;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.summit.util.RowMapperUtil;


public class LogBeaRowMapper implements RowMapper<LogBean>{
	@Autowired
	RowMapperUtil rmu;
	public LogBean mapRow(ResultSet rs, int arg1) throws SQLException {
		
		return new LogBean(
				rmu.resultSetGetString(rs, "id"),
				rmu.resultSetGetString(rs, "userName"),
				rmu.resultSetGetString(rs, "callerIP"),
				rmu.resultSetGetString(rs, "funName"),
				rmu.resultSetGetString(rs, "sTime"),
				rmu.resultSetGetString(rs, "eTime"),
				rmu.resultSetGetString(rs, "actionTime"),
				rmu.resultSetGetString(rs, "actionFlag"),
				rmu.resultSetGetString(rs, "erroInfo")
				);
	}
	
	
	

}
