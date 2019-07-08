package com.summit.sdk.huawei.callback;

import cn.hutool.core.util.StrUtil;
import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_DEVICE_REGISTER_RSP;
import com.summit.sdk.huawei.PU_EVENT_COMMON;
import com.summit.sdk.huawei.PU_EVENT_REGISTER;
import com.summit.sdk.huawei.PU_SYSTEM_TIME;
import com.summit.sdk.huawei.PU_TIME;
import com.summit.sdk.huawei.api.HuaWeiSdkApi;
import com.sun.jna.NativeLong;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class EventInfoCallBack implements HWPuSDKLibrary.pfGetEventInfoCallBack {

    private long sdkPort;
    private String sdkUserName;
    private String sdkPassword;
    private ConcurrentHashMap<String, NativeLong> deviceMap;

    public EventInfoCallBack(long sdkPort, String sdkUserName, String sdkPassword, ConcurrentHashMap<String, NativeLong> deviceMap) {
        this.sdkPort = sdkPort;
        this.sdkUserName = sdkUserName;
        this.sdkPassword = sdkPassword;
        this.deviceMap = deviceMap;
    }

    @Override
    public void apply(PU_EVENT_COMMON arg) {
        if (arg.enEventType == 2) {
            log.debug("发现设备主动注册");
            PU_EVENT_REGISTER registerEvent = new PU_EVENT_REGISTER(arg.getPointer());
            String deviceType = StrUtil.str(registerEvent.szDeviceType, "").trim();
            String deviceId = StrUtil.str(registerEvent.szDeviceId, "").trim().substring(0, 16);
            String deviceIp = StrUtil.str(registerEvent.szDeviceIp, "").trim();
            log.debug("设备类型:" + deviceType);
            log.debug("设备ID:" + deviceId);
            log.debug("设备IP地址:" + deviceIp);
            boolean responseRegisterStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_ResponseDeviceRegister(arg.ulIdentifyID,
                    new PU_DEVICE_REGISTER_RSP(0, new NativeLong(sdkPort)));
            log.debug("响应设备主动注册:" + responseRegisterStatus);
            if (!responseRegisterStatus) {
                HuaWeiSdkApi.printReturnMsg();
                return;
            }
            boolean loginStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_LoginByID(arg.ulIdentifyID, sdkUserName, sdkPassword);
            log.debug("设备登录状态:" + loginStatus);
            if (!loginStatus) {
                HuaWeiSdkApi.printReturnMsg();
                return;
            }
            deviceMap.put(deviceIp, arg.ulIdentifyID);
            PU_SYSTEM_TIME time = new PU_SYSTEM_TIME();
            boolean timeGetStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_GetDeviceTime(arg.ulIdentifyID, time);
            log.debug("时间获取状态:" + timeGetStatus);
            if (!timeGetStatus) {
                HuaWeiSdkApi.printReturnMsg();
            } else {
                PU_TIME timeBytes = time.stSystime;
                int year = Integer.valueOf(StrUtil.str(timeBytes.szYear, "").trim());
                int month = Integer.valueOf(StrUtil.str(timeBytes.szMonth, "").trim());
                int day = Integer.valueOf(StrUtil.str(timeBytes.szDay, "").trim());
                int hour = Integer.valueOf(StrUtil.str(timeBytes.szHour, "").trim());
                int minute = Integer.valueOf(StrUtil.str(timeBytes.szMinute, "").trim());
                int second = Integer.valueOf(StrUtil.str(timeBytes.szSecond, "").trim());
                int timeZone = time.lTimeZone.intValue();
                String timeString = new DateTime(year, month, day, hour + timeZone, minute, second).toString("yyyy-MM-dd HH:mm:ss");
                log.debug("设备时间:" + timeString);
            }
        }
    }
}
