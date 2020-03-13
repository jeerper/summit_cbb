package com.summit.service.auth.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.Common;
import com.summit.common.entity.AuthBean;
import com.summit.common.entity.DeptAuditBean;
import com.summit.repository.UserRepository;
import com.summit.service.auth.AuthService;
import com.summit.service.dept.DeptsService;
import com.summit.util.SummitTools;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AuthServiceImpl  implements AuthService {
    @Autowired
    private DeptsService deptsService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository ur;


    @Override
    public Page<AuthBean> queryByPage(Integer currentPage, Integer pageSize, JSONObject paramJson) throws Exception {
        List rolesList = null;
        if (Common.getLogUser() != null) {
            rolesList = Arrays.asList(Common.getLogUser().getRoles());
        }
        String depts = deptsService.currentDeptService();
        if (!rolesList.contains("ROLE_SUPERUSER") && !SummitTools.stringIsNull(depts)){
            StringBuffer sql_auth = new StringBuffer("SELECT auth.id,user.NAME as apply_name,auth.apply_type,auth.submitted_to,auth.isAudited,date_format(auth.apply_time, '%Y-%m-%d %H:%i:%s')as applytime ");
            sql_auth.append("from sys_auth auth INNER JOIN sys_user user on auth.apply_name=user.USERNAME ");
            sql_auth.append("WHERE 1=1 and auth.submitted_to in ('"+depts+"')");
            Integer index = 1;
            LinkedMap linkedMap = new LinkedMap();
            List<Object> list = new ArrayList();
            if (paramJson.containsKey("applytype")) {
                sql_auth.append(" and auth.apply_type like ? ");
                linkedMap.put(index, "%" + paramJson.get("applytype") + "%");
                index++;
            }
            if (paramJson.containsKey("applyName")) {
                sql_auth.append(" and user.NAME like ? ");
                linkedMap.put(index, "%" + paramJson.get("applyName") + "%");
                index++;
            }
            if (paramJson.containsKey("endTime")){
                sql_auth.append(" and auth.apply_time <= ? ");
                linkedMap.put(index, paramJson.get("endTime"));
                index++;
            }
            if (paramJson.containsKey("startTime")){
                sql_auth.append(" and auth.apply_time >= ? ");
                linkedMap.put(index, paramJson.get("startTime"));
                index++;
            }
            list.add(sql_auth.toString());
            list.add(linkedMap);
            Page<Object>  page = ur.queryByCustomPage(list.get(0).toString(), currentPage, pageSize, (LinkedMap) list.get(1));
            if (page != null){
                List<AuthBean> authList = new ArrayList<AuthBean>();
                if (page.getContent() != null && page.getContent().size() > 0) {
                    for (Object o : page.getContent()) {
                        AuthBean authBean = JSON.parseObject(o.toString(), new TypeReference<AuthBean>() {
                        });
                        authList.add(authBean);
                    }
                }
                return new Page<AuthBean>(authList, page.getPageable());
            }
            return null;

        }else if (rolesList.contains("ROLE_SUPERUSER")){
            StringBuffer sql_auth = new StringBuffer("SELECT auth.id,user.NAME as apply_name,auth.apply_type,auth.submitted_to,auth.isAudited,date_format(auth.apply_time, '%Y-%m-%d %H:%i:%s')as applytime ");
            sql_auth.append("from sys_auth auth INNER JOIN sys_user user on auth.apply_name=user.USERNAME ");
            sql_auth.append("WHERE 1=1 ");
            Integer index = 1;
            LinkedMap linkedMap = new LinkedMap();
            List<Object> list = new ArrayList();
            if (paramJson.containsKey("applytype")) {
                sql_auth.append(" and auth.apply_type like ? ");
                linkedMap.put(index, "%" + paramJson.get("applytype") + "%");
                index++;
            }
            if (paramJson.containsKey("applyName")) {
                sql_auth.append(" and user.NAME like ? ");
                linkedMap.put(index, "%" + paramJson.get("applyName") + "%");
                index++;
            }
            if (paramJson.containsKey("endTime")){
                sql_auth.append(" and auth.apply_time <= ? ");
                linkedMap.put(index, paramJson.get("endTime"));
                index++;
            }
            if (paramJson.containsKey("startTime")){
                sql_auth.append(" and auth.apply_time >= ? ");
                linkedMap.put(index, paramJson.get("startTime"));
                index++;
            }
            list.add(sql_auth.toString());
            list.add(linkedMap);
            Page<Object>  page = ur.queryByCustomPage(list.get(0).toString(), currentPage, pageSize, (LinkedMap) list.get(1));
            if (page != null){
                List<AuthBean> authList = new ArrayList<AuthBean>();
                if (page.getContent() != null && page.getContent().size() > 0) {
                    for (Object o : page.getContent()) {
                        AuthBean authBean = JSON.parseObject(o.toString(), new TypeReference<AuthBean>() {
                        });
                        authList.add(authBean);
                    }
                }
                return new Page<AuthBean>(authList, page.getPageable());
            }
            return null;
        }

        return null;
    }
}
