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
public class PU_OSDI_AREA_CFG_PARA_V20 extends Structure {
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulIndex;
	/** \u02b9\ufffd\ufffd */
	public boolean bEnable;
	/** OSDI\ufffd\ufffd\u01b3\ufffd\ufffd\ufffd */
	public NativeLong ulOSDINameLen;
	/**
	 * OSDI\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(96 + 4)]
	 */
	public byte[] szOSDIName = new byte[96 + 4];
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_OSDI_POINT_INFO_S
	 */
	public PU_OSDI_POINT_INFO stLowerLeftPoint;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_OSDI_POINT_INFO_S
	 */
	public PU_OSDI_POINT_INFO stUpperRightPoint;
	/**
	 * OSDI\ufffd\ufffd\ufffd\u03fd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_POINT_S
	 */
	public PU_POINT stOSDITopLeftPos;
	public PU_OSDI_AREA_CFG_PARA_V20() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulIndex", "bEnable", "ulOSDINameLen", "szOSDIName", "stLowerLeftPoint", "stUpperRightPoint", "stOSDITopLeftPos");
	}
	/**
	 * @param ulIndex \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * @param bEnable \u02b9\ufffd\ufffd<br>
	 * @param ulOSDINameLen OSDI\ufffd\ufffd\u01b3\ufffd\ufffd\ufffd<br>
	 * @param szOSDIName OSDI\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(96 + 4)]<br>
	 * @param stLowerLeftPoint \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_OSDI_POINT_INFO_S<br>
	 * @param stUpperRightPoint \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_OSDI_POINT_INFO_S<br>
	 * @param stOSDITopLeftPos OSDI\ufffd\ufffd\ufffd\u03fd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_POINT_S
	 */
	public PU_OSDI_AREA_CFG_PARA_V20(NativeLong ulIndex, boolean bEnable, NativeLong ulOSDINameLen, byte szOSDIName[], PU_OSDI_POINT_INFO stLowerLeftPoint, PU_OSDI_POINT_INFO stUpperRightPoint, PU_POINT stOSDITopLeftPos) {
		super();
		this.ulIndex = ulIndex;
		this.bEnable = bEnable;
		this.ulOSDINameLen = ulOSDINameLen;
		if ((szOSDIName.length != this.szOSDIName.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szOSDIName = szOSDIName;
		this.stLowerLeftPoint = stLowerLeftPoint;
		this.stUpperRightPoint = stUpperRightPoint;
		this.stOSDITopLeftPos = stOSDITopLeftPos;
	}
	public PU_OSDI_AREA_CFG_PARA_V20(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_OSDI_AREA_CFG_PARA_V20 implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_OSDI_AREA_CFG_PARA_V20 implements Structure.ByValue {
		
	};
}
