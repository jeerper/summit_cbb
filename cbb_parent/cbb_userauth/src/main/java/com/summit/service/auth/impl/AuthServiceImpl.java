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
import com.summit.repository.UserRepository;
import com.summit.service.auth.AuthService;
import com.summit.service.dept.DeptsService;
import com.summit.util.SummitTools;
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

    @Override
    public Page<AuthBean> queryByPage(Integer currentPage, Integer pageSize, JSONObject paramJson) throws Exception {
        List rolesList = null;
        if (Common.getLogUser() != null) {
            rolesList = Arrays.asList(Common.getLogUser().getRoles());
        }
        String depts = deptsService.currentDeptService();
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
        StringBuffer auth_sql=new StringBuffer("SELECT auth.id,auth.apply_type,auth.apply_Id,user.NAME as apply_name,date_format(auth.apply_time, '%Y-%m-%d %H:%i:%s') as apply_time from sys_auth auth INNER JOIN sys_user user on auth.apply_name=user.USERNAME where auth.id=? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, id);
        net.sf.json.JSONObject auth_json = ur.queryOneCustom(auth_sql.toString(), lm);
        String apply_type = auth_json.getString("apply_type");
        String apply_id = auth_json.getString("apply_Id");
        Map<String,Object> map =new HashMap<>();
        map.put("apply_name",auth_json.getString("apply_name"));
        map.put("apply_time",auth_json.getString("apply_time"));
        if ("0".equals(apply_type)){//机构
            StringBuffer deptAuth_sql=new StringBuffer("select du.id,du.deptId_auth,dept.DEPTNAME as pId_auth,du.deptcode_auth,du.deptName_auth,adcd.ADNM as adcd_auth,du.auth_person,dic.NAME AS deptType_auth,dic1.NAME as isAudited from  ");
            deptAuth_sql.append("sys_dept_auth du LEFT JOIN sys_dept dept on du.pId_auth=dept.ID LEFT JOIN sys_ad_cd adcd ");
            deptAuth_sql.append("on du.adcd_auth=adcd.ADCD LEFT JOIN  sys_dictionary dic on dic.PCODE='dept_type' and du.deptType_auth=dic.CKEY ");
            deptAuth_sql.append("LEFT JOIN  sys_dictionary dic1 on dic1.PCODE='isAudited' and du.isAudited=dic1.CKEY ");
            deptAuth_sql.append("where du.id=? ");
            LinkedMap deptAuth_lm = new LinkedMap();
            deptAuth_lm.put(1, apply_id);
            net.sf.json.JSONObject new_deptJson = ur.queryOneCustom(deptAuth_sql.toString(), deptAuth_lm);
            String old_deptId = new_deptJson.getString("deptId_auth");
            StringBuffer dept_sql=new StringBuffer("SELECT dept.ID, deptName.DEPTNAME AS PID, dept.DEPTCODE,dept.DEPTNAME,adcd.ADNM as ADCD,dic.NAME as deptType,dept.DEPTHEAD  from sys_dept dept ");
            dept_sql.append(" LEFT JOIN sys_dept deptName  on dept.PID=deptName.ID  LEFT JOIN sys_ad_cd adcd on dept.ADCD=adcd.ADCD ");
            dept_sql.append(" LEFT JOIN  sys_dictionary dic on dic.PCODE='dept_type' and dept.deptType=dic.CKEY ");
            dept_sql.append("where dept.id=? ");
            LinkedMap dept_lm = new LinkedMap();
            dept_lm.put(1, old_deptId);
            net.sf.json.JSONObject old_deptJson = ur.queryOneCustom(dept_sql.toString(), dept_lm);
            List<JSONObject> json=compareToDept(new_deptJson,old_deptJson);
            map.put("updateType","机构基础类型");
            map.put("updateContent",json);
            map.put("isAudited",new_deptJson.getString("isAudited"));
            return map;
        }else if ("1".equals(apply_type)){//用户基础信息
            map.put("updateType","人员基础类型");
            StringBuffer userAuth_sql=new StringBuffer("SELECT usa.id,usa.userName_auth,usa.name_auth,dic.NAME as sex_auth,usa.password_auth,usa.email_auth,usa.phone_number_auth,dic2.NAME as is_enabled_auth,usa.headPortrait_auth,usa.duty_auth,dept.DEPTNAME AS dept_auth,adcd.ADNM as adcd_auth,usa.post_auth,dic3.NAME as isAudited ");
            userAuth_sql.append("from sys_user_auth usa LEFT JOIN  sys_dictionary dic on dic.PCODE='sex' and usa.sex_auth=dic.CKEY ");
            userAuth_sql.append("LEFT JOIN sys_dictionary  dic2 on dic2.PCODE='isEnabled' and usa.is_enabled_auth=dic2.CKEY ");
            userAuth_sql.append("LEFT JOIN sys_dictionary  dic3 on dic3.PCODE='isAudited' and usa.isAudited=dic3.CKEY ");
            userAuth_sql.append("LEFT JOIN sys_dept dept on usa.dept_auth=dept.ID ");
            userAuth_sql.append("LEFT JOIN sys_ad_cd adcd on usa.adcd_auth=adcd.ADCD ");
            userAuth_sql.append(" where usa.id=? ");
            LinkedMap userAuth_lm = new LinkedMap();
            userAuth_lm.put(1,apply_id);
            net.sf.json.JSONObject new_userJson = ur.queryOneCustom(userAuth_sql.toString(), userAuth_lm);
            String userName_auth = new_userJson.getString("userName_auth");
            StringBuffer user_sql=new StringBuffer("SELECT user.USERNAME,user.name,dic.NAME as SEX,user.EMAIL,user.PHONE_NUMBER,dic2.name AS IS_ENABLED,user.DUTY,user.POST,dept2.names as DEPTNAME,adcd2.names as ADNM,user.HEADPORTRAIT ");
            user_sql.append("from sys_user user LEFT JOIN sys_dictionary dic on dic.PCODE='sex' and user.SEX=dic.CKEY ");
            user_sql.append("LEFT JOIN sys_dictionary  dic2 on dic2.PCODE='isEnabled' and user.IS_ENABLED=dic2.CKEY ");
            user_sql.append("LEFT JOIN (SELECT USERNAME,GROUP_CONCAT(userDept.DEPTID)AS deptIds, GROUP_CONCAT(dept.DEPTNAME)AS names  FROM sys_user_dept userDept ");
            user_sql.append("inner join sys_dept dept on userDept.DEPTID=dept.ID  GROUP BY USERNAME  )dept2 on user.USERNAME=dept2.USERNAME ");
            user_sql.append("LEFT JOIN (SELECT USERNAME,GROUP_CONCAT(userAdcd.ADCD)AS adcds, GROUP_CONCAT(adcd.ADNM)AS names  FROM sys_user_adcd userAdcd  ");
            user_sql.append("inner join sys_ad_cd adcd on userAdcd.ADCD=adcd.ADCD  GROUP BY USERNAME)adcd2 on user.USERNAME=adcd2.USERNAME ");
            user_sql.append("WHERE user.USERNAME=? ");
            LinkedMap user_lm = new LinkedMap();
            user_lm.put(1, userName_auth);
            net.sf.json.JSONObject old_userJson = ur.queryOneCustom(user_sql.toString(), user_lm);
            List<JSONObject> json=compareToUser(new_userJson,old_userJson);
            map.put("updateContent",json);
            map.put("isAudited",new_userJson.getString("isAudited"));
            return map;
        }
        return null;
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
                        StringBuffer dept_sql=new StringBuffer("UPDATE SYS_DEPT SET PID=?, DEPTCODE=?,DEPTNAME=?,ADCD=?,isAudited=?,deptType=?,DEPTHEAD=? ");
                        dept_sql.append("WHERE id=? ");
                        jdbcTemplate.update(dept_sql.toString(),
                                deptAuth_json.containsKey("pId_auth") ? deptAuth_json.getString("pId_auth") : null,
                                deptAuth_json.containsKey("deptcode_auth") ? deptAuth_json.getString("deptcode_auth") : null,
                                deptAuth_json.containsKey("deptName_auth") ? deptAuth_json.getString("deptName_auth") : null,
                                deptAuth_json.containsKey("adcd_auth") ? deptAuth_json.getString("adcd_auth") : null,
                                isAudited,
                                deptAuth_json.containsKey("deptType_auth") ? deptAuth_json.getString("deptType_auth") : null,
                                deptAuth_json.containsKey("deptHead_auth") ? deptAuth_json.getString("deptHead_auth") : null,
                                deptAuth_json.getString("deptId_auth")
                        );
                    }
                }else if ("1".equals(apply_type)){//用户
                    net.sf.json.JSONObject user_json= queryUserByApplyId(apply_id);
                    if (null != user_json){
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
                    }
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

    private UserInfo getUserInfo(net.sf.json.JSONObject user_json) {
        UserInfo userInfo=new UserInfo();
        userInfo.setName( user_json.getString("name_auth"));//姓名
        userInfo.setPhoneNumber( user_json.getString("phone_number_auth"));//电话
        userInfo.setPost( user_json.getString("post_auth"));//职位
        String adcd_auth = user_json.getString("adcd_auth");
        String[] adcds = adcd_auth.split(",");
        userInfo.setAdcds(adcds);//行政区划
        userInfo.setUserName(user_json.getString("userName_auth"));//用户名
        String dept_auth = user_json.getString("dept_auth");
        String[] depts = dept_auth.split(",");
        userInfo.setDepts(depts);//部门
        userInfo.setDuty(user_json.getString("duty_auth"));//岗位
        userInfo.setEmail(user_json.getString("email_auth"));//邮箱
        userInfo.setSex(user_json.getString("sex_auth"));//性别
        userInfo.setHeadPortrait(user_json.getString("headPortrait_auth"));//头像
        if (user_json.containsKey("is_enabled_auth") && !SummitTools.stringIsNull(user_json.getString("is_enabled_auth"))){
            userInfo.setIsEnabled(Integer.parseInt(user_json.getString("is_enabled_auth")));
        }
        return userInfo;

    }

    private net.sf.json.JSONObject queryUserByApplyId(String apply_id) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT userauth.userName_auth,userauth.name_auth,userauth.sex_auth,userauth.password_auth,userauth.email_auth,userauth.phone_number_auth,userauth.is_enabled_auth, ");
        sql.append("userauth.headPortrait_auth,userauth.duty_auth,userauth.dept_auth,userauth.adcd_auth,userauth.post_auth,userauth.isAudited ");
        sql.append("from sys_user_auth userauth  where userauth.id=? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, apply_id);
        net.sf.json.JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return  jsonObject;
    }

    private net.sf.json.JSONObject queryDeptByApplyId(String apply_id) throws Exception {
        StringBuffer sql=new StringBuffer(" SELECT deptAuth.id,deptAuth.deptId_auth,deptAuth.pId_auth,deptAuth.deptcode_auth,deptAuth.deptName_auth,deptAuth.adcd_auth,deptAuth.auth_person,deptAuth.auth_time,deptAuth.submitted_to ,deptAuth.deptType_auth,deptAuth.deptHead_auth ");
        sql.append("from sys_dept_auth  deptAuth WHERE deptAuth.id= ? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, apply_id);
        net.sf.json.JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return  jsonObject;
    }


    //对比用户
    private List<JSONObject> compareToUser(net.sf.json.JSONObject new_userJson, net.sf.json.JSONObject old_userJson) {
       // List<Map<String,JSONObject>> compares=new ArrayList<>();
        List<JSONObject> compares=new ArrayList<>();
        if (null !=new_userJson && new_userJson.containsKey("name_auth") && !SummitTools.stringIsNull(new_userJson.getString("name_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("name") && !SummitTools.stringIsNull(old_userJson.getString("name"))){
                String name_auth = new_userJson.getString("name_auth");
                String name = old_userJson.getString("name");
                if (!name_auth.equals(name)){
                    JSONObject comapre=new JSONObject();
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
            if (null !=old_userJson && old_userJson.containsKey("SEX") && !SummitTools.stringIsNull(old_userJson.getString("SEX"))){
                String sex_auth = new_userJson.getString("sex_auth");
                String sex = old_userJson.getString("SEX");
                if (!sex_auth.equals(sex)){
                    JSONObject comapre=new JSONObject();
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
            if (null !=old_userJson && old_userJson.containsKey("EMAIL") && !SummitTools.stringIsNull(old_userJson.getString("EMAIL"))){
                String email_auth = new_userJson.getString("email_auth");
                String email = old_userJson.getString("EMAIL");
                if (!email_auth.equals(email)){
                    JSONObject comapre=new JSONObject();
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
            if (null !=old_userJson && old_userJson.containsKey("PHONE_NUMBER") && !SummitTools.stringIsNull(old_userJson.getString("PHONE_NUMBER"))){
                String phone_number_auth = new_userJson.getString("phone_number_auth");
                String phone_number = old_userJson.getString("PHONE_NUMBER");
                if (!phone_number_auth.equals(phone_number)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("id","电话号码");
                    comapre.put("old",phone_number);
                    comapre.put("new",phone_number_auth);
                    /*Map<String,JSONObject> map=new HashMap<>();
                    map.put("phoneNumber",comapre);*/
                    compares.add(comapre);
                }

            }
        }

        if (null !=new_userJson && new_userJson.containsKey("is_enabled_auth") && !SummitTools.stringIsNull(new_userJson.getString("is_enabled_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("IS_ENABLED") && !SummitTools.stringIsNull(old_userJson.getString("IS_ENABLED"))){
                String is_enabled_auth = new_userJson.getString("is_enabled_auth");
                String is_enabled = old_userJson.getString("IS_ENABLED");
                if (!is_enabled_auth.equals(is_enabled)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("id","是否启动");
                    comapre.put("old",is_enabled);
                    comapre.put("new",is_enabled_auth);
                    /*Map<String,JSONObject> map=new HashMap<>();
                    map.put("isEnable",comapre);*/
                    compares.add(comapre);
                }

            }
        }

        if (null !=new_userJson && new_userJson.containsKey("duty_auth") && !SummitTools.stringIsNull(new_userJson.getString("duty_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("DUTY") && !SummitTools.stringIsNull(old_userJson.getString("DUTY"))){
                String duty_auth = new_userJson.getString("duty_auth");
                String duty = old_userJson.getString("DUTY");
                if (!duty_auth.equals(duty)){
                    JSONObject comapre=new JSONObject();
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
            if (null !=old_userJson && old_userJson.containsKey("POST") && !SummitTools.stringIsNull(old_userJson.getString("POST"))){
                String post_auth = new_userJson.getString("post_auth");
                String post = old_userJson.getString("POST");
                if (!post_auth.equals(post)){
                    JSONObject comapre=new JSONObject();
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
            if (null !=old_userJson && old_userJson.containsKey("DEPTNAME") && !SummitTools.stringIsNull(old_userJson.getString("DEPTNAME"))){
                String dept_auth = new_userJson.getString("dept_auth");
                String deptname = old_userJson.getString("DEPTNAME");
                if (!dept_auth.equals(deptname)){
                    JSONObject comapre=new JSONObject();
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
            if (null !=old_userJson && old_userJson.containsKey("ADNM") && !SummitTools.stringIsNull(old_userJson.getString("ADNM"))){
                String adcd_auth = new_userJson.getString("adcd_auth");
                String adnm = old_userJson.getString("ADNM");
                if (!adcd_auth.equals(adnm)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("id","行政区划");
                    comapre.put("old",adcd_auth);
                    comapre.put("new",adnm);
                  /*  Map<String,JSONObject> map=new HashMap<>();
                    map.put("adnm",comapre);*/
                    compares.add(comapre);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("headPortrait_auth") && !SummitTools.stringIsNull(new_userJson.getString("headPortrait_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("HEADPORTRAIT") && !SummitTools.stringIsNull(old_userJson.getString("HEADPORTRAIT"))){
                String headPortrait_auth = new_userJson.getString("headPortrait_auth");
                String headportrait = old_userJson.getString("HEADPORTRAIT");
                if (!headPortrait_auth.equals(headportrait)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("id","头像");
                    comapre.put("old",headportrait);
                    comapre.put("new",headPortrait_auth);
                   /* Map<String,JSONObject> map=new HashMap<>();
                    map.put("headportrait",comapre);*/
                    compares.add(comapre);
                }
            }
        }
        return compares;
    }

    //对比机构
    private List<JSONObject> compareToDept(net.sf.json.JSONObject new_deptJson, net.sf.json.JSONObject old_deptJson) {
        //List<Map<String,JSONObject>> compares=new ArrayList<>();
        List<JSONObject> compares=new ArrayList<>();
        //Integer index=1;
        if (null !=new_deptJson && new_deptJson.containsKey("pId_auth") && !SummitTools.stringIsNull(new_deptJson.getString("pId_auth"))){
            if (null !=old_deptJson && old_deptJson.containsKey("PID") && !SummitTools.stringIsNull(old_deptJson.getString("PID"))){
                String pId_auth = new_deptJson.getString("pId_auth");
                String pid = old_deptJson.getString("PID");
                if (!pId_auth.equals(pid)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("id","上级部门");
                    comapre.put("old",pid);
                    comapre.put("new",pId_auth);
                    //Map<String,JSONObject> map=new HashMap<>();
                   // map.put("superDept",comapre);
                    compares.add(comapre);
                    //index++;
                }
            }

        }
        if (null !=new_deptJson && new_deptJson.containsKey("deptcode_auth") && !SummitTools.stringIsNull(new_deptJson.getString("deptcode_auth"))){
            if (null !=old_deptJson && old_deptJson.containsKey("DEPTCODE") && !SummitTools.stringIsNull(old_deptJson.getString("DEPTCODE"))){
                String deptcode_auth = new_deptJson.getString("deptcode_auth");
                String deptcode = old_deptJson.getString("DEPTCODE");
                if (!deptcode_auth.equals(deptcode)){
                    JSONObject comapre=new JSONObject();
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
            if (null !=old_deptJson && old_deptJson.containsKey("DEPTNAME") && !SummitTools.stringIsNull(old_deptJson.getString("DEPTNAME"))){
                String deptName_auth = new_deptJson.getString("deptName_auth");
                String deptname = old_deptJson.getString("DEPTNAME");
                if (!deptName_auth.equals(deptname)){
                    JSONObject comapre=new JSONObject();
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
            if (null !=old_deptJson && old_deptJson.containsKey("ADCD") && !SummitTools.stringIsNull(old_deptJson.getString("ADCD"))){
                String adcd_auth = new_deptJson.getString("adcd_auth");
                String adcd = old_deptJson.getString("ADCD");
                if (!adcd_auth.equals(adcd)){
                    JSONObject comapre=new JSONObject();
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
