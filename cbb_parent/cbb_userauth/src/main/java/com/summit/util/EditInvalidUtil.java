package com.summit.util;

import com.summit.common.entity.DeptAuditBean;
import com.summit.repository.UserRepository;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public  boolean editInvalid(DeptAuditBean deptAuditBean) throws Exception {

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
        JSONObject jsonObject = ur.queryOneCustom(sql.toString(), lm);
        if (null !=jsonObject){
            return true;
        }
        return false;

    }
}
