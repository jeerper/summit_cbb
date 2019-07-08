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
public class PU_VIDEO_ENCODE_PARAV2 extends Structure {
	/**
	 * @see PU_STREAM_MODE_E<br>
	 * \ufffd\ufffd\ufffd\u05f2\u0377\ufffd\u02bd<br>
	 * C type : PU_STREAM_MODE_E
	 */
	public int enVideoStreamMode;
	/** \ufffd\ufffd\u032c\u05a1\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd */
	public boolean bDynamicFrmRateEnable;
	/** \ufffd\ufffd\u032c\u05a1\ufffd\ufffd */
	public NativeLong ulDynamicFrmRate;
	/** \ufffd\ufffd\u032c\ufffd\ufffd\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd */
	public boolean bDynamicBitRateEnable;
	/** \ufffd\ufffd\u032c\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulDynamicBitRate;
	/**
	 * \ufffd\ufffd\u01b5\ufffd\u027c\ufffd\u05a1\ufffd\ufffd\u02bd<br>
	 * C type : PU_FRAMERATE_FORMAT_E
	 */
	public int enFrameMat;
	/**
	 * \ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_STREAM_ATTRIBUTE_S[5]
	 */
	public PU_STREAM_ATTRIBUTE[] stStreamAttribute = new PU_STREAM_ATTRIBUTE[5];
	/**
	 * szReserved[0]\ufffd\ufffd\u02b6\ufffd\ufffd\u056d\ufffd\ufffd\u0123\u02bd 0:\ufffd\ufffd\ufffd\ufffd 1:\u056d\ufffd\ufffd<br>
	 * C type : CHAR[32]
	 */
	public byte[] szReserve = new byte[32];
	public PU_VIDEO_ENCODE_PARAV2() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("enVideoStreamMode", "bDynamicFrmRateEnable", "ulDynamicFrmRate", "bDynamicBitRateEnable", "ulDynamicBitRate", "enFrameMat", "stStreamAttribute", "szReserve");
	}
	/**
	 * @param enVideoStreamMode @see PU_STREAM_MODE_E<br>
	 * \ufffd\ufffd\ufffd\u05f2\u0377\ufffd\u02bd<br>
	 * C type : PU_STREAM_MODE_E<br>
	 * @param bDynamicFrmRateEnable \ufffd\ufffd\u032c\u05a1\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd<br>
	 * @param ulDynamicFrmRate \ufffd\ufffd\u032c\u05a1\ufffd\ufffd<br>
	 * @param bDynamicBitRateEnable \ufffd\ufffd\u032c\ufffd\ufffd\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd<br>
	 * @param ulDynamicBitRate \ufffd\ufffd\u032c\ufffd\ufffd\ufffd\ufffd<br>
	 * @param enFrameMat \ufffd\ufffd\u01b5\ufffd\u027c\ufffd\u05a1\ufffd\ufffd\u02bd<br>
	 * C type : PU_FRAMERATE_FORMAT_E<br>
	 * @param stStreamAttribute \ufffd\ufffd\u01b5\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_STREAM_ATTRIBUTE_S[5]<br>
	 * @param szReserve szReserved[0]\ufffd\ufffd\u02b6\ufffd\ufffd\u056d\ufffd\ufffd\u0123\u02bd 0:\ufffd\ufffd\ufffd\ufffd 1:\u056d\ufffd\ufffd<br>
	 * C type : CHAR[32]
	 */
	public PU_VIDEO_ENCODE_PARAV2(int enVideoStreamMode, boolean bDynamicFrmRateEnable, NativeLong ulDynamicFrmRate, boolean bDynamicBitRateEnable, NativeLong ulDynamicBitRate, int enFrameMat, PU_STREAM_ATTRIBUTE stStreamAttribute[], byte szReserve[]) {
		super();
		this.enVideoStreamMode = enVideoStreamMode;
		this.bDynamicFrmRateEnable = bDynamicFrmRateEnable;
		this.ulDynamicFrmRate = ulDynamicFrmRate;
		this.bDynamicBitRateEnable = bDynamicBitRateEnable;
		this.ulDynamicBitRate = ulDynamicBitRate;
		this.enFrameMat = enFrameMat;
		if ((stStreamAttribute.length != this.stStreamAttribute.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.stStreamAttribute = stStreamAttribute;
		if ((szReserve.length != this.szReserve.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserve = szReserve;
	}
	public PU_VIDEO_ENCODE_PARAV2(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_VIDEO_ENCODE_PARAV2 implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_VIDEO_ENCODE_PARAV2 implements Structure.ByValue {
		
	};
}
