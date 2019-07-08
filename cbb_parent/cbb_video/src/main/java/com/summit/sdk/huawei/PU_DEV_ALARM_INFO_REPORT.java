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
public class PU_DEV_ALARM_INFO_REPORT extends Structure {
	/** \ufffd\u8c78\ufffd\ufffd\ufffd\ufffdID */
	public NativeLong ulIdentifyID;
	/** \ufffd\u6faf\u0123\ufffd\ufffdID */
	public NativeLong ulAlarmModID;
	/** \ufffd\u01f7\ufffd\ufffd\ufffd\ufffd, 1:\ufffd\ufffd\u00e3\ufffd0:\ufffd\ufffd\ufffd\ufffd\ufffd */
	public boolean IsIntact;
	/**
	 * \ufffd\u6faf\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_ALARM_LEVEL_E
	 */
	public int enAlarmLevel;
	/**
	 * \ufffd\u6faf\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_ALARM_TYPE_E
	 */
	public int enAlarmType;
	/**
	 * \ufffd\u6faf\ufffd\ufffd\ufffd\ufffd/\ufffd\ufffd\ufffd\u02b1\ufffd\ufffdUTC \u02b1\ufffd\ufffd<br>
	 * C type : PU_TIME_S
	 */
	public PU_TIME stTime;
	/**
	 * \ufffd\u6faf\ufffd\ufffd\ufffd\ufffd/\ufffd\ufffd\ufffd<br>
	 * C type : PU_ALARM_ACTION_E
	 */
	public int enAction;
	/** \ufffd\u6faf\ufffd\ufffd\ufffd\u043a\ufffd */
	public NativeLong ulAlarmId;
	/**
	 * \ufffd\u6faf\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[64]
	 */
	public byte[] szAlarmDesc = new byte[64];
	/**
	 * \ufffd\u6faf\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[64]
	 */
	public byte[] szAlarmCleanDesc = new byte[64];
	/** C type : CHAR[32] */
	public byte[] szReserve = new byte[32];
	public PU_DEV_ALARM_INFO_REPORT() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulIdentifyID", "ulAlarmModID", "IsIntact", "enAlarmLevel", "enAlarmType", "stTime", "enAction", "ulAlarmId", "szAlarmDesc", "szAlarmCleanDesc", "szReserve");
	}
	public PU_DEV_ALARM_INFO_REPORT(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DEV_ALARM_INFO_REPORT implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DEV_ALARM_INFO_REPORT implements Structure.ByValue {
		
	};
}
