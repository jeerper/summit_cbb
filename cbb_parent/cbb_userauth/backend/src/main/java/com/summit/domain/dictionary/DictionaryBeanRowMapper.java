package com.summit.domain.dictionary;

import com.summit.util.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class DictionaryBeanRowMapper implements RowMapper<DictionaryBean> {
	@Autowired
	RowMapperUtil rmu;

	public DictionaryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new DictionaryBean(rmu.resultSetGetString(rs, "CODE"),
				rmu.resultSetGetString(rs, "PCODE"), rmu.resultSetGetString(rs, "NAME"),
				rmu.resultSetGetString(rs, "CKEY"), rmu.resultSetGetString(rs, "NOTE"));
	}
}
