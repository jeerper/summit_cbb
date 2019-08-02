package com.summit.config;


import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.util.Cryptographic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


public class SummitAuthenticationProvider extends DaoAuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(SummitAuthenticationProvider.class);


    @Value("${password.encode.key}")
    private String key;



    @Override
    public void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            LOGGER.debug("密码不能为空!");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    ResponseCodeEnum.CODE_4030.getCode()));
        }

        String presentedPassword = authentication.getCredentials().toString();
        String decodePassword = "";
        try {
            decodePassword = Cryptographic.decryptAES(presentedPassword, key);
        } catch (Exception e) {
            LOGGER.error("密码解密失败:{}", presentedPassword,e);
        }

        if (!getPasswordEncoder().matches(decodePassword, userDetails.getPassword())) {
            LOGGER.debug("用户输入的密码和数据库中的密码不匹配");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    ResponseCodeEnum.CODE_4010.getCode()));
        }
    }

}
