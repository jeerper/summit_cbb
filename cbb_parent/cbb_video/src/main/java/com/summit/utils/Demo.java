package com.summit.utils;

import cn.hutool.core.util.StrUtil;
import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_DEVICE_REGISTER_RSP;
import com.summit.sdk.huawei.PU_EVENT_COMMON;
import com.summit.sdk.huawei.PU_EVENT_REGISTER;
import com.sun.jna.NativeLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class Demo {
    public final static HWPuSDKLibrary.pfGetEventInfoCallBack pfGetEventInfoCallBack=new HWPuSDKLibrary.pfGetEventInfoCallBack(){
        @Override
        public NativeLong apply(PU_EVENT_COMMON arg)  {
            if (arg.enEventType == 2) {

                PU_EVENT_REGISTER registerEvent = new PU_EVENT_REGISTER(arg.getPointer());
                log.debug("设备类型:" + StrUtil.str(registerEvent.szDeviceType, "").trim());
                log.debug("设备ID:" + StrUtil.str(registerEvent.szDeviceId, "").trim().substring(0, 16));
                log.debug("设备IP地址:" + StrUtil.str(registerEvent.szDeviceIp, "").trim());
                boolean responseRegisterStatus= HWPuSDKLibrary.INSTANCE.IVS_PU_ResponseDeviceRegister(arg.ulIdentifyID,new PU_DEVICE_REGISTER_RSP(0,new NativeLong(6060)));

                log.debug("响应设备主动注册:" + responseRegisterStatus);
                printReturnMsg();

                boolean loginStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_LoginByID(arg.ulIdentifyID, "admin", "HuaWei123");
                log.debug("设备登录状态:" + loginStatus);
                printReturnMsg();
//
//                    PU_SYSTEM_TIME time = new PU_SYSTEM_TIME();
//                    boolean timeGetStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_GetDeviceTime(arg.ulIdentifyID, time);

//                    log.debug("时间获取状态:" + timeGetStatus);
            }
            return new NativeLong(0);
        }
    };
    @PostConstruct
    public void demo1() {
        System.setProperty("jna.debug_load", "true");
        System.setProperty("jna.debug_load.jna", "true");
//        Native.setProtected(true);
        boolean initStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_Init(new NativeLong(3), (String) null, new NativeLong(6060));
        printReturnMsg();
        log.debug("SDK加载状态:" + initStatus);
        boolean callBackBindStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_EventStatesCallBack(pfGetEventInfoCallBack);

        printReturnMsg();

        log.debug("回调函数绑定状态:" + callBackBindStatus);
    }

    @PreDestroy
    public void destroy() {
//        HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(deviceIdNative);
//        printReturnMsg();
        HWPuSDKLibrary.INSTANCE.IVS_PU_Cleanup();
        printReturnMsg();
    }
    public static void printReturnMsg() {
        NativeLong errorCode = HWPuSDKLibrary.INSTANCE.IVS_PU_GetLastError();
        String errorMsg = HWPuSDKLibrary.INSTANCE.IVS_PU_GetErrorMsg(errorCode);
        log.debug("返回码:{},返回信息:{}", errorCode.longValue(), errorMsg);
    }
}
