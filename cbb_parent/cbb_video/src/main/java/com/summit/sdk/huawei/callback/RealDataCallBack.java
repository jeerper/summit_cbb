package com.summit.sdk.huawei.callback;

import cn.hutool.core.util.StrUtil;
import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_META_DATA;
import com.summit.sdk.huawei.PU_UserData;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealDataCallBack implements HWPuSDKLibrary.pfRealDataCallBack {

    @Override
    public void apply(Pointer szBuffer, NativeLong lSize, Pointer pUsrData) {
//        log.debug("设备IP:" + pUsrData.getString(0));
        procBuffer(szBuffer, lSize, HWPuSDKLibrary.LAYER_TWO_TYPE.COMMON);
        procBuffer(szBuffer, lSize, HWPuSDKLibrary.LAYER_TWO_TYPE.TARGET);
    }

    private void procBuffer(Pointer szBuffer, NativeLong lSize, int layerTwoType) {
        PointerByReference pu_meta_data_pointer_pointer = new PointerByReference(Pointer.NULL);
        HWPuSDKLibrary.INSTANCE.IVS_User_GetMetaData(szBuffer, lSize, layerTwoType, pu_meta_data_pointer_pointer);
        procMetaData(pu_meta_data_pointer_pointer);
        HWPuSDKLibrary.INSTANCE.IVS_User_FreeMetaData(pu_meta_data_pointer_pointer);
    }

    private void procMetaData(PointerByReference pu_meta_data_pointer_pointer) {
        PU_META_DATA data = Structure.newInstance(PU_META_DATA.class, pu_meta_data_pointer_pointer.getValue());
        data.read();
        PU_UserData[] userData = (PU_UserData[]) data.pstMetaUserData.toArray(data.usValidNumber);
        for (PU_UserData userDataEntity : userData) {
            switch (userDataEntity.eType) {
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_INFO:
                    log.debug("================人脸信息业务处理=================");
                    log.debug("名字:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.name, "").trim());
                    if (userDataEntity.unMetaData.stFaceInfo.iGender == HWPuSDKLibrary.PU_GENDER.PU_MALE) {
                        log.debug("性别:男");
                    } else if (userDataEntity.unMetaData.stFaceInfo.iGender == HWPuSDKLibrary.PU_GENDER.PU_FEMALE) {
                        log.debug("性别:女");
                    } else {
                        log.debug("性别:未知");
                    }
                    log.debug("生日:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.birthday, "").trim());
                    log.debug("省级:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.province, "").trim());
                    log.debug("地市:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.city, "").trim());
                    int iCardType = userDataEntity.unMetaData.stFaceInfo.iCardType;
                    if (iCardType == HWPuSDKLibrary.PU_CARDTYPE.IDENTITY) {
                        log.debug("证件类型:身份证");
                    } else if (iCardType == HWPuSDKLibrary.PU_CARDTYPE.PASSPORT) {
                        log.debug("证件类型:护照");
                    } else if (iCardType == HWPuSDKLibrary.PU_CARDTYPE.OFFICER) {
                        log.debug("证件类型:军官证");
                    } else if (iCardType == HWPuSDKLibrary.PU_CARDTYPE.DRIVING) {
                        log.debug("证件类型:驾驶证");
                    } else if (iCardType == HWPuSDKLibrary.PU_CARDTYPE.OTHERS) {
                        log.debug("证件类型:其他");
                    }

                    log.debug("证件号:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.cardID, "").trim());
                    break;
                default:
//                    log.debug("未知数据类型eType-Hex: 0x" + Convert.toHex(Convert.intToBytes(userDataEntity.eType)).toUpperCase());
                    break;
            }
        }
    }
}
