package com.summit.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.summit.domain.user.UserDaoRowMapper;
import com.summit.util.Page;
import com.summit.util.SysConstants;

import net.sf.json.JSONObject;

/**
 * 
 * @author yt
 *
 */
@Repository
public class UserRepository extends JdbcDaoSupport {
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	DataSource dataSource;

	@Autowired
	public UserRepository(DataSource dataSource) {
		setDataSource(dataSource);
		jdbcTemplate = super.getJdbcTemplate();
	}

	@Autowired
	private UserDaoRowMapper userDaoRowMapper;

	
	public List<JSONObject> queryAllCustom(String sql, Object... args) {
		return jdbcTemplate.query(sql, args, userDaoRowMapper);
	}


	public <T> List<T> queryAllCustom(String sql, RowMapper<T> rowMapper,
			Object... args)  throws DataAccessException{
		return jdbcTemplate.query(sql, args, rowMapper);
	}


	
	public JSONArray queryAllCustomJsonArray(String sql, LinkedMap linkedMap) throws Exception {
		JSONArray array = null;
		DataSource ds = getJdbcTemplate().getDataSource();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if(null != linkedMap && linkedMap.size() > 0){
				try {
					for (Iterator iterator = linkedMap.keySet().iterator(); iterator.hasNext();) {
						Object object = (Object) iterator.next();
						pstmt.setObject(Integer.valueOf(object.toString()), linkedMap.get(object));
					}
				} catch (Exception e3) {
					throw e3;
				}
			}

			ResultSet rs = pstmt.executeQuery();//得到ResultSet
			ResultSetMetaData rsd = rs.getMetaData();
			if(rsd.getColumnCount() > 0){
				array = new JSONArray();
				String[] columnArray = new String[rsd.getColumnCount()];
				for(int i = 0; i < rsd.getColumnCount(); i++) {
					columnArray[i] = rsd.getColumnName(i + 1);
				}
				while(rs.next()){
					JSONObject o = new JSONObject();
					for (int j = 0; j < columnArray.length; j++) {
						o.put(columnArray[j], rs.getObject(columnArray[j]));
					}
					array.add(o);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(pstmt != null && !pstmt.isClosed()){
			   pstmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return array;
	}

	
	public Page<JSONObject> queryByCustomPage(String sql, int start,
			int pageSize, Object... args) throws SQLException {
		return queryByCustomPage(sql, userDaoRowMapper, start, pageSize, args);
	}
	
	public JSONObject queryOneCustom(String sql, LinkedMap linkedMap) throws Exception {
		JSONObject rtnJson = null;
		DataSource ds = getJdbcTemplate().getDataSource();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if(null != linkedMap && linkedMap.size() > 0){
				try {
					for (Iterator iterator = linkedMap.keySet().iterator(); iterator.hasNext();) {
						Object object = (Object) iterator.next();
						pstmt.setObject(Integer.valueOf(object.toString()), linkedMap.get(object));
					}
				} catch (Exception e3) {
					throw e3;
				}
			}

			ResultSet rs = pstmt.executeQuery();//得到ResultSet
			ResultSetMetaData rsd = rs.getMetaData();
			if(rsd.getColumnCount() > 0){
				String[] columnArray = new String[rsd.getColumnCount()];
				for(int i = 0; i < rsd.getColumnCount(); i++) {
					columnArray[i] = rsd.getColumnName(i + 1);
				}
				while(rs.next()){
					JSONObject o = new JSONObject();
					for (int j = 0; j < columnArray.length; j++) {
						o.put(columnArray[j], rs.getObject(columnArray[j]));
					}
					rtnJson = o;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if(pstmt != null && !pstmt.isClosed()){
				pstmt.close();
			}
			if (conn != null && !conn.isClosed()) {
			     conn.close();
			}
		}
		return rtnJson;
	}
	
		public List<Object> queryAllCustom(String sql,LinkedMap linkedMap) throws Exception{
			List<Object> list = null;
			DataSource ds = getJdbcTemplate().getDataSource();
			Connection conn = null;
			PreparedStatement pstmt = null;
			try {
				conn = ds.getConnection();
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				if(null != linkedMap && linkedMap.size() > 0){
					try {
						for (Iterator iterator = linkedMap.keySet().iterator(); iterator.hasNext();) {
							Object object = (Object) iterator.next();
							//pstmt.setString(1, json.get(object).toString());
							pstmt.setObject(Integer.valueOf(object.toString()), linkedMap.get(object));
						}
					} catch (Exception e3) {
						throw e3;
					}
				}
				
				ResultSet rs = pstmt.executeQuery();//得到ResultSet
				ResultSetMetaData rsd = rs.getMetaData();
				if(rsd.getColumnCount() > 0){
					list = new ArrayList<Object>();
					String[] columnArray = new String[rsd.getColumnCount()];
					for(int i = 0; i < rsd.getColumnCount(); i++) {  
				        columnArray[i] = rsd.getColumnName(i + 1);
				    }  
					while(rs.next()){
						JSONObject o = new JSONObject();
						for (int j = 0; j < columnArray.length; j++) {
							o.put(columnArray[j], rs.getObject(columnArray[j]));
						}
						list.add(o);
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				if(pstmt != null && !pstmt.isClosed()){
					pstmt.close();
				}
				if (conn != null && !conn.isClosed()) {
				     conn.close();
				}
			}
			return list;
		}

	
	public <T> Page<T> queryByCustomPage(String sql, RowMapper<T> rowMapper,
			int start, int pageSize, Object... args) throws SQLException {
		// TODO Auto-generated method stub
		/*log.info("自定义查询开始SQL:" + sql);
		log.info("自定义查询参数start:" + start + "  pageSize:" + pageSize + "  args:"
				+ args);*/
		List<T> list = new ArrayList<T>();
		Page<T> p = new Page<T>(list, 0);

		if (start < 0) {
			start = 0;
		}
		if (pageSize < 1) {
			pageSize = SysConstants.PAGE_SIZE;
		}
		int page = start / pageSize + 1;
		DataSource ds = getDataSource();
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			if (null != args && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					pstmt.setObject(i + 1, args[i]);
				}
			}
			ResultSet rs = pstmt.executeQuery();
			rs.last();
			int rowsCount = rs.getRow();
			if (rowsCount > 0) {
				start = (page - 1) * pageSize + 1;
				int end = page * pageSize;
				if (end > rowsCount) {
					end = rowsCount;
				}
				try {
					rs.absolute(start);
				} catch (SQLException e) {
				}
				for (; start <= end; start++) {
					list.add(rowMapper.mapRow(rs, start));
					rs.next();
				}
				rs.close();
				p.setTotalElements(rowsCount);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}
			if (conn != null && !conn.isClosed()) {
					conn.close();
			}
		}
		return p;
	}

}
