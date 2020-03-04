package com.summit.service.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.summit.common.Common;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.repository.UserRepository;
import com.summit.util.SummitTools;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserAuditService {

    @Autowired
    UserInfoCache userInfoCache;
    @Autowired
    private UserRepository ur;
    @Autowired
    private SummitTools st;
    @Autowired
    public JdbcTemplate jdbcTemplate;

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
}
