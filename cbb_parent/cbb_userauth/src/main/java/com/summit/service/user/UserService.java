package com.summit.service.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.summit.cbb.utils.page.Page;
import com.summit.common.Common;
import com.summit.common.entity.*;
import com.summit.common.util.Cryptographic;
import com.summit.exception.ErrorMsgException;
import com.summit.repository.UserRepository;
import com.summit.service.dept.DeptService;
import com.summit.service.dept.DeptsService;
import com.summit.util.CommonUtil;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository ur;
    @Autowired
    private SummitTools st;
    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Value("${password.encode.key}")
    private String key;
    @Autowired
    private DeptService ds;
    @Autowired
    private DeptsService deptsService;
    @Transactional
    public ResponseCodeEnum add(UserInfo userInfo, String key) {
        //保存用户
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String selectUserSql = "SELECT * FROM SYS_USER WHERE USERNAME = ?";
        List<JSONObject> oldUsers = ur.queryAllCustom(selectUserSql, userInfo.getUserName());
        if (st.collectionNotNull(oldUsers) && oldUsers.get(0).getString("STATE").equals("1")){
            return ResponseCodeEnum.CODE_4022;
        }else if (st.collectionNotNull(oldUsers) && oldUsers.get(0).getString("STATE").equals("0")) {
            if (userInfo.getIsEnabled() == null) {
                userInfo.setIsEnabled(1);
            }
            userInfo.setState(1);
            if (userInfo.getPassword() != null && !"".equals(userInfo.getPassword())) {
                //解密
                try {
                    userInfo.setPassword(Cryptographic.decryptAES(userInfo.getPassword(), key));
                } catch (Exception e) {
                    return ResponseCodeEnum.CODE_4014;
                }
            }
            StringBuffer sql = new StringBuffer("UPDATE SYS_USER SET NAME = ?,SEX=?, EMAIL = ?, PHONE_NUMBER =?, NOTE = ?, IS_ENABLED = ?, LAST_UPDATE_TIME = now() ");
            sql.append(" ,STATE =?,COMPANY=?,DUTY=?,POST=?,SN=? ");
            if (SummitTools.stringNotNull(userInfo.getHeadPortrait())) {
                sql.append(" ,HEADPORTRAIT=? ");
            }
            sql.append(" WHERE USERNAME = ? ");
            if (SummitTools.stringNotNull(userInfo.getHeadPortrait())) {
                jdbcTemplate.update(sql.toString(), userInfo.getName(), userInfo.getSex(), userInfo.getEmail(),
                        userInfo.getPhoneNumber(), userInfo.getNote(), userInfo
                                .getIsEnabled(), userInfo.getState(), userInfo.getCompany(), userInfo.getDuty(),
                        userInfo.getPost(), userInfo.getSn(), userInfo.getHeadPortrait(), userInfo.getUserName());
            } else {
                jdbcTemplate.update(sql.toString(), userInfo.getName(), userInfo.getSex(), userInfo.getEmail(),
                        userInfo.getPhoneNumber(), userInfo.getNote(), userInfo
                                .getIsEnabled(), userInfo.getState(), userInfo.getCompany(), userInfo.getDuty(),
                        userInfo.getPost(), userInfo.getSn(), userInfo.getUserName());
            }
            String adcdSql = " delete from sys_user_adcd where USERNAME  IN ('" + userInfo.getUserName() + "') ";
            jdbcTemplate.update(adcdSql);
            //保存行政区划
            if (userInfo.getAdcds() != null && userInfo.getAdcds().length > 0) {
                String insertAdcdSql = "INSERT INTO SYS_USER_ADCD(ID,USERNAME,ADCD,CREATETIME) VALUES ( ?, ?, ?, now())";
                List userAdcdParams = new ArrayList();
                for (String adcd : userInfo.getAdcds()) {
                    Object adcdParam[] = {
                            SummitTools.getKey(),
                            userInfo.getUserName(),
                            adcd,
                    };
                    userAdcdParams.add(adcdParam);
                }
                jdbcTemplate.batchUpdate(insertAdcdSql, userAdcdParams);
            }
            String deptSql = " delete from SYS_USER_DEPT where USERNAME  IN ('" + userInfo.getUserName() + "') ";
            jdbcTemplate.update(deptSql);
            //保存部门
            if (userInfo.getDepts() != null && userInfo.getDepts().length > 0) {
                String insertAdcdSql = "INSERT INTO SYS_USER_DEPT(ID,USERNAME,DEPTID,CREATETIME) VALUES ( ?, ?, ?, now())";
                List userdeptParams = new ArrayList();
                for (String deptId : userInfo.getDepts()) {
                    Object deptParam[] = {
                            SummitTools.getKey(),
                            userInfo.getUserName(),
                            deptId,
                    };
                    userdeptParams.add(deptParam);
                }
                jdbcTemplate.batchUpdate(insertAdcdSql, userdeptParams);
            }
            String uddSql = " delete from sys_user_dept_duty where USERNAME  IN ('" + userInfo.getUserName() + "') ";
            jdbcTemplate.update(uddSql);
            //保存部门，用户, 职位关系表
            if(userInfo.getUserName() !=null && userInfo.getDepts() !=null && !CommonUtil.isEmptyList(Arrays.asList(userInfo.getDepts())) && userInfo.getDuty() !=null && !StrUtil.isBlank(userInfo.getDuty())){
                String insertSql="INSERT INTO sys_user_dept_duty(ID,USERNAME,DEPTID,DUTY) VALUES ( ?, ?, ?, ?)";
                List params = new ArrayList();
                for (String deptId : userInfo.getDepts()) {
                    Object glxxParam[] = {
                            SummitTools.getKey(),
                            userInfo.getUserName(),
                            deptId,
                            userInfo.getDuty(),
                    };
                    params.add(glxxParam);
                }
                jdbcTemplate.batchUpdate(insertSql, params);
            }
        }else {
            if (userInfo.getIsEnabled() == null) {
                userInfo.setIsEnabled(1);
            }
            if (userInfo.getPassword() != null && !"".equals(userInfo.getPassword())) {
                //解密
                try {
                    userInfo.setPassword(Cryptographic.decryptAES(userInfo.getPassword(), key));
                } catch (Exception e) {
                    return ResponseCodeEnum.CODE_4014;
                }
            }

            String sql = "INSERT INTO SYS_USER (USERNAME,NAME,SEX,PASSWORD,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,LAST_UPDATE_TIME,COMPANY,DUTY,POST,SN,HEADPORTRAIT) VALUES ( ?,?, ?, ?, ?, ?, ?, ?, ?,now(), ?, ?, ?, ?,?)";
            jdbcTemplate.update(sql,
                    userInfo.getUserName(),
                    userInfo.getName(),
                    userInfo.getSex(),
                    encoder.encode(userInfo.getPassword()),
                    userInfo.getIsEnabled(),
                    userInfo.getEmail(),
                    userInfo.getPhoneNumber(),
                    1,
                    userInfo.getNote(),
                    userInfo.getCompany(),
                    userInfo.getDuty(),
                    userInfo.getPost(),
                    userInfo.getSn(),
                    userInfo.getHeadPortrait()
            );
            //保存行政区划
            if (userInfo.getAdcds() != null && userInfo.getAdcds().length > 0) {
                String insertAdcdSql = "INSERT INTO SYS_USER_ADCD(ID,USERNAME,ADCD,CREATETIME) VALUES ( ?, ?, ?, now())";
                List userAdcdParams = new ArrayList();
                for (String adcd : userInfo.getAdcds()) {
                    Object glxxParam[] = {
                            SummitTools.getKey(),
                            userInfo.getUserName(),
                            adcd,
                    };
                    userAdcdParams.add(glxxParam);
                }
                jdbcTemplate.batchUpdate(insertAdcdSql, userAdcdParams);
            }
            //保存部门
            if (userInfo.getDepts() != null && userInfo.getDepts().length > 0) {
                String insertAdcdSql = "INSERT INTO SYS_USER_DEPT(ID,USERNAME,DEPTID,CREATETIME) VALUES ( ?, ?, ?, now())";
                List userAdcdParams = new ArrayList();
                for (String deptId : userInfo.getDepts()) {
                    Object glxxParam[] = {
                            SummitTools.getKey(),
                            userInfo.getUserName(),
                            deptId,
                    };
                    userAdcdParams.add(glxxParam);
                }
                jdbcTemplate.batchUpdate(insertAdcdSql, userAdcdParams);
            }
            //保存部门，用户, 职位关系表
            if(userInfo.getUserName() !=null && userInfo.getDepts().length > 0 && userInfo.getDuty() !=null){
                String insertSql="INSERT INTO sys_user_dept_duty(ID,USERNAME,DEPTID,DUTY) VALUES ( ?, ?, ?, ?)";
                List params = new ArrayList();
                for (String deptId : userInfo.getDepts()) {
                    Object glxxParam[] = {
                            SummitTools.getKey(),
                            userInfo.getUserName(),
                            deptId,
                            userInfo.getDuty(),
                    };
                    params.add(glxxParam);
                }
                jdbcTemplate.batchUpdate(insertSql, params);
            }
        }
        return null;
    }


    @Transactional
    public ResponseCodeEnum edit(UserInfo userInfo, String key) {
        if (userInfo.getPassword() != null && !"".equals(userInfo.getPassword())) {
            //解密
            try {
                userInfo.setPassword(Cryptographic.decryptAES(userInfo.getPassword(), key));
            } catch (Exception e) {
                return ResponseCodeEnum.CODE_4014;
            }
        }
        StringBuffer sql = new StringBuffer("UPDATE SYS_USER SET NAME = ?,SEX=?, EMAIL = ?, PHONE_NUMBER =?, NOTE = ?, IS_ENABLED = ?, LAST_UPDATE_TIME = now() ");
        sql.append(" ,COMPANY=?,DUTY=?,POST=?,SN=? ");
        if (SummitTools.stringNotNull(userInfo.getHeadPortrait())) {
            sql.append(" ,HEADPORTRAIT=? ");

        }
        sql.append(" WHERE USERNAME = ? AND STATE = 1 ");
        if (SummitTools.stringNotNull(userInfo.getHeadPortrait())) {
            jdbcTemplate.update(sql.toString(), userInfo.getName(), userInfo.getSex(), userInfo.getEmail(),
                    userInfo.getPhoneNumber(), userInfo.getNote(), userInfo
                            .getIsEnabled(), userInfo.getCompany(), userInfo.getDuty(),
                    userInfo.getPost(), userInfo.getSn(), userInfo.getHeadPortrait(), userInfo.getUserName());
        } else {
            jdbcTemplate.update(sql.toString(), userInfo.getName(), userInfo.getSex(), userInfo.getEmail(),
                    userInfo.getPhoneNumber(), userInfo.getNote(), userInfo
                            .getIsEnabled(), userInfo.getCompany(), userInfo.getDuty(),
                    userInfo.getPost(), userInfo.getSn(), userInfo.getUserName());
        }


        String adcdSql = " delete from sys_user_adcd where USERNAME  IN ('" + userInfo.getUserName() + "') ";
        jdbcTemplate.update(adcdSql);

        //保存行政区划
        if (userInfo.getAdcds() != null && userInfo.getAdcds().length > 0) {
            String insertAdcdSql = "INSERT INTO SYS_USER_ADCD(ID,USERNAME,ADCD,CREATETIME) VALUES ( ?, ?, ?, now())";
            List userAdcdParams = new ArrayList();
            for (String adcd : userInfo.getAdcds()) {
                Object adcdParam[] = {
                        SummitTools.getKey(),
                        userInfo.getUserName(),
                        adcd,
                };
                userAdcdParams.add(adcdParam);
            }
            jdbcTemplate.batchUpdate(insertAdcdSql, userAdcdParams);
        }
        String deptSql = " delete from SYS_USER_DEPT where USERNAME  IN ('" + userInfo.getUserName() + "') ";
        jdbcTemplate.update(deptSql);
        //保存部门
        if (userInfo.getDepts() != null && userInfo.getDepts().length > 0) {
            String insertAdcdSql = "INSERT INTO SYS_USER_DEPT(ID,USERNAME,DEPTID,CREATETIME) VALUES ( ?, ?, ?, now())";
            List userdeptParams = new ArrayList();
            for (String deptId : userInfo.getDepts()) {
                Object deptParam[] = {
                        SummitTools.getKey(),
                        userInfo.getUserName(),
                        deptId,
                };
                userdeptParams.add(deptParam);
            }
            jdbcTemplate.batchUpdate(insertAdcdSql, userdeptParams);
        }


        String uddSql = " delete from sys_user_dept_duty where USERNAME  IN ('" + userInfo.getUserName() + "') ";
        jdbcTemplate.update(uddSql);
        //保存部门，用户, 职位关系表
        if(userInfo.getUserName() !=null && userInfo.getDepts() !=null && !CommonUtil.isEmptyList(Arrays.asList(userInfo.getDepts())) && userInfo.getDuty() !=null && !StrUtil.isBlank(userInfo.getDuty())){
            String insertSql="INSERT INTO sys_user_dept_duty(ID,USERNAME,DEPTID,DUTY) VALUES ( ?, ?, ?, ?)";
            List params = new ArrayList();
            for (String deptId : userInfo.getDepts()) {
                Object glxxParam[] = {
                        SummitTools.getKey(),
                        userInfo.getUserName(),
                        deptId,
                        userInfo.getDuty(),
                };
                params.add(glxxParam);
            }
            jdbcTemplate.batchUpdate(insertSql, params);
        }

        return null;
    }

    public ResponseCodeEnum editImei(String username, String imei) {
        String sql = "UPDATE SYS_USER SET imei = ? WHERE USERNAME = ? AND STATE = 1";
        jdbcTemplate.update(sql, imei, username);
        return null;
    }


    public ResponseCodeEnum editPassword(String userName, String oldPassword,
                                         String password, String repeatPassword, String key) throws Exception {

        UserInfo ub = queryByUserName(userName);
        if (ub == null) {
            return ResponseCodeEnum.CODE_4023;
        }
        String decodePasswordOld = "";
        //解密
        try {
            decodePasswordOld = Cryptographic.decryptAES(oldPassword, key);
        } catch (Exception e) {
            return ResponseCodeEnum.CODE_4014;
        }

        //验证旧密码是否正确
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(decodePasswordOld, ub.getPassword())) {
            return ResponseCodeEnum.CODE_4009;
        }

        try {
            password = Cryptographic.decryptAES(password, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //此处需要新密码解密后再加密保存
        String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
        jdbcTemplate.update(sql, encoder.encode(password), new Date(), userName);

        return null;
    }


    public void del(String userNames) {
        userNames = userNames.replaceAll(",", "','");
        String sql = "UPDATE SYS_USER SET STATE = '0',IS_ENABLED='0', LAST_UPDATE_TIME = ? WHERE USERNAME <> '" + SysConstants.SUPER_USERNAME + "' AND USERNAME IN ('" + userNames + "')";
        jdbcTemplate.update(sql, new Date());
        delUserRoleByUserName(userNames);

//		String adcdSql=" delete from sys_user_adcd where USERNAME  IN ('"+userNames+"') ";
//		jdbcTemplate.update(adcdSql);
//		
		String deptSql=" delete from SYS_USER_DEPT where USERNAME  IN ('"+userNames+"') ";
		jdbcTemplate.update(deptSql);
    }

    public UserInfo queryByUserName(String userName) throws Exception {
        String sql = "SELECT USERNAME ,NAME ,SEX,PASSWORD ,IS_ENABLED ,EMAIL ,PHONE_NUMBER ,STATE ,NOTE ,LAST_UPDATE_TIME,IMEI,COMPANY,DUTY,POST,SN,HEADPORTRAIT FROM SYS_USER WHERE STATE = 1 AND USERNAME = ?";
        LinkedMap linkedMap = new LinkedMap();
        linkedMap.put(1, userName);
        List<Object> dataList = ur.queryAllCustom(sql, linkedMap);
        if (dataList != null && dataList.size() > 0) {
            UserInfo userInfo = JSON.parseObject(dataList.get(0).toString(), new TypeReference<UserInfo>() {
            });
            return userInfo;
        }
        return null;
    }

    public Page<UserInfo> queryByPage(int start, int limit, JSONObject paramJson) throws Exception {
        LinkedMap linkedMap = new LinkedMap();
        Integer index = 1;
        StringBuilder sb = new StringBuilder(
                "SELECT user1.USERNAME,NAME,PASSWORD,SEX,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,COMPANY,DUTY,POST,SN,USERADCD.ADCD,useradcd.adnms,userdept2.DEPTID,userdept2.deptNames,userdept2.deptType,userdept2.deptContact,userRole.roleCodes,userRole.roleNames,date_format(user1.LAST_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') as lastUpdateTime,user1.HEADPORTRAIT FROM SYS_USER user1 ");
        sb.append(" left join (SELECT username,GROUP_CONCAT(useradcd.`adcd`)AS adcd ,GROUP_CONCAT(`adnm`)AS adnms ");
        sb.append(" FROM sys_user_adcd useradcd inner join sys_ad_cd ad on useradcd.adcd=ad.adcd GROUP BY username)useradcd on useradcd.username=user1.USERNAME ");
        sb.append(" left join (SELECT userdept.username,userdept.DEPTID,userdept.deptNames,userdept.deptType,user2.NAME as deptContact from (SELECT username,GROUP_CONCAT(userdept.`deptid`)AS DEPTID,GROUP_CONCAT(dept.`deptname`)AS deptNames,dept.deptType,dept.DEPTHEAD FROM sys_user_dept userdept");
        sb.append(" inner join sys_dept dept on userdept.deptid=dept.id GROUP BY username)userdept  INNER JOIN sys_user user2 on user2.USERNAME=userdept.DEPTHEAD)userdept2  on userdept2.username=user1.USERNAME");
        sb.append(" left join ( SELECT USERNAME,GROUP_CONCAT(userRole.`ROLE_CODE`)AS roleCodes, GROUP_CONCAT(role.NAME)AS roleNames  FROM sys_user_role userRole ");
        sb.append(" inner join sys_role role on userRole.ROLE_CODE=role.CODE  GROUP BY USERNAME)userRole on userRole.USERNAME=user1.USERNAME ");
        sb.append(" WHERE user1.USERNAME <> '");
        sb.append(SysConstants.SUPER_USERNAME);
        sb.append("'");
        if (paramJson.containsKey("name")) {
            sb.append(" AND NAME LIKE ? ");
            linkedMap.put(index, "%" + paramJson.get("name") + "%");
            index++;
        }
        if (paramJson.containsKey("userName")) {
            sb.append(" AND user1.USERNAME LIKE ? ");
            linkedMap.put(index, "%" + paramJson.get("userName") + "%");
            index++;
        }
        if (paramJson.containsKey("isEnabled")) {
            sb.append(" AND IS_ENABLED = ? ");
            linkedMap.put(index, paramJson.get("isEnabled"));
            index++;
        }
        if (paramJson.containsKey("state")) {
            sb.append(" AND STATE =  ? ");
            linkedMap.put(index, paramJson.get("state"));
            index++;
        }
        if (paramJson.containsKey("adcd")) {
            sb.append(" AND USERADCD.ADCD like  ? ");
            linkedMap.put(index, "%" + paramJson.get("adcd") + "%");
            index++;
        }

        if (paramJson.containsKey("phone") && paramJson.getString("phone") != null) {
            sb.append(" and  USER1.PHONE_NUMBER=  ? ");
            linkedMap.put(index, paramJson.get("phone"));
            index++;
        }

        if (paramJson.containsKey("deptName")) {
            sb.append(" AND userdept2.deptNames  like   ? ");
            linkedMap.put(index, "%" + paramJson.get("deptName") + "%");
            index++;
        }

        if (paramJson.containsKey("deptId") && paramJson.getString("deptId") != null) {
            sb.append(" and userdept2.DEPTID= ? ");
            linkedMap.put(index, paramJson.get("deptId"));
            index++;
        }
        if (paramJson.containsKey("sortColumn") && st.stringNotNull(paramJson.getString("sortColumn"))) {
//			if("sn".equalsIgnoreCase(paramJson.getString("sortColumn"))){
//				//sb.append(" order by  sn is null,cast(sn as SIGNED INTEGER) asc ");	
//			}else{
            sb.append(" order by  ");
            sb.append(paramJson.getString("sortColumn"));
            sb.append(" is null , ");
            sb.append(paramJson.getString("sortColumn"));
            sb.append("  ");
            if (paramJson.containsKey("sortName") && st.stringNotNull(paramJson.getString("sortName"))) {
                sb.append(paramJson.getString("sortName"));
            }
            //	}

        }
        System.out.println("sb:"+sb.toString());
        Page<Object> page = ur.queryByCustomPage(sb.toString(), start, limit, linkedMap);
        if (page != null) {
            /*List<UserInfo> userInfoList = new ArrayList<UserInfo>();
            if (page.getContent() != null && page.getContent().size() > 0) {
                for (Object o : page.getContent()) {
                    JSONObject jsonObject = (JSONObject) o;
                    System.out.println(o.toString());
                    UserInfo userInfo = JSON.parseObject(o.toString(), new TypeReference<UserInfo>() {
                    });
                    userInfo.setAdcds(jsonObject.containsKey("ADCD") ? jsonObject.getString("ADCD").split(",") : null);
                    userInfo.setDepts(jsonObject.containsKey("DEPTID") ? jsonObject.getString("DEPTID").split(",") : null);
                    userInfoList.add(userInfo);
                }
            }
            return new Page<UserInfo>(userInfoList, page.getPageable());*/
            Page<UserInfo> backContent = getBackContent(page);
            return backContent;

        }
        return null;
    }

    private Page<UserInfo> getBackContent(Page<Object> page) {
        List<UserInfo> userInfos = new ArrayList<>();
        if (page.getContent() != null && page.getContent().size() > 0) {
            for (Object o : page.getContent()) {
                JSONObject jsonObject = (JSONObject) o;
                UserInfo userInfo=new UserInfo();
                userInfo.setUserName(jsonObject.containsKey("USERNAME") ? jsonObject.getString("USERNAME") : null);
                userInfo.setName(jsonObject.containsKey("NAME") ? jsonObject.getString("NAME") : null);
                userInfo.setSex(jsonObject.containsKey("SEX") ? jsonObject.getString("SEX") : null);
                if (jsonObject !=null && SummitTools.stringNotNull(jsonObject.getString("IS_ENABLED"))){
                    userInfo.setIsEnabled(Integer.parseInt(jsonObject.containsKey("IS_ENABLED") ? jsonObject.getString("IS_ENABLED") : null));
                }
                userInfo.setEmail(jsonObject.containsKey("EMAIL") ? jsonObject.getString("EMAIL") : null);
                userInfo.setPhoneNumber(jsonObject.containsKey("PHONE_NUMBER") ? jsonObject.getString("PHONE_NUMBER") : null);
                if (jsonObject !=null && SummitTools.stringNotNull(jsonObject.getString("STATE"))){
                    userInfo.setState(Integer.parseInt(jsonObject.containsKey("STATE") ? jsonObject.getString("STATE") : null));
                }
                userInfo.setNote(jsonObject.containsKey("NOTE") ? jsonObject.getString("NOTE") : null);
                userInfo.setCompany(jsonObject.containsKey("COMPANY") ? jsonObject.getString("COMPANY") : null);
                userInfo.setDuty(jsonObject.containsKey("DUTY") ? jsonObject.getString("DUTY") : null);
                userInfo.setPost(jsonObject.containsKey("POST") ? jsonObject.getString("POST") : null);
                if (jsonObject !=null && jsonObject.containsKey("SN")&& SummitTools.stringNotNull(jsonObject.getString("SN"))){
                    userInfo.setSn(Integer.parseInt(jsonObject.containsKey("SN") ? jsonObject.getString("SN") : null));
                }
                if (jsonObject !=null && jsonObject.containsKey("PASSWORD")&& SummitTools.stringNotNull(jsonObject.getString("PASSWORD"))){
                    String password = jsonObject.getString("PASSWORD");
                    userInfo.setPassword("123456");
                }
                userInfo.setAdcds(jsonObject.containsKey("ADCD") ? jsonObject.getString("ADCD").split(",") : null);
                userInfo.setAdnms(jsonObject.containsKey("adnms") ? jsonObject.getString("adnms") : null);
                userInfo.setDepts(jsonObject.containsKey("DEPTID") ? jsonObject.getString("DEPTID").split(",") : null);
                userInfo.setDeptNames(jsonObject.containsKey("deptNames") ? jsonObject.getString("deptNames"): null);
                userInfo.setDeptType(jsonObject.containsKey("deptType") ? jsonObject.getString("deptType"): null);
                userInfo.setRoles(jsonObject.containsKey("roleCodes") ? jsonObject.getString("roleCodes").split(",") : null);
                userInfo.setRoleNames(jsonObject.containsKey("roleNames") ? jsonObject.getString("roleNames"): null);
                userInfo.setLastUpdateTime(jsonObject.containsKey("lastUpdateTime") ? jsonObject.getString("lastUpdateTime"): null);
                userInfo.setHeadPortrait(jsonObject.containsKey("HEADPORTRAIT") ? jsonObject.getString("HEADPORTRAIT"): null);
                userInfo.setDeptContact(jsonObject.containsKey("deptContact") ? jsonObject.getString("deptContact"): null);
                userInfos.add(userInfo);
            }
        }
        return new Page<UserInfo>(userInfos, page.getPageable());
    }


    public List<UserInfo> queryUserInfoList(JSONObject paramJson) throws Exception {
        StringBuilder sb = new StringBuilder(
                "SELECT user1.USERNAME,NAME,SEX,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,COMPANY,DUTY,POST,SN,USERADCD.ADCD,useradcd.adnms,userdept.DEPTID,userdept.deptNames,HEADPORTRAIT FROM SYS_USER user1 ");
        sb.append(" left join (SELECT username,GROUP_CONCAT(useradcd.`adcd`)AS adcd ,GROUP_CONCAT(`adnm`)AS adnms ");
        sb.append(" FROM sys_user_adcd useradcd inner join sys_ad_cd ad on useradcd.adcd=ad.adcd GROUP BY username)useradcd on useradcd.username=user1.USERNAME");
        sb.append(" left join (SELECT username,GROUP_CONCAT(userdept.`deptid`)AS DEPTID,GROUP_CONCAT(dept.`deptname`)AS deptNames FROM sys_user_dept userdept ");
        sb.append(" inner join sys_dept dept on userdept.deptid=dept.id  GROUP BY username)userdept on userdept.username=user1.USERNAME");
        sb.append(" WHERE user1.USERNAME <> '");
        sb.append(SysConstants.SUPER_USERNAME);
        sb.append("'");
        if (paramJson.containsKey("name")) {
            sb.append(" AND NAME LIKE '%" + paramJson.get("name") + "%'");
        }
        if (paramJson.containsKey("userName")) {
            sb.append(" AND user1.USERNAME LIKE '%" + paramJson.get("userName") + "%'");
        }
        if (paramJson.containsKey("isEnabled")) {
            sb.append(" AND IS_ENABLED = '" + paramJson.get("isEnabled") + "'");
        }
        if (paramJson.containsKey("state")) {
            sb.append(" AND STATE = '" + paramJson.get("state") + "'");
        }
        List<Object> list = ur.queryAllCustom(sb.toString(), new LinkedMap());
        if (list != null && list.size() > 0) {
            List<UserInfo> userInfoList = new ArrayList<UserInfo>();
            for (Object o : list) {
                JSONObject jsonObject = (JSONObject) o;
                UserInfo userInfo = JSON.parseObject(o.toString(), new TypeReference<UserInfo>() {
                });
                userInfo.setAdcds(jsonObject.containsKey("ADCD") ? jsonObject.getString("ADCD").split(",") : null);
                userInfo.setDepts(jsonObject.containsKey("DEPTID") ? jsonObject.getString("DEPTID").split(",") : null);
                userInfoList.add(userInfo);
            }
            return userInfoList;
        }
        return null;
    }

    public void resetPassword(String userNames, String password, String key) {
        userNames = userNames.replaceAll(",", "','");
        String password1 = Cryptographic.decryptAES(password, key);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ?";
        jdbcTemplate.update(sql, encoder.encode(password1), new Date(),
                userNames);
    }

    public List<String> queryRoleByUserName(String userName) {
        List<String> list = new ArrayList<String>();
        String sql = "SELECT ROLE_CODE FROM SYS_USER_ROLE WHERE USERNAME = ?";
        List<JSONObject> l = ur.queryAllCustom(sql, userName);
        for (JSONObject o : l) {
            list.add(st.objJsonGetString(o, "ROLE_CODE"));
        }
        return list;
    }

    public List<String> queryRoleListByUserName(String userName) {
        List<String> list = new ArrayList<String>();
        String sql = "SELECT ROLE_CODE as 'key' FROM SYS_USER_ROLE WHERE USERNAME = ?";
        List<JSONObject> l = ur.queryAllCustom(sql, userName);
        for (JSONObject o : l) {
            list.add(st.objJsonGetString(o, "key"));
        }
        return list;

    }

    public JSONObject queryAdcdByUserName(String userName) {
        String sql = "SELECT USERADCD.ADCD, ADCDS.ADNM FROM SYS_USER_ADCD USERADCD inner join SYS_AD_CD ADCDS  ON USERADCD.ADCD=ADCDS.ADCD  WHERE USERNAME = ?";
        List<JSONObject> userAdcdList = ur.queryAllCustom(sql, userName);
        if (userAdcdList != null && userAdcdList.size() > 0) {
            String adnms = "";
            String[] adcds = new String[userAdcdList.size()];
            int i = 0;
            for (JSONObject userAdcdObject : userAdcdList) {
                adcds[i] = st.objJsonGetString(userAdcdObject, "ADCD");
                if (userAdcdObject.containsKey("ADNM") && !st.stringIsNull(userAdcdObject.getString("ADNM"))) {
                    adnms += st.objJsonGetString(userAdcdObject, "ADNM") + ",";
                }
                i++;
            }
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("adcds", adcds);
            if (adnms != null && !"".equals(adnms)) {
                jsonobject.put("adnms", adnms.substring(0, adnms.length() - 1));
            }
            return jsonobject;
        }
        return null;
    }

    public JSONObject queryDeptByUserName(String userName) {
        String sql = "SELECT DEPTID,DEPTNAME FROM SYS_USER_DEPT userdept inner join sys_dept dept on userdept.DEPTID=dept.ID WHERE USERNAME = ?";
        List<JSONObject> userDeltList = ur.queryAllCustom(sql, userName);
        if (userDeltList != null && userDeltList.size() > 0) {
            String deptnames = "";
            String[] deptIds = new String[userDeltList.size()];
            int i = 0;
            for (JSONObject userdeptObject : userDeltList) {
                deptIds[i] = st.objJsonGetString(userdeptObject, "DEPTID");
                if (userdeptObject.containsKey("DEPTNAME") && !st.stringIsNull(userdeptObject.getString("DEPTNAME"))) {
                    deptnames += st.objJsonGetString(userdeptObject, "DEPTNAME") + ",";
                }
                i++;
            }
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("deptIds", deptIds);
            if (deptnames != null && !"".equals(deptnames)) {
                jsonobject.put("deptnames", deptnames.substring(0, deptnames.length() - 1));
            }
            return jsonobject;
        }
        return null;
    }

    public void delUserRoleByUserName(String userName) {
        String sql = "DELETE FROM SYS_USER_ROLE WHERE USERNAME =?";
        jdbcTemplate.update(sql, userName);
    }

    @Transactional
    public void grantRole(String userName, String[] role) {
        String deleteSql = "DELETE FROM SYS_USER_ROLE WHERE USERNAME =?";
        jdbcTemplate.update(deleteSql, userName);
        if (role != null && role.length > 0) {
            String sql = "INSERT INTO SYS_USER_ROLE (ID, USERNAME, ROLE_CODE) VALUES (?, ?, ?)";
            List<Object[]> batchArgs = new ArrayList<Object[]>();
            for (String roleCode : role) {
                batchArgs.add(new Object[]{SummitTools.getKey(), userName, roleCode});
            }
            jdbcTemplate.batchUpdate(sql, batchArgs);
        }
    }


    public List<String> getFunByUserName(String userName) {
        List<String> list = new ArrayList<String>();
        String sql = "SELECT DISTINCT SF.ID FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ?";
        List<JSONObject> l = ur.queryAllCustom(sql, userName);
        for (JSONObject o : l) {
            list.add(st.objJsonGetString(o, "ID"));
        }
        return list;
    }

    public List<FunctionBean> getFunInfoByUserName(String userName, boolean isSuroleCode) throws Exception {
        LinkedMap linkedMap = new LinkedMap();
        Integer index = 1;
        String sql = "";
        if (isSuroleCode) {
            sql = "SELECT  SF.* FROM SYS_FUNCTION SF  WHERE SF.IS_ENABLED = '1' and id!='root' ORDER BY FDESC ";
        } else {
            sql = "SELECT  SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ? ORDER BY FDESC";
            linkedMap.put(index, userName);
        }
        List list = ur.queryAllCustom(sql, linkedMap);
        if (list != null) {
            ArrayList<FunctionBean> functionBeans = JSON.parseObject(list.toString(), new TypeReference<ArrayList<FunctionBean>>() {
            });
            return functionBeans;
        }
        return null;
    }

    public List<UserDeptDutyBean> queryDutyByDpetId(String deptId) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT * FROM sys_user_dept_duty udd where udd.DEPTID=? ");
        LinkedMap lm = new LinkedMap();
        lm.put(1,deptId);
        List list = ur.queryAllCustom(sql.toString(), lm);
        ArrayList<UserDeptDutyBean> userDeptDutyBeans =null;
        if (list !=null){
            userDeptDutyBeans=JSON.parseObject(list.toString(), new TypeReference<ArrayList<UserDeptDutyBean>>() {
            });
        }
        return userDeptDutyBeans;
    }

    public UserDeptDutyBean editUserQueryDutyByDpetId(String username) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT * FROM sys_user_dept_duty udd where udd.USERNAME=?");
        LinkedMap lm = new LinkedMap();
        lm.put(1,username);
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        UserDeptDutyBean userDeptDutyBean=new UserDeptDutyBean();
        if (jsonObject !=null){
            userDeptDutyBean.setId(jsonObject.getString("ID"));
            userDeptDutyBean.setDeptId("DEPTID");
            userDeptDutyBean.setDuty(jsonObject.getString("DUTY"));
            userDeptDutyBean.setUsername(jsonObject.getString("USERNAME"));
        }
        return userDeptDutyBean;
    }

    @Transactional
    public ResponseCodeEnum editAudit(UserAuditBean userAuditBean, String key) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userAuditBean.getPasswordAuth() != null && !"".equals(userAuditBean.getPasswordAuth())) {
            //解密
            try {
                userAuditBean.setPasswordAuth(Cryptographic.decryptAES(userAuditBean.getPasswordAuth(), key));
            } catch (Exception e) {
                return ResponseCodeEnum.CODE_4014;
            }
        }
        JSONObject old_user=queryOldUserByUserName(userAuditBean.getUserNameAuth());
        String recordId = IdWorker.getIdStr();
        boolean b =insertSysUserRecord(old_user,recordId);
        if (!b){
            return ResponseCodeEnum.CODE_9999;
        }
        StringBuilder sbAdcd = new StringBuilder();
        String[] adcdAuths = userAuditBean.getAdcdAuth();
        if (adcdAuths !=null && adcdAuths.length>0){
            for (int i = 0; i <adcdAuths.length; i++) {
                if (i<adcdAuths.length-1){
                    sbAdcd.append(adcdAuths[i]+",");
                }else {
                    sbAdcd.append(adcdAuths[i]);
                }

            }
        }
        String adcdAuth = sbAdcd.toString();
        //处理部门
        StringBuilder sbDept = new StringBuilder();
        if (userAuditBean.getDeptAuth() !=null){
            String[] deptAuths = userAuditBean.getDeptAuth();
            for (int i = 0; i <deptAuths.length; i++) {
                if (i<deptAuths.length-1){
                    sbDept.append(deptAuths[i]+",");
                }else {
                    sbDept.append(deptAuths[i]);
                }

            }
        }else if (Common.getLogUser()!=null){
            String[] depts = Common.getLogUser().getDepts();
            for (int i = 0; i <depts.length; i++) {
                if (i<depts.length-1){
                    sbDept.append(depts[i]+",");
                }else {
                    sbDept.append(depts[i]);
                }
            }
        }
        String deptAuth = sbDept.toString();
        String sql="INSERT INTO sys_user_auth (id,userName_auth,name_auth,sex_auth,password_auth,email_auth,phone_number_auth,is_enabled_auth,headPortrait_auth,duty_auth,dept_auth,adcd_auth,post_auth,auth_person,isAudited,auth_time,submitted_to,remark,apply_name,userRecord_id) VALUES " +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?)";
        //查找原来头像
        UserInfo userInfo = queryByUserName(userAuditBean.getUserNameAuth());
        //上级部门
        JSONObject jsonObject=queryBySuperDeptByUserName(Common.getLogUser().getUserName());
        String  superDept=null;
        if (jsonObject !=null && !SummitTools.stringIsNull( jsonObject.getString("ID"))){
            superDept=jsonObject.getString("ID");
        }
        String idStr = IdWorker.getIdStr();
        try{
            if (SummitTools.stringNotNull(userAuditBean.getHeadPortraitAuth())) {
                jdbcTemplate.update(sql,
                        idStr,
                        userAuditBean.getUserNameAuth(),
                        userAuditBean.getNameAuth(),
                        userAuditBean.getSexAuth(),
                        userAuditBean.getPasswordAuth(),
                        userAuditBean.getEmailAuth(),
                        userAuditBean.getPhoneNumberAuth(),
                        userAuditBean.getIsEnabledAuth(),
                        userAuditBean.getHeadPortraitAuth(),
                        userAuditBean.getDutyAuth(),
                        deptAuth,
                        adcdAuth,
                        userAuditBean.getPostAuth(),
                        null,
                        "0",
                        superDept,
                        userAuditBean.getRemark(),
                        userAuditBean.getUserNameAuth(),
                        recordId
                );
            }else {
                jdbcTemplate.update(sql,
                        idStr,
                        userAuditBean.getUserNameAuth(),
                        userAuditBean.getNameAuth(),
                        userAuditBean.getSexAuth(),
                        userAuditBean.getPasswordAuth(),
                        userAuditBean.getEmailAuth(),
                        userAuditBean.getPhoneNumberAuth(),
                        userAuditBean.getIsEnabledAuth(),
                        userInfo.getHeadPortrait(),
                        userAuditBean.getDutyAuth(),
                        deptAuth,
                        adcdAuth,
                        userAuditBean.getPostAuth(),
                        null,
                        "0",
                        superDept,
                        userAuditBean.getRemark(),
                        userAuditBean.getUserNameAuth(),
                        recordId
                );
            }
            //修改用户表中的audit字段为发起申请
            StringBuffer sql2=new StringBuffer("UPDATE SYS_USER SET isAudited = ? where USERNAME=? ");
            jdbcTemplate.update(sql2.toString(),"0",userAuditBean.getUserNameAuth());
        }catch (Exception e){
            logger.error("修改用户失败:", e);
            return ResponseCodeEnum.CODE_9991;
        }
        //保存审核表
        String sql_auth="INSERT INTO sys_auth(id,apply_name,apply_type,submitted_to,isAudited,apply_time,apply_Id ) VALUES (?,?,?,?,?,now(),?) ";
        try{
            jdbcTemplate.update(sql_auth,
                    IdWorker.getIdStr(),
                     Common.getLogUser().getUserName(),
                    "1",
                    superDept,
                    "0",
                    idStr
            );
        }catch (Exception e){
            logger.error("修改部门失败:", e);
            return ResponseCodeEnum.CODE_9999;
        }
        return null;

    }

    private boolean insertSysUserRecord(JSONObject old_user, String recordId) {
        String sql_user_record="INSERT INTO sys_user_record(username,name,sex,password,email,phoneNumber,is_enable,headPortrait,duty,post,dept,adcd,id,state) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        try{
            jdbcTemplate.update(sql_user_record,
                    old_user.containsKey("USERNAME") ? old_user.getString("USERNAME") : null,
                    old_user.containsKey("NAME") ? old_user.getString("NAME") : null,
                    old_user.containsKey("SEX") ? old_user.getString("SEX") : null,
                    old_user.containsKey("PASSWORD") ? old_user.getString("PASSWORD") : null,
                    old_user.containsKey("EMAIL") ? old_user.getString("EMAIL") : null,
                    old_user.containsKey("PHONE_NUMBER") ? old_user.getString("PHONE_NUMBER") : null,
                    old_user.containsKey("IS_ENABLED") ? old_user.getString("IS_ENABLED") : null,
                    old_user.containsKey("HEADPORTRAIT") ? old_user.getString("HEADPORTRAIT") : null,
                    old_user.containsKey("DUTY") ? old_user.getString("DUTY") : null,
                    old_user.containsKey("POST") ? old_user.getString("POST") : null,
                    old_user.containsKey("deptIds") ? old_user.getString("deptIds") : null,
                    old_user.containsKey("adcds") ? old_user.getString("adcds") : null,
                    recordId,
                    old_user.containsKey("STATE") ? old_user.getString("STATE") : null

            );
        }catch (Exception e){
            logger.error("保存用户记录表失败:", e);
            return false;
        }
        return true;
    }

    private JSONObject queryOldUserByUserName(String userNameAuth) throws Exception {
        StringBuffer user_sql=new StringBuffer("SELECT user.USERNAME,user.NAME,user.SEX,user.PASSWORD,user.EMAIL,user.PHONE_NUMBER,user.IS_ENABLED,user.STATE,user.DUTY,user.POST,user.HEADPORTRAIT,userdept2.deptIds,userAdcd1.adcds  from sys_user user ");
        user_sql.append("LEFT JOIN (SELECT userdept.username,GROUP_CONCAT(userdept.deptid)AS deptIds,GROUP_CONCAT(dept.deptname)AS deptNames FROM sys_user_dept userdept ");
        user_sql.append("inner join sys_dept dept on userdept.deptid=dept.id GROUP BY username)userdept2 on user.USERNAME=userdept2.username ");
        user_sql.append("LEFT JOIN(SELECT USERNAME,GROUP_CONCAT(userAdcd.ADCD)AS adcds, GROUP_CONCAT(adcd.ADNM)AS names  FROM sys_user_adcd userAdcd  inner join sys_ad_cd ");
        user_sql.append("adcd on userAdcd.ADCD=adcd.ADCD  GROUP BY USERNAME)userAdcd1 on user.USERNAME=userAdcd1.USERNAME ");
        user_sql.append("WHERE user.USERNAME= ? ");
        LinkedMap lm=new LinkedMap();
        lm.put(1,userNameAuth);
        JSONObject jsonObject = ur.queryOneCustom(user_sql.toString(), lm);
        return jsonObject;
    }
    private JSONObject queryOldUserStateByUserName(String userNameAuth) throws Exception {
        StringBuffer user_sql=new StringBuffer("SELECT user.USERNAME,user.NAME,user.IS_ENABLED,user.STATE from sys_user user ");
        user_sql.append("WHERE user.USERNAME= ? ");
        LinkedMap lm=new LinkedMap();
        lm.put(1,userNameAuth);
        JSONObject jsonObject = ur.queryOneCustom(user_sql.toString(), lm);
        return jsonObject;
    }




    private JSONObject queryBySuperDeptByUserName(String userNameAuth) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT superDept.ID,superDept.DEPTCODE,superDept.DEPTNAME from sys_dept superDept INNER JOIN ");
        sql.append("(SELECT dept.ID,dept.PID from sys_user user INNER JOIN sys_user_dept userDpet on user.USERNAME =userDpet.USERNAME ");
        sql.append("INNER JOIN sys_dept dept on userDpet.DEPTID=dept.ID WHERE user.USERNAME=? )dept on dept.PID=superDept.ID ");
        LinkedMap lm = new LinkedMap();
        lm.put(1, userNameAuth);
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        return  jsonObject;
    }

    public Page<UserInfo> queryUserByPage(int start, int limit, JSONObject paramJson) throws Exception {
        List<String> dept_ids =new ArrayList<>();
        if (paramJson.containsKey("deptId") && paramJson.getString("deptId") != null) {
            String deptIds = paramJson.getString("deptId");
            if (deptIds.contains(",")){//多个部门
                String[] list = deptIds.split(",");
                List<String> deptIdList = Arrays.asList(list);
                for (String deptId:deptIdList){
                    com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
                    jsonObject.put("pdept",deptId);
                    List<String> depts = deptsService.getAllDeptByPdept(jsonObject);
                    if (!CommonUtil.isEmptyList(depts)){
                        for (String dept_id:depts){
                            dept_ids.add(dept_id);
                        }
                    }
                }
            }else {//一个部门
                com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
                jsonObject.put("pdept",deptIds);
                List<String> depts = deptsService.getAllDeptByPdept(jsonObject);
                if (!CommonUtil.isEmptyList(depts)){
                    for (String dept_id:depts){
                        dept_ids.add(dept_id);
                    }
                }
            }
        }else {
            String currentDeptService = deptsService.getCurrentDeptService();
            com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
            jsonObject.put("pdept",currentDeptService);
            List<String> depts = deptsService.getAllDeptByPdept(jsonObject);
            if (!CommonUtil.isEmptyList(depts)){
                for (String dept_id:depts){
                    dept_ids.add(dept_id);
                }
            }
        }
        CommonUtil.removeDuplicate(dept_ids);//去重
        String deptIds = String.join("','", dept_ids);
        String usernames=queryUserNamesByDeptIds(deptIds);
        //userNames=ds.queryUserNamesByCurrentDeptId();
        if (usernames !=null && StrUtil.isNotBlank(usernames)){
            LinkedMap linkedMap = new LinkedMap();
            Integer index = 1;
            StringBuilder sb = new StringBuilder("SELECT user1.USERNAME,NAME,PASSWORD,SEX,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,COMPANY,DUTY,POST,SN,USERADCD.ADCD,useradcd.adnms,userdept2.DEPTID,userdept2.deptNames,userdept2.deptType,userdept2.deptContact,userRole.roleCodes,userRole.roleNames,date_format(user1.LAST_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') as lastUpdateTime,user1.HEADPORTRAIT FROM SYS_USER user1 ");
            sb.append(" left join (SELECT username,GROUP_CONCAT(useradcd.`adcd`)AS adcd ,GROUP_CONCAT(`adnm`)AS adnms ");
            sb.append(" FROM sys_user_adcd useradcd inner join sys_ad_cd ad on useradcd.adcd=ad.adcd GROUP BY username)useradcd on useradcd.username=user1.USERNAME ");
            sb.append(" left join (SELECT userdept.username,userdept.DEPTID,userdept.deptNames,userdept.deptType,user2.NAME as deptContact from (SELECT username,GROUP_CONCAT(userdept.`deptid`)AS DEPTID,GROUP_CONCAT(dept.`deptname`)AS deptNames,dept.deptType,dept.DEPTHEAD FROM sys_user_dept userdept");
            sb.append(" inner join sys_dept dept on userdept.deptid=dept.id GROUP BY username)userdept  left JOIN sys_user user2 on user2.USERNAME=userdept.DEPTHEAD)userdept2  on userdept2.username=user1.USERNAME");
            sb.append(" left join ( SELECT USERNAME,GROUP_CONCAT(userRole.`ROLE_CODE`)AS roleCodes, GROUP_CONCAT(role.NAME)AS roleNames  FROM sys_user_role userRole ");
            sb.append(" inner join sys_role role on userRole.ROLE_CODE=role.CODE  GROUP BY USERNAME)userRole on userRole.USERNAME=user1.USERNAME ");
            sb.append(" WHERE user1.USERNAME <> '");
            sb.append(SysConstants.SUPER_USERNAME);
            sb.append("'");
            sb.append(" and user1.USERNAME IN('"+usernames+"') ");
            if (paramJson.containsKey("name")) {
                sb.append(" AND NAME LIKE ? ");
                linkedMap.put(index, "%" + paramJson.get("name") + "%");
                index++;
            }
            if (paramJson.containsKey("userName")) {
                sb.append(" AND user1.USERNAME LIKE ? ");
                linkedMap.put(index, "%" + paramJson.get("userName") + "%");
                index++;
            }
            if (paramJson.containsKey("isEnabled")) {
                sb.append(" AND IS_ENABLED = ? ");
                linkedMap.put(index, paramJson.get("isEnabled"));
                index++;
            }
            if (paramJson.containsKey("state")) {
                sb.append(" AND STATE =  ? ");
                linkedMap.put(index, paramJson.get("state"));
                index++;
            }
            if (paramJson.containsKey("adcd")) {
                sb.append(" AND USERADCD.ADCD like  ? ");
                linkedMap.put(index, "%" + paramJson.get("adcd") + "%");
                index++;
            }

            if (paramJson.containsKey("phone") && paramJson.getString("phone") != null) {
                sb.append(" and  USER1.PHONE_NUMBER=  ? ");
                linkedMap.put(index, paramJson.get("phone"));
                index++;
            }

            if (paramJson.containsKey("deptName") && paramJson.getString("deptName") != null) {
                sb.append(" AND userdept2.deptNames  like   ? ");
                linkedMap.put(index, "%" + paramJson.get("deptName") + "%");
                index++;
            }


            if (paramJson.containsKey("sortColumn") && st.stringNotNull(paramJson.getString("sortColumn"))) {
//			if("sn".equalsIgnoreCase(paramJson.getString("sortColumn"))){
//				//sb.append(" order by  sn is null,cast(sn as SIGNED INTEGER) asc ");
//			}else{
                sb.append(" order by  ");
                sb.append(paramJson.getString("sortColumn"));
                sb.append(" is null , ");
                sb.append(paramJson.getString("sortColumn"));
                sb.append("  ");
                if (paramJson.containsKey("sortName") && st.stringNotNull(paramJson.getString("sortName"))) {
                    sb.append(paramJson.getString("sortName"));
                }
                //	}

            }
            Page<Object> page = ur.queryByCustomPage(sb.toString(), start, limit, linkedMap);
            if (page != null) {
            /*List<UserInfo> userInfoList = new ArrayList<UserInfo>();
            if (page.getContent() != null && page.getContent().size() > 0) {
                for (Object o : page.getContent()) {
                    JSONObject jsonObject = (JSONObject) o;
                    System.out.println(o.toString());
                    UserInfo userInfo = JSON.parseObject(o.toString(), new TypeReference<UserInfo>() {
                    });
                    userInfo.setAdcds(jsonObject.containsKey("ADCD") ? jsonObject.getString("ADCD").split(",") : null);
                    userInfo.setDepts(jsonObject.containsKey("DEPTID") ? jsonObject.getString("DEPTID").split(",") : null);
                    userInfoList.add(userInfo);
                }
            }
            return new Page<UserInfo>(userInfoList, page.getPageable());*/
                Page<UserInfo> backContent = getBackContent(page);
                return backContent;

            }
            return null;

        }
        return null;
    }

    private String queryUserNamesByDeptIds(String deptIds) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT DISTINCT ud.USERNAME FROM sys_user_dept ud where ");
        sql.append("ud.DEPTID IN('"+deptIds+"')");
        List<Object> l = ur.queryAllCustom(sql.toString(), new LinkedMap());
        List<String> userNames = new ArrayList<String>();
        if (l.size() > 0) {
            for (Object username : l) {
                userNames.add(((JSONObject) username).getString("USERNAME"));
            }
        }
        String username = String.join("','", userNames);
        return username;
    }

    public List<UserInfo> queryUsersByCurrentLoginName() throws Exception {
        String user_names = ds.queryUserNamesByCurrentLoginDeptId();
        List<UserInfo> userInfoList = new ArrayList<>();
        if (StrUtil.isNotBlank(user_names)){
            StringBuffer sql = new StringBuffer("SELECT user.USERNAME,user.NAME,user.PHONE_NUMBER from sys_user user where user.USERNAME <> 'admin' and user.USERNAME IN ('"+user_names+"')");
            List<Object> users = ur.queryAllCustom(sql.toString(), new LinkedMap());
            for (Object o : users) {
                UserInfo userInfo = JSON.parseObject(o.toString(), new TypeReference<UserInfo>() {});
                userInfoList.add(userInfo);
            }
        }
        return userInfoList;
    }
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor = {Exception.class} )
    public void delAuth(String userNames) throws Exception {
        //上级部门
        JSONObject jsonObject=queryBySuperDeptByUserName(Common.getLogUser().getUserName());
        String  superDept=null;
        if (jsonObject !=null && !SummitTools.stringIsNull( jsonObject.getString("ID"))){
            superDept=jsonObject.getString("ID");
        }
        if (userNames.contains(",")){
            for (String username : userNames.split(",")) {
                JSONObject old_user=queryOldUserStateByUserName(username);
                String recordId = IdWorker.getIdStr();
                boolean b =insertSysUserRecord(old_user,recordId);
                if (!b){
                    throw new ErrorMsgException("删除审批失败");
                }
                String idStr = IdWorker.getIdStr();
                String user_auth="INSERT INTO sys_user_auth (id,userName_auth,name_auth,is_enabled_auth,state_auth,auth_person,isAudited,auth_time,submitted_to,apply_name,userRecord_id) VALUES " +
                        "(?,?,?,?,?,?,?,now(),?,?,?)";
                jdbcTemplate.update(user_auth,
                        idStr,
                        username,
                        old_user.containsKey("NAME") ? old_user.getString("NAME") : null,
                        "0",
                        "0",
                        null,
                        "0",
                        superDept,
                        Common.getLogUser().getUserName(),
                        recordId
                );
                //修改用户表中的audit字段为发起申请
                StringBuffer update_user=new StringBuffer("UPDATE SYS_USER SET isAudited = ? where USERNAME=? ");
                jdbcTemplate.update(update_user.toString(),"0",username);
                //保存审核表
                String sql_auth="INSERT INTO sys_auth(id,apply_name,apply_type,submitted_to,isAudited,apply_time,apply_Id ) VALUES (?,?,?,?,?,now(),?) ";
                jdbcTemplate.update(sql_auth,
                        IdWorker.getIdStr(),
                        Common.getLogUser().getUserName(),
                        "1",
                        superDept,
                        "0",
                        idStr
                );
            }

        }else {
            JSONObject old_user=queryOldUserStateByUserName(userNames);
            String recordId = IdWorker.getIdStr();
            boolean b =insertSysUserRecord(old_user,recordId);
            if (!b){
                throw new ErrorMsgException("删除审批失败");
            }
            String idStr = IdWorker.getIdStr();
            String user_auth="INSERT INTO sys_user_auth (id,userName_auth,name_auth,is_enabled_auth,state_auth,auth_person,isAudited,auth_time,submitted_to,apply_name,userRecord_id) VALUES " +
                    "(?,?,?,?,?,?,?,now(),?,?,?)";
            jdbcTemplate.update(user_auth,
                    idStr,
                    userNames,
                    old_user.containsKey("NAME") ? old_user.getString("NAME") : null,
                    "0",
                    "0",
                    null,
                    "0",
                    superDept,
                    Common.getLogUser().getUserName(),
                    recordId
            );
            //修改用户表中的audit字段为发起申请
            StringBuffer update_user=new StringBuffer("UPDATE SYS_USER SET isAudited = ? where USERNAME=? ");
            jdbcTemplate.update(update_user.toString(),"0",userNames);
            //保存审核表
            String sql_auth="INSERT INTO sys_auth(id,apply_name,apply_type,submitted_to,isAudited,apply_time,apply_Id ) VALUES (?,?,?,?,?,now(),?) ";
            jdbcTemplate.update(sql_auth,
                    IdWorker.getIdStr(),
                    Common.getLogUser().getUserName(),
                    "1",
                    superDept,
                    "0",
                    idStr
            );
        }

    }
}
