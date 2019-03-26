package com.summit.controller;

import com.summit.common.api.demo.RemoteDemoService;
import com.summit.common.api.userauth.RemoteUserAuthService;
import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.model.user.FileUploadInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


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
        //网关需要通过SecurityContextHolder获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new RestfulEntityBySummit(ResponseCodeBySummit.CODE_0000);
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

    @ApiOperation(value = "文件上传测试接口")
    @PostMapping(value = "/loghahaha", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RestfulEntityBySummit fileUpload(@ApiParam(value ="文件上传对象",allowMultiple = true)MultipartFile[] uploadFile, FileUploadInfo fileUploadInfo) {

        return new RestfulEntityBySummit(ResponseCodeBySummit.CODE_0000);
    }


}
