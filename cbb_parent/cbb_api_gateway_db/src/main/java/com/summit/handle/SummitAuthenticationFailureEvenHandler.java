package com.summit.handle;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.summit.common.api.userauth.RemoteUserLogService;
import com.summit.model.user.LogSuccessOrNot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 用户登录验证失败事件处理器
 *
 * @author liuyuan
 */
@Slf4j
@Component
public class SummitAuthenticationFailureEvenHandler implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Autowired
    RemoteUserLogService remoteUserLogService;


    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (authentication.getDetails() == null) {
            return;
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        //获取IP
        String loginIp = ServletUtil.getClientIP(request);

        if ("0:0:0:0:0:0:0:1".equals(loginIp)) {
            try {
                loginIp = InetAddress.getLocalHost().toString();
                int computNameIndex = loginIp.indexOf("/");
                if (computNameIndex != -1) {
                    loginIp = loginIp.substring(computNameIndex + 1);
                }
            } catch (UnknownHostException e) {
                log.error("获取ip失败！" + e.getMessage());
            }
        }
        AuthenticationException authenticationException = event.getException();

        authenticationException.getLocalizedMessage();

        log.debug("用户名:" + authentication.getPrincipal());
        //todo
        String loginUserName =(String) authentication.getPrincipal();
        Observable.just(loginIp)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String loginIp) {
                        String loginId = IdWorker.getIdStr();
                        String logSuccessOrNot=LogSuccessOrNot.Failure.getCode();
                        remoteUserLogService.addLoginLog(loginId, loginUserName, loginIp, logSuccessOrNot);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        log.error("消息分发线程执行异常", throwable);
                    }
                });
    }
}
