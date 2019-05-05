package com.summit.service.role;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RoleBean;
import com.summit.common.entity.AntdJsonBean;
import com.summit.domain.role.RoleBeanRowMapper;
import com.summit.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.summit.util.SummitTools;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



@Service
@Transactional
public class RoleService {
	@Autowired
	private UserRepository ur;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private SummitTools st;
	@Autowired
	private RoleBeanRowMapper rbrm;

	public ResponseCodeEnum add(RoleBean rb) {
		String sql = "SELECT * FROM SYS_ROLE WHERE NAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, rb.getName());
		if (st.collectionNotNull(l)) {
			return ResponseCodeEnum.CODE_9992;
		}
		sql = "INSERT INTO SYS_ROLE (CODE,NAME,NOTE) VALUES ( ?, ?, ?)";
		jdbcTemplate.update(sql, "ROLE_" + System.currentTimeMillis(), rb.getName(), rb.getNote());
		return null;
	}
	
	@Transactional
	public void del(String codes) {
		codes = codes.replaceAll(",", "','");
		String sql = "DELETE FROM SYS_ROLE WHERE CODE IN ('" + codes + "')";
		jdbcTemplate.update(sql);

		sql = "DELETE FROM SYS_USER_ROLE WHERE ROLE_CODE IN ('" + codes + "')";
		jdbcTemplate.update(sql);
		
		delRoleAuthorizationByRoleCode(codes);
	}

	public void edit(RoleBean rb) {
		String sql = "UPDATE SYS_ROLE SET NOTE = ?,NAME=? WHERE CODE = ?";
		jdbcTemplate.update(sql, rb.getNote(),rb.getName(), rb.getCode());
	}

	public RoleBean queryByCode(String code) {
		String sql = "SELECT * FROM SYS_ROLE WHERE CODE = ?";
		List<RoleBean> l = ur.queryAllCustom(sql, rbrm, code);
		return  l.get(0);
	}

	public Page<RoleBean> queryByPage(int start, int limit, String name) throws Exception {
		LinkedMap linkedMap=null;
		StringBuilder sb = new StringBuilder(
				"SELECT * FROM SYS_ROLE WHERE 1 = 1");
		if (st.stringNotNull(name)) {
			sb.append(" AND NAME LIKE ? ");
			linkedMap=new LinkedMap();
		    linkedMap.put(1,"%" + name + "%");
		}
		Page<Object> rs = ur.queryByCustomPage(sb.toString(), start, limit,linkedMap);
		if(rs!=null){
			 ArrayList<RoleBean> roles = JSON.parseObject(rs.getContent().toString(), new TypeReference<ArrayList<RoleBean>>() {});
			// pageRoleBeanInfo.setContent(students);
			 ///pageRoleBeanInfo.setTotalElements(rs.getTotalElements());
			 //return pageRoleBeanInfo;
			 return new PageImpl(roles,rs.getPageable(),rs.getTotalElements());
		}
		return null;
	}

	public List<RoleBean> queryAll() {
		String sql = "SELECT * FROM SYS_ROLE";
		return ur.queryAllCustom(sql, rbrm);
	}
	
	public List<AntdJsonBean> queryRoleAntdJsonAll() throws Exception {
		String sql = "SELECT  code  as 'key',code as 'value',name as title FROM SYS_ROLE";
		JSONArray listRole= ur.queryAllCustomJsonArray(sql,null);
		if(listRole!=null && listRole.size()>0){
			 ArrayList<AntdJsonBean> students = JSON.parseObject(listRole.toString(), new TypeReference<ArrayList<AntdJsonBean>>() {});
		     return students;
		}
		return null;
	}

	public List<String> queryFunIdByRoleCode(String roleCode) {
		String sql = "SELECT FUNCTION_ID FROM SYS_ROLE_FUNCTION WHERE ROLE_CODE = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, roleCode);
		List<String> list = new ArrayList<String>();
		for (JSONObject o : l) {
			list.add(st.objJsonGetString(o, "FUNCTION_ID"));
		}
		return list;
	}
	
	public void delRoleAuthorizationByRoleCode(String roleCodes){
		String sql = "DELETE FROM SYS_ROLE_FUNCTION WHERE ROLE_CODE IN ('" + roleCodes + "')";
		jdbcTemplate.update(sql);
	}
	
	public void roleAuthorization(String roleCode, String funIds) {
		delRoleAuthorizationByRoleCode(roleCode);
		String sql = "INSERT INTO SYS_ROLE_FUNCTION (ID, ROLE_CODE, FUNCTION_ID) VALUES (?, ?, ?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		String[] funIdArr = funIds.split(",");
		for (String funId : funIdArr) {
			batchArgs.add(new Object[] { SummitTools.getKey(), roleCode, funId });
		}
		jdbcTemplate.batchUpdate(sql, batchArgs);
	}
}
