package com.summit.domain.adcd;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
@Service
public class ADCDTreeBeanRowMapper implements RowMapper<ADCDTreeBean>{
    
	public ADCDTreeBean mapRow(ResultSet rs, int arg1) throws SQLException {
		return new ADCDTreeBean(rs.getString("ADCD"),rs.getString("ADNM"),rs.getString("PADCD"),rs.getString("LEVEL"));
	}

}
