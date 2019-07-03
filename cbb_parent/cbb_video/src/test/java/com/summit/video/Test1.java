package com.summit.video;

import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_EVENT_COMMON;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Test1 {
    HWPuSDKLibrary.pfGetEventInfoCallBack pfGetEventInfoCallBack;
    private NativeLong deviceIdNative;

    @Before
    public void init() {
        System.setProperty("jna.debug_load", "true");
        System.setProperty("jna.debug_load.jna", "true");
        boolean initStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_Init(new NativeLong(3), (String) null,
                new NativeLong(6060));
        printReturnMsg();
        log.debug("SDK加载状态:" + initStatus);
    }

    @Test
    public void test1() {
        pfGetEventInfoCallBack = new HWPuSDKLibrary.pfGetEventInfoCallBack() {
            @Override
            public NativeLong apply(PU_EVENT_COMMON arg) {

//                log.debug(""+arg.enEventType);
                return null;
            }
        };
        boolean callBackBindStatus=HWPuSDKLibrary.INSTANCE.IVS_PU_EventStatesCallBack(pfGetEventInfoCallBack);

        printReturnMsg();

        log.debug("回调函数绑定状态:" + callBackBindStatus);

        deviceIdNative = HWPuSDKLibrary.INSTANCE.IVS_PU_Login("192.168.141.141", new NativeLong(6060), "admin",
                "HuaWei123");
        log.debug("设备ID:" + deviceIdNative.longValue());
        printReturnMsg();
        NativeLongByReference longNative = new NativeLongByReference();
        HWPuSDKLibrary.INSTANCE.IVS_PU_GetVersion(longNative);
        log.debug("SDK版本号:" + longNative.getValue().longValue());
        printReturnMsg();


        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (Exception e) {
            log.error("执行失败:",e);
        }

    }

    @After
    public void destroy() {
        HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(deviceIdNative);
        printReturnMsg();
        HWPuSDKLibrary.INSTANCE.IVS_PU_Cleanup();
        printReturnMsg();
    }

    public void printReturnMsg() {
        NativeLong errorCode = HWPuSDKLibrary.INSTANCE.IVS_PU_GetLastError();
        String errorMsg = HWPuSDKLibrary.INSTANCE.IVS_PU_GetErrorMsg(errorCode);
        log.debug("返回码:{},返回信息:{}", errorCode.longValue(), errorMsg);
    }
}
