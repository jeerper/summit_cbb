package com.summit.common.redis.user;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.summit.common.constant.CommonConstant;
import com.summit.common.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户信息缓存处理类
 *
 * @author Administrator
 */
@Component
public class UserInfoCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoCache.class);

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RedisTemplate<String, String> redisTemplateWithUserInfo;

    /**
     * 设置用户信息缓存
     *
     * @param userName 用户名称
     * @param userInfo 用户信息
     */
    public void setUserInfo(String userName, UserInfo userInfo) {
        try {
            String userInfoString = objectMapper.writeValueAsString(userInfo);
            LOGGER.debug("设置用户信息缓存:{}:{}", userName, userInfoString);
            redisTemplateWithUserInfo.opsForValue().set(CommonConstant.USER_INFO_CACHE_PREFIX + userName,
                    userInfoString);
        } catch (Exception e) {
            LOGGER.error("设置用户信息缓存失败", e);
        }
    }

    /**
     * 获取用户信息缓存
     *
     * @param userName 用户名称
     * @return
     */
    public UserInfo getUserInfo(String userName) {
        try {
            String userInfoString =
                    redisTemplateWithUserInfo.opsForValue().get(CommonConstant.USER_INFO_CACHE_PREFIX + userName);
            LOGGER.debug("读取用户信息缓存:{}:{}", userName, userInfoString);
            if (StrUtil.isBlank(userInfoString)) {
                return null;
            }
            return objectMapper.readValue(userInfoString, UserInfo.class);
        } catch (Exception e) {
            LOGGER.error("获取用户信息缓存失败", e);
            return null;
        }
    }

    /**
     * 删除用户信息缓存
     *
     * @param userName 用户名称
     * @return
     */
    public boolean deleteUserInfo(String userName) {
        boolean result = redisTemplateWithUserInfo.delete(CommonConstant.USER_INFO_CACHE_PREFIX + userName);
        LOGGER.debug("删除用户信息缓存:{}:{}", userName, result);
        return true;
    }
}
