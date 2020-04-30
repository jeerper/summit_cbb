package com.summit.util;

import cn.hutool.core.util.StrUtil;
import com.summit.common.entity.DeptAuditBean;
import com.summit.common.entity.UserAuditBean;
import com.summit.repository.UserRepository;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EditInvalidUtil {
    @Autowired
    private UserRepository ur;


    /**
     *
     * @param deptAuditBean
     * @return  true 说明是无效编辑
     * @throws Exception
     */
    public  boolean editDeptInvalid(DeptAuditBean deptAuditBean) throws Exception {
        StringBuffer sql=new StringBuffer("select * from sys_dept dept where 1=1  ");
        LinkedMap lm=new LinkedMap();
        Integer index =1;
        if(!SummitTools.stringIsNull(deptAuditBean.getDeptcodeAuth())){
            sql.append(" and dept.DEPTCODE=? ");
            lm.put(index,deptAuditBean.getDeptcodeAuth());
            index ++;
        }

        if(!SummitTools.stringIsNull(deptAuditBean.getDeptNameAuth())){
            sql.append(" and dept.DEPTNAME=? ");
            lm.put(index,deptAuditBean.getDeptNameAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(deptAuditBean.getPIdAuth())){
            sql.append(" and dept.PID=? ");
            lm.put(index,deptAuditBean.getPIdAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(deptAuditBean.getDeptHeadAuth())){
            sql.append(" and dept.DEPTHEAD=? ");
            lm.put(index,deptAuditBean.getDeptHeadAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(deptAuditBean.getDeptTypeAuth())){
            sql.append(" and dept.deptType=? ");
            lm.put(index,deptAuditBean.getDeptTypeAuth());
            index ++;
        }
        if(deptAuditBean.getRemark()!=null){
            sql.append(" and dept.remark=? ");
            lm.put(index,deptAuditBean.getRemark());
            index ++;
        }
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        if (null !=jsonObject){
            return true;
        }
        return false;

    }

    /**
     *
     * @param userAuditBean
     * @return true 无效编辑
     * @throws Exception
     */
    public boolean editUserInvalid(UserAuditBean userAuditBean) throws Exception {
        StringBuffer sql=new StringBuffer("SELECT user.USERNAME,user.NAME, userdept2.DEPTID,userdept2.deptNames from sys_user user  ");
        sql.append("LEFT JOIN (SELECT userdept.username,GROUP_CONCAT(userdept.deptid)AS DEPTID,GROUP_CONCAT(dept.deptname)AS deptNames FROM sys_user_dept userdept ");
        sql.append("inner join sys_dept dept on userdept.deptid=dept.id GROUP BY username)userdept2 on user.USERNAME=userdept2.username ");
        sql.append("LEFT JOIN(SELECT USERNAME,GROUP_CONCAT(userAdcd.ADCD)AS adcds, GROUP_CONCAT(adcd.ADNM)AS names  FROM sys_user_adcd userAdcd  inner join sys_ad_cd ");
        sql.append("adcd on userAdcd.ADCD=adcd.ADCD  GROUP BY USERNAME)userAdcd1 on user.USERNAME=userAdcd1.USERNAME  WHERE 1=1 ");
        LinkedMap lm=new LinkedMap();
        Integer index =1;
        if(!SummitTools.stringIsNull(userAuditBean.getUserNameAuth())){
            sql.append(" and user.USERNAME=? ");
            lm.put(index,userAuditBean.getUserNameAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getNameAuth())){
            sql.append(" and user.NAME=? ");
            lm.put(index,userAuditBean.getNameAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getSexAuth())){
            sql.append(" and user.SEX=? ");
            lm.put(index,userAuditBean.getSexAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getPasswordAuth())){
            sql.append(" and user.PASSWORD=? ");
            lm.put(index,userAuditBean.getPasswordAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getEmailAuth())){
            sql.append(" and user.EMAIL=? ");
            lm.put(index,userAuditBean.getEmailAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getPhoneNumberAuth())){
            sql.append(" and user.PHONE_NUMBER=? ");
            lm.put(index,userAuditBean.getPhoneNumberAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getIsEnabledAuth())){
            sql.append(" and user.IS_ENABLED=? ");
            lm.put(index,userAuditBean.getIsEnabledAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getDutyAuth())){
            sql.append(" and user.DUTY=? ");
            lm.put(index,userAuditBean.getDutyAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getPostAuth())){
            sql.append(" and user.POST=? ");
            lm.put(index,userAuditBean.getPostAuth());
            index ++;
        }
        if(!SummitTools.stringIsNull(userAuditBean.getHeadPortraitAuth())){
            sql.append(" and user.HEADPORTRAIT=? ");
            lm.put(index,userAuditBean.getHeadPortraitAuth());
            index ++;
        }
        if(null != userAuditBean.getDeptAuth() && userAuditBean.getDeptAuth().length>0 ){
            sql.append(" and userdept2.DEPTID=? ");
            String[] deptAuth = userAuditBean.getDeptAuth();
            List<String> depts = Arrays.asList(deptAuth);
            String dept = StringUtils.join(depts, ",");
            lm.put(index,dept);
            index ++;
        }
        if(null != userAuditBean.getAdcdAuth() && userAuditBean.getAdcdAuth().length>0 ){
            sql.append(" and userAdcd1.adcds=? ");
            String[] adcdAuth = userAuditBean.getAdcdAuth();
            List<String> adcds = Arrays.asList(adcdAuth);
            String adcd = StringUtils.join(adcds, ",");
            lm.put(index,adcd);
            index ++;
        }
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        if (null !=jsonObject){
            return true;
        }
        return false;
    }

}
