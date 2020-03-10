package com.summit.service.dept.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.Common;
import com.summit.common.entity.DeptAuditBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.UserAuditBean;
import com.summit.common.entity.UserInfo;
import com.summit.repository.UserRepository;
import com.summit.service.dept.DeptAuditService;
import com.summit.service.dept.DeptsService;
import com.summit.util.SummitTools;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DeptAuditServiceImpl  implements DeptAuditService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository ur;

    @Autowired
    private DeptsService deptsService;

    @Override
    public ResponseCodeEnum deptAudit(String id, String deptIdAuth, String isAudited) throws Exception {
        if (SummitTools.stringNotNull(isAudited)){
            //1、修改审核表字段为1或2以及审核人
            StringBuffer sql=new StringBuffer("UPDATE sys_dept_auth SET isAudited = ? ");
            if(Common.getLogUser()!=null ){
                sql.append(" ,auth_person=?");
            }
            sql.append(" where id =? ");
            jdbcTemplate.update(sql.toString(),isAudited,Common.getLogUser().getUserName(),id);

            if("1".equals(isAudited)) {//审核通过
                //2、修改用户表信息以及审核字段为1
                JSONObject dept_json= queryDeptByDeptId(deptIdAuth);
                if (null !=dept_json){
                    StringBuffer dept_sql=new StringBuffer("UPDATE SYS_DEPT SET PID=?, DEPTCODE=?,DEPTNAME=?,ADCD=?,isAudited=? ");
                    dept_sql.append("WHERE id=? ");
                    jdbcTemplate.update(dept_sql.toString(),
                            dept_json.containsKey("pId_auth") ? dept_json.getString("pId_auth") : null,
                            dept_json.containsKey("deptcode_auth") ? dept_json.getString("deptcode_auth") : null,
                            dept_json.containsKey("deptName_auth") ? dept_json.getString("deptName_auth") : null,
                            dept_json.containsKey("adcd_auth") ? dept_json.getString("adcd_auth") : null,
                            isAudited,
                            deptIdAuth
                            );
                }


            }else if (("2".equals(isAudited))){//审核不通过
                JSONObject dept_json= queryDeptByDeptId(deptIdAuth);
                if (null !=dept_json){
                    StringBuffer sql_json=new StringBuffer("UPDATE sys_dept SET isAudited = ? ");
                    sql_json.append(" where id=? ");
                    jdbcTemplate.update(sql_json.toString(),isAudited,deptIdAuth);
                }
            }
            return null;
        }
        return ResponseCodeEnum.CODE_9999;
    }

    @Override
    public Page<DeptAuditBean> queryByPage(Integer currentPage, Integer pageSize, com.alibaba.fastjson.JSONObject paramJson) throws Exception {
        List rolesList = null;
        if (Common.getLogUser() != null) {
            rolesList = Arrays.asList(Common.getLogUser().getRoles());
        }
        String depts = deptsService.DeptsService();
        if (!SummitTools.stringIsNull(depts)){
            StringBuffer sql=new StringBuffer("SELECT deptAuth.id,deptAuth.deptId_auth,deptAuth.pId_auth,deptAuth.deptcode_auth,deptAuth.deptName_auth,deptAuth.adcd_auth,deptAuth.auth_person,deptAuth.isAudited, ");
            sql.append("date_format(deptAuth.auth_time,'%Y-%m-%d %H:%i:%s')as auth_time,remark FROM  sys_dept_auth  deptAuth ");
            sql.append("WHERE 1=1 and deptAuth.submitted_to in ('"+depts+"')");
            Integer index = 1;
            LinkedMap linkedMap = new LinkedMap();
            List<Object> list = new ArrayList();
            if (paramJson.containsKey("deptNameAuth")) {
                sql.append(" and deptAuth.deptName_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("deptNameAuth") + "%");
                index++;
            }
            if (paramJson.containsKey("authPerson")) {
                sql.append(" and deptAuth.auth_person like ? ");
                linkedMap.put(index, "%" + paramJson.get("authPerson") + "%");
                index++;
            }
            list.add(sql.toString());
            list.add(linkedMap);
            Page<Object>  page = ur.queryByCustomPage(list.get(0).toString(), currentPage, pageSize, (LinkedMap) list.get(1));
            if (page != null){
                List<DeptAuditBean> deptInfoList = new ArrayList<DeptAuditBean>();
                if (page.getContent() != null && page.getContent().size() > 0) {
                    for (Object o : page.getContent()) {
                        DeptAuditBean deptAuditBean = JSON.parseObject(o.toString(), new TypeReference<DeptAuditBean>() {
                        });
                        deptInfoList.add(deptAuditBean);
                    }
                }
                return new Page<DeptAuditBean>(deptInfoList, page.getPageable());
            }
            return null;

        }else if (rolesList.contains("ROLE_SUPERUSER")){
            StringBuffer sql=new StringBuffer("SELECT deptAuth.id,deptAuth.deptId_auth,deptAuth.pId_auth,deptAuth.deptcode_auth,deptAuth.deptName_auth,deptAuth.adcd_auth,deptAuth.auth_person,deptAuth.isAudited, ");
            sql.append("date_format(deptAuth.auth_time,'%Y-%m-%d %H:%i:%s')as auth_time,remark FROM  sys_dept_auth  deptAuth ");
            sql.append("WHERE 1=1 ");
            Integer index = 1;
            LinkedMap linkedMap = new LinkedMap();
            List<Object> list = new ArrayList();
            if (paramJson.containsKey("deptNameAuth")) {
                sql.append(" and deptAuth.deptName_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("deptNameAuth") + "%");
                index++;
            }
            if (paramJson.containsKey("authPerson")) {
                sql.append(" and deptAuth.auth_person like ? ");
                linkedMap.put(index, "%" + paramJson.get("authPerson") + "%");
                index++;
            }
            list.add(sql.toString());
            list.add(linkedMap);
            Page<Object>  page = ur.queryByCustomPage(list.get(0).toString(), currentPage, pageSize, (LinkedMap) list.get(1));
            if (page != null){
                List<DeptAuditBean> deptInfoList = new ArrayList<DeptAuditBean>();
                if (page.getContent() != null && page.getContent().size() > 0) {
                    for (Object o : page.getContent()) {
                        DeptAuditBean deptAuditBean = JSON.parseObject(o.toString(), new TypeReference<DeptAuditBean>() {
                        });
                        deptInfoList.add(deptAuditBean);
                    }
                }
                return new Page<DeptAuditBean>(deptInfoList, page.getPageable());
            }
            return null;
        }
        return null;
    }

    private JSONObject queryDeptByDeptId(String deptIdAuth) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT deptAuth.id,deptAuth.deptId_auth,deptAuth.pId_auth,deptAuth.deptcode_auth,deptAuth.deptName_auth,deptAuth.adcd_auth,deptAuth.auth_person,deptAuth.auth_time,deptAuth.submitted_to ");
        sql.append("from sys_dept_auth  deptAuth WHERE deptAuth.deptId_auth= ?");
        LinkedMap lm = new LinkedMap();
        lm.put(1, deptIdAuth);
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return  jsonObject;

    }
}
