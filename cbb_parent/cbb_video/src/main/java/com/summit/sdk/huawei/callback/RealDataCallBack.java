package com.summit.sdk.huawei.callback;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.summit.sdk.huawei.HWPuSDKLibrary;
import com.summit.sdk.huawei.PU_META_DATA;
import com.summit.sdk.huawei.PU_UserData;
import com.summit.sdk.huawei.model.CardType;
import com.summit.sdk.huawei.model.Gender;
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
                //人脸信息,对应摄像头的人脸库信息
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_INFO:
                    log.debug("================人脸信息业务处理=================");
                    log.debug("名字:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.name, "").trim());

                    Gender gender = Gender.codeOf(userDataEntity.unMetaData.stFaceInfo.iGender);

                    if (gender == Gender.PU_MALE) {
                        log.debug("性别:男");
                    } else if (gender == Gender.PU_FEMALE) {
                        log.debug("性别:女");
                    } else if (gender == Gender.PU_GENDER_UNKNOW) {
                        log.debug("性别:未知");
                    }
                    log.debug("生日:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.birthday, "").trim());
                    log.debug("省级:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.province, "").trim());
                    log.debug("地市:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.city, "").trim());
                    CardType iCardType = CardType.codeOf(userDataEntity.unMetaData.stFaceInfo.iCardType);
                    if (iCardType == CardType.IDENTITY) {
                        log.debug("证件类型:身份证");
                    } else if (iCardType == CardType.PASSPORT) {
                        log.debug("证件类型:护照");
                    } else if (iCardType == CardType.OFFICER) {
                        log.debug("证件类型:军官证");
                    } else if (iCardType == CardType.DRIVING) {
                        log.debug("证件类型:驾驶证");
                    } else if (iCardType == CardType.OTHERS) {
                        log.debug("证件类型:其他");
                    }

                    log.debug("证件号:" + StrUtil.str(userDataEntity.unMetaData.stFaceInfo.cardID, "").trim());
                    break;
                //人脸匹配率
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_MATCHRATE:
                    float matchScores = userDataEntity.unMetaData.IntValue / 100f;
                    log.debug("人脸匹配率:{}%", matchScores);
                    break;
                //人脸特征属性
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_FEATURE:
                    break;
                //人体特征属性
                case HWPuSDKLibrary.LAYER_THREE_TYPE.HUMAN_FEATURE:
                    break;
                //人脸识别全景图
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_PANORAMA:
                    System.out.println("dataHandler 全景图： " + userDataEntity.unMetaData.stBinay.ulBinaryLenth);
//                    realDataImpl.saveJPG(userData[i].unMetaData.stBinay, 3);
                    break;
                //人脸识别抠图
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_PIC:
                    break;
                //人脸识别和人脸库中匹配的图片
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_MATCH:
                    break;
                //抓拍时间
                case HWPuSDKLibrary.LAYER_THREE_TYPE.PIC_SNAPSHOT_TIME:
                    DateTime time = new DateTime(userDataEntity.unMetaData.IntValue * 1000L);
                    log.debug("抓怕时间:" + time.toString("yyyy-MM-dd HH:mm:ss"));
                    break;
                //名单库中的人脸ID，用来维持特征 record的一致性
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACELIB_RECORDID:
                    break;
                //相机通道号
                case HWPuSDKLibrary.LAYER_THREE_TYPE.CHANNEL_ID:
                    break;
                //人脸位置(实时位置框)
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_POS:
                    break;
                //target类型，当前用于区分人脸后处理抠图和人脸识别以及人脸识别多机协同
                case HWPuSDKLibrary.LAYER_THREE_TYPE.TARGET_TYPE:
                    break;
                //车辆类型
                case HWPuSDKLibrary.LAYER_THREE_TYPE.VEHICLE_TYPE:
                    break;
                //C50车辆类型
                case HWPuSDKLibrary.LAYER_THREE_TYPE.VEHICLE_TYPE_EXT:
                    break;
                //人体位置(实时位置框)
                case HWPuSDKLibrary.LAYER_THREE_TYPE.HUMAN_RECT:
                    break;
                default:
//                    log.debug("未知数据类型eType-Hex: 0x" + Convert.toHex(Convert.intToBytes(userDataEntity.eType)).toUpperCase());
                    break;
            }
        }
    }
}
