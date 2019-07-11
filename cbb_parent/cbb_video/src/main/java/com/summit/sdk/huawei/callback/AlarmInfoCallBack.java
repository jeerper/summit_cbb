package com.summit.sdk.huawei.callback;

import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_ALARM_REPORT;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmInfoCallBack implements HWPuSDKLibrary.pfGetAlarmInfoCallBack {
    @Override
    public void apply(PU_ALARM_REPORT pstAlarmReport, Pointer pUsrData) {

        log.debug("告警设备ID:"+pstAlarmReport.ulDeviceId.longValue());
        log.debug("告警类型:"+pstAlarmReport.enAlarmType);
//        log.debug("设备IP:"+ StrUtil.str(pUsrData.getByteArray(0,16), "UTF-8").trim());
        try {
            log.debug("设备IP:"+new String(pUsrData.getByteArray(0,15)).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
