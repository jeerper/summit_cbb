package com.summit.config;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import com.summit.common.entity.ResponseCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SummitAuthenticationProvider extends DaoAuthenticationProvider {
    private static  final Logger LOGGER= LoggerFactory.getLogger(SummitAuthenticationProvider.class);

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NOPadding";
    @Value("${password.encode.key}")
    private String key;

    @Override
    public  void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            LOGGER.debug("密码不能为空!");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    ResponseCodeEnum.CODE_4030.getCode()));
        }

        String presentedPassword = authentication.getCredentials().toString();
        String decodePassword="";
        try {
            decodePassword = decryptAES(presentedPassword, key).trim();
        } catch (Exception e) {
            LOGGER.error("密码解密失败:{}", presentedPassword);
        }

        if (!getPasswordEncoder().matches(decodePassword, userDetails.getPassword())) {
            LOGGER.debug("用户输入的密码和数据库中的密码不匹配");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    ResponseCodeEnum.CODE_4010.getCode()));
        }
    }
    private static String decryptAES(String data, String pass) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        SecretKeySpec keyspec = new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM);
        IvParameterSpec ivspec = new IvParameterSpec(pass.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] result = cipher.doFinal(Base64.decode(data.getBytes(CharsetUtil.UTF_8)));
        return new String(result, CharsetUtil.UTF_8);
    }

}
