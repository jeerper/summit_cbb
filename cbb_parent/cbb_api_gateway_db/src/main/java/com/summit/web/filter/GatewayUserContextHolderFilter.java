package com.summit.web.filter;

import cn.hutool.core.util.StrUtil;
import com.summit.common.constant.CommonConstant;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.common.web.filter.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayUserContextHolderFilter extends GenericFilterBean {

    @Autowired
    UserInfoCache userInfoCache;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String userName = request.getHeader(CommonConstant.USER_HEADER);
        if (StrUtil.isNotBlank(userName)) {
            //从redis取出用户信息
            UserInfo userInfo = userInfoCache.getUserInfo(userName);
            UserContextHolder.setUserInfo(userInfo);
        }
        filterChain.doFilter(request, response);
        UserContextHolder.clear();
    }
}
