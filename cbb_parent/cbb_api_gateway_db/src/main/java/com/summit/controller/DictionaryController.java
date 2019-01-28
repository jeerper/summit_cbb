package com.summit.controller;

import com.summit.common.api.demo.RemoteDemoService;
import com.summit.common.api.userauth.RemoteUserAuthService;
import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "路由管理")
@RestController
@RequestMapping("/route")
public class DictionaryController {

    @Autowired
    RemoteDemoService remoteDemoService;

    @Autowired
    RemoteUserAuthService remoteUserAuthService;

    @ApiOperation(value = "测试页面", notes = "asdsad")
    @RequestMapping(value = "/logPage", method = RequestMethod.GET)
    public RestfulEntityBySummit add() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000, "haha");
    }

    @ApiOperation(value = "第三个demo接口,无参数传递，返回失败代码，返回用户名称")
    @GetMapping(value = "/logTest")
    public RestfulEntityBySummit<String> logTest() {

        return remoteDemoService.haveParamDemo();
    }

    @ApiOperation(value = "第四个demo接口,无参数传递，返回失败代码，返回用户名称")
    @GetMapping(value = "/loghahaha")
    public RestfulEntityBySummit<UserInfo> loghahaha() {
        return remoteUserAuthService.queryUserInfoByUserName("admin");
    }

}
