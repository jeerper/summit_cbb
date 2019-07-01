package com.summit.utils;

import cn.hutool.core.util.StrUtil;
import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Slf4j
@Component
public class Demo {

    @PostConstruct
    public void demo1() {
        log.debug(Boolean.toString(Native.DEBUG_LOAD));
        log.debug(Boolean.toString(Native.DEBUG_JNA_LOAD));
        HWPuSDKLibrary.INSTANCE.IVS_PU_Init(new NativeLong(0), StrUtil.byteBuffer("192.168.141.222", ""),new NativeLong(46666));
        HWPuSDKLibrary.INSTANCE.IVS_PU_Login(StrUtil.byteBuffer("192.168.141.141", ""),new NativeLong(80),StrUtil.byteBuffer("admin", ""),StrUtil.byteBuffer("Summit123", ""));


    }
}
