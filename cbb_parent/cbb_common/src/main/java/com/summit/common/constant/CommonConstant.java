package com.summit.common.constant;


public interface CommonConstant {
    /**
     * token请求头名称
     */
    String REQ_HEADER = "authorization";

    /**
     * token分割符
     */
    String TOKEN_SPLIT = "Bearer ";
    /**
     * 用户信息头
     */
    String USER_HEADER = "x-user-header";
    /**
     * 用户信息缓存前缀
     */
    String USER_INFO_CACHE_PREFIX = "user_info_";

    /**
     * 角色信息头
     */
    String ROLE_HEADER = "x-role-header";
    /**
     * jwt签名
     */
    String SIGN_KEY = "Summit";
    /**
     * 许可
     */
    String LICENSE = "MadeBySummit";
    /**
     * 前缀
     */
    String PROJECT_PREFIX = "summit_";

    /**
     * oauth 相关前缀
     */
    String OAUTH_PREFIX = "oauth:";

    /**
     * 删除
     */
    String STATUS_DEL = "1";
    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 菜单
     */
    String MENU = "0";

    /**
     * 按钮
     */
    String BUTTON = "1";

    /**
     * 删除标记
     */
    String DEL_FLAG = "del_flag";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";

    /**
     * 阿里大鱼
     */
    String ALIYUN_SMS = "aliyun_sms";

    /**
     * 路由信息Redis保存的key
     */
    String ROUTE_KEY = "_ROUTE_KEY";

    String LOGIN_LOG_PREFIX="LOGIN_LOG_KEY:";
    /**
     * 用户登录token缓存
     */
    String LOGIN_TOKEN_PREFIX="LOGIN_TOKEN_KEY:";
}
