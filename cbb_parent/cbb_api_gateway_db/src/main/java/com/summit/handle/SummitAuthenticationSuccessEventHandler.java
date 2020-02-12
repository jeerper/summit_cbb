package com.summit.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.summit.model.user.UserBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

/**
 * 用户登录验证成功事件处理器
 *
 * @author liuyuan
 */
@Slf4j
//@Component
public class SummitAuthenticationSuccessEventHandler implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (CollUtil.isEmpty(authentication.getAuthorities())) {
            return;
        }

        if(authentication instanceof UsernamePasswordAuthenticationToken){

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= (UsernamePasswordAuthenticationToken)authentication;
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            UserBean userBean = (UserBean) authentication.getPrincipal();

            String loginId = IdUtil.objectId();
            String loginUserName = userBean.getUserName();
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
            }else{
                loginIp = request.getRemoteAddr();
            }
            Date loginLogCreateTime = new Date();
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();


            //TODO:请求用户组件记录用户登录日志接口


        }
        else if (authentication instanceof OAuth2Authentication){

        }


    }


}
