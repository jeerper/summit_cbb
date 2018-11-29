package com.summit.handle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summit.config.UserContext;
import com.summit.domain.log.LogBean;
import com.summit.service.function.FunctionService;
import com.summit.service.log.ILogUtil;

/**
 * 
 * @author yt
 *
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	FunctionService fs;
	@Autowired
	ILogUtil logUtil;
	@Autowired
	RestTemplate restTemplate;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.debug("Loading menuItem ....");
		HttpSession session = request.getSession();
		String sysFunctions = getUserFunction();
		session.setAttribute("userName", UserContext.getUsername());
		session.setAttribute("fun", sysFunctions);
		session.setAttribute("SESSION_VALIAD", true);
		LogBean logBean = logUtil.insertLog(request,"1", "用户登录");
		logUtil.updateLog(logBean, "1");
		//request.getSession().setAttribute("LOGIN_USER", user);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json;charset=utf-8");
		// 返回JSON字符串
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "200");// 401
		map.put("msg", "登录成功");
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(mapper.writeValueAsString(map));
		
	}
	
	public String getUserFunction() {
		logger.debug("user name:" + UserContext.getUsername());
		return fs.getFunByUserName(UserContext.getUsername());
	}

}
