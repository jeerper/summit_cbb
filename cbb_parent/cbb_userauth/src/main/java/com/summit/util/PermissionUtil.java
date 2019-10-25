package com.summit.util;

import cn.hutool.core.util.StrUtil;
import com.summit.common.entity.FunctionBean;
import com.summit.common.util.UserAuthUtils;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.dao.repository.FunctionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PermissionUtil {
    @Autowired
    FunctionDao functionDao;

    /**
     * 检查登录用户对其他用户的访问权限
     *
     * @param userName 其他用户的用户名称
     * @return true代表有权限；false代表无权限
     */
    public boolean checkLoginUserAccessPermissionToOtherUser(String userName) {
        if (StrUtil.equals(userName, UserContextHolder.getUserInfo().getUserName())) {
            return true;
        }
        if (StrUtil.equalsIgnoreCase(userName, SysConstants.SUPER_USERNAME)) {
            return false;
        }

        List<FunctionBean> functionList = functionDao.getFunctionInfoListByRole(UserAuthUtils.getRoles());
        boolean effectivePermission = false;
        for (FunctionBean functionBean : functionList) {
            if (functionBean.getName().equals("用户管理")) {
                effectivePermission = true;
                break;
            }
        }
        return effectivePermission;
    }

}
