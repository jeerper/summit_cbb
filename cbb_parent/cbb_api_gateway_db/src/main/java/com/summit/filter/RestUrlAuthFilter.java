package com.summit.filter;

import com.netflix.zuul.ZuulFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * URL访问过滤器
 */
@Component
public class RestUrlAuthFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//                判断该用户是否有访问URL的权限，没有则不允许网关转发
//                requestContext.getRequest().getRequestURI();
//                requestContext.setSendZuulResponse(false);
//                requestContext.setResponseStatusCode(500);
//                requestContext.setResponseBody("{\"result\":\"username is not correct!\"}");
//                return null;

//        }
        return null;
    }
}
