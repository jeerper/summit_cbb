package com.summit.common.web.filter;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 用户信息全局获取工具类
 *
 * @author Administrator
 */
public class UserContextHolder {
    /**
     * 用户信息实体存储容器.
     */
    private final static ThreadLocal<String> USER_NAME_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 获取用户名称
     *
     * @return
     */
    public static String getUserName() {
        return USER_NAME_THREAD_LOCAL.get();
    }

    /**
     * 设置用户名称
     *
     * @param userName 用户名称
     */
    static void setUserName(String userName) {
        USER_NAME_THREAD_LOCAL.set(userName);
    }

    /**
     * 移除用户名称
     */
    public static void clear() {
        USER_NAME_THREAD_LOCAL.remove();
    }


}
