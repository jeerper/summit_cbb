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

package com.summit.config;

import com.summit.handle.SummitAccessDeniedHandler;
import com.summit.handle.SummitResourceAuthExceptionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;


@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    protected SummitResourceAuthExceptionEntryPoint summitResourceAuthExceptionEntryPoint;
    @Autowired
    private OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler;
    @Autowired
    private SummitAccessDeniedHandler summitAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/actuator/**"
                        , "/user/info/*"
                        , "/social/info/**"
                        , "/log/**"
                        , "/v2/api-docs").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.expressionHandler(oAuth2WebSecurityExpressionHandler)
                .accessDeniedHandler(summitAccessDeniedHandler)
                .authenticationEntryPoint(summitResourceAuthExceptionEntryPoint);
    }


    /**
     * 配置解决 spring-security-oauth问题
     * https://github.com/spring-projects/spring-security-oauth/issues/730
     *
     * @param applicationContext ApplicationContext
     * @return OAuth2WebSecurityExpressionHandler
     */
    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }


}