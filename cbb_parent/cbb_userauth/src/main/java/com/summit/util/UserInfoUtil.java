package com.summit.util;

import com.summit.common.entity.DeptAuditBean;
import com.summit.common.entity.DeptBean;
import com.summit.common.entity.UserAuditBean;
import com.summit.common.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserInfoUtil {
    public UserInfo getUserInfo(UserAuditBean userAuditBean) {
        UserInfo userInfo=new UserInfo();
        userInfo.setIsEnabled(Integer.parseInt(userAuditBean.getIsEnabledAuth()));
        userInfo.setName(userAuditBean.getUserNameAuth());
        userInfo.setPhoneNumber(userAuditBean.getPhoneNumberAuth());
        userInfo.setUserName(userAuditBean.getUserNameAuth());
        userInfo.setDepts(userAuditBean.getDeptAuth());
        userInfo.setHeadPortrait(userAuditBean.getHeadPortraitAuth());
        return userInfo;
    }

    public DeptBean getDeptBean(DeptAuditBean deptAuditBean) {
        DeptBean deptBean=new DeptBean();
        deptBean.setId(deptAuditBean.getDeptIdAuth());
        deptBean.setDeptHead(deptAuditBean.getDeptHeadAuth());
        deptBean.setDeptName(deptAuditBean.getDeptNameAuth());
        deptBean.setDeptType(deptAuditBean.getDeptTypeAuth());
        deptBean.setDeptCode(deptAuditBean.getDeptcodeAuth());
        deptBean.setPid(deptAuditBean.getPIdAuth());
        deptBean.setRemark(deptAuditBean.getRemark());
        return deptBean;
    }
}
