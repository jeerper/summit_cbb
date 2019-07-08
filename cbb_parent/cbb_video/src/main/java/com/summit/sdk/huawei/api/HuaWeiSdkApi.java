package com.summit.sdk.huawei.api;

import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.callback.EventInfoCallBack;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class HuaWeiSdkApi {

    private long sdkPort = 6060;
    private String sdkUserName = "admin";
    private String sdkPassword = "HuaWei123";

    private HWPuSDKLibrary.pfGetEventInfoCallBack pfGetEventInfoCallBack;

    public static void printReturnMsg() {
        NativeLong errorCode = HWPuSDKLibrary.INSTANCE.IVS_PU_GetLastError();
        String errorMsg = HWPuSDKLibrary.INSTANCE.IVS_PU_GetErrorMsg(errorCode);
        log.debug("返回码:{},返回信息:{}", errorCode.longValue(), errorMsg);
    }

    @PostConstruct
    public void demo1() {
        System.setProperty("jna.debug_load", "true");
        System.setProperty("jna.debug_load.jna", "true");
        boolean initStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_Init(new NativeLong(3), (String) null, new NativeLong(sdkPort));
        log.debug("SDK加载状态:" + initStatus);
        if (!initStatus) {
            printReturnMsg();
        }
        NativeLongByReference longNative = new NativeLongByReference();
        HWPuSDKLibrary.INSTANCE.IVS_PU_GetVersion(longNative);
        log.debug("SDK版本号:" + longNative.getValue().longValue());
        //SDK注册事件回调
        pfGetEventInfoCallBack = new EventInfoCallBack(sdkPort, sdkUserName, sdkPassword);
        boolean callBackBindStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_EventStatesCallBack(pfGetEventInfoCallBack);
        log.debug("注册事件回调函数绑定:" + callBackBindStatus);
        if (!callBackBindStatus) {
            printReturnMsg();
        }


    }

    @PreDestroy
    public void destroy() {
//        HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(deviceIdNative);
        HWPuSDKLibrary.INSTANCE.IVS_PU_Cleanup();
        printReturnMsg();
    }
}
