package com.summit.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
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

	public int getRowCount(String sql,LinkedMap linkedMap) throws Exception{
		DataSource ds = getJdbcTemplate().getDataSource();
		// Connection conn = null;
		int totalElements = 0;
		StringBuffer sqlTmp = new StringBuffer(sql);
		try{
		try (Connection conn  =ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlTmp.toString());
			) {
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
			try (ResultSet rs = pstmt.executeQuery();){
			  rs.next();
			  totalElements = rs.getInt(1);
			}
		 }
		} catch (Exception e) {
			throw e;
		} 
		return totalElements;
	}
	public JSONArray queryAllCustomJsonArray(String sql, LinkedMap linkedMap) throws Exception {
		JSONArray array = null;
		DataSource ds = getJdbcTemplate().getDataSource();
		try {
			try (Connection conn  =ds.getConnection();
				 PreparedStatement pstmt=conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			 ){
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
				
				try (ResultSet rs = pstmt.executeQuery()){////得到ResultSet
					ResultSetMetaData rsd = rs.getMetaData();
					if(rsd.getColumnCount() > 0){
						array = new JSONArray();
						String[] columnArray = new String[rsd.getColumnCount()];
						for(int i = 0; i < rsd.getColumnCount(); i++) {
							columnArray[i] = rsd.getColumnName(i + 1);
						}
                      	System.out.println(Arrays.toString(columnArray));
						while(rs.next()){
							JSONObject o = new JSONObject();
							for (int j = 0; j < columnArray.length; j++) {
								o.put(columnArray[j], rs.getObject(columnArray[j]));
							}
							array.add(o);
						}
					}
				}
				
				
			} catch (Exception e) {
				throw e;
			}
			

			
		} catch (Exception e) {
			throw e;
		}
		return array;
	}

	
	public JSONObject queryOneCustom(String sql, LinkedMap linkedMap) throws Exception {
		JSONObject rtnJson = null;
		DataSource ds = getJdbcTemplate().getDataSource();
		try {
			try (Connection conn  =ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			){
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

				try (ResultSet rs = pstmt.executeQuery()){
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
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return rtnJson;
	}
	
		public List<Object> queryAllCustom(String sql,LinkedMap linkedMap) throws Exception{
			List<Object> list = null;
			DataSource ds = getJdbcTemplate().getDataSource();
			try {
				try (Connection conn  =ds.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				){
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
			  
				
					try (ResultSet rs = pstmt.executeQuery();){
						//得到ResultSet
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
					}
					}
			} catch (Exception e) {
				throw e;
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
			
			try {
				try (Connection conn  =ds.getConnection();
						PreparedStatement pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				){
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
					try (PageableResultSet rs=new PageableResultSet(pstmt.executeQuery());){////得到ResultSet
						ResultSetMetaData rsd = rs.getMetaData();
						if(rsd.getColumnCount() > 0){
							list = new ArrayList<Object>();
							String[] columnArray = new String[rsd.getColumnCount()];
							for(int i = 0; i < rsd.getColumnCount(); i++) {  
						        columnArray[i] = rsd.getColumnName(i + 1);
						    }
//							if(null == pageSize || (null != pageSize && pageSize.trim().length() == 0)){
//								pageSize = Common.PAGE_SIZE;
//							}
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
					}
					
				}
				
			} catch (Exception e) {
				throw e;
			} 
			return page;
		}

	

}
