package com.summit.handle;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
public  class SummitAuthenticationFailureEvenHandler implements ApplicationListener<AbstractAuthenticationFailureEvent> {


	@Override
	public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
		Authentication authentication = (Authentication) event.getSource();

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(requestAttributes==null){
			return;
		}
		HttpServletRequest request = requestAttributes.getRequest();
		HttpServletResponse response = requestAttributes.getResponse();

		//获取IP
		String loginIp= ServletUtil.getClientIP(request);

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
		log.debug("用户名:"+authentication.getPrincipal());



	}
}
