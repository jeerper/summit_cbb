package com.summit.service.user.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.config.UserContext;
import com.summit.domain.ConditionEnum;
import com.summit.domain.OperatorEnum;
import com.summit.domain.UserEnum;
import com.summit.domain.UserRoleEnum;
import com.summit.service.impl.BaseServiceImpl;
import com.summit.service.user.UserService;
import com.summit.util.SummitTools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author yt
 */
@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {

    String index = "sys_user";
    String contactIndex = "user_role";

    @Autowired
    SummitTools st;

    @Override
    public Map<String, Object> addUser(JSONObject jsonObject) {
        JSONObject jt = null;
        if (jsonObject != null && jsonObject.size() > 0) {
            //isEnabled,lastUpdateTime,state

            jsonObject.put(UserEnum.IS_ENABLED.toString(), 1);
            jsonObject.put(UserEnum.STATE.toString(), 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
            Date date = new Date();
            String updateTime = sdf.format(date);
            jsonObject.put(UserEnum.LAST_UPDATE_TIME.toString(), updateTime);
            String userName = jsonObject.getString(UserEnum.USERNAME.toString());
            JSONArray jsList = new JSONArray();
            JSONObject js = new JSONObject();
            js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
            js.put(ConditionEnum.SORT.toString(), "asc");
            js.put(ConditionEnum.VALUE.toString(), userName);
            js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            jsList.add(js);
            try {
                jt = this.add(index, jsonObject, jsList);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return st.success("新增成功", jt);

    }

    @Override
    public Map<String, Object> editUser(JSONObject jsonObject) {
        JSONArray jt = null;
        if (jsonObject != null && jsonObject.size() > 0) {
            String username = jsonObject.getString(UserEnum.USERNAME.toString());
            JSONArray jsList = new JSONArray();
            JSONObject js = new JSONObject();
            js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
            js.put(ConditionEnum.SORT.toString(), "asc");
            js.put(ConditionEnum.VALUE.toString(), username);
            js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            JSONObject js1 = new JSONObject();
            js1.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.STATE.toString());
            js1.put(ConditionEnum.SORT.toString(), "asc");
            js1.put(ConditionEnum.VALUE.toString(), 1);
            js1.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            jsList.add(js);
            jsList.add(js1);
            try {
                jt = this.edit(index, jsonObject, jsList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return st.success("修改成功", jt);
    }

    @Override
    public Map<String, Object> deleteUser(String userName) {
        // TODO Auto-generated method stub
        JSONArray jsonArray = null;
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonList = new JSONArray();
        JSONArray jsList = new JSONArray();
        // 构建查询条件
        JSONObject js = new JSONObject();
        js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
        js.put(ConditionEnum.SORT.toString(), "asc");
        js.put(ConditionEnum.VALUE.toString(), userName);
        js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.IN.toString());
        jsList.add(js);

        // 修改条件
        jsonObject.put(UserEnum.STATE.toString(), 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        Date date = new Date();
        String lastUpdateTime = sdf.format(date);
        jsonObject.put(UserEnum.LAST_UPDATE_TIME.toString(), lastUpdateTime);
        try {
            // 修改sys_user 表的状态
            this.edit(index, jsonObject, jsList);
            jsonList.add(jsonObject);
            // 删除SYS_USER_ROLE
            jsonArray = this.delete(contactIndex, jsList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            st.success("删除失败", "");

        }
        return st.success("删除成功", jsonArray);
    }

    @Override
    public Map<String, Object> queryUser(String userName) {
        // TODO Auto-generated method stub
        JSONArray jsonList = new JSONArray();
        JSONObject js = new JSONObject();
        js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
        js.put(ConditionEnum.SORT.toString(), "asc");
        js.put(ConditionEnum.VALUE.toString(), userName);
        js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        JSONObject js1 = new JSONObject();
        js1.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.STATE.toString());
        //js1.put(ConditionEnum.SORT.toString(), "asc");
        js1.put(ConditionEnum.VALUE.toString(), 1);
        js1.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsonList.add(js);
        jsonList.add(js1);
        JSONArray jsonArray = this.query(index, jsonList);
        if (jsonArray != null && jsonArray.size() > 1) {
            //因为第一个是count 数  不需要进行循环遍历
            for (int i = 1; i < jsonArray.size(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                json.remove(UserEnum.PASSWORD.toString());
                json.remove(UserEnum.STATE.toString());
                json.remove(UserEnum.IS_ENABLED.toString());
            }
        }
        return st.success("查询成功", jsonArray);
    }


    @Override
    public Map<String, Object> queryUserByLogin(String userName) {
        // TODO Auto-generated method stub
        JSONArray jsonList = new JSONArray();
        JSONObject js = new JSONObject();
        js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
        js.put(ConditionEnum.SORT.toString(), "asc");
        js.put(ConditionEnum.VALUE.toString(), userName);
        js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        JSONObject js1 = new JSONObject();
        js1.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.STATE.toString());
        //js1.put(ConditionEnum.SORT.toString(), "asc");
        js1.put(ConditionEnum.VALUE.toString(), 1);
        js1.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsonList.add(js);
        jsonList.add(js1);
        JSONArray jsonArray = this.query(index, jsonList);
	/*	if(jsonArray != null && jsonArray.size() > 1){
			//因为第一个是count 数  不需要进行循环遍历
			for(int i = 1 ; i < jsonArray.size() ; i++){
				JSONObject  json = jsonArray.getJSONObject(i);
				json.remove(UserEnum.PASSWORD.toString());
				json.remove(UserEnum.STATE.toString());
				json.remove(UserEnum.IS_ENABLED.toString());
			}
		}*/
        return st.success("查询成功", jsonArray);
    }


    @Override
    public Map<String, Object> queryPageUser(Integer currentPage, Integer pageSize, Integer willPage, JSONObject jsonObject) {
        JSONArray jsonList = new JSONArray();
        if (jsonObject != null && jsonObject.size() > 0) {
            if (jsonObject.containsKey(UserEnum.NAME.toString())) {
                String name = jsonObject.getString(UserEnum.NAME.toString());
                JSONObject js = new JSONObject();
                js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.NAME.toString());
                js.put(ConditionEnum.SORT.toString(), "asc");
                js.put(ConditionEnum.VALUE.toString(), name);
                js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.LIKE.toString());
                jsonList.add(js);
            }
            if (jsonObject.containsKey(UserEnum.USERNAME.toString())) {
                String username = jsonObject.getString(UserEnum.USERNAME.toString());
                JSONObject js1 = new JSONObject();
                js1.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
                js1.put(ConditionEnum.SORT.toString(), "asc");
                js1.put(ConditionEnum.VALUE.toString(), username);
                js1.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.LIKE.toString());
                jsonList.add(js1);
            }
            if (jsonObject.containsKey(UserEnum.STATE.toString())) {
                Integer state = jsonObject.getInt(UserEnum.STATE.toString());
                JSONObject js2 = new JSONObject();
                js2.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.STATE.toString());
                js2.put(ConditionEnum.VALUE.toString(), state);
                js2.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
                jsonList.add(js2);
            }
            if (jsonObject.containsKey(UserEnum.STATE.toString())) {
                Integer isEnabled = jsonObject.getInt(UserEnum.IS_ENABLED.toString());
                JSONObject js3 = new JSONObject();
                js3.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.IS_ENABLED.toString());
                js3.put(ConditionEnum.VALUE.toString(), isEnabled);
                js3.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
                jsonList.add(js3);
            }
        } else {
            Integer isEnabled = 1;
            Integer state = 1;
            JSONObject js2 = new JSONObject();
            js2.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.STATE.toString());
            js2.put(ConditionEnum.VALUE.toString(), state);
            js2.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            jsonList.add(js2);
            JSONObject js3 = new JSONObject();
            js3.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.IS_ENABLED.toString());
            js3.put(ConditionEnum.VALUE.toString(), isEnabled);
            js3.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
            jsonList.add(js3);
        }
        JSONArray jsonArray = this.pageQuery(index, jsonList, currentPage, pageSize, willPage);
        if (jsonArray != null && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jt = jsonArray.getJSONObject(i);
                jt.remove(UserEnum.PASSWORD.toString());
            }
        }

        return st.success("查询成功", jsonArray);
    }

    @Override
    public Map<String, Object> queryRoleByUserName(String userName) {
        JSONArray jsonArray = null;
        JSONArray jsonList = new JSONArray();
        JSONObject js = new JSONObject();
        js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
        js.put(ConditionEnum.SORT.toString(), "asc");
        js.put(ConditionEnum.VALUE.toString(), userName);
        js.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsonList.add(js);
        List<String> roleList = null;
        jsonArray = this.query(contactIndex, jsonList);
        if (null != jsonArray && jsonArray.size() > 0) {
            roleList = new ArrayList<String>();
            for (int i = 0; i < jsonArray.size(); i++) {
                if (i != 0) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String role = jsonObject.getString(UserRoleEnum.ROLE_CODE.toString());
                    roleList.add(role);
                }

            }
        }
        return st.success("查询成功", roleList);
    }


    @Override
    public Map<String, Object> editPwdUser(String oldPassword, String password, String repeatPassword) {
        // TODO Auto-generated method stub
        if (password.equalsIgnoreCase(repeatPassword)) {
            Map<String, Object> json = this.queryUser(UserContext.getUsername());
            JSONArray jsonList = null;
            String data = "data";
            if (json != null && json.containsKey(data)) {
                jsonList = (JSONArray) json.get(data);
            }
            if (null != jsonList && jsonList.size() > 1) {
                JSONObject js = jsonList.getJSONObject(1);
                password = new BCryptPasswordEncoder().encode(password.trim());
                //oldPassword =  new BCryptPasswordEncoder().encode(oldPassword.trim());
                if (new BCryptPasswordEncoder().matches(oldPassword, js.getString(UserEnum.PASSWORD.toString()))) {
                    JSONObject jsonObject = new JSONObject();
                    // 修改内容
                    jsonObject.put(UserEnum.PASSWORD.toString(), password);
			/*		jsonObject.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.password.toString());
					jsonObject.put(ConditionEnum.SORT.toString(), "asc");
					jsonObject.put(ConditionEnum.VALUE.toString(), password);
					jsonObject.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());*/
                    // 查询条件
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsCondition = new JSONObject();
                    jsCondition.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
                    jsCondition.put(ConditionEnum.SORT.toString(), "asc");
                    jsCondition.put(ConditionEnum.VALUE.toString(), UserContext.getUsername());
                    jsCondition.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
                    jsonArray.add(jsCondition);
                    try {
                        this.edit(index, jsonObject, jsonArray);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    //System.out.println("密码错误，请重新输入");
                    return st.error("密码错误，请重新输入", "");
                }
            }
        } else {
            //System.out.println("两次输入的密码不相同");
            return st.error("两次输入的密码不相同", "");
        }
        return st.success("修改成功", "");
    }

    @Override
    public Map<String, Object> resetPassword(String userName) {
        // 查询条件
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode("888888");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsCondition = new JSONObject();
        jsCondition.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
        jsCondition.put(ConditionEnum.SORT.toString(), "asc");
        jsCondition.put(ConditionEnum.VALUE.toString(), userName);
        jsCondition.put(ConditionEnum.OPERATOR.toString(), OperatorEnum.EQ.toString());
        jsonArray.add(jsCondition);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(UserEnum.PASSWORD.toString(), encodePasswd);
        try {
            this.edit(index, jsonObject, jsonArray);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return st.error("重置失败", "");
        }
        return st.success("重置成功", "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> delUserRoleByUserName(String name) throws Exception {

        JSONArray jsonList = new JSONArray();
        JSONObject js = new JSONObject();
        js.put(ConditionEnum.COLUMNNAME.toString(), UserEnum.USERNAME.toString());
        js.put(ConditionEnum.SORT.toString(), "asc");
        js.put(ConditionEnum.VALUE.toString(), name);
        js.put(ConditionEnum.OPERATOR.toString(), "IN");
        jsonList.add(js);
        JSONArray jsList = this.delete(contactIndex, jsonList);

        return st.success("删除用户权限成功", jsList);
    }

    @Override
    public Collection<GrantedAuthority> getGrantedAuthoritiesByUserName(String userName) {
        Map<String, Object> json = this.queryRoleByUserName(userName);
        List<String> roleList = null;
        String data = "data";
        if (json != null && json.containsKey(data)) {
            roleList = (List<String>) json.get(data);
        } else {
            return null;
        }

        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        for (String role : roleList) {
            result.add(new SimpleGrantedAuthority(role));
        }
        result.add(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        return result;
    }

    @Override
    public Map<String, Object> grantRole(String userName, String role) throws Exception {
        delUserRoleByUserName(userName);
        Map<String, Object> js = new HashMap<String, Object>(2);
        if (role == null || role.isEmpty()) {
            js.put("success", true);
            js.put("msg", "");
            return js;
        }
        String[] roleArr = role.split(",");
        JSONArray jsonList = new JSONArray();
        for (String roleCode : roleArr) {
            JSONObject jsonObject = new JSONObject();
            String keys = SummitTools.getKey();
            //jsonObject.put("_id", keys);
            jsonObject.put(UserRoleEnum.ID.toString(), keys);
            jsonObject.put(UserRoleEnum.USERNAME.toString(), userName);
            jsonObject.put(UserRoleEnum.ROLE_CODE.toString(), roleCode);
            jsonList.add(jsonObject);
        }
        JSONArray jsonArray = saveBatch(contactIndex, jsonList);
        if (jsonArray.size() == jsonList.size()) {
            js.put("success", true);
            js.put("msg", jsonArray);

        }
        return js;
    }
}
