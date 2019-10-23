package com.summit.service.function.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.config.UserContext;
import com.summit.dao.QueryDao;
import com.summit.domain.ConditionEnum;
import com.summit.domain.FunctionBean;
import com.summit.domain.FunctionEnum;
import com.summit.domain.OperatorEnum;
import com.summit.domain.RoleFunctionEnum;
import com.summit.domain.UserRoleEnum;
import com.summit.service.function.FunctionService;
import com.summit.service.impl.BaseServiceImpl;
import com.summit.util.SummitTools;
import com.summit.util.TreeNode;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @author yt
 */
@Service
public class FunctionServiceImpl extends BaseServiceImpl implements FunctionService {

    String index = "sys_function";
    String contactIndex = "role_function";

    String userRoleIndex = "user_role";
    @Autowired
    SummitTools st;


    @Autowired
    QueryDao queryDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> add(JSONObject json) {
        String id = json.getString(FunctionEnum.ID.toString());
        JSONArray jsList = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.ID.toString());
        jsonObject.put(ConditionEnum.VALUE.toString(), id);
        jsonObject.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsList.add(jsonObject);
        JSONObject js = null;
        try {
            js = add(index, json, jsList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return st.error("插入失败", "");
        }
        return st.success("插入成功", js);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> del(String ids) {
        JSONArray jsList = new JSONArray();
        //先查询子节点是否包含数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.PID.toString());
        jsonObject.put(ConditionEnum.VALUE.toString(), ids);
        jsonObject.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
        jsList.add(jsonObject);
        JSONArray jsonList = query(index, jsList);
        if (jsonList != null && jsonList.size() > 1) {
            return st.error("不能删除包含子节点数据", jsonList);
        }

        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject deleteCondition = new JSONObject();
            deleteCondition.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.ID.toString());
            deleteCondition.put(ConditionEnum.VALUE.toString(), ids);
            deleteCondition.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
            jsonArray.add(deleteCondition);


            JSONObject deleteCondition1 = new JSONObject();
            deleteCondition1.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.SUPER_FUN.toString());
            deleteCondition1.put(ConditionEnum.VALUE.toString(), 1);
            deleteCondition1.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.NE.toString());
            jsonArray.add(deleteCondition1);
            delete(index, jsonArray);

            jsonArray.removeAll(jsonArray);

            //删除相关索引内的数据
            JSONObject contactCondition = new JSONObject();
            contactCondition.put(ConditionEnum.COLUMNNAME.toString(), RoleFunctionEnum.FUNCTION_ID.toString());
            contactCondition.put(ConditionEnum.VALUE.toString(), ids);
            contactCondition.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
            jsonArray.add(contactCondition);
            delete(contactIndex, jsonArray);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return st.success("删除成功", "");
    }

    @Override
    public Map<String, Object> edit(JSONObject json) {
        // String sql = "UPDATE SYS_FUNCTION SET NAME = ?, FDESC = ?, IS_ENABLED
        // = ?, FURL = ?, IMGULR = ?, NOTE = ? WHERE ID = ?";
        String id = json.getString(FunctionEnum.ID.toString());
        JSONArray jsList = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.ID.toString());
        jsonObject.put(ConditionEnum.VALUE.toString(), id);
        jsonObject.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsList.add(jsonObject);
        JSONArray jsonList = null;
        try {
            jsonList = edit(index, json, jsList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return st.success("更新成功", jsonList);
    }

    private boolean isSuperUser() {
		/*if (st.stringEquals("admin", UserContext.getCurrentUser().getUsername())) {
			return true;
		}*/
        return false;
    }

    @Override
    public Map<String, Object> queryById(String id) {
        JSONArray jsList = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.ID.toString());
        jsonObject.put(ConditionEnum.VALUE.toString(), id);
        jsonObject.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsList.add(jsonObject);
        if (isSuperUser()) {
            // sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ?";
        } else {
            // sql = "SELECT * FROM SYS_FUNCTION WHERE ID = ? AND SUPER_FUN =
            // 0";
            JSONObject js = new JSONObject();
            js.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.SUPER_FUN.toString());
            js.put(ConditionEnum.VALUE.toString(), 0);
            js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            jsList.add(js);
        }
        JSONArray jsonList = query(index, jsList);
        return st.success("查询成功", jsonList);
    }

    @Override
    public Map<String, Object> queryAll() {
        // TODO Auto-generated method stub
        JSONArray jsList = new JSONArray();
        JSONObject sortJs = new JSONObject();
        sortJs.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.FDESC.toString());
        sortJs.put(ConditionEnum.SORT.toString(), "desc");
        jsList.add(sortJs);
        if (isSuperUser()) {
        } else {
            JSONObject js = new JSONObject();
            js.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.SUPER_FUN.toString());
            js.put(ConditionEnum.VALUE.toString(), 0);
            js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            jsList.add(js);
        }
        JSONArray jsonList = query(index, jsList);
        return st.success("查询成功", jsonList);
    }

    @Override
    public Map<String, Object> queryByPage(Integer currentPage, Integer pageSize, Integer willJumpPage, String pId) {
        JSONArray jsList = new JSONArray();
        JSONObject sortJs = new JSONObject();
        sortJs.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.FDESC.toString());
        sortJs.put(ConditionEnum.SORT.toString(), "desc");
        jsList.add(sortJs);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.PID.toString());
        jsonObject.put(ConditionEnum.VALUE.toString(), pId);
        jsonObject.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsList.add(jsonObject);
        if (isSuperUser()) {
        } else {
            JSONObject js = new JSONObject();
            js.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.SUPER_FUN.toString());
            js.put(ConditionEnum.VALUE.toString(), 0);
            js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            jsList.add(js);
        }
        JSONArray jsonList = this.pageQuery(index, jsList, currentPage, pageSize, willJumpPage);
        return st.success("查询成功", jsonList);
    }

    @Override
    public String getFunByUserName(String userName) {
        List<TreeNode<JSONObject>> tn = null;
        // 返回的结果jsonArray
        JSONArray jsonArray = null;
        // 构建条件
        JSONArray jsList = new JSONArray();
        // 排序json
        JSONObject sortJs = new JSONObject();
        sortJs.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.FDESC.toString());
        sortJs.put(ConditionEnum.SORT.toString(), "desc");
        jsList.add(sortJs);
        if (isSuperUser()) {
            JSONObject jsCondition = new JSONObject();
            jsCondition.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.IS_ENABLED.toString());
            jsCondition.put(ConditionEnum.VALUE.toString(), 1);
            jsCondition.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            jsList.add(jsCondition);
            jsonArray = query(index, jsList);
        } else {
            JSONArray childList = new JSONArray();
            JSONObject childJs = new JSONObject();
            childJs.put(ConditionEnum.COLUMNNAME.toString(), UserRoleEnum.USERNAME.toString());
            childJs.put(ConditionEnum.VALUE.toString(), userName);
            childJs.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            childList.add(childJs);
            // 根据username 查询rolecode
            JSONArray userRoleList = this.query(userRoleIndex, childList);
            StringBuffer roleCodeSf = new StringBuffer();
            StringBuffer funIdSf = new StringBuffer();
            JSONArray childReturn = null;
            // 拼装rolecode 字符串
            if (userRoleList != null && userRoleList.size() > 0) {
                for (int i = 0; i < userRoleList.size(); i++) {
                    JSONObject js = userRoleList.getJSONObject(i);
                    if (i == userRoleList.size() - 1) {
                        if (js.containsKey(UserRoleEnum.ROLE_CODE.toString())) {
                            roleCodeSf.append(js.getString(UserRoleEnum.ROLE_CODE.toString()));
                        }
                    } else {
                        if (js.containsKey(UserRoleEnum.ROLE_CODE.toString())) {
                            roleCodeSf.append(js.getString(UserRoleEnum.ROLE_CODE.toString()) + ",");
                        }
                    }

                }
            }
            // 根据封装好的rolecode 查询functionId
            if (roleCodeSf != null && roleCodeSf.length() > 0) {
                JSONArray childList2 = new JSONArray();
                JSONObject js = new JSONObject();
                js.put(ConditionEnum.COLUMNNAME.toString(), UserRoleEnum.ROLE_CODE.toString());
                js.put(ConditionEnum.VALUE.toString(), roleCodeSf.toString());
                js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
                childList2.add(js);
                childReturn = query(contactIndex, childList2);
            }
            if (childReturn != null && childReturn.size() > 0) {
                for (int i = 0; i < childReturn.size(); i++) {
                    JSONObject js = childReturn.getJSONObject(i);
                    if (i == userRoleList.size() - 1) {
                        if (js.containsKey(RoleFunctionEnum.FUNCTION_ID.toString())) {
                            funIdSf.append(js.getString(RoleFunctionEnum.FUNCTION_ID.toString()));
                        }
                    } else {
                        if (js.containsKey(js.containsKey(RoleFunctionEnum.FUNCTION_ID.toString()))) {
                            funIdSf.append(js.getString(RoleFunctionEnum.FUNCTION_ID.toString()) + ",");
                        }
                    }
                }
            }
            // 根据查询的functionId 查询所有满足的结果
            if (funIdSf != null && funIdSf.length() > 0) {
                JSONObject js = new JSONObject();
                js.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.ID.toString());
                js.put(ConditionEnum.VALUE.toString(), funIdSf.toString());
                js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
                JSONObject js1 = new JSONObject();
                js1.put(ConditionEnum.COLUMNNAME.toString(), FunctionEnum.SUPER_FUN.toString());
                js1.put(ConditionEnum.VALUE.toString(), 0);
                js1.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
                jsList.add(js);
                jsList.add(js1);
                jsonArray = query(index, jsList);
            }
        }
        if (jsonArray != null && jsonArray.size() > 0) {
            jsonArray.remove(0);
            List<FunctionBean> jsonList = JSONArray.toList(jsonArray, new FunctionBean(), new JsonConfig());
            String admin = "admin";
            if (st.stringEquals(userName, admin)) {
                tn = st.creatTreeNode(jsonList, null);
            } else {
                tn = st.creatTreeNode(jsonList, null);
            }
        }
        return tn == null ? null : JSONArray.fromObject(tn).toString();
    }

    @Override
    public Map<String, Collection<ConfigAttribute>> getResourceMap() {
        // TODO Auto-generated method stub
        String sql = "SELECT rf.roleCode as roleCode, f.furl as furl  from " + contactIndex + " rf join " + index + " f on rf.functionId = f.id";
        JSONArray jsonArray = queryDao.queryBySql(sql);
        List<JSONObject> jsonList = JSONArray.toList(jsonArray, new JSONObject(), new JsonConfig());
        Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>(jsonList.size());
        String role, url;
        for (JSONObject o : jsonList) {
            url = st.objJsonGetString(o, FunctionEnum.FURL.toString());
            if (st.stringIsNull(url)) {
                continue;
            }
            role = st.objJsonGetString(o, RoleFunctionEnum.ROLE_CODE.toString());
            if (resourceMap.get(url) == null) {
                resourceMap.put(url, new ArrayList<ConfigAttribute>());
                resourceMap.get(url).add(
                        new SecurityConfig("ROLE_SUPERUSER"));
            }
            resourceMap.get(url).add(new SecurityConfig(role));
        }
        return resourceMap;
    }

}
