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
public class PU_DEVICE_ABILITY extends Structure {
	/** \ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulVideoInputChannelNum;
	/** \ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulAudioInputChannelNum;
	/** \ufffd\u6faf\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulAlarmDINum;
	/** \ufffd\u6faf\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulAlarmDONum;
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulSerialPortNum;
	/** \ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulNetworkCardNum;
	/** \u04f2\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulHardDiskNum;
	/** \ufffd\ufffd\u0328\ufffd\ufffd */
	public NativeLong ulPTZNum;
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\u053d\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulTalkbackInputNum;
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\u053d\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulTalkbackOutputNum;
	/** \ufffd\ufffd\ufffd\ufffd\u042d\ufffd\ufffd: 0:RTPoverUDP 1:RTPoverTCP 2:RTPoverUDP&TCP */
	public NativeLong ulRTPOverFlag;
	/**
	 * \u04f2\ufffd\ufffd\ufffd\ufffd\u03e2<br>
	 * C type : PU_HARD_DISK_PARA_S
	 */
	public PU_HARD_DISK_PARA stHardDiskInfo;
	/** C type : CHAR[32] */
	public byte[] szReserved = new byte[32];
	public PU_DEVICE_ABILITY() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulVideoInputChannelNum", "ulAudioInputChannelNum", "ulAlarmDINum", "ulAlarmDONum", "ulSerialPortNum", "ulNetworkCardNum", "ulHardDiskNum", "ulPTZNum", "ulTalkbackInputNum", "ulTalkbackOutputNum", "ulRTPOverFlag", "stHardDiskInfo", "szReserved");
	}
	public PU_DEVICE_ABILITY(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DEVICE_ABILITY implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DEVICE_ABILITY implements Structure.ByValue {
		
	};
}
