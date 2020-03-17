package com.summit.service.auth.impl;

import cn.hutool.core.util.StrUtil;
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

import java.util.*;

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
            StringBuffer deptAuth_sql=new StringBuffer("select du.id,du.deptId_auth,dept.DEPTNAME as pId_auth,du.deptcode_auth,du.deptName_auth,adcd.ADNM as adcd_auth,du.auth_person,dic.NAME AS deptType_auth from  ");
            deptAuth_sql.append("sys_dept_auth du LEFT JOIN sys_dept dept on du.pId_auth=dept.ID LEFT JOIN sys_ad_cd adcd ");
            deptAuth_sql.append("on du.adcd_auth=adcd.ADCD LEFT JOIN  sys_dictionary dic on dic.PCODE='dept_type' and du.deptType_auth=dic.CKEY ");
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
            List<Map<String,JSONObject>> json=compareToDept(new_deptJson,old_deptJson);
            map.put("updateType","机构基础类型");
            map.put("updateContent",json);
            return map;
        }else if ("1".equals(apply_type)){//用户基础信息
            map.put("updateType","人员基础类型");
            StringBuffer userAuth_sql=new StringBuffer("SELECT usa.id,usa.userName_auth,usa.name_auth,dic.NAME as sex_auth,usa.password_auth,usa.email_auth,usa.phone_number_auth,dic2.NAME as is_enabled_auth,usa.headPortrait_auth,usa.duty_auth,dept.DEPTNAME AS dept_auth,adcd.ADNM as adcd_auth,usa.post_auth ");
            userAuth_sql.append("from sys_user_auth usa LEFT JOIN  sys_dictionary dic on dic.PCODE='sex' and usa.sex_auth=dic.CKEY ");
            userAuth_sql.append("LEFT JOIN sys_dictionary  dic2 on dic2.PCODE='isEnabled' and usa.is_enabled_auth=dic2.CKEY ");
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
            List<Map<String,JSONObject>> json=compareToUser(new_userJson,old_userJson);
            map.put("updateContent",json);
            return map;
        }
        return null;
    }

    //对比用户
    private List<Map<String, JSONObject>> compareToUser(net.sf.json.JSONObject new_userJson, net.sf.json.JSONObject old_userJson) {
        List<Map<String,JSONObject>> compares=new ArrayList<>();
        if (null !=new_userJson && new_userJson.containsKey("name_auth") && !SummitTools.stringIsNull(new_userJson.getString("name_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("NAME") && !SummitTools.stringIsNull(old_userJson.getString("NAME"))){
                String name_auth = new_userJson.getString("name_auth");
                String name = old_userJson.getString("NAME");
                if (!name_auth.equals(name)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",name);
                    comapre.put("new",name_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("name",comapre);
                    compares.add(map);
                }

            }
        }
        if (null !=new_userJson && new_userJson.containsKey("sex_auth") && !SummitTools.stringIsNull(new_userJson.getString("sex_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("SEX") && !SummitTools.stringIsNull(old_userJson.getString("SEX"))){
                String sex_auth = new_userJson.getString("sex_auth");
                String sex = old_userJson.getString("SEX");
                if (!sex_auth.equals(sex)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",sex);
                    comapre.put("new",sex_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("sex",comapre);
                    compares.add(map);
                }

            }
        }
        if (null !=new_userJson && new_userJson.containsKey("email_auth") && !SummitTools.stringIsNull(new_userJson.getString("email_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("EMAIL") && !SummitTools.stringIsNull(old_userJson.getString("EMAIL"))){
                String email_auth = new_userJson.getString("email_auth");
                String email = old_userJson.getString("EMAIL");
                if (!email_auth.equals(email)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",email);
                    comapre.put("new",email_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("email",comapre);
                    compares.add(map);
                }

            }
        }
        if (null !=new_userJson && new_userJson.containsKey("phone_number_auth") && !SummitTools.stringIsNull(new_userJson.getString("phone_number_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("PHONE_NUMBER") && !SummitTools.stringIsNull(old_userJson.getString("PHONE_NUMBER"))){
                String phone_number_auth = new_userJson.getString("phone_number_auth");
                String phone_number = old_userJson.getString("PHONE_NUMBER");
                if (!phone_number_auth.equals(phone_number)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",phone_number);
                    comapre.put("new",phone_number_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("phoneNumber",comapre);
                    compares.add(map);
                }

            }
        }

        if (null !=new_userJson && new_userJson.containsKey("is_enabled_auth") && !SummitTools.stringIsNull(new_userJson.getString("is_enabled_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("IS_ENABLED") && !SummitTools.stringIsNull(old_userJson.getString("IS_ENABLED"))){
                String is_enabled_auth = new_userJson.getString("is_enabled_auth");
                String is_enabled = old_userJson.getString("IS_ENABLED");
                if (!is_enabled_auth.equals(is_enabled)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",is_enabled);
                    comapre.put("new",is_enabled_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("isEnable",comapre);
                    compares.add(map);
                }

            }
        }

        if (null !=new_userJson && new_userJson.containsKey("duty_auth") && !SummitTools.stringIsNull(new_userJson.getString("duty_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("DUTY") && !SummitTools.stringIsNull(old_userJson.getString("DUTY"))){
                String duty_auth = new_userJson.getString("duty_auth");
                String duty = old_userJson.getString("DUTY");
                if (!duty_auth.equals(duty)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",duty);
                    comapre.put("new",duty_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("duty",comapre);
                    compares.add(map);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("post_auth") && !SummitTools.stringIsNull(new_userJson.getString("post_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("POST") && !SummitTools.stringIsNull(old_userJson.getString("POST"))){
                String post_auth = new_userJson.getString("post_auth");
                String post = old_userJson.getString("POST");
                if (!post_auth.equals(post)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",post);
                    comapre.put("new",post_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("post",comapre);
                    compares.add(map);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("dept_auth") && !SummitTools.stringIsNull(new_userJson.getString("dept_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("DEPTNAME") && !SummitTools.stringIsNull(old_userJson.getString("DEPTNAME"))){
                String dept_auth = new_userJson.getString("dept_auth");
                String deptname = old_userJson.getString("DEPTNAME");
                if (!dept_auth.equals(deptname)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",deptname);
                    comapre.put("new",dept_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("deptName",comapre);
                    compares.add(map);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("adcd_auth") && !SummitTools.stringIsNull(new_userJson.getString("adcd_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("ADNM") && !SummitTools.stringIsNull(old_userJson.getString("ADNM"))){
                String adcd_auth = new_userJson.getString("adcd_auth");
                String adnm = old_userJson.getString("ADNM");
                if (!adcd_auth.equals(adnm)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",adcd_auth);
                    comapre.put("new",adnm);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("adnm",comapre);
                    compares.add(map);
                }
            }
        }

        if (null !=new_userJson && new_userJson.containsKey("headPortrait_auth") && !SummitTools.stringIsNull(new_userJson.getString("headPortrait_auth"))){
            if (null !=old_userJson && old_userJson.containsKey("HEADPORTRAIT") && !SummitTools.stringIsNull(old_userJson.getString("HEADPORTRAIT"))){
                String headPortrait_auth = new_userJson.getString("headPortrait_auth");
                String headportrait = old_userJson.getString("HEADPORTRAIT");
                if (!headPortrait_auth.equals(headportrait)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",headportrait);
                    comapre.put("new",headPortrait_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("headportrait",comapre);
                    compares.add(map);
                }
            }
        }
        return compares;
    }

    //对比机构
    private List<Map<String,JSONObject>> compareToDept(net.sf.json.JSONObject new_deptJson, net.sf.json.JSONObject old_deptJson) {
        List<Map<String,JSONObject>> compares=new ArrayList<>();
        //Integer index=1;
        if (null !=new_deptJson && new_deptJson.containsKey("pId_auth") && !SummitTools.stringIsNull(new_deptJson.getString("pId_auth"))){
            if (null !=old_deptJson && old_deptJson.containsKey("PID") && !SummitTools.stringIsNull(old_deptJson.getString("PID"))){
                String pId_auth = new_deptJson.getString("pId_auth");
                String pid = old_deptJson.getString("PID");
                if (!pId_auth.equals(pid)){
                    JSONObject comapre=new JSONObject();
                    comapre.put("old",pid);
                    comapre.put("new",pId_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("superDept",comapre);
                    compares.add(map);
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
                    comapre.put("old",deptcode);
                    comapre.put("new",deptcode_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("deptCode",comapre);
                    compares.add(map);
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
                    comapre.put("old",deptname);
                    comapre.put("new",deptName_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("deptName",comapre);
                    compares.add(map);
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
                    comapre.put("old",adcd);
                    comapre.put("new",adcd_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("adcd",comapre);
                    compares.add(map);
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
                    comapre.put("old",deptType);
                    comapre.put("new",deptType_auth);
                    Map<String,JSONObject> map=new HashMap<>();
                    map.put("deptType",comapre);
                    compares.add(map);
                   // index++;
                }
            }

        }
      return compares;
    }












}
