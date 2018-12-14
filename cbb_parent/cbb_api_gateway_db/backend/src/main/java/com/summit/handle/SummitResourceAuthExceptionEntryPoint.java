/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.summit.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summit.common.constant.CommonConstant;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/7/8
 * 客户端异常处理
 * 1. 可以根据 AuthenticationException 不同细化异常处理
 */

@Component
public class SummitResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private  ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

		Map<String, Object> map = new HashMap<String, Object>();
		Throwable cause = authException.getCause();
		if(cause instanceof InvalidTokenException) {
			map.put("code", HttpStatus.UNAUTHORIZED.value());//401
			map.put("msg", "无效的token");
		}else{
			map.put("code", "login_error");//401
			map.put("msg", "访问此资源需要完全的身份验证");
		}
		map.put("data", authException.getMessage());
		map.put("success", false);
		map.put("path", request.getServletPath());
		map.put("timestamp", String.valueOf(new Date().getTime()));


		response.setCharacterEncoding(CommonConstant.UTF8);
		response.setContentType(CommonConstant.CONTENT_TYPE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(map));
	}
}
