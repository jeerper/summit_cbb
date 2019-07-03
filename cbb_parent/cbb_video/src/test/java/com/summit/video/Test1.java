package com.summit.video;

import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.sun.jna.NativeLong;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class Test1 {
    private NativeLong deviceIdNative;
    @Before
    public void init(){
        System.setProperty("jna.debug_load", "true");
        System.setProperty("jna.debug_load.jna", "true");
        boolean initStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_Init(new NativeLong(3), (String)null, new NativeLong(6060));
        printReturnMsg();
        log.debug("SDK加载状态:" + initStatus);
    }
    @Test
    public void test1() {


        deviceIdNative = HWPuSDKLibrary.INSTANCE.IVS_PU_Login("192.168.141.141", new NativeLong(6060), "admin","HuaWei123");
        log.debug("设备ID:" + deviceIdNative.longValue());
        printReturnMsg();

    }
    @After
    public void destroy(){
        HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(deviceIdNative);
        printReturnMsg();
        HWPuSDKLibrary.INSTANCE.IVS_PU_Cleanup();
        printReturnMsg();
    }

    public void printReturnMsg(){
        NativeLong errorCode=HWPuSDKLibrary.INSTANCE.IVS_PU_GetLastError();
        String errorMsg= HWPuSDKLibrary.INSTANCE.IVS_PU_GetErrorMsg(errorCode);
        log.debug("返回码:{},返回信息:{}",errorCode.longValue(),errorMsg);
    }
}
