package com.summit.service.role;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RoleBean;
import com.summit.common.entity.AntdJsonBean;
import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.LogBean;
import com.summit.repository.UserRepository;
import com.summit.cbb.utils.page.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ResponseCodeEnum add(RoleBean rb) {
        String sql = "SELECT * FROM SYS_ROLE WHERE NAME = ? ";
        List<JSONObject> l = ur.queryAllCustom(sql, rb.getName());
        if (st.collectionNotNull(l)) {
            return ResponseCodeEnum.CODE_9992;
        }
        sql = "INSERT INTO SYS_ROLE (CODE,NAME,NOTE) VALUES ( ?, ?, ?)";
        jdbcTemplate.update(sql, "ROLE_" + System.currentTimeMillis(), rb.getName(), rb.getNote());


        return null;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseCodeEnum del(String codes) throws Exception {
        codes = codes.replaceAll(",", "','");
        String querySql = "SELECT count(0) FROM sys_user_role WHERE ROLE_CODE in('" + codes + "') and  ROLE_CODE <> '" + SysConstants.SUROLE_CODE + "'";
        LinkedMap linkedMap = new LinkedMap();
        // linkedMap.put(1,"'"+codes+"'");
        int countRow = ur.getRowCount(querySql, linkedMap);
        if (countRow > 0) {
            throw new Exception("请先解除用户角色授权关系");
        }

        String sql = "DELETE FROM SYS_ROLE WHERE CODE IN ('" + codes + "') and  code <> '" + SysConstants.SUROLE_CODE + "'";
        jdbcTemplate.update(sql);

        sql = "DELETE FROM SYS_USER_ROLE WHERE ROLE_CODE IN ('" + codes + "') and ROLE_CODE <> '" + SysConstants.SUROLE_CODE + "'";
        jdbcTemplate.update(sql);

        delRoleAuthorizationByRoleCode(codes);
        return null;
    }

    public ResponseCodeEnum edit(RoleBean rb) throws Exception {
        String sql = "SELECT * FROM SYS_ROLE WHERE NAME = ?  and code <> ?";
        LinkedMap linkedMap = new LinkedMap();
        linkedMap.put(1, rb.getName());
        linkedMap.put(2, rb.getCode());
        List<Object> l = ur.queryAllCustom(sql, linkedMap);
        if (st.collectionNotNull(l)) {
            return ResponseCodeEnum.CODE_9992;
        }
        String updateSql = "UPDATE SYS_ROLE SET NOTE = ?,NAME=? WHERE CODE = ? and  code <> '" + SysConstants.SUROLE_CODE + "' ";
        jdbcTemplate.update(updateSql, rb.getNote(), rb.getName(), rb.getCode());
        return null;
    }

    public RoleBean queryByCode(String code) throws Exception {
        String sql = "SELECT * FROM SYS_ROLE WHERE CODE = ? and  code <> '" + SysConstants.SUROLE_CODE + "'";
        LinkedMap linkedMap = new LinkedMap();
        linkedMap.put(1, code);
        List roleList = ur.queryAllCustom(sql, linkedMap);
        if (roleList != null && roleList.size() > 0) {
            RoleBean roleBean = JSON.parseObject(roleList.get(0).toString(), new TypeReference<RoleBean>() {
            });
            return roleBean;
        }
        return null;
    }

    public Page<RoleBean> queryByPage(int start, int limit, String name) throws Exception {
        LinkedMap linkedMap = null;
        StringBuilder sb = new StringBuilder("SELECT * FROM SYS_ROLE WHERE 1 = 1 and code <> '");
        sb.append(SysConstants.SUROLE_CODE);
        sb.append("'");
        if (st.stringNotNull(name)) {
            sb.append(" AND NAME LIKE ? ");
            linkedMap = new LinkedMap();
            linkedMap.put(1, "%" + name + "%");
        }
        Page<Object> rs = ur.queryByCustomPage(sb.toString(), start, limit, linkedMap);
        if (rs != null) {
            ArrayList<RoleBean> roles = JSON.parseObject(rs.getContent().toString(), new TypeReference<ArrayList<RoleBean>>() {
            });
            return new Page<RoleBean>(roles, rs.getPageable());
            // pageRoleBeanInfo.setContent(students);
            ///pageRoleBeanInfo.setTotalElements(rs.getTotalElements());
            //return pageRoleBeanInfo;
            // return new PageImpl(roles,rs.getPageable(),rs.getTotalElements());
        }
        return null;
    }

    public List<RoleBean> queryAll() throws Exception {
        String sql = "SELECT * FROM SYS_ROLE where code <> '" + SysConstants.SUROLE_CODE + "'";

        List roleList = ur.queryAllCustom(sql, new LinkedMap());
        if (roleList != null && roleList.size() > 0) {
            ArrayList<RoleBean> roleBeanList = JSON.parseObject(roleList.toString(), new TypeReference<ArrayList<RoleBean>>() {
            });
            return roleBeanList;
        }
        return null;
    }

    public List<AntdJsonBean> queryRoleAntdJsonAll() throws Exception {
        String sql = "SELECT  code  as 'key',code as 'value',name as title FROM SYS_ROLE where code <> '" + SysConstants.SUROLE_CODE + "'";
        JSONArray listRole = ur.queryAllCustomJsonArray(sql, null);
        if (listRole != null && listRole.size() > 0) {
            ArrayList<AntdJsonBean> students = JSON.parseObject(listRole.toString(), new TypeReference<ArrayList<AntdJsonBean>>() {
            });
            return students;
        }
        return null;
    }

    public List<FunctionBean> queryFunIdByRoleCode(String roleCode) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT fun.* FROM SYS_ROLE_FUNCTION roleFun ");
        sql.append(" inner join sys_function fun on roleFun.FUNCTION_ID=fun.ID WHERE ROLE_CODE = ?  ");
        LinkedMap linkedMap = new LinkedMap();
        linkedMap.put(1, roleCode);
        List functionList = ur.queryAllCustom(sql.toString(), linkedMap);
        if (functionList != null && functionList.size() > 0) {
            ArrayList<FunctionBean> students = JSON.parseObject(functionList.toString(), new TypeReference<ArrayList<FunctionBean>>() {
            });
            return students;
        }
        return null;
    }

    public void delRoleAuthorizationByRoleCode(String roleCodes) {
        String sql = "DELETE FROM SYS_ROLE_FUNCTION WHERE ROLE_CODE IN ('" + roleCodes + "') and ROLE_CODE <> '" + SysConstants.SUROLE_CODE + "'";
        jdbcTemplate.update(sql);
    }

    public void roleAuthorization(String roleCode, String funIds) {
        delRoleAuthorizationByRoleCode(roleCode);
        String sql = "INSERT INTO SYS_ROLE_FUNCTION (ID, ROLE_CODE, FUNCTION_ID) VALUES (?, ?, ?) ";
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        String[] funIdArr = funIds.split(",");
        for (String funId : funIdArr) {
            batchArgs.add(new Object[]{SummitTools.getKey(), roleCode, funId});
        }
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
