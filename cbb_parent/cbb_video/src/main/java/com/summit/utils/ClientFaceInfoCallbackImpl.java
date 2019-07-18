package com.summit.utils;

import com.summit.sdk.huawei.callback.ClientFaceInfoCallback;
import com.summit.sdk.huawei.model.FaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientFaceInfoCallbackImpl implements ClientFaceInfoCallback {
    @Override
    public void invoke(FaceInfo faceInfo) {
        log.debug("============客户端调用开始=============");
        log.debug("设备IP:" + faceInfo.getDeviceIp());
        log.debug("名字:" + faceInfo.getName());
        log.debug("性别:{}", faceInfo.getGender().getGenderDescription());
        log.debug("生日:" + faceInfo.getBirthday());
        log.debug("省级:" + faceInfo.getProvince());
        log.debug("地市:" + faceInfo.getCity());
        log.debug("证件类型:{}", faceInfo.getCardType().getCardTypeDescription());
        log.debug("证件号:" + faceInfo.getCardId());
        log.debug("人脸匹配率:{}%", faceInfo.getFaceMatchRate());
        log.debug("名单库名称:{}", faceInfo.getFaceLibName());
        log.debug("名单库类型:{}", faceInfo.getFaceLibType().getFaceLibTypeDescription());
    }
}
