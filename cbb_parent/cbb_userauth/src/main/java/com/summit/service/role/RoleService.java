package com.summit.service.role;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.domain.role.RoleBean;
import com.summit.domain.role.RoleBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

	public ResponseCodeBySummit add(RoleBean rb) {
		String sql = "SELECT * FROM SYS_ROLE WHERE NAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, rb.getName());
		if (st.collectionNotNull(l)) {
			return ResponseCodeBySummit.CODE_4033;
		}
		sql = "INSERT INTO SYS_ROLE (CODE,NAME,NOTE) VALUES ( ?, ?, ?)";
		jdbcTemplate.update(sql, "ROLE_" + System.currentTimeMillis(), rb.getName(), rb.getNote());
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit del(String codes) {
		codes = codes.replaceAll(",", "','");
		String sql = "DELETE FROM SYS_ROLE WHERE CODE IN ('" + codes + "')";
		jdbcTemplate.update(sql);

		sql = "DELETE FROM SYS_USER_ROLE WHERE ROLE_CODE IN ('" + codes + "')";
		jdbcTemplate.update(sql);
		
		delRoleAuthorizationByRoleCode(codes);
		return ResponseCodeBySummit.CODE_0000;
	}

	public ResponseCodeBySummit edit(RoleBean rb) {
		String sql = "UPDATE SYS_ROLE SET NOTE = ?,NAME=? WHERE CODE = ?";
		jdbcTemplate.update(sql, rb.getNote(),rb.getName(), rb.getCode());
		return ResponseCodeBySummit.CODE_0000;
	}

	public RoleBean queryByCode(String code) {
		String sql = "SELECT * FROM SYS_ROLE WHERE CODE = ?";
		List<RoleBean> l = ur.queryAllCustom(sql, rbrm, code);
		return  l.get(0);
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
		jdbcTemplate.update(sql);
	}
	
	public ResponseCodeBySummit roleAuthorization(String roleCode, String funIds) {
		delRoleAuthorizationByRoleCode(roleCode);
		String sql = "INSERT INTO SYS_ROLE_FUNCTION (ID, ROLE_CODE, FUNCTION_ID) VALUES (?, ?, ?)";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		String[] funIdArr = funIds.split(",");
		for (String funId : funIdArr) {
			batchArgs.add(new Object[] { st.getKey(), roleCode, funId });
		}
		jdbcTemplate.batchUpdate(sql, batchArgs);
		return ResponseCodeBySummit.CODE_0000;
	}
}
