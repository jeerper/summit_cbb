package com.summit.mobilePush.controller;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

/**
 * 移动端消息推送采用极光推送(https://docs.jiguang.cn//jpush/server/sdk/java_sdk/)
 * 推送前标签和别名都要注册
 * 极光推送有两种方式:第一种是使用极光推送官方提供的推送请求API：https://api.jpush.cn/v3/push，另一种则是使用官方提供的第三方Java SDK
 *
 * @author liuyh
 */
@Slf4j
@RestController
@RequestMapping("/jiguangJPush")
@Api(value = "/jiguangJPush", tags = "极光推送服务")
public class JiguangJpushController {
    @Value("${jpush.appKey}")
    private String appkey;

    @Value("${jpush.masterSecret}")
    private String masterSecret;

    @Value("${jpush.liveTime}")
    private int liveTime;

    private static final String pushUrl = "https://api.jpush.cn/v3/push";
    private static final boolean apns_production = true;
    //设置别名

    //设置标签

    /**
     * java后台极光推送方式一：使用Http API
     * 此种方式需要自定义http请求发送客户端:HttpClient
     */
    @ApiOperation(value = "Android:通过API推送", notes = "message(发送的内容),title(发送的标题),pushType(推送类型：1:所有用户，2 :指定别名，3：指定标签),pushArray(别名或者标签,推送类型是2或3该参数必填,别名一次推送最多 1000 个，标签一次推送最多 20 个。),extras(扩展字段,格式为:JSONObject格式)")
    @GetMapping("/jiguangAndroidPushByAPI")
    public RestfulEntityBySummit<String> jiguangAndroidPushByAPI(
            @RequestParam(value = "message", required = true) String message,//message
            @RequestParam(value = "title", required = true) String title,//发送的标题
            @RequestParam(value = "pushType", required = true) String pushType,//推送类型：1:所有用户，2 :指定别名，3：指定标签
            @RequestParam(value = "pushArray", required = false) List<String> pushArray,//别名或者标签,推送类型是2或3该参数必填,别名一次推送最多 1000 个，标签一次推送最多 20 个。
            @RequestParam(value = "extras", required = false) String extras) {//扩展字段,格式为:JSONObject格式
        JSONObject extrasparam = null;
        if (extras != null) {
            try {
                extrasparam = JSON.parseObject(extras);
            } catch (Exception e) {
                log.info("格式转换出错！错误信息为：" + extras + "，不是JSONObject格式");
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_9993);
            }
        }
        String result = push("android", pushUrl, pushType, pushArray, message, title, appkey, masterSecret, apns_production, liveTime, extrasparam);
        JSONObject resData = JSONObject.parseObject(result);
        if (resData.containsKey("error")) {
            JSONObject error = JSONObject.parseObject(resData.getString("error"));
            log.info("消息推送失败！错误信息为：" + error.get("message").toString());
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025);

        } else {
            log.info("信息推送成功！");
            return ResultBuilder.buildSuccess();
        }
    }

    /**
     * 通过SDK的方式推送
     *
     * @param appKey
     * @param masterSecret
     * @param message
     * @param title
     * @param pushType
     * @param aliasArray
     * @return
     */
    @ApiOperation(value = "Android:通过SDK推送", notes = "message(发送的内容),title(发送的标题),pushType(推送类型：1:所有用户，2 :指定别名，3：指定标签),pushArray(别名或者标签,推送类型是2或3该参数必填,别名一次推送最多 1000 个，标签一次推送最多 20 个。),extras(扩展字段,格式为:JSONObject格式)")
    @GetMapping("/jiguangAndroidPushBySDK")
    public RestfulEntityBySummit<String> jiguangAndroidPushBySDK(
            @RequestParam(value = "message", required = true) String message,//发送的内容
            @RequestParam(value = "title", required = true) String title,//发送的标题
            @RequestParam(value = "pushType", required = true) String pushType,//推送类型：1:所有用户，2 :指定别名，3：指定标签
            @RequestParam(value = "pushArray", required = false) List<String> pushArray,//别名或者标签,推送类型是2或3该参数必填,别名一次推送最多 1000 个，标签一次推送最多 20 个。
            @RequestParam(value = "extras", required = false) String extras) {//扩展字段,格式为:JSONObject格式
        Map<String, String> extraMap;
        try {
            JSONObject extrasparam = JSON.parseObject(extras);
            extraMap = JSONObject.toJavaObject(extrasparam, Map.class);
        } catch (Exception e) {
            log.info("格式转换出错！错误信息为：" + extras + "，不是JSONObject格式");
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9993);
        }
        Audience audience = null;
        if ("1".equals(pushType)) {
            audience = Audience.all();//项目中的所有用户
        } else if ("2".equals(pushType)) {
            audience = Audience.alias(pushArray);//指定别名
        } else if ("3".equals(pushType)) {
            audience = Audience.tag(pushArray);//指定标签
        } else {
            log.info("非法的请求参数！错误信息为：" + pushType + " ");
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9993);
        }
        JPushClient jpushClient = new JPushClient(masterSecret, appkey);
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())//指定android平台的用户
                .setAudience(audience)
                .setNotification(Notification.android(message, title, extraMap))
                //发送内容
                .setOptions(Options.newBuilder().setApnsProduction(false).build())
                //这里是指定开发环境,不用设置也没关系
                .setMessage(Message.content(message))//自定义信息
                .build();
        try {
            jpushClient.sendPush(payload);
            log.info("信息推送成功！");
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            log.info("消息推送失败！错误信息为：" + e.getMessage());
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_4025);
        }
    }


    /**
     * 推送方法-调用极光API
     *
     * @param reqUrl
     * @param alias
     * @param alert
     * @return result
     */
    public static String push(String platformType, String reqUrl, String pushType, List<String> alias, String alert, String title, String appKey, String masterSecret, boolean apns_production, int time_to_live, JSONObject extrasparam) {
        String base64_auth_string = encryptBASE64(appKey + ":" + masterSecret);
        String authorization = "Basic " + base64_auth_string;
        return sendPostRequest(reqUrl, generateJson(platformType, pushType, alias, alert, title, apns_production, time_to_live, extrasparam).toString(), "UTF-8", authorization);
    }

    /**
     * 发送Post请求（json格式）
     *
     * @param reqURL
     * @param data
     * @param encodeCharset
     * @param authorization
     * @return result
     */
    @SuppressWarnings({"resource"})
    public static String sendPostRequest(String reqURL, String data, String encodeCharset, String authorization) {
        HttpPost httpPost = new HttpPost(reqURL);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        String result = "";
        try {
            StringEntity entity = new StringEntity(data, encodeCharset);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", authorization.trim());
            response = client.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), encodeCharset);
        } catch (Exception e) {
            log.error("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下", e);
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * 组装极光推送专用json串
     *
     * @param alias
     * @param alert
     * @return json
     */
    public static JSONObject generateJson(String platformType, String pushType, List<String> alias, String alert, String title, boolean apns_production, int time_to_live, JSONObject extrasparam) {
        JSONObject json = new JSONObject();
        JSONArray platform = new JSONArray();//平台
        //platform.add(platformType);
        platform.add("android");

        JSONObject audience = new JSONObject();//推送目标
        if ("1".equals(pushType)) {
            json.put("audience", "all");//项目中的所有用户
        } else if ("2".equals(pushType)) {
            audience.put("alias", alias);
            json.put("audience", audience);
        } else if ("3".equals(pushType)) {
            audience.put("tag", alias);//指定标签
            json.put("audience", audience);
        }


        JSONObject notification = new JSONObject();//通知内容
        JSONObject android = new JSONObject();//android通知内容
        android.put("alert", alert);
        android.put("title", title);
        android.put("builder_id", 1);
        // JSONObject android_extras = new JSONObject();//android额外参数
        // android_extras.put("type", "infomation");
        android.put("extras", extrasparam);
        notification.put("android", android);

//	        JSONObject ios = new JSONObject();//ios通知内容
//	        ios.put("alert", alert);
//	        ios.put("sound", "default");
//	        ios.put("badge", "+1");

//	        JSONObject ios_extras = new JSONObject();//ios额外参数
//	        ios_extras.put("type", "infomation");
//	        ios.put("extras", ios_extras);

//	        notification.put("ios", ios);

        JSONObject options = new JSONObject();//设置参数
        options.put("time_to_live", Integer.valueOf(time_to_live));
        options.put("apns_production", apns_production);

        json.put("platform", platform);

        json.put("notification", notification);
        json.put("options", options);
        return json;

    }

    /**
     * 　　　　* BASE64加密工具
     *
     */
    public static String encryptBASE64(String str) {
        byte[] key = str.getBytes();
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String strs = base64Encoder.encodeBuffer(key);
        return strs;
    }


//	public static void jpushAndroidAPI() {
//		        List<String> alias=new ArrayList<String>();
//		        alias.add("songli");
//		        try{
//		            String result = push("android",pushUrl,"2",alias,"songli关于安塞河长制培训。。。。。","河长制消息推送songli",appkey,masterSecret,apns_production,liveTime);
//		            JSONObject resData = JSONObject.parseObject(result);
//		                if(resData.containsKey("error")){
//		                    log.info("针对别名为" + alias + "的信息推送失败！");
//		                    //String a= resData.getString("error");
//		                    JSONObject error = JSONObject.parseObject(resData.getString("error"));
//		                    log.info("错误信息为：" + error.get("message").toString());
//		                }else{
//		                  log.info("针对别名为" + alias + "的信息推送成功！");
//		                }
//		        }catch(Exception e){
//		        	e.printStackTrace();
//		            log.error("针对别名为" + alias + "的信息推送失败！",e);
//		        }  
//	}
//	
//	public static void jpushAndroid() {
//			//创建JPushClient(极光推送的实例)
//			JPushClient jpushClient = new JPushClient(masterSecret, appkey);
//			Map<String, String> parm= new HashMap<String, String>();
//			//推送的关键,构造一个payload 
//			JSONObject audience = new JSONObject();//推送目标
//		    List alias1=new ArrayList();
//		     alias1.add("douzz");
//		     alias1.add("songli");
//			PushPayload payload = PushPayload.newBuilder()
//					.setPlatform(Platform.android())//指定android平台的用户
//					//.setAudience(Audience.all())//你项目中的所有用户
//					//.setAudience(Audience.registrationId("songli"))//registrationId指定用户
//					.setAudience(Audience.alias(alias1))
//					.setNotification(Notification.android("河长制测试数据呢", "河长制", null))
//					//发送内容
//					.setOptions(Options.newBuilder().setApnsProduction(false).build())
//					//这里是指定开发环境,不用设置也没关系
//					.setMessage(Message.content("河长制测试数据呢1111111111111"))//自定义信息
//					.build();
//	 
//			try {
//				jpushClient.sendPush(payload);  
//			} catch (APIConnectionException e) {
//				e.printStackTrace();
//			} catch (APIRequestException e) {
//				e.printStackTrace();
//			}    
//	}


    public static void main(String[] args) {
        //jpushAndroid();
        //jpushAndroidAPI();
    }
}
