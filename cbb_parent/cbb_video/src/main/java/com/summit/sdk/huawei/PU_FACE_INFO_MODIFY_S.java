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
public class PU_FACE_INFO_MODIFY_S extends Structure {
	/** \u0368\ufffd\ufffdID */
	public NativeLong ulChannelId;
	/**
	 * \ufffd\ufffd\ufffd<br>
	 * C type : PU_FACE_LIB_S
	 */
	public PU_FACE_LIB_S stFacelib;
	/**
	 * \ufffd\u07b8\u013a\ufffd\u013d\ufffd\ufffd<br>
	 * C type : PU_FACE_RECORD_S
	 */
	public PU_FACE_RECORD stRecord;
	/** C type : CHAR[32] */
	public byte[] szReserve = new byte[32];
	public PU_FACE_INFO_MODIFY_S() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChannelId", "stFacelib", "stRecord", "szReserve");
	}
	/**
	 * @param ulChannelId \u0368\ufffd\ufffdID<br>
	 * @param stFacelib \ufffd\ufffd\ufffd<br>
	 * C type : PU_FACE_LIB_S<br>
	 * @param stRecord \ufffd\u07b8\u013a\ufffd\u013d\ufffd\ufffd<br>
	 * C type : PU_FACE_RECORD_S<br>
	 * @param szReserve C type : CHAR[32]
	 */
	public PU_FACE_INFO_MODIFY_S(NativeLong ulChannelId, PU_FACE_LIB_S stFacelib, PU_FACE_RECORD stRecord, byte szReserve[]) {
		super();
		this.ulChannelId = ulChannelId;
		this.stFacelib = stFacelib;
		this.stRecord = stRecord;
		if ((szReserve.length != this.szReserve.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserve = szReserve;
	}
	public PU_FACE_INFO_MODIFY_S(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_FACE_INFO_MODIFY_S implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_FACE_INFO_MODIFY_S implements Structure.ByValue {
		
	};
}
