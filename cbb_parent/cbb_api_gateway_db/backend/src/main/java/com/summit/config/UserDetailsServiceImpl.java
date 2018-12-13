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

import com.summit.model.user.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取用户详细信息
 *
 * @author Administrator
 */

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
//	private final RemoteUserService remoteUserService;

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO:来自用户服务组件
        UserBean user = new UserBean();
        user.setUserName("admin");
        user.setPassword("$2a$10$qXkUoWj.gogGI49w1f/UXeIDVvviYhM0wmZD3uAN3CqmJsQf5unNK");
        user.setName("系统管理员");
        user.setIsEnabled(1);
        user.setRoles(new String[]{"ROLE_ADMIN"});
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getRoles());
        authorities.add(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        user.setAuthorities(authorities);
        return user;
    }
}
