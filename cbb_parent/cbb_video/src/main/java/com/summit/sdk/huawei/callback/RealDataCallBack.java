package com.summit.sdk.huawei.callback;

import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealDataCallBack implements HWPuSDKLibrary.pfRealDataCallBack {

    @Override
    public void apply(Pointer szBuffer, NativeLong lSize, Pointer pUsrData) {

        log.debug("asdasd");
    }
}
