package com.summit.send.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.gexin.fastjson.serializer.SerializerFeature;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.summit.send.service.GeTuiPushService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GeTuiPushServiceImpl implements GeTuiPushService {
    @Override
    public String push(String android, String pushUrl, String pushType,List<String> pushArray, String content, String title, String appId, String appKey, String masterSecret, JSONObject extrasparam) {
        String result =null;
        IGtPush push = new IGtPush(pushUrl, appKey, masterSecret);
        if (!StrUtil.isEmpty(pushType) && pushType.equals("1")){//所有用户
            NotificationTemplate template = notificationTemplate(title ,content,appId,appKey,extrasparam);
            AppMessage  appMessage= new AppMessage();
            appMessage.setData(template);
            appMessage.setOffline(true);
            //离线有效时间，单位为毫秒，可选
            appMessage.setOfflineExpireTime(24 * 1000 * 3600);
            //推送给App的目标用户需要满足的条件
            List<String> appIdList = new ArrayList<String>();
            appIdList.add(appId);
            appMessage.setAppIdList(appIdList);
            AppConditions conditions = new AppConditions();
            conditions.addCondition(AppConditions.TAG,new ArrayList<>(), AppConditions.OptType.not);
            IPushResult ret = push.pushMessageToApp(appMessage,"群发所有人");
            result = ret.getResponse().toString();
        }else if (!StrUtil.isEmpty(pushType) && pushType.equals("2")){//指定别名群发
            ListMessage listMessage = new ListMessage();
            listMessage.setData(transmissionTemplate(appId, appKey, title, content, extrasparam));
            listMessage.setOffline(true);
            listMessage.setOfflineExpireTime(24 * 1000 * 3600);
            List<Target> targets = new ArrayList();
            for(String s :pushArray){
                Target target=new Target();
                target.setAppId(appId);
                target.setAlias(s);
                targets.add(target);
            }
            String taskId=push.getContentId(listMessage);
            IPushResult ret = push.pushMessageToList(taskId, targets);
            result = ret.getResponse().toString();
        }
        return result;
    }

    private  NotificationTemplate notificationTemplate(String title, String content,String appId,String appKey,JSONObject extrasparam) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(1);
        if (extrasparam != null){
            template.setTransmissionContent(extrasparam.getString("key"));//需要输入的内容
            template.setTransmissionContent(extrasparam.getString("taskId"));
        }
        Long timeMillis = System.currentTimeMillis();
        template.setTransmissionContent(timeMillis.toString());
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(title);
        style.setText(content);
        // 配置通知栏图标
        style.setLogo("XXX");
        // 配置通知栏网络图标
        //style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        return template;
    }


    public TransmissionTemplate transmissionTemplate(String appId,String appKey,String title,String message,JSONObject extrasparam  ) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionType(2);
        JSONObject map=new JSONObject();
        map.put("title", title);
        map.put("content", message);
        if (extrasparam != null){
            if (extrasparam.containsKey("key")){
                map.put("key", extrasparam.getString("key"));
            }
            if (extrasparam.containsKey("taskId")){
                map.put("taskId",  extrasparam.getString("taskId"));
            }
        }
        map.put("time", System.currentTimeMillis());
        String json = com.gexin.fastjson.JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
        template.setTransmissionContent(json);
        return template;
    }



}
