package com.summit.sdk.huawei.callback;

import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_ALARM_REPORT;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public class AlarmInfoCallBack implements HWPuSDKLibrary.pfGetAlarmInfoCallBack {
    @Override
    public NativeLong apply(PU_ALARM_REPORT pstAlarmReport, Pointer pUsrData) {
        return null;
    }
}
