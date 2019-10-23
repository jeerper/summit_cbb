package com.summit.domain;

/**
 * @author yt
 */
public enum SummitClientEnum {
    //clientId
    CLIENT_ID,
    //resourceId
    RESOURCE_IDS,
    //client密码
    CLIENT_SECRET,
    //client权限
    SCOPE,
    //client模式(授权码，密码...)
    AUTHORIZED_GRANTTYPES,
    //重定向url
    REGISTERED_REDIRECTURL,
    //权限
    AUTHORITIES,
    //token时间
    ACCESSTOKEN_VALIDITY_SECONDS,
    //刷新token
    REFRESHTOKEN_VALIDITY_SECONDS,
    //其他信息
    ADDITIONAL_INFORMATION
}
