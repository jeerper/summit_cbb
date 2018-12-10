package com.summit.service.function;

import com.summit.domain.function.FunctionBean;
import com.summit.domain.function.FunctionBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import com.summit.util.TreeNode;
import com.summit.util.UserContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FunctionService {
	@Autowired
	private UserRepository ur;
	@Autowired
	private SummitTools st;
	@Autowired
	private FunctionBeanRowMapper fbrm;

	public Map<String, Object> add(FunctionBean fb) {
		String sql = "INSERT INTO [SYS_FUNCTION] ([ID], [PID], [NAME], [FDESC], [IS_ENABLED], [FURL], [IMGULR], [NOTE], [SUPER_FUN]) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
		ur.jdbcTemplate.update(sql, st.getKey(), fb.getPid(), fb.getName(), fb
				.getFdesc(), fb.getIsEnabled(), fb.getFurl(), fb.getImgUlr(),
				fb.getNote(), 0);
		return st.success("");
	}

	public Map<String, Object> del(String ids) {
		ids = ids.replaceAll(",", "','");
		String sql = "SELECT * FROM SYS_FUNCTION WHERE PID IN ('" + ids + "')";
		List<FunctionBean> l = ur.queryAllCustom(sql, fbrm);
		if (st.collectionNotNull(l)) {
			return st.error("不能删除包含子节点的数据");
		}
		sql = "DELETE FROM SYS_FUNCTION WHERE ID IN ('" + ids
				+ "') AND SUPER_FUN <> 1";
		ur.jdbcTemplate.update(sql);

		sql = "DELETE FROM SYS_ROLE_FUNCTION WHERE FUNCTION_ID IN ('" + ids + "')";
		ur.jdbcTemplate.update(sql);
		return st.success("");
	}

	public Map<String, Object> edit(FunctionBean fb) {
		String sql = "UPDATE SYS_FUNCTION SET NAME = ?, FDESC = ?, IS_ENABLED = ?, FURL = ?, IMGULR = ?, NOTE = ? WHERE ID = ?";
		ur.jdbcTemplate.update(sql, fb.getName(), fb.getFdesc(), fb
				.getIsEnabled(), fb.getFurl(), fb.getImgUlr(), fb.getNote(), fb
				.getId());
		return st.success("");
	}

	private boolean isSuperUser() {
		if (st.stringEquals(SysConstants.SUPER_USERNAME, UserContext
				.getCurrentUser().getUserName())) {
			return true;
		}
		return false;
	}

	public Map<String, Object> queryById(String id) {
		String sql;
		if (isSuperUser()) {
			sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ?";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ? AND SUPER_FUN = 0";
		}
		List<FunctionBean> l = ur.queryAllCustom(sql, fbrm, id);
		if (st.collectionIsNull(l)) {
			return st.error("");
		}
		return st.success("", l.get(0));
	}

	public List<FunctionBean> queryAll() {
		String sql;
		if (isSuperUser()) {
			sql = "SELECT * FROM SYS_FUNCTION ORDER BY FDESC";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE SUPER_FUN = 0 ORDER BY FDESC";
		}
		return ur.queryAllCustom(sql, fbrm);
	}

	public Page<JSONObject> queryByPage(int start, int limit, String pId) {
		String sql;
		if (isSuperUser()) {
			sql = "SELECT * FROM SYS_FUNCTION WHERE PID = ? ORDER BY FDESC";
		} else {
			sql = "SELECT * FROM SYS_FUNCTION WHERE PID = ? AND SUPER_FUN = 0 ORDER BY FDESC";
		}
		return ur.queryByCustomPage(sql, start, limit, pId);
	}

	public String getFunByUserName(String userName) {
		List<TreeNode<JSONObject>> tn;
		String sql;
		if (isSuperUser()) {
			sql = "SELECT * FROM SYS_FUNCTION WHERE IS_ENABLED = '1' ORDER BY FDESC";
		} else {
			sql = "SELECT DISTINCT SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ? ORDER BY FDESC";
		}
		if (st.stringEquals(userName, SysConstants.SUPER_USERNAME)) {
			tn = st.creatTreeNode(ur.queryAllCustom(sql, fbrm), null);
		} else {
			tn = st.creatTreeNode(ur.queryAllCustom(sql, fbrm, userName), null);
		}
		return JSONArray.fromObject(tn).toString();
	}

	public Map<String, Collection<ConfigAttribute>> getResourceMap() {
		Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		String sql = "SELECT SRF.ROLE_CODE,SF.FURL FROM SYS_ROLE_FUNCTION SRF INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID)";
		List<JSONObject> l = ur.queryAllCustom(sql);
		String role, url;
		for (JSONObject o : l) {
			url = st.objJsonGetString(o, "FURL");
			if (st.stringIsNull(url)) {
				continue;
			}
			role = st.objJsonGetString(o, "ROLE_CODE");
			if (resourceMap.get(url) == null) {
				resourceMap.put(url, new ArrayList<ConfigAttribute>());
				resourceMap.get(url).add(
						new SecurityConfig(SysConstants.SUROLE_CODE));
			}
			resourceMap.get(url).add(new SecurityConfig(role));
		}
		return resourceMap;
	}
}
