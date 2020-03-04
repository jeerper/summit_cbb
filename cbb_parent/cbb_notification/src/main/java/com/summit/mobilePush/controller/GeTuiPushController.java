package com.summit.mobilePush.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.send.service.GeTuiPushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/geTuiPush")
@Api(value = "/geTuiPush", tags = "个推服务")
public class GeTuiPushController {
    @Value("${geTuiPush.appId}")
    private String appId;
    @Value("${geTuiPush.appKey}")
    private String appKey;
    @Value("${geTuiPush.masterSecret}")
    private String masterSecret;
    private static final String pushUrl = "http://sdk.open.api.igexin.com/apiex.htm";

    @Autowired
    private GeTuiPushService geTuiPushService;

    /**
     * 【toSingle】执行单推
     * 【toList】执行批量推 pushType=2单次推送数量限制1000以内，此接口频次限制200万次/天
     * 【toApp】执行群推 pushType=1时所有人推送，此接口频次限制100次/天，每分钟不能超过5次，定时推送功能需要申请开通才可以使用
     * @return
     */

   @ApiOperation(value = "android:个推消息推送", notes = "title(发送的标题),content(发送的内容),pushType(推送类型：1:所有用户，2 :指定别名),extras(扩展字段,格式为:JSONObject格式)")
    @GetMapping("/geTuiAndroidPushByAPI")
     public RestfulEntityBySummit<String> geTuiAndroidPushByAPI(
             @RequestParam(value = "content", required = true) String content,//发送的内容
             @RequestParam(value = "title", required = true) String title,//发送的标题
             @RequestParam(value = "pushType", required = true) String pushType,//推送类型：1:所有用户，2 :指定别名
             @RequestParam(value = "pushArray", required = false) List<String> pushArray,//别名推送类型是2该参数必填,别名一次推送最多 1000 个
             @RequestParam(value = "extras", required = false) String extras){
        JSONObject extrasparam = null;
        if (extras !=null ){
            try{
                extrasparam = JSON.parseObject(extras);
            }catch (Exception e){
                log.info("格式转换出错！错误信息为：" + extras + "，不是JSONObject格式");
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_9993);
            }
        }
       String result = geTuiPushService.push("android", pushUrl, pushType,pushArray, content, title, appId,appKey, masterSecret, extrasparam);
       String replace = result.replaceAll("=", ":");
       cn.hutool.json.JSONObject resData = new cn.hutool.json.JSONObject(replace);
       if ("ok".equals(resData.getStr("result"))){
           log.info("信息推送成功！");
           return ResultBuilder.buildSuccess();
       }else{
           //JSONObject respose = JSON.parseObject(resData.getString("result"));
           log.info("消息推送失败！错误信息为：" + resData.get("result").toString());
           return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025);
       }


    }



}
