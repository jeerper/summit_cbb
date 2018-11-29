package com.summit.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.summit.util.SummitTools.DateTimeType;

@Component
public class RowMapperUtil {
	@Autowired
	SummitTools st;
	public String resultSetGetString(ResultSet rs, String columnLabel) {
		try {
			if (rs.getObject(columnLabel) == null) {
				return null;
			}
			return rs.getString(columnLabel);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Integer resultSetGetInt(ResultSet rs, String columnLabel) {
		try {
			if (rs.getObject(columnLabel) == null) {
				return null;
			}
			return rs.getInt(columnLabel);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Double resultSetGetDouble(ResultSet rs, String columnLabel) {
		try {
			if (rs.getObject(columnLabel) == null) {
				return null;
			}
			return rs.getDouble(columnLabel);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Timestamp resultSetGetTimestamp(ResultSet rs, String columnLabel) {
		try {
			if (rs.getObject(columnLabel) == null) {
				return null;
			}
			return rs.getTimestamp(columnLabel);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public String resultSetGetStringTimestamp(ResultSet rs, String columnLabel) {
		Timestamp t = resultSetGetTimestamp( rs,  columnLabel);
		
		try {
			if(t!=null){
				return st.DTFormat(DateTimeType.dateTime, t.getTime());
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
