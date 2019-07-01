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
public class PU_DYNAMIC_STREAM_INFO extends Structure {
	public NativeLong ulChannelId;
	/** \ufffd\ufffd\u032c\ufffd\ufffd\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd */
	public boolean ulEnable;
	/** \ufffd\ufffd\u032c\u05a1\ufffd\ufffd */
	public NativeLong ulDynamicFrameRate;
	/** \ufffd\ufffd\u032c\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulDynamicBitRate;
	/**
	 * szReserved[0]\ufffd\ufffd\u032c\u05a1\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd<br>
	 * C type : CHAR[32]
	 */
	public byte[] szReserved = new byte[32];
	public PU_DYNAMIC_STREAM_INFO() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChannelId", "ulEnable", "ulDynamicFrameRate", "ulDynamicBitRate", "szReserved");
	}
	/**
	 * @param ulEnable \ufffd\ufffd\u032c\ufffd\ufffd\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd<br>
	 * @param ulDynamicFrameRate \ufffd\ufffd\u032c\u05a1\ufffd\ufffd<br>
	 * @param ulDynamicBitRate \ufffd\ufffd\u032c\ufffd\ufffd\ufffd\ufffd<br>
	 * @param szReserved szReserved[0]\ufffd\ufffd\u032c\u05a1\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd<br>
	 * C type : CHAR[32]
	 */
	public PU_DYNAMIC_STREAM_INFO(NativeLong ulChannelId, boolean ulEnable, NativeLong ulDynamicFrameRate, NativeLong ulDynamicBitRate, byte szReserved[]) {
		super();
		this.ulChannelId = ulChannelId;
		this.ulEnable = ulEnable;
		this.ulDynamicFrameRate = ulDynamicFrameRate;
		this.ulDynamicBitRate = ulDynamicBitRate;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_DYNAMIC_STREAM_INFO(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DYNAMIC_STREAM_INFO implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DYNAMIC_STREAM_INFO implements Structure.ByValue {
		
	};
}
