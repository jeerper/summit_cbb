package com.summit.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * 
 * @author yt
 *
 */
public class JdbcUtil {

	public static String DRIVERNAME = null;
	public static String URL = null;
	public static String USER = null;
	public static String PASSWORD = null;

	public static Connection conn = null;
	/*
	 * static { try {
	 * 
	 * Properties props = new Properties(); //Reader in = new
	 * FileReader("db.properties"); InputStream in =
	 * JDBCUtil.class.getClassLoader().getResourceAsStream(
	 * "com/summit/util/client.properties"); props.load(in);
	 * 
	 * DRIVERNAME = props.getProperty("drivername"); URL =
	 * props.getProperty("url"); USER = props.getProperty("user"); PASSWORD =
	 * props.getProperty("password");
	 * 
	 * } catch (Exception e) { throw new RuntimeException(e); } }
	 * 
	 * public static Connection getConnection() throws Exception { if (conn !=
	 * null) { return conn; }
	 * 
	 * Class.forName(DRIVERNAME); conn = DriverManager.getConnection(URL, USER,
	 * PASSWORD);
	 * 
	 * return conn; }
	 * 
	 * public static void closeResource(Connection conn, PreparedStatement st)
	 * throws SQLException { st.close(); conn.close(); }
	 * 
	 * public static void closeResource(Connection conn, ResultSet rs,
	 * PreparedStatement st) throws SQLException { st.close(); rs.close();
	 * conn.close(); }
	 */


	public static JsonArray getColumnAndData(Connection conn, String sql) throws Exception {

		System.out.println(conn);

		JsonArray jsonArray = new JsonArray();
		PreparedStatement ps;
		ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnNum = rsmd.getColumnCount();
		while (rs.next()) {
			JsonObject js = new JsonObject();
			for (int i = 1; i <= columnNum; i++) {
				if (null == rs.getObject(i)) {
					js.add(rsmd.getColumnName(i), JsonNull.INSTANCE);
				} else {
					if (rs.getObject(i) instanceof Timestamp) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						String dateStr = sdf.format(sdf.parse(rs.getObject(i).toString()));
						js.addProperty(rsmd.getColumnName(i), dateStr);
					} else if (rs.getObject(i) instanceof Integer) {
						Integer rsInt = 0; 
						if(String.valueOf(rs.getObject(i)).startsWith(".")){
							rsInt = Integer.valueOf("0" + String.valueOf(rs.getObject(i)));
						}else{
							rsInt = rs.getInt(i);
						}
						js.addProperty(rsmd.getColumnName(i), rs.getInt(i) == 0 ? 0 : rsInt);
					} else if (rs.getObject(i) instanceof Long) {
						Integer rsInt = 0;
						if(String.valueOf(rs.getObject(i)).startsWith(".")){
							rsInt = Integer.valueOf("0" + String.valueOf(rs.getObject(i)));
						}else{
							rsInt = rs.getInt(i);
						}
						js.addProperty(rsmd.getColumnName(i), rs.getInt(i) == 0 ? 0 : rsInt);
					} else if (rs.getObject(i) instanceof Double) {
						Double rsDouble = 0D ;
						if(String.valueOf(rs.getObject(i)).startsWith(".")){
							rsDouble = Double.valueOf("0" + String.valueOf(rs.getObject(i)));
						}else{
							rsDouble = rs.getDouble(i);
						}
						js.addProperty(rsmd.getColumnName(i), rs.getDouble(i) == 0 ? 0 : rsDouble);
					} else if (rs.getObject(i) instanceof Float) {
						Double rsDouble = 0D ;
						if(String.valueOf(rs.getObject(i)).startsWith(".")){
							rsDouble = Double.valueOf("0" + String.valueOf(rs.getObject(i)));
						}else{
							rsDouble = rs.getDouble(i);
						}
						js.addProperty(rsmd.getColumnName(i), rs.getFloat(i) == 0 ? 0 : rsDouble);
					} else {
						String rsStr = "";
						if(String.valueOf(rs.getObject(i)).startsWith(".")){
							rsStr = "0" + String.valueOf(rs.getObject(i));
						}else{
							rsStr = String.valueOf(rs.getObject(i));
						}
				
						js.addProperty(rsmd.getColumnName(i), rs.getString(i) == null ? "null" : rsStr);
					}
				}
			}
			jsonArray.add(js);
		}
		return jsonArray;
	}
}
