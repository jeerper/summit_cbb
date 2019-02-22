package com.summit.service.dept;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.domain.dept.DeptBean;
import com.summit.domain.dept.DeptBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;

import net.sf.json.JSONObject;

@Service
@Transactional
public class DeptService {
	@Autowired
	private UserRepository ur;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private SummitTools st;
	@Autowired
	private DeptBeanRowMapper atm;
	/**
	 * 
	 * 查询部门树
	 * @return
	 */
	public List<DeptBean> queryDeptTree() {
		String sql = "SELECT ID,PID,DEPTCODE,DEPTNAME,REMARK FROM SYS_DEPT";
		List<DeptBean> all = ur.queryAllCustom(sql,atm);
		List<DeptBean> list = new ArrayList<DeptBean>();
		for (DeptBean deptBean : all) {
			if(deptBean.getPid() == null){
				deptBean.setOpen(true);
			}
			list.add(deptBean);
		}
		return list;
	}
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public DeptBean queryById(String id) {
		String sql = "SELECT ID,PID,DEPTCODE,DEPTNAME,REMARK FROM SYS_DEPT WHERE id = ?";
		List<DeptBean> l = ur.queryAllCustom(sql, atm, id);
		return l.get(0);
	}

	/**
	 * 
	 * 编辑（查询）
	 * @param start
	 * @param limit
	 * @param pId
	 * @return
	 */
	public Page<JSONObject> queryByPage(int start, int limit, String pid) {
		String sql = "SELECT * FROM SYS_DEPT WHERE pid = ?";
		//if("".equals(padcd))padcd ="root";
		Page<JSONObject> rs = ur.queryByCustomPage(sql, start, limit, pid);
		return rs;
	}


	/**
	 * 
	 * 编辑（保存）
	 * @param 
	 * @return
	 */
	public String edit(DeptBean ab) {
		String sql = "UPDATE SYS_DEPT SET  pid = ?, DEPTCODE = ?, DEPTNAME = ?, REMARK = ? where id = ?";
		jdbcTemplate.update(
				sql,
				ab.getPid(),
				ab.getDeptCode(),
				ab.getDeptName(),
				ab.getRemark(),
				ab.getId()
		);
		return "";
	}
	/**
	 * 新增
	 */
	public String add(DeptBean ab) {
		String hasadcd="select * from SYS_DEPT where DEPTCODE='"+ab.getDeptCode()+"'";
		List l=ur.queryAllCustom(hasadcd);
		if(l.size()>0){
			return "部门重复";
		}
		String sql = "INSERT INTO SYS_DEPT (ID, PID, DEPTCODE,DEPTNAME,REMARK) VALUES (?, ? ,?, ?,?)";
		jdbcTemplate.update(
				sql,
				ab.getId(),
				ab.getPid(),
				ab.getDeptCode(),
				ab.getDeptName(),
				ab.getRemark());
		return "";
	}


	/**
	 * 
	 * 
	 * 删除
	 * @param ids
	 * @return
	 */
	public String del(String ids) {
		ids = ids.replaceAll(",", "','");
		String sql = "SELECT * FROM SYS_DEPT WHERE pid IN ('" + ids + "')";
		List<DeptBean> l = ur.queryAllCustom(sql, atm);
		if (st.collectionNotNull(l)) {
			return "不能删除包含子节点的数据";
		}
		sql = "DELETE FROM SYS_DEPT WHERE id IN ('" + ids+ "') ";
		jdbcTemplate.update(sql);
		return "";
	}




}
