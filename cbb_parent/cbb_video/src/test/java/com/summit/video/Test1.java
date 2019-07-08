package com.summit.video;

import cn.hutool.core.util.StrUtil;
import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_DEVICE_REGISTER_RSP;
import com.summit.sdk.huawei.PU_EVENT_COMMON;
import com.summit.sdk.huawei.PU_EVENT_REGISTER;
import com.sun.jna.NativeLong;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Test1 {
    public static HWPuSDKLibrary.pfGetEventInfoCallBack pfGetEventInfoCallBack = new HWPuSDKLibrary.pfGetEventInfoCallBack() {
                @Override
                public void apply(PU_EVENT_COMMON arg) {
                    if (arg.enEventType == 2) {

                        PU_EVENT_REGISTER registerEvent = new PU_EVENT_REGISTER(arg.getPointer());
                        log.debug("设备类型:" + StrUtil.str(registerEvent.szDeviceType, "").trim());
                        log.debug("设备ID:" + StrUtil.str(registerEvent.szDeviceId, "").trim().substring(0, 16));
                        log.debug("设备IP地址:" + StrUtil.str(registerEvent.szDeviceIp, "").trim());
                        boolean responseRegisterStatus =
                                HWPuSDKLibrary.INSTANCE.IVS_PU_ResponseDeviceRegister(arg.ulIdentifyID,
                                        new PU_DEVICE_REGISTER_RSP(0, new NativeLong(6060)));

                        log.debug("响应设备主动注册:" + responseRegisterStatus);
                        printReturnMsg();

                        boolean loginStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_LoginByID(arg.ulIdentifyID, "admin",
                                "HuaWei123");
                        log.debug("设备登录状态:" + loginStatus);
                        printReturnMsg();
//
//                    PU_SYSTEM_TIME time = new PU_SYSTEM_TIME();
//                    boolean timeGetStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_GetDeviceTime(arg.ulIdentifyID, time);

//                    log.debug("时间获取状态:" + timeGetStatus);
                    }
                }
            };

    public static void printReturnMsg() {
        NativeLong errorCode = HWPuSDKLibrary.INSTANCE.IVS_PU_GetLastError();
        String errorMsg = HWPuSDKLibrary.INSTANCE.IVS_PU_GetErrorMsg(errorCode);
        log.debug("返回码:{},返回信息:{}", errorCode.longValue(), errorMsg);
    }

    @Before
    public void init() {
        System.setProperty("jna.debug_load", "true");
        System.setProperty("jna.debug_load.jna", "true");
//        Native.setProtected(false);

        boolean initStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_Init(new NativeLong(3), (String) null,
                new NativeLong(6060));
        printReturnMsg();
        log.debug("SDK加载状态:" + initStatus);
    }

    @Test
    public void test1() {
        boolean callBackBindStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_EventStatesCallBack(pfGetEventInfoCallBack);

        printReturnMsg();

        log.debug("回调函数绑定状态:" + callBackBindStatus);

//        deviceIdNative = HWPuSDKLibrary.INSTANCE.IVS_PU_Login("192.168.141.141", new NativeLong(6060), "admin",
//                "HuaWei123");
//        log.debug("设备ID:" + deviceIdNative.longValue());
//        printReturnMsg();
//        NativeLongByReference longNative = new NativeLongByReference();
//        HWPuSDKLibrary.INSTANCE.IVS_PU_GetVersion(longNative);
//        log.debug("SDK版本号:" + longNative.getValue().longValue());
//        printReturnMsg();
//        PU_SYSTEM_TIME time = new PU_SYSTEM_TIME();
//
//        boolean timeGetStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_GetDeviceTime(deviceIdNative, time);
//
//        log.debug("时间获取状态:" + timeGetStatus);


        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (Exception e) {
            log.error("执行失败:", e);
        }
    }

    @After
    public void destroy() {
//        HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(deviceIdNative);
        printReturnMsg();
        HWPuSDKLibrary.INSTANCE.IVS_PU_Cleanup();
        printReturnMsg();
    }
}
