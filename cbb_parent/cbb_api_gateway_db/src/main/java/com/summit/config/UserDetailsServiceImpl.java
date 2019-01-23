package com.summit.config;

import com.summit.common.api.userauth.RemoteUserAuthService;
import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestFulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.model.user.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
    @Autowired
    private RemoteUserAuthService remoteUserAuthService;

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RestFulEntityBySummit<UserInfo> userInfoRestFulEntity = remoteUserAuthService.queryUserRoleByUserName(username);

        if (!userInfoRestFulEntity.getCode().equals(ResponseCodeBySummit.CODE_0000.name())) {
            throw new UsernameNotFoundException("用户不存在");
        }
        UserInfo userInfo = userInfoRestFulEntity.getData();

        UserBean user = new UserBean();
        user.setUserName(userInfo.getUserName());
        user.setPassword(userInfo.getPassword());
        user.setName(userInfo.getName());
        user.setIsEnabled(userInfo.getIsEnabled());
        user.setRoles(userInfo.getRoles());
        user.setPermissions(userInfo.getPermissions());
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getRoles());
//        authorities.add(new SimpleGrantedAuthority("ROLE_DEFAULT"));
        user.setAuthorities(authorities);
        return user;
    }
}
