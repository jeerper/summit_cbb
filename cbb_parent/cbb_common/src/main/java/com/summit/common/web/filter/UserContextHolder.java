package com.summit.common.web.filter;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.summit.common.entity.UserInfo;

/**
 * 用户信息全局获取工具类
 *
 * @author Administrator
 */
public class UserContextHolder {
    /**
     * 用户信息实体存储容器.
     */
    private final static ThreadLocal<UserInfo> USER_INFO_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserInfo getUserInfo() {
        return USER_INFO_THREAD_LOCAL.get();
    }

    /**
     * 设置用户信息
     *
     * @param userInfo 用户信息
     */
    static void setUserInfo(UserInfo userInfo) {
        USER_INFO_THREAD_LOCAL.set(userInfo);
    }

    /**
     * 移除用户信息
     */
    public static void clear() {
        USER_INFO_THREAD_LOCAL.remove();
    }


}
