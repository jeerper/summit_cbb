package com.summit.sdk.huawei;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : E:\video\HWPuSDK.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class PU_PIC_OSD_PARA extends Structure {
	/** \u0368\ufffd\ufffdID */
	public NativeLong ulChnID;
	/**
	 * \ufffd\ufffd\ufffd\ufffdOSD\ufffd\ufffd\u037c\u01ac\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_OVERLAY_PIC_TYPE_E
	 */
	public int enOverlayPicType;
	/**
	 * \u037c\u01ac\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u03e2<br>
	 * C type : PU_PIC_OSD_INFO_S
	 */
	public PU_PIC_OSD_INFO_S stPicOSDInfo;
	/**
	 * \u02b1\ufffd\ufffdOSD\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_TIME_OSD_PARA_S
	 */
	public PU_TIME_OSD_PARA stTimeOSDPara;
	/**
	 * \u00b7\ufffd\u06b1\ufffd\u0172\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S
	 */
	public PU_CUSTOM_OSD_PARA stRoadIdOSDPara;
	/**
	 * \ufffd\u8c78\ufffd\ufffd\u0172\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S
	 */
	public PU_CUSTOM_OSD_PARA stDevIdOSDPara;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\u0172\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S
	 */
	public PU_CUSTOM_OSD_PARA stDirIdOSDPara;
	/**
	 * \ufffd\ufffd\ufffd\ufffdOSD\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S
	 */
	public PU_CUSTOM_OSD_PARA stDirOSDPara;
	/**
	 * \ufffd\u0536\ufffd\ufffd\ufffdOSD\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S
	 */
	public PU_CUSTOM_OSD_PARA stCustomOSDPara;
	public PU_PIC_OSD_PARA() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChnID", "enOverlayPicType", "stPicOSDInfo", "stTimeOSDPara", "stRoadIdOSDPara", "stDevIdOSDPara", "stDirIdOSDPara", "stDirOSDPara", "stCustomOSDPara");
	}
	/**
	 * @param ulChnID \u0368\ufffd\ufffdID<br>
	 * @param enOverlayPicType \ufffd\ufffd\ufffd\ufffdOSD\ufffd\ufffd\u037c\u01ac\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_OVERLAY_PIC_TYPE_E<br>
	 * @param stPicOSDInfo \u037c\u01ac\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u03e2<br>
	 * C type : PU_PIC_OSD_INFO_S<br>
	 * @param stTimeOSDPara \u02b1\ufffd\ufffdOSD\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_TIME_OSD_PARA_S<br>
	 * @param stRoadIdOSDPara \u00b7\ufffd\u06b1\ufffd\u0172\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S<br>
	 * @param stDevIdOSDPara \ufffd\u8c78\ufffd\ufffd\u0172\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S<br>
	 * @param stDirIdOSDPara \ufffd\ufffd\ufffd\ufffd\ufffd\u0172\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S<br>
	 * @param stDirOSDPara \ufffd\ufffd\ufffd\ufffdOSD\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S<br>
	 * @param stCustomOSDPara \ufffd\u0536\ufffd\ufffd\ufffdOSD\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_CUSTOM_OSD_PARA_S
	 */
	public PU_PIC_OSD_PARA(NativeLong ulChnID, int enOverlayPicType, PU_PIC_OSD_INFO_S stPicOSDInfo, PU_TIME_OSD_PARA stTimeOSDPara, PU_CUSTOM_OSD_PARA stRoadIdOSDPara, PU_CUSTOM_OSD_PARA stDevIdOSDPara, PU_CUSTOM_OSD_PARA stDirIdOSDPara, PU_CUSTOM_OSD_PARA stDirOSDPara, PU_CUSTOM_OSD_PARA stCustomOSDPara) {
		super();
		this.ulChnID = ulChnID;
		this.enOverlayPicType = enOverlayPicType;
		this.stPicOSDInfo = stPicOSDInfo;
		this.stTimeOSDPara = stTimeOSDPara;
		this.stRoadIdOSDPara = stRoadIdOSDPara;
		this.stDevIdOSDPara = stDevIdOSDPara;
		this.stDirIdOSDPara = stDirIdOSDPara;
		this.stDirOSDPara = stDirOSDPara;
		this.stCustomOSDPara = stCustomOSDPara;
	}
	public PU_PIC_OSD_PARA(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_PIC_OSD_PARA implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_PIC_OSD_PARA implements Structure.ByValue {
		
	};
}
