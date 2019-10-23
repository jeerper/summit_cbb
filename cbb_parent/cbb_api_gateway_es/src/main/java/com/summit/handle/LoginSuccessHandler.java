package com.summit.handle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summit.domain.User;

/**
 * @author yt
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 获取当前用户(domain接收)
        System.out.println("abcd success");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.toString());
        request.getSession().setAttribute("LOGIN_USER", user);
        // logger.info("登陆成功，用户："+user.getChineseName()); 转发到index页面
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        // 返回JSON字符串
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "200");// 401
        map.put("msg", "登录成功");

        Map<String, Object> params = new HashMap<>();
        params.put("client_id", "yt");
        params.put("redirect_uri", "http://localhost:8769/code/redirect");
        params.put("response_type", "code");

		/*System.out.println(restTemplate.getForObject(
				"http://localhost:8769/oauth/authorize?response_type=code&client_id=yt&redirect_uri=http://localhost:8769/code/redirect",
				String.class));
*/
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(map));

    }

}
