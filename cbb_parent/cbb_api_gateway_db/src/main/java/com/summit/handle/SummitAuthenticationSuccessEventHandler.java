package com.summit.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.summit.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录验证成功事件处理器
 *
 * @author liuyuan
 */
@Slf4j
@Component
public class SummitAuthenticationSuccessEventHandler implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    RedisTemplate<String, Object> genericRedisTemplate;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (CollUtil.isEmpty(authentication.getAuthorities())) {
            return;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String loginIp = "";
        if ("0:0:0:0:0:0:0:1".equals(request.getRemoteAddr())) {
            try {
                loginIp = InetAddress.getLocalHost().toString();
                int computNameIndex = loginIp.indexOf("/");
                if (computNameIndex != -1) {
                    loginIp = loginIp.substring(computNameIndex + 1);
                }
            } catch (UnknownHostException e) {
                log.error("获取ip失败！" + e.getMessage());
            }
        } else {
            loginIp = request.getRemoteAddr();
        }
        UserDetails userBean = (UserDetails) authentication.getPrincipal();
        String loginUserName = userBean.getUsername();
        String loginLogKey = CommonConstant.LOGIN_LOG_PREFIX + loginIp + ":" + loginUserName;

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            //登陆完成后操作
            String loginId = IdUtil.objectId();
            genericRedisTemplate.opsForValue().set(loginLogKey, loginId, 5, TimeUnit.MINUTES);

            Date loginLogCreateTime = new Date();
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
            //TODO:请求用户组件记录用户登录日志接口

        } else if (authentication instanceof OAuth2Authentication) {
            //访问资源的操作
            if (genericRedisTemplate.hasKey(loginLogKey)) {
                //如果key存在则刷新过期时间
                genericRedisTemplate.expire(loginLogKey, 5, TimeUnit.MINUTES);
            } else {
                //TODO:如果key不存在则拉取最后一次登陆记录ID并重新加入缓存

            }
        }


    }


}
