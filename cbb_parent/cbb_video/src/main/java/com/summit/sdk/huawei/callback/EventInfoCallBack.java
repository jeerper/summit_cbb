package com.summit.sdk.huawei.callback;

import cn.hutool.core.util.StrUtil;
import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_DEVICE_REGISTER_RSP;
import com.summit.sdk.huawei.PU_EVENT_COMMON;
import com.summit.sdk.huawei.PU_EVENT_REGISTER;
import com.summit.sdk.huawei.PU_SYSTEM_TIME;
import com.summit.sdk.huawei.PU_TIME;
import com.summit.sdk.huawei.api.HuaWeiSdkApi;
import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class EventInfoCallBack implements HWPuSDKLibrary.pfGetEventInfoCallBack {

    private long sdkPort;
    private String sdkUserName;
    private String sdkPassword;
    private ConcurrentHashMap<String, NativeLong> deviceMap;
    private HWPuSDKLibrary.pfGetAlarmInfoCallBack pfGetAlarmInfoCallBack;
    private HWPuSDKLibrary.pfRealDataCallBack pfRealDataCallBack;

    public EventInfoCallBack(long sdkPort, String sdkUserName, String sdkPassword, ConcurrentHashMap<String, NativeLong> deviceMap) {
        this.sdkPort = sdkPort;
        this.sdkUserName = sdkUserName;
        this.sdkPassword = sdkPassword;
        this.deviceMap = deviceMap;
        this.pfGetAlarmInfoCallBack = new AlarmInfoCallBack();
        this.pfRealDataCallBack = new RealDataCallBack();
    }

    @Override
    public void apply(PU_EVENT_COMMON arg) {
        switch (arg.enEventType) {
            case 1:
                log.debug("设备主动连接，网络连接上");
                break;
            case 2:
                log.debug("设备主动注册,发送注册消息");
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
                    break;
                }
                boolean loginStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_LoginByID(arg.ulIdentifyID, sdkUserName, sdkPassword);
                log.debug("设备登录状态:" + loginStatus);
                if (!loginStatus) {
                    HuaWeiSdkApi.printReturnMsg();
                    break;
                }


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

                Pointer deviceIpPointer = new Memory(deviceIp.length()+1);
                deviceIpPointer.setString(0, deviceIp);

                boolean alarmCallBackBindStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_AlarmInfoStatesCallBack(arg.ulIdentifyID, pfGetAlarmInfoCallBack,
                        deviceIpPointer);
                log.debug("告警上报回调函数绑定:" + alarmCallBackBindStatus);


//                PU_REAL_PLAY_INFO realPlayInfo = new PU_REAL_PLAY_INFO();
//                NativeLong realDataCallBackBindStatus = HWPuSDKLibrary.INSTANCE.IVS_PU_RealPlay(arg.ulIdentifyID, realPlayInfo,pfRealDataCallBack, deviceIpPointer);
//                log.debug("实况播放回调函数绑定:" + realDataCallBackBindStatus.longValue());
//                HuaWeiSdkApi.printReturnMsg();

                deviceMap.put(deviceIp, arg.ulIdentifyID);
                break;
            case 3:
                log.debug("设备主动连接后未注册");
                break;
            case 4:
                log.debug("设备主动注销");
                HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(arg.ulIdentifyID);
                deviceMap.values().remove(arg.ulIdentifyID);
                break;
            case 5:
                log.debug("设备网络连接断开");
                HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(arg.ulIdentifyID);
                deviceMap.values().remove(arg.ulIdentifyID);
                break;
            case 6:
                log.debug("发送或接收失败");
                HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(arg.ulIdentifyID);
                deviceMap.values().remove(arg.ulIdentifyID);
                break;
            case 7:
                log.debug("设备保活失败");
                HWPuSDKLibrary.INSTANCE.IVS_PU_Logout(arg.ulIdentifyID);
                deviceMap.values().remove(arg.ulIdentifyID);
                break;
            case 8:
                log.debug("流套餐变更");
                break;
            case 9:
                log.debug("数字水印校验错误");
                break;
            case 10:
                log.debug("设备主动获取抓拍图片上传URL请求");
                break;
            case 11:
                log.debug("设备抓拍图片上载完成通知");
                break;
            case 12:
                log.debug("透明通道数据上报");
                break;
            case 13:
                log.debug("实况异常");
                break;
            case 14:
                log.debug("上报可视化信息");
                break;
            case 15:
                log.debug("多机协同数据上报");
                break;
            case 16:
                log.debug("录像下载完成通知");
                break;
            case 17:
                log.debug("新增从设备事件");
                break;
            case 18:
                log.debug("修改从设备事件");
                break;
            case 19:
                log.debug("删除从设备事件");
                break;
            case 20:
                log.debug("从设备上线事件");
                break;
            case 21:
                log.debug("从设备下线事件");
                break;
            default:
                break;
        }
    }
}
