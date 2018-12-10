package com.summit.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义403无权限访问的处理，重定向到/403页面
 * 
 * @author Administrator
 *
 */
//@Component("myAccessDeniedHandler")
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	private static Logger logger = LoggerFactory.getLogger(MyAccessDeniedHandler.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		response.setContentType("application/json;charset=UTF-8");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code", "401");//401
        map.put("msg", "权限不足");
        map.put("data", accessDeniedException.getMessage());
        map.put("success", false);
        map.put("path", request.getServletPath());
        map.put("timestamp", String.valueOf(new Date().getTime()));
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(map));
		
		
		/*if (auth != null) {
			logger.info("用戶： '" + auth.getName() + "'試圖訪問受保護的 URL: "
					+ request.getRequestURI());
		}
		response.sendRedirect(request.getContextPath() + "/403");*/
	}
}
