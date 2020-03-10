package com.summit.send.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface GeTuiPushService {

    String push(String android, String pushUrl, String pushType, List<String> pushArray, String content, String title, String appId, String appKey, String masterSecret, JSONObject extrasparam);
}
