package com.summit.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
import com.summit.cbb.utils.page.Page;
import com.summit.cbb.utils.page.Pageable;
import com.summit.domain.user.UserDaoRowMapper;
import com.summit.util.PageUtil;
import com.summit.util.SysConstants;

import net.sf.json.JSONObject;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
			   pstmt.close();
				conn.close();
		}
		return array;
	}

	
//	public Page<JSONObject> queryByCustomPage(String sql, int start,
//			int pageSize, Object... args) throws SQLException {
//		return queryByCustomPage(sql, userDaoRowMapper, start, pageSize, args);
//	}
	
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
				pstmt.close();
			     conn.close();
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
					pstmt.close();
				    conn.close();
			}
			return list;
		}

		public Page<Object> queryByCustomPage(String sql,int start,int pageSize,LinkedMap linkedMap) throws Exception{
			Page<Object> page = null;
			List<Object> list = null;
			if (start <= 0) {
				start = 1;
			}
			if (pageSize < 1) {
				pageSize = SysConstants.PAGE_SIZE;
			}
			start=(start-1)*pageSize;
			int currentPage = PageUtil.computePageNum(start, pageSize);
			PageRequest pr = PageUtil.createPageRequest(start, pageSize, null);
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
				PageableResultSet rs=new PageableResultSet(pstmt.executeQuery());//得到ResultSet
				ResultSetMetaData rsd = rs.getMetaData();
				if(rsd.getColumnCount() > 0){
					list = new ArrayList<Object>();
					String[] columnArray = new String[rsd.getColumnCount()];
					for(int i = 0; i < rsd.getColumnCount(); i++) {  
				        columnArray[i] = rsd.getColumnName(i + 1);
				    }
//					if(null == pageSize || (null != pageSize && pageSize.trim().length() == 0)){
//						pageSize = Common.PAGE_SIZE;
//					}
					rs.setPageSize(Integer.valueOf(pageSize));//设置每页显示数目
					rs.gotoPage(currentPage + 1);//设置当前选择第几页
					for (int i = 0; i < rs.getPageRowsCount(); i++) {
						JSONObject o = new JSONObject();
						for (int j = 0; j < columnArray.length; j++) {
							o.put(columnArray[j], rs.getObject(columnArray[j]));
						}
						list.add(o);
						rs.next();
					}
				}
				// Page page1=new PageImpl(list,pr,rs.getRowsCount());
				Pageable pageable=new Pageable(rs.getRowsCount(),rs.getPageCount(),currentPage + 1,pageSize,rs.getPageRowsCount());
				
				page=new Page(list,pageable);
				//List<T> content, Integer rowsCount,Integer pageCount,Integer curPage,Integer pageSize,Integer pageRowsCount
				
			} catch (Exception e) {
				throw e;
			} finally {
				if(pstmt != null && !pstmt.isClosed()){
					try {
						pstmt.close();
						if (conn != null && !conn.isClosed()) {
							try {
								conn.close();
							} catch (Exception e2) {
								throw e2;
							}
						}
					} catch (Exception e1) {
						throw e1;
					}
				}
			}
			return page;
		}

		public org.springframework.data.domain.Page<Object> queryByCustomPage11(String sql,int start,int pageSize,LinkedMap linkedMap) throws Exception{
			PageImpl page = null;
			List<Object> list = null;
			if (start <= 0) {
				start = 1;
			}
			if (pageSize < 1) {
				pageSize = SysConstants.PAGE_SIZE;
			}
			int currentPage = PageUtil.computePageNum(start, pageSize);
			PageRequest pr = PageUtil.createPageRequest(start, pageSize, null);
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
				PageableResultSet rs=new PageableResultSet(pstmt.executeQuery());//得到ResultSet
				ResultSetMetaData rsd = rs.getMetaData();
				System.out.println(rs.getPageRowsCount());
				System.out.println(rs.getPageCount());
				if(rsd.getColumnCount() > 0){
					list = new ArrayList<Object>();
					String[] columnArray = new String[rsd.getColumnCount()];
					for(int i = 0; i < rsd.getColumnCount(); i++) {  
				        columnArray[i] = rsd.getColumnName(i + 1);
				    }
//					if(null == pageSize || (null != pageSize && pageSize.trim().length() == 0)){
//						pageSize = Common.PAGE_SIZE;
//					}
					rs.setPageSize(Integer.valueOf(pageSize));//设置每页显示数目
					rs.gotoPage(currentPage + 1);//设置当前选择第几页
					for (int i = 0; i < rs.getPageRowsCount(); i++) {
						JSONObject o = new JSONObject();
						for (int j = 0; j < columnArray.length; j++) {
							o.put(columnArray[j], rs.getObject(columnArray[j]));
						}
						list.add(o);
						rs.next();
					}
				}
				System.out.println("1:"+rs.getPageRowsCount());
				System.out.println("1:"+rs.getPageCount());
				PageImpl page1=new org.springframework.data.domain.PageImpl(list,pr,rs.getRowsCount());
				page = new PageImpl(list,pr,rs.getRowsCount());
				
			} catch (Exception e) {
				throw e;
			} finally {
				if(pstmt != null && !pstmt.isClosed()){
					try {
						pstmt.close();
						if (conn != null && !conn.isClosed()) {
							try {
								conn.close();
							} catch (Exception e2) {
								throw e2;
							}
						}
					} catch (Exception e1) {
						throw e1;
					}
				}
			}
			return page;
		}

}
