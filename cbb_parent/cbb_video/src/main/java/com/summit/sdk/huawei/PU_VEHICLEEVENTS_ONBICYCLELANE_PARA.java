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
public class PU_VEHICLEEVENTS_ONBICYCLELANE_PARA extends Structure {
	/** \u0368\ufffd\ufffdID */
	public NativeLong ulChannelId;
	/** \ufffd\u3de8\u02b9\ufffd\ufffd */
	public boolean enEnable;
	/** \u05e5\ufffd\ufffd\u02b9\ufffd\ufffd */
	public boolean enSnapEnable;
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd1~100 */
	public int unVehicleSensitivity;
	/** \u057c\ufffd\ufffd\u02b1\ufffd\u4d65\u03bb\ufffd\ufffd0~10 */
	public int unOccupiedTime;
	/** \ufffd\u6faf\ufffd\u03f1\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulAlarmTime;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : AERADTC_LIST_S
	 */
	public AERADTC_LIST stAreaList;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\u01bb\ufffd<br>
	 * C type : PU_ALARM_TIME_LIST_S
	 */
	public PU_ALARM_TIME_PARA_LIST stGuardPlan;
	/** C type : CHAR[256] */
	public byte[] szReserved = new byte[256];
	public PU_VEHICLEEVENTS_ONBICYCLELANE_PARA() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChannelId", "enEnable", "enSnapEnable", "unVehicleSensitivity", "unOccupiedTime", "ulAlarmTime", "stAreaList", "stGuardPlan", "szReserved");
	}
	/**
	 * @param ulChannelId \u0368\ufffd\ufffdID<br>
	 * @param enEnable \ufffd\u3de8\u02b9\ufffd\ufffd<br>
	 * @param enSnapEnable \u05e5\ufffd\ufffd\u02b9\ufffd\ufffd<br>
	 * @param unVehicleSensitivity \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd1~100<br>
	 * @param unOccupiedTime \u057c\ufffd\ufffd\u02b1\ufffd\u4d65\u03bb\ufffd\ufffd0~10<br>
	 * @param ulAlarmTime \ufffd\u6faf\ufffd\u03f1\ufffd\ufffd\ufffd\ufffd<br>
	 * @param stAreaList \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : AERADTC_LIST_S<br>
	 * @param stGuardPlan \ufffd\ufffd\ufffd\ufffd\ufffd\u01bb\ufffd<br>
	 * C type : PU_ALARM_TIME_LIST_S<br>
	 * @param szReserved C type : CHAR[256]
	 */
	public PU_VEHICLEEVENTS_ONBICYCLELANE_PARA(NativeLong ulChannelId, boolean enEnable, boolean enSnapEnable, int unVehicleSensitivity, int unOccupiedTime, NativeLong ulAlarmTime, AERADTC_LIST stAreaList, PU_ALARM_TIME_PARA_LIST stGuardPlan, byte szReserved[]) {
		super();
		this.ulChannelId = ulChannelId;
		this.enEnable = enEnable;
		this.enSnapEnable = enSnapEnable;
		this.unVehicleSensitivity = unVehicleSensitivity;
		this.unOccupiedTime = unOccupiedTime;
		this.ulAlarmTime = ulAlarmTime;
		this.stAreaList = stAreaList;
		this.stGuardPlan = stGuardPlan;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_VEHICLEEVENTS_ONBICYCLELANE_PARA(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_VEHICLEEVENTS_ONBICYCLELANE_PARA implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_VEHICLEEVENTS_ONBICYCLELANE_PARA implements Structure.ByValue {
		
	};
}
