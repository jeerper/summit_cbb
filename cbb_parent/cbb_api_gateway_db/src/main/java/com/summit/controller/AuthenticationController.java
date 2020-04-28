package com.summit.controller;

import com.summit.common.constant.CommonConstant;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "登录管理")
@RestController
@RequestMapping("/oauth")
public class AuthenticationController {

    @Autowired
    RedisTemplate<String, Object> genericRedisTemplate;
    @Resource(name = "consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RestfulEntityBySummit<Boolean> logout(HttpServletRequest request) {
        String username=request.getHeader(CommonConstant.USER_HEADER);
        String authorizationToken = request.getHeader(CommonConstant.REQ_HEADER);
        if ((authorizationToken.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
            authorizationToken = authorizationToken.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
        }
        genericRedisTemplate.delete(CommonConstant.LOGIN_TOKEN_PREFIX + username);
        return ResultBuilder.buildSuccess(consumerTokenServices.revokeToken(authorizationToken));
    }

    @ApiOperation(value = "通过用户名称退出登录")
    @PostMapping("/logout/{username}")
    public RestfulEntityBySummit<Boolean> logout(@PathVariable String username) {
        String authorizationToken = (String) genericRedisTemplate.opsForValue().get(CommonConstant.LOGIN_TOKEN_PREFIX + username);
        genericRedisTemplate.delete(CommonConstant.LOGIN_TOKEN_PREFIX + username);
        return ResultBuilder.buildSuccess(consumerTokenServices.revokeToken(authorizationToken));
    }


}
