package com.summit.domain.adcd;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.summit.util.RowMapperUtil;


@Component
public class ADCDBeanRowMapper implements RowMapper<ADCDBean> {
	@Autowired
	RowMapperUtil rmu;

	public ADCDBean mapRow(ResultSet rs, int arg1) throws SQLException {
		
		return new ADCDBean(
				rmu.resultSetGetString(rs, "ADCD"),
				rmu.resultSetGetString(rs, "ADNM"),
				rmu.resultSetGetString(rs, "PADCD"),
				rmu.resultSetGetString(rs, "ADLEVEL")
				);
	}



}
