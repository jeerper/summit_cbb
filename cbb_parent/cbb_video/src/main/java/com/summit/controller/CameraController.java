package com.summit.controller;

import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.sdk.huawei.api.HuaWeiSdkApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "video模块接口测试")
@RestController
@RequestMapping("/video")
public class CameraController {
    @Autowired
    HuaWeiSdkApi huaWeiSdkApi;

    @ApiOperation(value = "重启摄像机")
    @PutMapping(value = "/reboot")
    public RestfulEntityBySummit rebootCamera(String cameraIp) {
        boolean rebootStatus = huaWeiSdkApi.rebootCamera(cameraIp);
        if (rebootStatus) {
            return ResultBuilder.buildSuccess("重启成功");
        } else {
            return ResultBuilder.buildSuccess("重启失败");
        }


    }


}
