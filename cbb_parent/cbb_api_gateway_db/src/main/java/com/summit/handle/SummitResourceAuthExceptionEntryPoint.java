
package com.summit.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summit.common.constant.CommonConstant;
import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestFulEntityBySummit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 * 客户端异常处理
 * 可以根据 AuthenticationException 不同细化异常处理
 */

@Component
public class SummitResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private  ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

		Throwable cause = authException.getCause();
		RestFulEntityBySummit entity;
		if(cause instanceof InvalidTokenException) {
			entity=new RestFulEntityBySummit(ResponseCodeBySummit.CODE_4008,null);
		}else{
			entity=new RestFulEntityBySummit(ResponseCodeBySummit.CODE_4007,null);
		}
		response.setCharacterEncoding(CommonConstant.UTF8);
		response.setContentType(CommonConstant.CONTENT_TYPE);
		response.setStatus(HttpStatus.OK.value());
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(entity));
	}
}
