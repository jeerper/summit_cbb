package com.summit.common.web.filter;

import cn.hutool.core.util.StrUtil;
import com.summit.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserContextHolderFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String userName = request.getHeader(CommonConstant.USER_HEADER);
        log.debug("获取header中的用户名称为:{}", userName);

        if (StrUtil.isNotBlank(userName)) {
            UserContextHolder.setUserName(userName);
        }
        filterChain.doFilter(request, response);
        UserContextHolder.clear();
    }
}
