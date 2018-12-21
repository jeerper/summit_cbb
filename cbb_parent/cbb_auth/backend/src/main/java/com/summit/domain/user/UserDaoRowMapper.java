package com.summit.domain.user;

import com.summit.util.SummitTools;
import com.summit.util.SummitTools.DateTimeType;
import net.sf.json.JSONObject;
import net.sourceforge.jtds.jdbc.ClobImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;


@Component
public class UserDaoRowMapper implements RowMapper<JSONObject> {
	@Autowired
	SummitTools st;

	public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsd = rs.getMetaData();
		JSONObject o = new JSONObject();
		String columnName;
		for (int i = 0; i < rsd.getColumnCount(); i++) {
			columnName = rsd.getColumnName(i + 1);
			Object oo = rs.getObject(columnName);
			if (oo instanceof ClobImpl) {
				ClobImpl c = (ClobImpl) oo;
				Reader reader = c.getCharacterStream();
				BufferedReader r = new BufferedReader(reader);
				StringBuilder b = new StringBuilder();
				String line;
				try {
					while ((line = r.readLine()) != null) {
						b.append(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				o.put(columnName, b.toString());
			} else if (oo instanceof Date) {
				o.put(columnName, st.DTFormat(DateTimeType.dateTime,
						((Date) oo).getTime()));
			} else if (oo instanceof Timestamp) {
				o.put(columnName, st.DTFormat(DateTimeType.dateTime,
						((Timestamp) oo).getTime()));
			} else {
				o.put(columnName, oo);
			}
		}
		return o;
	}
}
