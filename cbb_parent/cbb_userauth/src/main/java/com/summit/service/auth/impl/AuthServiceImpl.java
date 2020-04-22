package com.summit.service.auth.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.Common;
import com.summit.common.CommonConstants;
import com.summit.common.entity.AuthBean;
import com.summit.common.entity.DeptAuditBean;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.dao.repository.*;
import com.summit.repository.UserRepository;
import com.summit.service.auth.AuthService;
import com.summit.service.dept.DeptsService;
import com.summit.util.CommonUtil;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl  implements AuthService {
    @Autowired
    private DeptsService deptsService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository ur;
    @Autowired
    UserInfoCache userInfoCache;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private UserRecordDao  userRecordDao;
    @Autowired
    private DeptAuditDao deptAuditDao;
    @Autowired
    private  DeptRecordDao deptRecordDao;
    @Autowired
    private UserAuditDao userAuditDao;

    @Override
    public Page<AuthBean> queryByPage(Integer currentPage, Integer pageSize, JSONObject paramJson) throws Exception {
        List rolesList = null;
        if (Common.getLogUser() != null && Common.getLogUser().getRoles()!=null) {
            rolesList = Arrays.asList(Common.getLogUser().getRoles());
        }
        String depts = deptsService.getCurrentDeptService();
        if (!rolesList.contains("ROLE_SUPERUSER") && !SummitTools.stringIsNull(depts)){
            StringBuffer sql_auth = new StringBuffer("SELECT auth.id,user.NAME as apply_name,auth.apply_type,auth.submitted_to,date_format(auth.apply_time, '%Y-%m-%d %H:%i:%s')as applytime,dic.NAME as isAudited  ");
            sql_auth.append("from sys_auth auth INNER JOIN sys_user user on auth.apply_name=user.USERNAME ");
            sql_auth.append("LEFT JOIN sys_dictionary dic on dic.PCODE='isAudited' and auth.isAudited=dic.CKEY ");
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
            sql_auth.append(" ORDER BY auth.apply_time DESC ");
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
            StringBuffer sql_auth = new StringBuffer("SELECT auth.id,user.NAME as apply_name,auth.apply_type,auth.submitted_to,date_format(auth.apply_time, '%Y-%m-%d %H:%i:%s')as applytime,dic.NAME as isAudited ");
            sql_auth.append("from sys_auth auth INNER JOIN sys_user user on auth.apply_name=user.USERNAME ");
            sql_auth.append("LEFT JOIN sys_dictionary dic on dic.PCODE='isAudited' and auth.isAudited=dic.CKEY ");
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
            sql_auth.append(" ORDER BY auth.apply_time DESC ");
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

    @Override
    public  Map<String,Object> findById(String id) throws Exception {
        StringBuffer auth_sql=new StringBuffer("SELECT auth.id,auth.apply_type,auth.apply_Id,user.NAME as apply_name,user.USERNAME,date_format(auth.apply_time, '%Y-%m-%d %H:%i:%s') as apply_time from sys_auth auth INNER JOIN sys_user user on auth.apply_name=user.USERNAME where auth.id=? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, id);
        net.sf.json.JSONObject auth_json = ur.queryOneCustom(auth_sql.toString(), lm);
        if (null !=auth_json){
            String apply_type = auth_json.getString("apply_type");
            String apply_id = auth_json.getString("apply_Id");
            Map<String,Object> map =new HashMap<>();
            map.put("apply_name",auth_json.getString("apply_name"));
            map.put("apply_username",auth_json.getString("USERNAME"));
            map.put("apply_time",auth_json.getString("apply_time"));
            if ("0".equals(apply_type)){//机构
                StringBuffer dept_auth_sql=new StringBuffer("SELECT auth.id,auth.apply_type,auth.apply_Id,dept.DEPTNAME,dept.DEPTCODE from sys_auth auth  ");
                dept_auth_sql.append("INNER JOIN sys_dept_auth authDept on auth.apply_Id=authDept.id  ");
                dept_auth_sql.append("INNER JOIN sys_dept dept on authDept.deptId_auth=dept.ID  where auth.id=? ");
                LinkedMap user_auth = new LinkedMap();
                user_auth.put(1, id);
                net.sf.json.JSONObject dept_auth_json = ur.queryOneCustom(dept_auth_sql.toString(), user_auth);
                String deptname = dept_auth_json.getString("DEPTNAME");
                String deptcode = dept_auth_json.getString("DEPTCODE");
                net.sf.json.JSONObject new_deptJson=queryDeptAuthByDeptID(apply_id);
                String deptRecord_id = new_deptJson.getString("deptRecord_id");
                net.sf.json.JSONObject old_deptJson=queryDeptRecordByDeptID(deptRecord_id);
                List<JSONObject> json=compareToDept(new_deptJson,old_deptJson,deptname,deptcode);
                map.put("updateType","机构基础类型");
                map.put("updateContent",json);
                map.put("isAudited",new_deptJson.getString("isAudited"));
                return map;
            }else if ("1".equals(apply_type)){//用户基础信息
                StringBuffer user_auth_sql=new StringBuffer("SELECT auth.id,auth.apply_type,auth.apply_Id,user.NAME,user.USERNAME from sys_auth auth ");
                user_auth_sql.append("INNER JOIN sys_user_auth authUser on auth.apply_Id=authUser.id ");
                user_auth_sql.append("INNER JOIN sys_user user on authUser.userName_auth=user.USERNAME  where auth.id=? ");
                LinkedMap user_auth = new LinkedMap();
                user_auth.put(1, id);
                net.sf.json.JSONObject user_auth_json = ur.queryOneCustom(user_auth_sql.toString(), user_auth);
                String applyname = user_auth_json.getString("NAME");
                String username = user_auth_json.getString("USERNAME");
                map.put("updateType","人员基础类型");
                net.sf.json.JSONObject new_userJson =queryUserAuthById(apply_id);
                String userRecord_id = new_userJson.getString("userRecord_id");
                net.sf.json.JSONObject old_userJson =queryUserRecordById(userRecord_id);
                List<JSONObject> json=compareToUser(new_userJson,old_userJson,applyname,username);
                map.put("updateContent",json);
                map.put("isAudited",new_userJson.getString("isAudited"));
                return map;
            }
        }

        return null;
    }

    private net.sf.json.JSONObject queryUserRecordById(String userRecord_id) throws Exception {
        /*StringBuffer user_sql=new StringBuffer("SELECT user.USERNAME,user.name,dic.NAME as SEX,user.EMAIL,user.PHONE_NUMBER,dic2.name AS IS_ENABLED,user.DUTY,user.POST,dept2.names as DEPTNAME,adcd2.names as ADNM,user.HEADPORTRAIT ");
        user_sql.append("from sys_user user LEFT JOIN sys_dictionary dic on dic.PCODE='sex' and user.SEX=dic.CKEY ");
        user_sql.append("LEFT JOIN sys_dictionary  dic2 on dic2.PCODE='isEnabled' and user.IS_ENABLED=dic2.CKEY ");
        user_sql.append("LEFT JOIN (SELECT USERNAME,GROUP_CONCAT(userDept.DEPTID)AS deptIds, GROUP_CONCAT(dept.DEPTNAME)AS names  FROM sys_user_dept userDept ");
        user_sql.append("inner join sys_dept dept on userDept.DEPTID=dept.ID  GROUP BY USERNAME  )dept2 on user.USERNAME=dept2.USERNAME ");
        user_sql.append("LEFT JOIN (SELECT USERNAME,GROUP_CONCAT(userAdcd.ADCD)AS adcds, GROUP_CONCAT(adcd.ADNM)AS names  FROM sys_user_adcd userAdcd  ");
        user_sql.append("inner join sys_ad_cd adcd on userAdcd.ADCD=adcd.ADCD  GROUP BY USERNAME)adcd2 on user.USERNAME=adcd2.USERNAME ");
        user_sql.append("WHERE user.USERNAME=? ");*/
        StringBuffer userRecord_sql=new StringBuffer("SELECT ur.id,ur.username,ur.name,dic.NAME as sex,ur.password,ur.email,ur.phoneNumber,dic2.name AS is_enable,ur.duty,ur.post,ur.dept,ur.adcd,ur.headPortrait,ur.state ");
        userRecord_sql.append("from sys_user_record ur LEFT JOIN sys_dictionary dic on dic.PCODE='sex' and ur.sex=dic.CKEY ");
        userRecord_sql.append("LEFT JOIN sys_dictionary  dic2 on dic2.PCODE='isEnabled' and ur.is_enable=dic2.CKEY  WHERE ur.id=? ");
        LinkedMap userRecord_lm = new LinkedMap();
        userRecord_lm.put(1, userRecord_id);
        net.sf.json.JSONObject old_userJson = ur.queryOneCustom(userRecord_sql.toString(), userRecord_lm);
        if (null !=old_userJson && old_userJson.containsKey("dept") && !SummitTools.stringIsNull(old_userJson.getString("dept"))){
            String dept = old_userJson.getString("dept");
            String[] depts = dept.split(",");
            List<String> json = new ArrayList<>();
            for (int i = 0; i < depts.length; i++) {
                StringBuffer stringBuffer=new StringBuffer("SELECT dept.ID,dept.DEPTNAME FROM sys_dept dept WHERE dept.ID=? ");
                LinkedMap lm = new LinkedMap();
                lm.put(1, depts[i]);
                net.sf.json.JSONObject deptName_json  = ur.queryOneCustom(stringBuffer.toString(), lm);
                if (null !=deptName_json){
                    String deptName = deptName_json.getString("DEPTNAME");
                    json.add(deptName);
                }
            }
            String str = StringUtils.join(json, ",");
            old_userJson.put("dept",str);

        }
        if (null !=old_userJson && old_userJson.containsKey("adcd") && !SummitTools.stringIsNull(old_userJson.getString("adcd"))){
            String adcd = old_userJson.getString("adcd");
            String[] adcds = adcd.split(",");
            List<String> json = new ArrayList<>();
            for (int i = 0; i < adcds.length; i++) {
                StringBuffer stringBuffer=new StringBuffer("SELECT adcd.ADCD,adcd.ADNM from sys_ad_cd adcd WHERE adcd.ADCD=? ");
                LinkedMap lm = new LinkedMap();
                lm.put(1, adcds[i]);
                net.sf.json.JSONObject adcd_json  = ur.queryOneCustom(stringBuffer.toString(), lm);
                if (null !=adcd_json){
                    String adnm = adcd_json.getString("ADNM");
                    json.add(adnm);
                }
            }
            String str = StringUtils.join(json, ",");
            old_userJson.put("adcd",str);
        }
        return old_userJson;
    }

    private net.sf.json.JSONObject queryUserAuthById(String apply_id) throws Exception {
        StringBuffer userAuth_sql=new StringBuffer("SELECT usa.id,usa.userName_auth,usa.name_auth,dic.NAME as sex_auth,usa.password_auth,usa.email_auth,usa.phone_number_auth,dic2.NAME as is_enabled_auth,usa.headPortrait_auth,usa.duty_auth,usa.dept_auth,usa.adcd_auth,usa.post_auth,dic3.NAME as isAudited,usa.userRecord_id,usa.state_auth ");
        userAuth_sql.append("from sys_user_auth usa LEFT JOIN  sys_dictionary dic on dic.PCODE='sex' and usa.sex_auth=dic.CKEY ");
        userAuth_sql.append("LEFT JOIN sys_dictionary  dic2 on dic2.PCODE='isEnabled' and usa.is_enabled_auth=dic2.CKEY ");
        userAuth_sql.append("LEFT JOIN sys_dictionary  dic3 on dic3.PCODE='isAudited' and usa.isAudited=dic3.CKEY ");
        userAuth_sql.append(" where usa.id=? ");
        LinkedMap userAuth_lm = new LinkedMap();
        userAuth_lm.put(1,apply_id);
        net.sf.json.JSONObject new_userJson = ur.queryOneCustom(userAuth_sql.toString(), userAuth_lm);
        if (null !=new_userJson && new_userJson.containsKey("dept_auth") && !SummitTools.stringIsNull(new_userJson.getString("dept_auth"))){
            String dept_auth = new_userJson.getString("dept_auth");
            String[] dept_auths = dept_auth.split(",");
            List<String> json = new ArrayList<>();
            for (int i = 0; i < dept_auths.length; i++) {
                StringBuffer stringBuffer=new StringBuffer("SELECT dept.ID,dept.DEPTNAME FROM sys_dept dept WHERE dept.ID=? ");
                LinkedMap lm = new LinkedMap();
                lm.put(1, dept_auths[i]);
                net.sf.json.JSONObject deptName_json  = ur.queryOneCustom(stringBuffer.toString(), lm);
                if (null !=deptName_json){
                    String deptName = deptName_json.getString("DEPTNAME");
                    json.add(deptName);
                }
            }
            String str = StringUtils.join(json, ",");
            new_userJson.put("dept_auth",str);

        }
        if (null !=new_userJson && new_userJson.containsKey("adcd_auth") && !SummitTools.stringIsNull(new_userJson.getString("adcd_auth"))){
            String adcd_auth = new_userJson.getString("adcd_auth");
            String[] adcd_auths = adcd_auth.split(",");
            List<String> json = new ArrayList<>();
            for (int i = 0; i < adcd_auths.length; i++) {
                StringBuffer stringBuffer=new StringBuffer("SELECT adcd.ADCD,adcd.ADNM from sys_ad_cd adcd WHERE adcd.ADCD=? ");
                LinkedMap lm = new LinkedMap();
                lm.put(1, adcd_auths[i]);
                net.sf.json.JSONObject adcd_json  = ur.queryOneCustom(stringBuffer.toString(), lm);
                if (null !=adcd_json){
                    String adnm = adcd_json.getString("ADNM");
                    json.add(adnm);
                }
            }
            String str = StringUtils.join(json, ",");
            new_userJson.put("adcd_auth",str);
        }

        return new_userJson;
    }




    private net.sf.json.JSONObject queryDeptAuthByDeptID(String apply_id) throws Exception {
        StringBuffer deptAuth_sql=new StringBuffer("select du.id,du.deptId_auth,dept.DEPTNAME as pId_auth,du.deptcode_auth,du.deptName_auth,adcd.ADNM as adcd_auth,du.auth_person,dic.NAME AS deptType_auth,dic1.NAME as isAudited,user.NAME as deptHead_auth,du.deptRecord_id,du.remark from  ");
        deptAuth_sql.append("sys_dept_auth du LEFT JOIN sys_dept dept on du.pId_auth=dept.ID LEFT JOIN sys_ad_cd adcd ");
        deptAuth_sql.append("on du.adcd_auth=adcd.ADCD LEFT JOIN  sys_dictionary dic on dic.PCODE='dept_type' and du.deptType_auth=dic.CKEY ");
        deptAuth_sql.append("LEFT JOIN  sys_dictionary dic1 on dic1.PCODE='isAudited' and du.isAudited=dic1.CKEY ");
        deptAuth_sql.append("LEFT JOIN sys_user user on user.USERNAME=du.deptHead_auth ");
        deptAuth_sql.append("where du.id=? ");
        LinkedMap deptAuth_lm = new LinkedMap();
        deptAuth_lm.put(1, apply_id);
        net.sf.json.JSONObject new_deptJson = ur.queryOneCustom(deptAuth_sql.toString(), deptAuth_lm);
        return new_deptJson;
    }

    private net.sf.json.JSONObject queryDeptRecordByDeptID(String deptRecord_id) throws Exception {
        StringBuffer dept_sql=new StringBuffer("SELECT dr.id, deptName.DEPTNAME AS pid, dr.deptcode,dr.deptName,adcd.ADNM as adcd,dic.NAME as deptType,user.NAME as deptHead,dr.deptId,dr.remark from ");
        dept_sql.append("sys_dept_record dr LEFT JOIN sys_dept deptName  on dr.pid=deptName.ID ");
        dept_sql.append("LEFT JOIN sys_ad_cd adcd on dr.adcd=adcd.ADCD ");
        dept_sql.append("LEFT JOIN  sys_dictionary dic on dic.PCODE='dept_type' and dr.deptType=dic.CKEY ");
        dept_sql.append("LEFT JOIN sys_user user on user.USERNAME=dr.deptHead where dr.id=? ");
        LinkedMap dept_lm = new LinkedMap();
        dept_lm.put(1, deptRecord_id);
        net.sf.json.JSONObject old_deptJson = ur.queryOneCustom(dept_sql.toString(), dept_lm);
        return old_deptJson;
    }

    @Override
    public int authByIdBatch(List<String> authIds, String isAudited) throws Exception {
        if(authIds == null || isAudited == null){
            return CommonConstants.UPDATE_ERROR;
        }
        for (String id:authIds){
            //修改sys_auth
            StringBuffer updateAuth_sql=new StringBuffer("UPDATE sys_auth SET isAudited = ? where id =?");
            jdbcTemplate.update(updateAuth_sql.toString(),isAudited,id);
            StringBuffer  queryAuth_sql=new StringBuffer("SELECT auth.id, auth.apply_type,auth.apply_Id  from sys_auth auth WHERE auth.id= ?");
            LinkedMap auth_lm = new LinkedMap();
            auth_lm.put(1, id);
            net.sf.json.JSONObject auth_json = ur.queryOneCustom(queryAuth_sql.toString(), auth_lm);
            if (auth_json ==null || SummitTools.stringIsNull(auth_json.getString("apply_Id"))){
                continue;
            }
            String apply_id = auth_json.getString("apply_Id");//申请id
            String apply_type = auth_json.getString("apply_type");
            if ("0".equals(apply_type)){//机构
                //修改sys_dept_auth
                StringBuffer uadateDeptAuth_sql=new StringBuffer("UPDATE sys_dept_auth SET isAudited = ? ");
                if(Common.getLogUser()!=null ){
                    uadateDeptAuth_sql.append(" ,auth_person=? ");
                }
                uadateDeptAuth_sql.append(" where id =? ");
                jdbcTemplate.update(uadateDeptAuth_sql.toString(),isAudited,Common.getLogUser().getUserName(),apply_id);
            }
            if ("1".equals(apply_type)){//人员
                //修改sys_user_auth
                StringBuffer sql=new StringBuffer("UPDATE sys_user_auth SET isAudited = ? ");
                if(Common.getLogUser()!=null ){
                    sql.append(" ,auth_person=?");
                }
                sql.append(" where id =? ");
                jdbcTemplate.update(sql.toString(),isAudited,Common.getLogUser().getUserName(),apply_id);
            }
            if ("1".equals(isAudited)){//批准
                if ("0".equals(apply_type)){//机构
                    net.sf.json.JSONObject deptAuth_json= queryDeptByApplyId(apply_id);
                    if (deptAuth_json == null || SummitTools.stringIsNull(deptAuth_json.getString("deptId_auth"))) {
                        continue;
                    }
                    if (null !=deptAuth_json){
                        StringBuffer dept_sql=new StringBuffer("UPDATE SYS_DEPT SET PID=?, DEPTCODE=?,DEPTNAME=?,ADCD=?,isAudited=?,deptType=?,DEPTHEAD=?,REMARK=? ");
                        dept_sql.append("WHERE id=? ");
                        jdbcTemplate.update(dept_sql.toString(),
                                deptAuth_json.containsKey("pId_auth") ? deptAuth_json.getString("pId_auth") : null,
                                deptAuth_json.containsKey("deptcode_auth") ? deptAuth_json.getString("deptcode_auth") : null,
                                deptAuth_json.containsKey("deptName_auth") ? deptAuth_json.getString("deptName_auth") : null,
                                deptAuth_json.containsKey("adcd_auth") ? deptAuth_json.getString("adcd_auth") : null,
                                isAudited,
                                deptAuth_json.containsKey("deptType_auth") ? deptAuth_json.getString("deptType_auth") : null,
                                deptAuth_json.containsKey("deptHead_auth") ? deptAuth_json.getString("deptHead_auth") : null,
                                deptAuth_json.containsKey("remark") ? deptAuth_json.getString("remark") : null,
                                deptAuth_json.getString("deptId_auth")
                        );
                    }
                }else if ("1".equals(apply_type)){//用户
                    net.sf.json.JSONObject user_json= queryUserByApplyId(apply_id);
                    if (null != user_json && !user_json.containsKey("state_auth")){
                        //2、修改SYS_USER
                        StringBuffer sql_user = new StringBuffer("UPDATE SYS_USER SET NAME = ?,SEX=?, EMAIL = ?, PHONE_NUMBER =?, IS_ENABLED = ?, LAST_UPDATE_TIME = now() ");
                        sql_user.append(" ,DUTY=?,POST=?,HEADPORTRAIT=?,isAudited=? ");
                        sql_user.append("WHERE USERNAME = ? AND STATE = 1");
                        jdbcTemplate.update(sql_user.toString(),
                                user_json.containsKey("name_auth") ? user_json.getString("name_auth") : null,
                                user_json.containsKey("sex_auth") ? user_json.getString("sex_auth") : null,
                                user_json.containsKey("email_auth") ? user_json.getString("email_auth") : null,
                                user_json.containsKey("phone_number_auth") ? user_json.getString("phone_number_auth") : null,
                                user_json.containsKey("is_enabled_auth") ? user_json.getString("is_enabled_auth") : null,
                                user_json.containsKey("duty_auth") ? user_json.getString("duty_auth") : null,
                                user_json.containsKey("post_auth") ? user_json.getString("post_auth") : null,
                                user_json.containsKey("headPortrait_auth") ? user_json.getString("headPortrait_auth") : null,
                                isAudited,
                                user_json.containsKey("userName_auth") ? user_json.getString("userName_auth") : null
                        );
                        //3、保存行政区划
                        String adcdSql = " delete from sys_user_adcd where USERNAME  IN ('" + user_json.getString("userName_auth") + "') ";
                        jdbcTemplate.update(adcdSql);
                        if (null != user_json && SummitTools.stringNotNull(user_json.getString("adcd_auth"))){
                            String adcd_auth = user_json.getString("adcd_auth");
                            String[] adcds = adcd_auth.split(",");
                            if (adcds != null && adcds.length > 0) {
                                String insertAdcdSql = "INSERT INTO SYS_USER_ADCD(ID,USERNAME,ADCD,CREATETIME) VALUES ( ?, ?, ?, now())";
                                List userAdcdParams = new ArrayList();
                                for (String adcd : adcds) {
                                    Object adcdParam[] = {
                                            SummitTools.getKey(),
                                            user_json.getString("userName_auth"),
                                            adcd,
                                    };
                                    userAdcdParams.add(adcdParam);
                                }
                                jdbcTemplate.batchUpdate(insertAdcdSql, userAdcdParams);

                            }
                        }
                        //4、保存部门表
                        String deptSql = " delete from SYS_USER_DEPT where USERNAME  IN ('" + user_json.getString("userName_auth") + "') ";
                        jdbcTemplate.update(deptSql);
                        if (null != user_json && SummitTools.stringNotNull(user_json.getString("dept_auth"))){
                            String dept_auth = user_json.getString("dept_auth");
                            String[] depts = dept_auth.split(",");
                            if (depts != null && depts.length > 0) {
                                String insertAdcdSql = "INSERT INTO SYS_USER_DEPT(ID,USERNAME,DEPTID,CREATETIME) VALUES ( ?, ?, ?, now())";
                                List userdeptParams = new ArrayList();
                                for (String deptId : depts) {
                                    Object deptParam[] = {
                                            SummitTools.getKey(),
                                            user_json.getString("userName_auth"),
                                            deptId,
                                    };
                                    userdeptParams.add(deptParam);
                                }
                                jdbcTemplate.batchUpdate(insertAdcdSql, userdeptParams);
                            }

                        }
                        //5、设置redis缓存
                        UserInfo cacheUserInfo = userInfoCache.getUserInfo( user_json.getString("userName_auth"));
                        if (cacheUserInfo != null) {
                            UserInfo userInfo=getUserInfo(user_json);
                            BeanUtil.copyProperties(userInfo, cacheUserInfo, CopyOptions.create().setIgnoreNullValue(true));
                            userInfoCache.setUserInfo(userInfo.getUserName(), cacheUserInfo);
                        }
                    }else if (null != user_json && user_json.containsKey("state_auth")){
                        String userName_auth = user_json.getString("userName_auth");
                        String sql = "UPDATE SYS_USER SET STATE = '0',IS_ENABLED='0', LAST_UPDATE_TIME = ? WHERE USERNAME <> '" + SysConstants.SUPER_USERNAME + "' AND USERNAME IN ('" + userName_auth + "')";
                        jdbcTemplate.update(sql, new Date());
                        if (SummitTools.stringEquals(SysConstants.SUPER_USERNAME, userName_auth)) {
                            continue;
                        }
                        userInfoCache.deleteUserInfo(userName_auth);
                        delUserRoleByUserName(userName_auth);
                        String deptSql=" delete from SYS_USER_DEPT where USERNAME  IN ('"+userName_auth+"') ";
                        jdbcTemplate.update(deptSql);
                    }
                }
            }else if ("2".equals(isAudited)){//拒绝
                if ("0".equals(apply_type)){//机构
                    net.sf.json.JSONObject deptAuth_json= queryDeptByApplyId(apply_id);
                    if (deptAuth_json == null || SummitTools.stringIsNull(deptAuth_json.getString("deptId_auth"))) {
                        continue;
                    }
                    if (null !=deptAuth_json){
                        StringBuffer sql_json=new StringBuffer("UPDATE sys_dept SET isAudited = ? ");
                        sql_json.append(" where id=? ");
                        jdbcTemplate.update(sql_json.toString(),isAudited,deptAuth_json.getString("deptId_auth"));
                    }
                }else if ("1".equals(apply_type)){//用户
                    net.sf.json.JSONObject user_json= queryUserByApplyId(apply_id);
                    if (null !=user_json){
                        StringBuffer sql_json=new StringBuffer("UPDATE sys_user SET isAudited = ? ");
                        sql_json.append(" where USERNAME=? ");
                        jdbcTemplate.update(sql_json.toString(),isAudited, user_json.getString("userName_auth"));
                    }
                }
            }
        }
        return 0;
    }

    private void delUserRoleByUserName(String userName_auth) {
        String sql = "DELETE FROM SYS_USER_ROLE WHERE USERNAME =?";
        jdbcTemplate.update(sql, userName_auth);
    }

    @Override
    public void delAlarmByIdBatch(List<String> authIds) throws Exception {
        List<String> userRecords=new ArrayList<>();
        List<String> deptRecords=new ArrayList<>();
        List<String> userAuths=new ArrayList<>();
        List<String> deptAuths=new ArrayList<>();
        for (String authId:authIds){
            StringBuffer sys_auth_sql=new StringBuffer("SELECT auth.id,auth.apply_type,auth.apply_Id,user.NAME as apply_name,date_format(auth.apply_time, '%Y-%m-%d %H:%i:%s') as apply_time from sys_auth auth INNER JOIN sys_user user on auth.apply_name=user.USERNAME where auth.id=? ");
            LinkedMap lm = new LinkedMap();
            lm.put(1, authId);
            net.sf.json.JSONObject auth_json = ur.queryOneCustom(sys_auth_sql.toString(), lm);
            if (null ==auth_json ){
                continue;
            }
            String apply_type = auth_json.getString("apply_type");
            String apply_id = auth_json.getString("apply_Id");
            if ("0".equals(apply_type)){//机构
                StringBuffer sys_dept_auth_sql=new StringBuffer("SELECT da.id,da.deptRecord_id from sys_dept_auth da WHERE da.id=? ");
                LinkedMap sys_dept_auth_lm = new LinkedMap();
                sys_dept_auth_lm.put(1, apply_id);
                net.sf.json.JSONObject sys_dept_auth_json = ur.queryOneCustom(sys_dept_auth_sql.toString(), sys_dept_auth_lm);
                if (null ==sys_dept_auth_json){
                    continue;
                }
                String deptRecord_id = sys_dept_auth_json.getString("deptRecord_id");
                deptRecords.add(deptRecord_id);
                deptAuths.add(apply_id);
            }else {
                StringBuffer sys_user_auth_sql=new StringBuffer("SELECT usa.id,usa.userRecord_id from   sys_user_auth usa where usa.id=? ");
                LinkedMap sys_user_auth_lm = new LinkedMap();
                sys_user_auth_lm.put(1, apply_id);
                net.sf.json.JSONObject sys_user_auth_json = ur.queryOneCustom(sys_user_auth_sql.toString(), sys_user_auth_lm);
                if (null ==sys_user_auth_json){
                    continue;
                }
                String userRecord_id = sys_user_auth_json.getString("userRecord_id");
                userRecords.add(userRecord_id);
                userAuths.add(apply_id);

            }
        }
        if (authIds !=null && !authIds.isEmpty()){
             authDao.deleteBatchIds(authIds);
        }
        if (userRecords !=null && !userRecords.isEmpty()){
            userRecordDao.deleteBatchIds(userRecords);
        }
        if (userAuths !=null && !userAuths.isEmpty()){
            userAuditDao.deleteBatchIds(userAuths);
        }
        if (deptRecords !=null && !deptRecords.isEmpty()){
            deptRecordDao.deleteBatchIds(deptRecords);
        }
        if (deptAuths !=null && !deptAuths.isEmpty()){
            deptAuditDao.deleteBatchIds(deptAuths);
        }

    }

    private UserInfo getUserInfo(net.sf.json.JSONObject user_json) {
        UserInfo userInfo=new UserInfo();
        userInfo.setName( user_json.containsKey("name_auth") ? user_json.getString("name_auth") : null);//姓名
        userInfo.setPhoneNumber(user_json.containsKey("phone_number_auth") ? user_json.getString("phone_number_auth") : null);//电话
        userInfo.setPost( user_json.containsKey("post_auth") ? user_json.getString("post_auth") : null);//职位
        if (user_json.containsKey("adcd_auth") && !SummitTools.stringIsNull(user_json.getString("adcd_auth"))){
            String adcd_auth = user_json.getString("adcd_auth");
            String[] adcds = adcd_auth.split(",");
            userInfo.setAdcds(adcds);//行政区划
        }
        userInfo.setUserName(user_json.containsKey("userName_auth") ? user_json.getString("userName_auth") : null);//用户名
        if (user_json.containsKey("dept_auth") && !SummitTools.stringIsNull(user_json.getString("dept_auth"))){
            String dept_auth = user_json.getString("dept_auth");
            String[] depts = dept_auth.split(",");
            userInfo.setDepts(depts);//部门
        }
        userInfo.setDuty(user_json.containsKey("duty_auth") ? user_json.getString("duty_auth") : null);//岗位
        userInfo.setEmail(user_json.containsKey("email_auth") ? user_json.getString("email_auth") : null);//邮箱
        userInfo.setSex(user_json.containsKey("sex_auth") ? user_json.getString("sex_auth") : null);//性别
        userInfo.setHeadPortrait( user_json.containsKey("headPortrait_auth") ? user_json.getString("headPortrait_auth") : null);//头像
        if (user_json.containsKey("is_enabled_auth") && !SummitTools.stringIsNull(user_json.getString("is_enabled_auth"))){
            userInfo.setIsEnabled(Integer.parseInt(user_json.getString("is_enabled_auth")));
        }
        return userInfo;

    }

    private net.sf.json.JSONObject queryUserByApplyId(String apply_id) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT userauth.userName_auth,userauth.name_auth,userauth.sex_auth,userauth.password_auth,userauth.email_auth,userauth.phone_number_auth,userauth.is_enabled_auth, ");
        sql.append("userauth.headPortrait_auth,userauth.duty_auth,userauth.dept_auth,userauth.adcd_auth,userauth.post_auth,userauth.isAudited,userauth.state_auth ");
        sql.append("from sys_user_auth userauth  where userauth.id=? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, apply_id);
        net.sf.json.JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return  jsonObject;
    }

    private net.sf.json.JSONObject queryDeptByApplyId(String apply_id) throws Exception {
        StringBuffer sql=new StringBuffer(" SELECT deptAuth.id,deptAuth.deptId_auth,deptAuth.pId_auth,deptAuth.deptcode_auth,deptAuth.deptName_auth,deptAuth.adcd_auth,deptAuth.auth_person,deptAuth.auth_time,deptAuth.submitted_to ,deptAuth.deptType_auth,deptAuth.deptHead_auth,deptAuth.remark ");
        sql.append("from sys_dept_auth  deptAuth WHERE deptAuth.id= ? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, apply_id);
        net.sf.json.JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return  jsonObject;
    }


    //对比用户
    private List<JSONObject> compareToUser(net.sf.json.JSONObject new_userJson, net.sf.json.JSONObject old_userJson, String applyname,String username) {
       // List<Map<String,JSONObject>> compares=new ArrayList<>();
        List<JSONObject> compares=new ArrayList<>();
        if (null !=new_userJson && new_userJson.containsKey("name_auth") && !SummitTools.stringIsNull(new_userJson.getString("name_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("name") && !SummitTools.stringIsNull(old_userJson.getString("name"))){
                String name_auth = new_userJson.getString("name_auth");
                String name = old_userJson.getString("name");
                if (!name_auth.equals(name)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","姓名");
                    comapre.put("old",name);
                    comapre.put("new",name_auth);
                    /*Map<String,JSONObject> map=new HashMap<>();
                    map.put("name",comapre);*/
                    compares.add(comapre);
                }

            }
        }
        if (null !=new_userJson && new_userJson.containsKey("sex_auth") && !SummitTools.stringIsNull(new_userJson.getString("sex_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("sex") && !SummitTools.stringIsNull(old_userJson.getString("sex"))){
                String sex_auth = new_userJson.getString("sex_auth");
                String sex = old_userJson.getString("sex");
                if (!sex_auth.equals(sex)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","性别");
                    comapre.put("old",sex);
                    comapre.put("new",sex_auth);
                    /*Map<String,JSONObject> map=new HashMap<>();
                    map.put("sex",comapre);*/
                    compares.add(comapre);
                }

            }
        }
        if (null !=new_userJson && new_userJson.containsKey("email_auth") && !SummitTools.stringIsNull(new_userJson.getString("email_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("email") && !SummitTools.stringIsNull(old_userJson.getString("email"))){
                String email_auth = new_userJson.getString("email_auth");
                String email = old_userJson.getString("email");
                if (!email_auth.equals(email)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","邮箱");
                    comapre.put("old",email);
                    comapre.put("new",email_auth);
                    /*Map<String,JSONObject> map=new HashMap<>();
                    map.put("email",comapre);*/
                    compares.add(comapre);
                }

            }
        }
        if (null !=new_userJson && new_userJson.containsKey("phone_number_auth") && !SummitTools.stringIsNull(new_userJson.getString("phone_number_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("phoneNumber") && !SummitTools.stringIsNull(old_userJson.getString("phoneNumber"))){
                String phone_number_auth = new_userJson.getString("phone_number_auth");
                String phone_number = old_userJson.getString("phoneNumber");
                if (!phone_number_auth.equals(phone_number)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","电话号码");
                    comapre.put("old",phone_number);
                    comapre.put("new",phone_number_auth);
                    compares.add(comapre);
                }

            }
        }

        if (null !=new_userJson && new_userJson.containsKey("is_enabled_auth") && !new_userJson.containsKey("state_auth")){
            if (null !=old_userJson && old_userJson.containsKey("is_enable")){
                String is_enabled_auth = new_userJson.getString("is_enabled_auth");
                String is_enabled = old_userJson.getString("is_enable");
                if(!is_enabled_auth.equals(is_enabled)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","是否启用");
                    comapre.put("old",is_enabled);
                    comapre.put("new",is_enabled_auth);
                    compares.add(comapre);
                }

            }
        }
        if (null !=new_userJson && new_userJson.containsKey("is_enabled_auth")&& new_userJson.containsKey("state_auth")){
            if (null !=old_userJson && old_userJson.containsKey("is_enable")&& old_userJson.containsKey("state")){
                String is_enabled_auth = new_userJson.getString("is_enabled_auth");
                String is_enabled = old_userJson.getString("is_enable");
                String state_auth = new_userJson.getString("state_auth");
                String state = old_userJson.getString("state");
                if (!is_enabled_auth.equals(is_enabled) && !state_auth.equals(state)) {
                    JSONObject comapre = new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id", "用户状态");
                    comapre.put("old", "正常");
                    comapre.put("new", "已删除");
                    compares.add(comapre);
                }
            }
        }




        if (null !=new_userJson && new_userJson.containsKey("duty_auth") && !SummitTools.stringIsNull(new_userJson.getString("duty_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("duty") && !SummitTools.stringIsNull(old_userJson.getString("duty"))){
                String duty_auth = new_userJson.getString("duty_auth");
                String duty = old_userJson.getString("duty");
                if (!duty_auth.equals(duty)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","岗位");
                    comapre.put("old",duty);
                    comapre.put("new",duty_auth);
                    /*Map<String,JSONObject> map=new HashMap<>();
                    map.put("duty",comapre);*/
                    compares.add(comapre);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("post_auth") && !SummitTools.stringIsNull(new_userJson.getString("post_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("post") && !SummitTools.stringIsNull(old_userJson.getString("post"))){
                String post_auth = new_userJson.getString("post_auth");
                String post = old_userJson.getString("post");
                if (!post_auth.equals(post)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","职位");
                    comapre.put("old",post);
                    comapre.put("new",post_auth);
                   /* Map<String,JSONObject> map=new HashMap<>();
                    map.put("post",comapre);*/
                    compares.add(comapre);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("dept_auth") && !SummitTools.stringIsNull(new_userJson.getString("dept_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("dept") && !SummitTools.stringIsNull(old_userJson.getString("dept"))){
                String dept_auth = new_userJson.getString("dept_auth");
                String deptname = old_userJson.getString("dept");
                if (!dept_auth.equals(deptname)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","部门");
                    comapre.put("old",deptname);
                    comapre.put("new",dept_auth);
                   /* Map<String,JSONObject> map=new HashMap<>();
                    map.put("deptName",comapre);*/
                    compares.add(comapre);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("adcd_auth") && !SummitTools.stringIsNull(new_userJson.getString("adcd_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("adcd") && !SummitTools.stringIsNull(old_userJson.getString("adcd"))){
                String adcd_auth = new_userJson.getString("adcd_auth");
                String adnm = old_userJson.getString("adcd");
                if (!adcd_auth.equals(adnm)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","行政区划");
                    comapre.put("old",adcd_auth);
                    comapre.put("new",adnm);
                  /*  Map<String,JSONObject> map=new HashMap<>();
                    map.put("adnm",comapre);*/
                    compares.add(comapre);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("headPortrait_auth")){
            if (null !=old_userJson && old_userJson.containsKey("headPortrait")){
                String headPortrait_auth = new_userJson.getString("headPortrait_auth");
                String headportrait = old_userJson.getString("headPortrait");
                if (!headPortrait_auth.equals(headportrait)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",applyname);
                    comapre.put("applyusername",username);
                    comapre.put("id","头像");
                    comapre.put("old",headportrait);
                    comapre.put("new",headPortrait_auth);
                    compares.add(comapre);
                }
            }
        }
        if (null !=new_userJson && new_userJson.containsKey("headPortrait_auth")){
            if (null !=old_userJson && !old_userJson.containsKey("headPortrait")){
                String headPortrait_auth = new_userJson.getString("headPortrait_auth");
                JSONObject comapre=new JSONObject();
                comapre.put("applyname",applyname);
                comapre.put("applyusername",username);
                comapre.put("id","头像");
                comapre.put("old","");
                comapre.put("new",headPortrait_auth);
                compares.add(comapre);
            }
        }
        if (null !=new_userJson && !new_userJson.containsKey("headPortrait_auth")){
            if (null !=old_userJson && old_userJson.containsKey("headPortrait")){
                String headportrait = old_userJson.getString("headPortrait");
                JSONObject comapre=new JSONObject();
                comapre.put("applyname",applyname);
                comapre.put("applyusername",username);
                comapre.put("id","头像");
                comapre.put("old",headportrait);
                comapre.put("new","");
                compares.add(comapre);
            }
        }
        return compares;
    }

    //对比机构
    private List<JSONObject> compareToDept(net.sf.json.JSONObject new_deptJson, net.sf.json.JSONObject old_deptJson,String deptName,String deptCode) {
        List<JSONObject> compares=new ArrayList<>();
        if (null !=new_deptJson && new_deptJson.containsKey("pId_auth") && !SummitTools.stringIsNull(new_deptJson.getString("pId_auth"))){
            if (null !=old_deptJson && old_deptJson.containsKey("pid") && !SummitTools.stringIsNull(old_deptJson.getString("pid"))){
                String pId_auth = new_deptJson.getString("pId_auth");
                String pid = old_deptJson.getString("pid");
                if (!pId_auth.equals(pid)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",deptName);
                    comapre.put("applyusername",deptCode);
                    comapre.put("id","上级部门");
                    comapre.put("old",pid);
                    comapre.put("new",pId_auth);
                    compares.add(comapre);
                }
            }

        }
        if (null !=new_deptJson && new_deptJson.containsKey("remark")){
            if (null !=old_deptJson && old_deptJson.containsKey("remark")){
                String newRemark = new_deptJson.getString("remark");
                String oldRemark = old_deptJson.getString("remark");
                if (!newRemark.equals(oldRemark)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",deptName);
                    comapre.put("applyusername",deptCode);
                    comapre.put("id","备注");
                    comapre.put("old",oldRemark);
                    comapre.put("new",newRemark);
                    compares.add(comapre);
                }
            }
        }
        if (null !=new_deptJson && new_deptJson.containsKey("remark")){
            if (null !=old_deptJson && !old_deptJson.containsKey("remark")){
                String newRemark = new_deptJson.getString("remark");
                JSONObject comapre=new JSONObject();
                comapre.put("applyname",deptName);
                comapre.put("applyusername",deptCode);
                comapre.put("id","备注");
                comapre.put("old","");
                comapre.put("new",newRemark);
                compares.add(comapre);
            }
        }

        if (null !=new_deptJson && !new_deptJson.containsKey("remark")){
            if (null !=old_deptJson && old_deptJson.containsKey("remark")){
                String oldRemark = old_deptJson.getString("remark");
                JSONObject comapre=new JSONObject();
                comapre.put("applyname",deptName);
                comapre.put("applyusername",deptCode);
                comapre.put("id","备注");
                comapre.put("old",oldRemark);
                comapre.put("new","");
                compares.add(comapre);
            }
        }
        if (null !=new_deptJson && new_deptJson.containsKey("deptHead_auth")){
            if (null !=old_deptJson && old_deptJson.containsKey("deptHead")){
                String deptHead_auth = new_deptJson.getString("deptHead_auth");
                String deptHead = old_deptJson.getString("deptHead");
                if (!deptHead_auth.equals(deptHead)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",deptName);
                    comapre.put("applyusername",deptCode);
                    comapre.put("id","机构联系人");
                    comapre.put("old",deptHead);
                    comapre.put("new",deptHead_auth);
                    compares.add(comapre);
                }
            }
        }
        if (null !=new_deptJson && new_deptJson.containsKey("deptHead_auth")){
            if (null !=old_deptJson && !old_deptJson.containsKey("deptHead")){
                String deptHead_auth = new_deptJson.getString("deptHead_auth");
                JSONObject comapre=new JSONObject();
                comapre.put("applyname",deptName);
                comapre.put("applyusername",deptCode);
                comapre.put("id","机构联系人");
                comapre.put("old","");
                comapre.put("new",deptHead_auth);
                compares.add(comapre);
            }
        }
        if (null !=new_deptJson && !new_deptJson.containsKey("deptHead_auth")){
            if (null !=old_deptJson && old_deptJson.containsKey("deptHead")){
                String deptHead = old_deptJson.getString("deptHead");
                JSONObject comapre=new JSONObject();
                comapre.put("applyname",deptName);
                comapre.put("applyusername",deptCode);
                comapre.put("id","机构联系人");
                comapre.put("old",deptHead);
                comapre.put("new","");
                compares.add(comapre);
            }
        }

        if (null !=new_deptJson && new_deptJson.containsKey("deptcode_auth") && !SummitTools.stringIsNull(new_deptJson.getString("deptcode_auth"))){
            if (null !=old_deptJson && old_deptJson.containsKey("deptcode") && !SummitTools.stringIsNull(old_deptJson.getString("deptcode"))){
                String deptcode_auth = new_deptJson.getString("deptcode_auth");
                String deptcode = old_deptJson.getString("deptcode");
                if (!deptcode_auth.equals(deptcode)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",deptName);
                    comapre.put("applyusername",deptCode);
                    comapre.put("id","机构编码");
                    comapre.put("old",deptcode);
                    comapre.put("new",deptcode_auth);
                    //Map<String,JSONObject> map=new HashMap<>();
                   // map.put("deptCode",comapre);
                    compares.add(comapre);
                    //index++;
                }
            }
        }
        if (null !=new_deptJson && new_deptJson.containsKey("deptName_auth")&& !SummitTools.stringIsNull(new_deptJson.getString("deptName_auth"))){
            if (null !=old_deptJson && old_deptJson.containsKey("deptName") && !SummitTools.stringIsNull(old_deptJson.getString("deptName"))){
                String deptName_auth = new_deptJson.getString("deptName_auth");
                String deptname = old_deptJson.getString("deptName");
                if (!deptName_auth.equals(deptname)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",deptName);
                    comapre.put("applyusername",deptCode);
                    comapre.put("id","机构名称");
                    comapre.put("old",deptname);
                    comapre.put("new",deptName_auth);
                 /*   Map<String,JSONObject> map=new HashMap<>();
                    map.put("deptName",comapre);*/
                    compares.add(comapre);
                   // index++;
                }
            }

        }
        if (null !=new_deptJson && new_deptJson.containsKey("adcd_auth")&& !SummitTools.stringIsNull(new_deptJson.getString("adcd_auth"))){
            if (null !=old_deptJson && old_deptJson.containsKey("adcd") && !SummitTools.stringIsNull(old_deptJson.getString("adcd"))){
                String adcd_auth = new_deptJson.getString("adcd_auth");
                String adcd = old_deptJson.getString("adcd");
                if (!adcd_auth.equals(adcd)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",deptName);
                    comapre.put("applyusername",deptCode);
                    comapre.put("id","行政区划");
                    comapre.put("old",adcd);
                    comapre.put("new",adcd_auth);
                    /*Map<String,JSONObject> map=new HashMap<>();
                    map.put("adcd",comapre);*/
                    compares.add(comapre);
                   // index++;
                }
            }

        }
        if (null !=new_deptJson && new_deptJson.containsKey("deptType_auth") && !SummitTools.stringIsNull(new_deptJson.getString("deptType_auth"))){
            if (null !=old_deptJson && old_deptJson.containsKey("deptType") && !SummitTools.stringIsNull(old_deptJson.getString("deptType"))){
                String deptType_auth = new_deptJson.getString("deptType_auth");
                String deptType = old_deptJson.getString("deptType");
                if (!deptType_auth.equals(deptType)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("applyname",deptName);
                    comapre.put("applyusername",deptCode);
                    comapre.put("id","机构类型");
                    comapre.put("old",deptType);
                    comapre.put("new",deptType_auth);
                  /*  Map<String,JSONObject> map=new HashMap<>();
                    map.put("deptType",comapre);*/
                    compares.add(comapre);
                   // index++;
                }
            }

        }

      return compares;
    }












}
