package com.summit.repository;

import com.summit.domain.user.UserDaoRowMapper;
import com.summit.util.Page;
import com.summit.util.SysConstants;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yt
 *
 */
@Repository
public class UserRepository extends JdbcDaoSupport {
	
	@Autowired
    @Qualifier("masterJdbcTemplate")
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public UserRepository(@Autowired @Qualifier("masterDataSource")DataSource dataSource) {
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


	public Page<JSONObject> queryByCustomPage(String sql, int start,
			int pageSize, Object... args) {
		return queryByCustomPage(sql, userDaoRowMapper, start, pageSize, args);
	}

	
	public <T> Page<T> queryByCustomPage(String sql, RowMapper<T> rowMapper,
			int start, int pageSize, Object... args) {
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
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return p;
	}

	public int excuteBatch(List list) {
		if(list !=null && list.size() >0){
			DataSource ds = getDataSource();
			Connection conn = null;
			Statement stmt = null;
			int[] result = null;
			try {
				conn = ds.getConnection();
				 stmt =conn.createStatement();  
				conn.setAutoCommit(false);//取消自动提交
				for(int i =0;i<list.size();i++){
					stmt.addBatch(list.get(i).toString());
				}
				result =stmt.executeBatch();
				conn.commit();  
			} catch (Exception e) {
				try {
					conn.rollback();
					e.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			} finally {
				try {
					try {
						conn.rollback();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					if (stmt != null) {
						try {
							stmt.close();
							if (conn != null) {
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
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result.length;
		}
		return 0;
	
		
	}
}
