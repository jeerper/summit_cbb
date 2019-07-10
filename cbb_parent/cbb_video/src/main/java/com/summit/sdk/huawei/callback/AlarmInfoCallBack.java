package com.summit.sdk.huawei.callback;

import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_ALARM_REPORT;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmInfoCallBack implements HWPuSDKLibrary.pfGetAlarmInfoCallBack {
    @Override
    public void apply(PU_ALARM_REPORT pstAlarmReport, Pointer pUsrData) {

        log.debug(""+pstAlarmReport.ulDeviceId.longValue());
        log.debug(""+pstAlarmReport.enAlarmType);
        log.debug(pUsrData.getString(0));

    }
}
