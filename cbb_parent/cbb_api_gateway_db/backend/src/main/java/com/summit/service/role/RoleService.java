package com.summit.service.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.domain.role.RoleBean;
import com.summit.domain.role.RoleBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;

import net.sf.json.JSONObject;


@Service
@Transactional
public class RoleService {
	@Autowired
	private UserRepository ur;
	@Autowired
	private SummitTools st;
	@Autowired
	private RoleBeanRowMapper rbrm;

	public Map<String, Object> add(RoleBean rb) {
		String sql = "SELECT * FROM SYS_ROLE WHERE NAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, rb.getName());
		if (st.collectionNotNull(l)) {
			return st.error("角色名" + rb.getName() + "已存在！");
		}
		sql = "INSERT INTO [SYS_ROLE] (CODE,NAME,NOTE) VALUES ( ?, ?, ?)";
		ur.jdbcTemplate.update(sql, "ROLE_" + System.currentTimeMillis(), rb
				.getName(), rb.getNote());
		return st.success("");
	}

	public Map<String, Object> del(String codes) {
		codes = codes.replaceAll(",", "','");
		String sql = "DELETE FROM SYS_ROLE WHERE CODE IN ('" + codes + "')";
		ur.jdbcTemplate.update(sql);

		sql = "DELETE FROM SYS_USER_ROLE WHERE ROLE_CODE IN ('" + codes + "')";
		ur.jdbcTemplate.update(sql);
		
		delRoleAuthorizationByRoleCode(codes);
		return st.success("");
	}

	public Map<String, Object> edit(RoleBean rb) {
		String sql = "UPDATE SYS_ROLE SET NOTE = ? WHERE CODE = ?";
		ur.jdbcTemplate.update(sql, rb.getNote(), rb.getCode());
		return st.success("");
	}

	public Map<String, Object> queryByCode(String code) {
		String sql = "SELECT * FROM SYS_ROLE WHERE CODE = ?";
		List<RoleBean> l = ur.queryAllCustom(sql, rbrm, code);
		if (st.collectionNotNull(l)) {
			return st.success("", l.get(0));
		}
		return st.error("");
	}

	public Page<JSONObject> queryByPage(int start, int limit, RoleBean rb) {
		StringBuilder sb = new StringBuilder(
				"SELECT * FROM SYS_ROLE WHERE 1 = 1");
		if (st.stringNotNull(rb.getName())) {
			sb.append(" AND NAME LIKE '%").append(rb.getName()).append("%'");
		}
		return ur.queryByCustomPage(sb.toString(), start, limit);
	}

	public Page<JSONObject> queryAll() {
		String sql = "SELECT * FROM SYS_ROLE";
		List<JSONObject> l = ur.queryAllCustom(sql);
		return new Page<JSONObject>(l, l.size());
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
		ur.jdbcTemplate.update(sql);
	}
	
	public Map<String, Object> roleAuthorization(String roleCode, String funIds) {
		delRoleAuthorizationByRoleCode(roleCode);
		if (st.stringIsNull(funIds)) {
			return st.success("");
		}
		String sql = "INSERT INTO [SYS_ROLE_FUNCTION] ([ID], [ROLE_CODE], [FUNCTION_ID]) VALUES (?, ?, ?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		String[] funIdArr = funIds.split(",");
		for (String funId : funIdArr) {
			batchArgs.add(new Object[] { st.getKey(), roleCode, funId });
		}
		ur.jdbcTemplate.batchUpdate(sql, batchArgs);
		return st.success("");
	}
}
