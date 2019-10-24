package com.summit.common.util;

import com.summit.common.entity.UserInfo;
import com.summit.common.web.filter.UserContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class UserAuthUtils {
    /**
     * 获取当前登录用户的角色列表
     */
    public static List<String> getRoles() {
        UserInfo uerInfo = UserContextHolder.getUserInfo();
        List<String> rolesList;
        if (uerInfo != null) {
            rolesList = Arrays.asList(uerInfo.getRoles());
        } else {
            rolesList = Arrays.asList("ROLE_INVALID");
        }
        if(rolesList.contains("ROLE_SUPERUSER")){
            rolesList = null;
        }
        return rolesList;
    }
}
