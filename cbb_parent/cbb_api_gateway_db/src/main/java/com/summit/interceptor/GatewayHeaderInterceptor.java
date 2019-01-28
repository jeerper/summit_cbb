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
            if (authentication instanceof OAuth2Authentication) {
                requestTemplate.header(CommonConstant.USER_HEADER, authentication.getName());
            }
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                // 在调用方服务接到content-length 与实际传的 content-length 长度是不一致的。
                // 所以导致了：feign.FeignException: status 400 reading,所以需要过滤掉该字段。
                if (name.equals("content-length")) {
                    continue;
                }
                String values = request.getHeader(name);
                requestTemplate.header(name, values);
            }
        }
    }
}
