package com.summit.service.role.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.domain.ConditionEnum;
import com.summit.domain.FunctionEnum;
import com.summit.domain.OperatorEnum;
import com.summit.domain.RoleEnum;
import com.summit.domain.RoleFunctionEnum;
import com.summit.domain.UserEnum;
import com.summit.domain.UserRoleEnum;
import com.summit.service.impl.BaseServiceImpl;
import com.summit.service.role.RoleService;
import com.summit.util.SummitTools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author yt
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {

    String index = "sys_role";
    String contactIndex = "role_function";
    String userRoleIndex = "user_role";
    String functionIndex = "sys_function";
    @Autowired
    SummitTools st;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addRole(JSONObject jsonObject) {

        String name = jsonObject.getString(RoleEnum.NAME.toString());
        JSONArray jsList = new JSONArray();
        JSONObject json = new JSONObject();
        json.put(ConditionEnum.COLUMNNAME.toString(), RoleEnum.NAME.toString());
        json.put(ConditionEnum.VALUE.toString(), name);
        json.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsList.add(json);
        JSONObject js = null;
        try {
            js = add(index, jsonObject, jsList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return st.error("插入失败", "");
        }
        return st.success("插入成功", js);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> editRole(JSONObject jsonObject) {
        String code = jsonObject.getString(RoleEnum.CODE.toString());
        JSONArray jsList = new JSONArray();
        JSONObject json = new JSONObject();
        json.put(ConditionEnum.COLUMNNAME.toString(), RoleEnum.CODE.toString());
        json.put(ConditionEnum.VALUE.toString(), code);
        json.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsList.add(json);
        JSONArray jsonList = null;
        try {
            jsonList = edit(index, jsonObject, jsList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return st.error("修改失败", "");

        }
        return st.success("修改成功", jsonList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> deleteRole(String codes) {
        // TODO Auto-generated method stub
        JSONArray jsList = new JSONArray();
        JSONObject json = new JSONObject();
        json.put(ConditionEnum.COLUMNNAME.toString(), RoleEnum.CODE.toString());
        json.put(ConditionEnum.VALUE.toString(), codes);
        json.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
        jsList.add(json);
        JSONArray jsonList = null;
        try {
            jsonList = delete(index, jsList);
            jsList.removeAll(jsList);
            //修改删除条件
            JSONObject json1 = new JSONObject();
            json1.put(ConditionEnum.COLUMNNAME.toString(), UserRoleEnum.ROLE_CODE.toString());
            json1.put(ConditionEnum.VALUE.toString(), codes);
            json1.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
            jsList.add(json1);
            delete(userRoleIndex, jsList);
            delRoleAuthorizationByRoleCode(codes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return st.error("删除失败", "");
        }

        return st.success("修改成功", jsonList);
    }

    @Override
    public Map<String, Object> queryRoleByCodes(String codes) {
        // TODO Auto-generated method stub
        JSONArray jsList = new JSONArray();
        JSONObject json = new JSONObject();
        json.put(ConditionEnum.COLUMNNAME.toString(), RoleEnum.CODE.toString());
        json.put(ConditionEnum.VALUE.toString(), codes);
        json.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsList.add(json);
        JSONArray jsonList = query(index, jsList);
        return st.success("查询成功", jsonList);
    }

    @Override
    public Map<String, Object> queryByPage(Integer currentPage, Integer pageSize, Integer willJumpPage, JSONObject jsonObject) {
        // TODO Auto-generated method stub
        String name = jsonObject.getString(RoleEnum.NAME.toString());
        JSONArray jsList = new JSONArray();
        JSONObject json = new JSONObject();
        json.put(ConditionEnum.COLUMNNAME.toString(), RoleEnum.NAME.toString());
        json.put(ConditionEnum.VALUE.toString(), name);
        json.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.LIKE.toString());
        jsList.add(json);
        JSONArray jsonList = pageQuery(index, jsList, currentPage, pageSize, willJumpPage);
        return st.success("查询成功", jsonList);
    }

    @Override
    public Map<String, Object> queryAll() {
        // TODO Auto-generated method stub
        JSONArray jsonList = queryAll(index);
        return st.success("查询成功", jsonList);
    }

    @Override
    public List<String> queryFunIdByRoleCode(String roleCode) {
        JSONArray jsList = new JSONArray();
        JSONObject json = new JSONObject();
        json.put(ConditionEnum.COLUMNNAME.toString(), RoleFunctionEnum.ROLE_CODE.toString());
        json.put(ConditionEnum.VALUE.toString(), roleCode);
        json.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsList.add(json);
        JSONArray jsonList = query(contactIndex, jsList);
        List<String> functionIdList = new ArrayList<String>();
        if (jsonList != null && jsonList.size() > 0 && jsonList.size() != 1) {
            for (int i = 0; i < jsonList.size(); i++) {
                if (i != 0) {
                    JSONObject js = jsonList.getJSONObject(i);
                    String functionId = js.getString(RoleFunctionEnum.FUNCTION_ID.toString());
                    functionIdList.add(functionId);
                }
            }
        }
        return functionIdList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> delRoleAuthorizationByRoleCode(String roleCodes) {
        // TODO Auto-generated method stub
        JSONArray jsList = new JSONArray();
        JSONObject json = new JSONObject();
        json.put(ConditionEnum.COLUMNNAME.toString(), RoleFunctionEnum.ROLE_CODE.toString());
        json.put(ConditionEnum.VALUE.toString(), roleCodes);
        json.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
        jsList.add(json);
        JSONArray jsonList = null;
        try {
            jsonList = delete(contactIndex, jsList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return st.error("删除失败", "");
        }
        return st.success("删除成功", jsonList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> roleGrantUser(String roleCode, String userName) {
        Map<String, Object> js = new HashMap<String, Object>(2);
        try {
            //delUserRoleByUserName(userName);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (userName == null || userName.isEmpty()) {
            js.put("success", true);
            js.put("msg", "");
            return js;
        }
        String[] userNameArr = userName.split(",");
        JSONArray jsonList = new JSONArray();
        for (String username : userNameArr) {
            JSONObject jsonObject = new JSONObject();
            String keys = SummitTools.getKey();
            jsonObject.put(UserRoleEnum.ID.toString(), keys);
            jsonObject.put(UserRoleEnum.USERNAME.toString(), username);
            jsonObject.put(UserRoleEnum.ROLE_CODE.toString(), roleCode);
            jsonList.add(jsonObject);
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = saveBatch(userRoleIndex, jsonList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (jsonArray.size() == jsonList.size()) {
            js.put("success", true);
            js.put("msg", jsonArray);
        }
        return js;
    }

    @Transactional(rollbackFor = Exception.class)
    private Map<String, Object> delUserRoleByUserName(String name) throws Exception {

        JSONArray jsonList = new JSONArray();
        JSONObject js = new JSONObject();
        js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
        js.put(ConditionEnum.SORT.toString(), "asc");
        js.put(ConditionEnum.VALUE.toString(), name);
        js.put(ConditionEnum.OPERATOR.toString(), "IN");
        jsonList.add(js);
        JSONArray jsList = this.delete(userRoleIndex, jsonList);

        return st.success("删除用户权限成功", jsList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> roleAuthorization(String roleCode, String funIds) {
        // TODO Auto-generated method stub
        delRoleAuthorizationByRoleCode(roleCode);
        if (st.stringIsNull(funIds)) {
            return st.success("id为空", "");
        }
        //插入json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RoleFunctionEnum.ID.toString(), SummitTools.getKey());
        jsonObject.put(RoleFunctionEnum.ROLE_CODE.toString(), roleCode);
        jsonObject.put(RoleFunctionEnum.FUNCTION_ID.toString(), funIds);
        //查询条件jsList
        JSONArray jsList = new JSONArray();
        JSONObject jsCondition = new JSONObject();
        jsCondition.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.ID.toString());
        jsCondition.put(ConditionEnum.VALUE.toString(), funIds);
        jsCondition.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
        jsList.add(jsCondition);

        JSONArray jsList1 = new JSONArray();
        JSONObject jsCondition1 = new JSONObject();
        jsCondition1.put(ConditionEnum.COLUMNNAME.toString(), RoleEnum.CODE.toString());
        jsCondition1.put(ConditionEnum.VALUE.toString(), roleCode);
        jsCondition1.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
        jsList1.add(jsCondition1);
        JSONArray jsonList1 = query(index, jsList1);
        JSONObject returnJs = null;
        JSONArray jsonList = query(functionIndex, jsList);
        if (jsonList != null && jsonList.size() > 1 && jsonList1 != null && jsonList1.size() > 1) {
            try {
                jsList.removeAll(jsList);
                JSONObject jsCondition2 = new JSONObject();
                jsCondition2.put(ConditionEnum.COLUMNNAME.toString(), RoleFunctionEnum.FUNCTION_ID.toString());
                jsCondition2.put(ConditionEnum.VALUE.toString(), funIds);
                jsCondition2.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
                jsList.add(jsCondition2);
                returnJs = add(contactIndex, jsonObject, jsList);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return st.error("更新失败", "");
            }
        } else {
            return st.error("不存在对应的权限或功能", "");
        }
        return st.success("更新成功", returnJs);
    }


}
