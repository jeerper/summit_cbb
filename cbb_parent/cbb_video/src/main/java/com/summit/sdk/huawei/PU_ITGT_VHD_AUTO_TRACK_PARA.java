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
public class PU_ITGT_VHD_AUTO_TRACK_PARA extends Structure {
	/** \u0368\ufffd\ufffdID */
	public NativeLong ulChannelID;
	/** \ufffd\u3de8\u02b9\ufffd\ufffd */
	public boolean bEnable;
	/** \ufffd\u3de8\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulSensitivity;
	/** \ufffd\u6faf\ufffd\u03f1\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulAlarmTime;
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u02b1\ufffd\ufffd */
	public NativeLong ulMaxTraceTime;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : AERADTC_LIST_S
	 */
	public AERADTC_LIST stAreaList;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\u01bb\ufffd<br>
	 * C type : PU_ALARM_TIME_LIST_S
	 */
	public PU_ALARM_TIME_PARA_LIST stAlarmTimeList;
	/**
	 * \ufffd\u0536\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u023c\ufffd<br>
	 * C type : PU_ITGT_VHD_OBJECT_ITEM_S[(3)]
	 */
	public PU_ITGT_VHD_OBJECT_ITEM[] objItem = new PU_ITGT_VHD_OBJECT_ITEM[3];
	/** C type : CHAR[256] */
	public byte[] szReserved = new byte[256];
	public PU_ITGT_VHD_AUTO_TRACK_PARA() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChannelID", "bEnable", "ulSensitivity", "ulAlarmTime", "ulMaxTraceTime", "stAreaList", "stAlarmTimeList", "objItem", "szReserved");
	}
	/**
	 * @param ulChannelID \u0368\ufffd\ufffdID<br>
	 * @param bEnable \ufffd\u3de8\u02b9\ufffd\ufffd<br>
	 * @param ulSensitivity \ufffd\u3de8\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * @param ulAlarmTime \ufffd\u6faf\ufffd\u03f1\ufffd\ufffd\ufffd\ufffd<br>
	 * @param ulMaxTraceTime \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u02b1\ufffd\ufffd<br>
	 * @param stAreaList \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : AERADTC_LIST_S<br>
	 * @param stAlarmTimeList \ufffd\ufffd\ufffd\ufffd\ufffd\u01bb\ufffd<br>
	 * C type : PU_ALARM_TIME_LIST_S<br>
	 * @param objItem \ufffd\u0536\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u023c\ufffd<br>
	 * C type : PU_ITGT_VHD_OBJECT_ITEM_S[(3)]<br>
	 * @param szReserved C type : CHAR[256]
	 */
	public PU_ITGT_VHD_AUTO_TRACK_PARA(NativeLong ulChannelID, boolean bEnable, NativeLong ulSensitivity, NativeLong ulAlarmTime, NativeLong ulMaxTraceTime, AERADTC_LIST stAreaList, PU_ALARM_TIME_PARA_LIST stAlarmTimeList, PU_ITGT_VHD_OBJECT_ITEM objItem[], byte szReserved[]) {
		super();
		this.ulChannelID = ulChannelID;
		this.bEnable = bEnable;
		this.ulSensitivity = ulSensitivity;
		this.ulAlarmTime = ulAlarmTime;
		this.ulMaxTraceTime = ulMaxTraceTime;
		this.stAreaList = stAreaList;
		this.stAlarmTimeList = stAlarmTimeList;
		if ((objItem.length != this.objItem.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.objItem = objItem;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_ITGT_VHD_AUTO_TRACK_PARA(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_ITGT_VHD_AUTO_TRACK_PARA implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_ITGT_VHD_AUTO_TRACK_PARA implements Structure.ByValue {
		
	};
}
