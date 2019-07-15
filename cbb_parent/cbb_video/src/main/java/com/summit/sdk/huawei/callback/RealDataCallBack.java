package com.summit.sdk.huawei.callback;

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
        PointerByReference pu_meta_data_pointer_pointer = new PointerByReference(Pointer.NULL);
//        PointerByReference pu_meta_data_pointer_pointer = new PointerByReference(Pointer.NULL);
        HWPuSDKLibrary.INSTANCE.IVS_User_GetMetaData(szBuffer, lSize, 1, pu_meta_data_pointer_pointer);
        PU_META_DATA data = Structure.newInstance(PU_META_DATA.class, pu_meta_data_pointer_pointer.getValue());
        data.read();
        PU_UserData[] userData = (PU_UserData[]) data.pstMetaUserData.toArray(data.usValidNumber);
        for (PU_UserData userDataEntity : userData) {
            log.debug("eType:" + userDataEntity.eType);
        }
        log.debug("设备IP:" + pUsrData.getString(0));
        HWPuSDKLibrary.INSTANCE.IVS_User_FreeMetaData(pu_meta_data_pointer_pointer);
    }
}
