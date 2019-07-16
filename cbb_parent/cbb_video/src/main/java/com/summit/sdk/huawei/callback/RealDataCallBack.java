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
        log.debug("设备IP:" + pUsrData.getString(0));
        procBuffer(szBuffer,lSize,HWPuSDKLibrary.LAYER_TWO_TYPE.COMMON);
        procBuffer(szBuffer,lSize,HWPuSDKLibrary.LAYER_TWO_TYPE.TARGET);
    }

    private void procBuffer(Pointer szBuffer, NativeLong lSize,int layerTwoType){
        PointerByReference pu_meta_data_pointer_pointer = new PointerByReference(Pointer.NULL);
        HWPuSDKLibrary.INSTANCE.IVS_User_GetMetaData(szBuffer, lSize, layerTwoType, pu_meta_data_pointer_pointer);
        procMetaData(pu_meta_data_pointer_pointer);
        HWPuSDKLibrary.INSTANCE.IVS_User_FreeMetaData(pu_meta_data_pointer_pointer);
    }

    private void procMetaData(PointerByReference pu_meta_data_pointer_pointer){
        PU_META_DATA data = Structure.newInstance(PU_META_DATA.class, pu_meta_data_pointer_pointer.getValue());
        data.read();
        PU_UserData[] userData = (PU_UserData[]) data.pstMetaUserData.toArray(data.usValidNumber);
        for (PU_UserData userDataEntity : userData) {
            switch (userDataEntity.eType) {
                case HWPuSDKLibrary.LAYER_THREE_TYPE.FACE_INFO:
                    log.debug("================人脸信息业务处理=================");
                    log.debug(StrUtil.str(userDataEntity.unMetaData.stFaceInfo.name,"").trim());

                    break;
                default:
//                    log.debug("未知数据类型eType-Hex: 0x" + Convert.toHex(Convert.intToBytes(userDataEntity.eType)).toUpperCase());
                    break;
            }
        }
    }
}
