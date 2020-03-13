package com.summit.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.Common;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.UserAuditBean;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.dao.repository.UserAuditDao;
import com.summit.exception.ErrorMsgException;
import com.summit.repository.UserRepository;
import com.summit.service.dept.DeptsService;
import com.summit.util.SummitTools;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserAuditService {

    @Autowired
    UserInfoCache userInfoCache;
    @Autowired
    private UserRepository ur;
    @Autowired
    private SummitTools st;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DeptsService deptsService;

    @Autowired
    private UserAuditDao userAuditDao;


    @Transactional
    public ResponseCodeEnum userAudit(String id,String username, String isAudited) throws Exception {
        if (SummitTools.stringNotNull(isAudited)){
            //1、修改审核表字段为1或2以及审核人
            StringBuffer sql=new StringBuffer("UPDATE sys_user_auth SET isAudited = ? ");
            if(Common.getLogUser()!=null ){
                sql.append(" ,auth_person=?");
            }
            sql.append(" where id =? ");
            jdbcTemplate.update(sql.toString(),isAudited,Common.getLogUser().getUserName(),id);
            if("1".equals(isAudited)){//审核通过
                //2、修改用户表信息以及审核字段为1
                JSONObject user_json= queryUserByUserName(username);
                if (null != user_json){
                    StringBuffer sql_user = new StringBuffer("UPDATE SYS_USER SET NAME = ?,SEX=?, EMAIL = ?, PHONE_NUMBER =?, IS_ENABLED = ?, LAST_UPDATE_TIME = now() ");
                    sql_user.append(" ,DUTY=?,POST=?,HEADPORTRAIT=?,isAudited=? ");
                    sql_user.append("WHERE USERNAME = ? AND STATE = 1");
                    jdbcTemplate.update(sql_user.toString(),
                            user_json.getString("name_auth"),
                            user_json.getString("sex_auth"),
                            user_json.getString("email_auth"),
                            user_json.getString("phone_number_auth"),
                            user_json.getString("is_enabled_auth"),
                            user_json.getString("duty_auth"),
                            user_json.getString("post_auth"),
                            user_json.getString("headPortrait_auth"),
                            isAudited,
                            username
                            );
                }
                //3、保存行政区划
                String adcdSql = " delete from sys_user_adcd where USERNAME  IN ('" + username + "') ";
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
                                    username,
                                    adcd,
                            };
                            userAdcdParams.add(adcdParam);
                        }
                        jdbcTemplate.batchUpdate(insertAdcdSql, userAdcdParams);

                    }
                }
                //4、保存部门表
                String deptSql = " delete from SYS_USER_DEPT where USERNAME  IN ('" + username + "') ";
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
                                    username,
                                    deptId,
                            };
                            userdeptParams.add(deptParam);
                        }
                        jdbcTemplate.batchUpdate(insertAdcdSql, userdeptParams);
                    }

                }
                //5、设置redis缓存
                UserInfo cacheUserInfo = userInfoCache.getUserInfo(username);
                if (cacheUserInfo != null) {
                    UserInfo userInfo=getUserInfo(user_json);
                    BeanUtil.copyProperties(userInfo, cacheUserInfo, CopyOptions.create().setIgnoreNullValue(true));
                    userInfoCache.setUserInfo(userInfo.getUserName(), cacheUserInfo);
                }
            }else if ("2".equals(isAudited)){//审核不通过
                //todo:1、修改用户表信息以及审核字段为2
                JSONObject user_json= queryUserByUserName(username);
                if (null !=user_json){
                    StringBuffer sql_json=new StringBuffer("UPDATE sys_user SET isAudited = ? ");
                    sql_json.append(" where USERNAME=? ");
                    jdbcTemplate.update(sql_json.toString(),isAudited,username);
                }
            }
            return null;
        }
        return ResponseCodeEnum.CODE_9999;
    }

    private UserInfo getUserInfo(JSONObject user_json) {
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
        return userInfo;
    }

    private JSONObject queryUserByUserName(String username) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT userauth.userName_auth,userauth.name_auth,userauth.sex_auth,userauth.password_auth,userauth.email_auth,userauth.phone_number_auth,userauth.is_enabled_auth, ");
        sql.append("userauth.headPortrait_auth,userauth.duty_auth,userauth.dept_auth,userauth.adcd_auth,userauth.post_auth,userauth.isAudited ");
        sql.append("from sys_user_auth userauth  where userauth.userName_auth=? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, username);
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return  jsonObject;
    }

    public Page<UserAuditBean> queryByPage(Integer currentPage, Integer pageSize, com.alibaba.fastjson.JSONObject paramJson) throws Exception {
        List rolesList = null;
        if (Common.getLogUser() != null) {
            rolesList = Arrays.asList(Common.getLogUser().getRoles());
        }
        String depts = deptsService.currentDeptService();
        if (!rolesList.contains("ROLE_SUPERUSER") && !SummitTools.stringIsNull(depts)){
            StringBuffer sql=new StringBuffer("SELECT  sua.id,sua.userName_auth,sua.name_auth,sua.sex_auth,sua.password_auth,sua.email_auth,sua.phone_number_auth,sua.is_enabled_auth,sua.headPortrait_auth, ");
            sql.append("sua.duty_auth,sua.dept_auth,sua.adcd_auth, sua.post_auth, sua.auth_person,sua.isAudited,date_format(sua.auth_time, '%Y-%m-%d %H:%i:%s')as auth_time,remark,user.NAME as apply_name ");
            sql.append("from  sys_user_auth  sua inner join sys_user user on sua.apply_name=user.USERNAME  where 1=1 ");
            sql.append("and sua.submitted_to in ('"+depts+"')");
            Integer index = 1;
            LinkedMap linkedMap = new LinkedMap();
            List<Object> list = new ArrayList();
            if (paramJson.containsKey("userName")) {
                sql.append(" and sua.userName_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("userName") + "%");
                index++;
            }
            if (paramJson.containsKey("name")) {
                sql.append(" and sua.name_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("name") + "%");
                index++;
            }
            if (paramJson.containsKey("isEnabled")) {
                sql.append(" and sua.is_enabled_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("isEnabled") + "%");
                index++;
            }
            if (paramJson.containsKey("phone")) {
                sql.append(" and sua.phone_number_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("phone") + "%");
                index++;
            }
            list.add(sql.toString());
            list.add(linkedMap);
            Page<Object>  page = ur.queryByCustomPage(list.get(0).toString(), currentPage, pageSize, (LinkedMap) list.get(1));
            if (page != null){
                Page<UserAuditBean> backContent = getBackContent(page);
                return backContent;
            }
        }else if (rolesList.contains("ROLE_SUPERUSER")){
            StringBuffer sql=new StringBuffer("SELECT  sua.id,sua.userName_auth,sua.name_auth,sua.sex_auth,sua.password_auth,sua.email_auth,sua.phone_number_auth,sua.is_enabled_auth,sua.headPortrait_auth, ");
            sql.append("sua.duty_auth,sua.dept_auth,sua.adcd_auth, sua.post_auth, sua.auth_person,sua.isAudited,date_format(sua.auth_time, '%Y-%m-%d %H:%i:%s')as auth_time,remark,user.NAME as apply_name ");
            sql.append("from  sys_user_auth  sua inner join sys_user user on sua.apply_name=user.USERNAME where 1=1 ");
            Integer index = 1;
            LinkedMap linkedMap = new LinkedMap();
            List<Object> list = new ArrayList();
            if (paramJson.containsKey("userName")) {
                sql.append(" and sua.userName_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("userName") + "%");
                index++;
            }
            if (paramJson.containsKey("name")) {
                sql.append(" and sua.name_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("name") + "%");
                index++;
            }
            if (paramJson.containsKey("isEnabled")) {
                sql.append(" and sua.is_enabled_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("isEnabled") + "%");
                index++;
            }
            if (paramJson.containsKey("phone")) {
                sql.append(" and sua.phone_number_auth like ? ");
                linkedMap.put(index, "%" + paramJson.get("phone") + "%");
                index++;
            }
            list.add(sql.toString());
            list.add(linkedMap);
            Page<Object>  page = ur.queryByCustomPage(list.get(0).toString(), currentPage, pageSize, (LinkedMap) list.get(1));
            if (page != null){
                Page<UserAuditBean> backContent = getBackContent(page);
                return backContent;
            }
        }
         return null;
    }


    public Page<UserAuditBean>  getBackContent(Page<Object>  page) throws Exception {
        List<UserAuditBean> userAuditBeans = new ArrayList<>();
        List<JSONObject> contents = new ArrayList<>();
        if (page.getContent() != null && page.getContent().size() > 0) {
            for (Object o : page.getContent()) {
                JSONObject jsonObject = (JSONObject) o;
                String dept_auth = jsonObject.getString("dept_auth");
                List<String> dept_json = new ArrayList<>();
                if (StrUtil.isEmpty(dept_auth)){
                    String[] deptAuths = dept_auth.split(",");
                    for (int j = 0; j < deptAuths.length; j++) {
                        StringBuffer stringBuffer=new StringBuffer("SELECT dept.DEPTNAME from sys_dept dept where dept.ID=? ");
                        LinkedMap lm = new LinkedMap();
                        lm.put(1, deptAuths[j]);
                        JSONObject deptName  = ur.queryOneCustom(stringBuffer.toString(), lm);
                        if(deptName ==null){
                            continue;
                        }
                        String name = deptName.getString("DEPTNAME");
                        dept_json.add(name);
                    }
                    String dept = StringUtils.join(dept_json, ",");
                    jsonObject.put("dept_auth",dept);
                }
                String adcd_auth = jsonObject.getString("adcd_auth");
                List<String> adcd_json = new ArrayList<>();
                if (StrUtil.isEmpty(adcd_auth)){
                    String[] adcdAuths = adcd_auth.split(",");
                    for (int j = 0; j < adcdAuths.length; j++) {
                        StringBuffer stringBuffer=new StringBuffer("SELECT adcd.ADNM from sys_ad_cd adcd where adcd.ADCD=? ");
                        LinkedMap lm = new LinkedMap();
                        lm.put(1, adcdAuths[j]);
                        JSONObject adnm  = ur.queryOneCustom(stringBuffer.toString(), lm);
                        if (adnm==null){
                            continue;
                        }
                        String name = adnm.getString("ADNM");
                        adcd_json.add(name);
                    }
                    String adcd = StringUtils.join(adcd_json, ",");
                    jsonObject.put("adcd_auth",adcd);
                }
                contents.add(jsonObject);
            }
            for (Object o : contents) {
                JSONObject jsonObject = (JSONObject) o;
                /*
                System.out.println(o.toString());
                UserAuditBean userAuditBean = JSON.parseObject(o.toString(),UserAuditBean.class);
                userAuditBean.setDeptAuth(jsonObject.containsKey("dept_auth") ? jsonObject.getString("dept_auth").split(",") : null);
                userAuditBean.setAdcdAuth(jsonObject.containsKey("adcd_auth") ? jsonObject.getString("adcd_auth").split(",") : null);
                userAuditBeans.add(userAuditBean);*/
                UserAuditBean userAuditBean = getUserAuditBean(jsonObject);
                userAuditBeans.add(userAuditBean);
            }
        }
        return new Page<UserAuditBean>(userAuditBeans, page.getPageable());
    }


    public  UserAuditBean getUserAuditBean(JSONObject jsonObject){
        UserAuditBean userAuditBean=new UserAuditBean();
        userAuditBean.setId(jsonObject.containsKey("id") ? jsonObject.getString("id") : null);
        userAuditBean.setDeptAuth(jsonObject.containsKey("dept_auth") ? jsonObject.getString("dept_auth").split(",") : null);
        userAuditBean.setPasswordAuth(jsonObject.containsKey("password_auth") ? jsonObject.getString("password_auth") : null);
        userAuditBean.setHeadPortraitAuth(jsonObject.containsKey("headPortrait_auth") ? jsonObject.getString("headPortrait_auth") : null);
        userAuditBean.setAdcdAuth(jsonObject.containsKey("adcd_auth") ? jsonObject.getString("adcd_auth").split(",") : null);
        if (jsonObject.containsKey("auth_time")){
            Date auth_time = DateUtil.parse(jsonObject.getString("auth_time"));
            userAuditBean.setAuth_time(auth_time);
        }else{
            userAuditBean.setAuth_time(null);
        }
        userAuditBean.setDutyAuth(jsonObject.containsKey("duty_auth") ? jsonObject.getString("duty_auth"): null);
        userAuditBean.setAuthPerson(jsonObject.containsKey("auth_person") ? jsonObject.getString("auth_person"): null);
        userAuditBean.setEmailAuth(jsonObject.containsKey("email_auth") ? jsonObject.getString("email_auth"): null);
        userAuditBean.setIsAudited(jsonObject.containsKey("isAudited") ? jsonObject.getString("isAudited"): null);
        userAuditBean.setIsEnabledAuth(jsonObject.containsKey("is_enabled_auth") ? jsonObject.getString("is_enabled_auth"): null);
        userAuditBean.setNameAuth(jsonObject.containsKey("name_auth") ? jsonObject.getString("name_auth"): null);
        userAuditBean.setPhoneNumberAuth(jsonObject.containsKey("phone_number_auth") ? jsonObject.getString("phone_number_auth"): null);
        userAuditBean.setPostAuth(jsonObject.containsKey("post_auth") ? jsonObject.getString("post_auth"): null);
        userAuditBean.setSexAuth(jsonObject.containsKey("sex_auth") ? jsonObject.getString("sex_auth"): null);
        userAuditBean.setUserNameAuth(jsonObject.containsKey("userName_auth") ? jsonObject.getString("userName_auth"): null);
        userAuditBean.setRemark(jsonObject.containsKey("remark") ? jsonObject.getString("remark"): null);
        userAuditBean.setApplyName(jsonObject.containsKey("apply_name") ? jsonObject.getString("apply_name"): null);
        return userAuditBean;
    }


    public void delUserAudit(List<String> userAuditIds) {
        try {
             userAuditDao.deleteBatchIds(userAuditIds);
        } catch (Exception e) {
            log.error("删除统计信息失败");
            throw new ErrorMsgException("删除统计信息失败");
        }
    }
}
