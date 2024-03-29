package com.summit.config;

import com.summit.common.api.userauth.RemoteUserAuthService;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
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
    UserInfoCache userInfoCache;
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
        RestfulEntityBySummit<UserInfo> userInfoRestFulEntity = remoteUserAuthService.queryUserInfoByUserNameService(username);

        if (!userInfoRestFulEntity.getCode().equals(ResponseCodeEnum.CODE_0000.getCode())) {
            throw new UsernameNotFoundException(ResponseCodeEnum.CODE_4023.getCode());
        }
        UserInfo userInfo = userInfoRestFulEntity.getData();

        userInfoCache.setUserInfo(userInfo.getUserName(), userInfo);

        UserBean user = new UserBean();
        user.setUserName(userInfo.getUserName());
        user.setPassword(userInfo.getPassword());
        user.setName(userInfo.getName());
        user.setIsEnabled(userInfo.getIsEnabled());
        user.setRoles(userInfo.getRoles());
        user.setPermissions(userInfo.getPermissions());
        if (userInfo.getRoles() != null && userInfo.getRoles().length > 0) {
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getRoles());
            //authorities.add(new SimpleGrantedAuthority("ROLE_DEFAULT"));
            user.setAuthorities(authorities);
        }
        return user;
    }
}
