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
public class PU_VIDEO_STREAM_INFO extends Structure {
	/** \u0368\ufffd\ufffdID */
	public NativeLong ulChannelId;
	/**
	 * @see PU_STREAM_MODE_E<br>
	 * \ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_STREAM_MODE_E
	 */
	public int enStreamMode;
	/**
	 * \ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_STREAM_ATTRIBUTE_S[PU_VIEDO_STREAM_TRIPLE]
	 */
	public PU_STREAM_ATTRIBUTE[] stStreamAttribute = new PU_STREAM_ATTRIBUTE[(int)com.summit.sdk.huawei.HWPuSDKLibrary.PU_STREAM_MODE_E.PU_VIEDO_STREAM_TRIPLE];
	/**
	 * szReserve[0]\ufffd\ufffd\u02b6\u022b\u05a1\ufffd\ufffd<br>
	 * C type : CHAR[32]
	 */
	public byte[] szReserved = new byte[32];
	public PU_VIDEO_STREAM_INFO() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChannelId", "enStreamMode", "stStreamAttribute", "szReserved");
	}
	/**
	 * @param ulChannelId \u0368\ufffd\ufffdID<br>
	 * @param enStreamMode @see PU_STREAM_MODE_E<br>
	 * \ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_STREAM_MODE_E<br>
	 * @param stStreamAttribute \ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_STREAM_ATTRIBUTE_S[PU_VIEDO_STREAM_TRIPLE]<br>
	 * @param szReserved szReserve[0]\ufffd\ufffd\u02b6\u022b\u05a1\ufffd\ufffd<br>
	 * C type : CHAR[32]
	 */
	public PU_VIDEO_STREAM_INFO(NativeLong ulChannelId, int enStreamMode, PU_STREAM_ATTRIBUTE stStreamAttribute[], byte szReserved[]) {
		super();
		this.ulChannelId = ulChannelId;
		this.enStreamMode = enStreamMode;
		if ((stStreamAttribute.length != this.stStreamAttribute.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.stStreamAttribute = stStreamAttribute;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_VIDEO_STREAM_INFO(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_VIDEO_STREAM_INFO implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_VIDEO_STREAM_INFO implements Structure.ByValue {
		
	};
}
