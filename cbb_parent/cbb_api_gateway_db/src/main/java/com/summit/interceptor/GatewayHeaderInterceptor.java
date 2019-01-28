package com.summit.interceptor;


import com.summit.common.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign请求头部信息追加拦截器
 *
 * @author Administrator
 */
@Slf4j
public class GatewayHeaderInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if(authentication instanceof OAuth2Authentication){
                requestTemplate.header(CommonConstant.USER_HEADER, authentication.getName());
            }
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();

//        String userHeader = request.getHeader(CommonConstant.USER_HEADER);
//        if (StrUtil.isNotBlank(userHeader)) {
//            requestTemplate.header(CommonConstant.USER_HEADER, userHeader);
//        }
//        String roleHeader = request.getHeader(CommonConstant.ROLE_HEADER);
//        if (StrUtil.isNotBlank(roleHeader)) {
//            requestTemplate.header(CommonConstant.ROLE_HEADER, roleHeader);
//        }

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                log.debug("{}:{}", name, values);
                requestTemplate.header(name, values);
            }
        }
    }
}
