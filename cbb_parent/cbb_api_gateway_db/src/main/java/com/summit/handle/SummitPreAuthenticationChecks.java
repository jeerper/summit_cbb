package com.summit.handle;

import com.summit.common.entity.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

/**
 * 用户信息预处理.
 */
@Slf4j
@Component
public class SummitPreAuthenticationChecks implements UserDetailsChecker {
    @Override
    public void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            log.debug("User account is locked");

            throw new LockedException(ResponseCodeEnum.CODE_4024.getCode());
        }

        if (!user.isEnabled()) {
            log.debug("User account is disabled");

            throw new DisabledException(ResponseCodeEnum.CODE_4031.getCode());
        }

        if (!user.isAccountNonExpired()) {
            log.debug("User account is expired");

            throw new AccountExpiredException(ResponseCodeEnum.CODE_4032.getCode());
        }
    }
}
