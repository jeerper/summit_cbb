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
public class PU_DEC_PORT_ABILITY extends Structure {
	/**
	 * 1\2\4\9\16 mode,0 the end<br>
	 * C type : CHAR[16]
	 */
	public byte[] szOutPutModes = new byte[16];
	/** 0:Audio, 1:Video 2:both */
	public NativeLong ulPortType;
	/** \ufffd\u02ff\ufffd\ufffd\u01f7\ufffd\u05a7\ufffd\u05b7\u0174\ufffd\ufffd\ufffd\u02be */
	public boolean bSupportEnlarge;
	public PU_DEC_PORT_ABILITY() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("szOutPutModes", "ulPortType", "bSupportEnlarge");
	}
	/**
	 * @param szOutPutModes 1\2\4\9\16 mode,0 the end<br>
	 * C type : CHAR[16]<br>
	 * @param ulPortType 0:Audio, 1:Video 2:both<br>
	 * @param bSupportEnlarge \ufffd\u02ff\ufffd\ufffd\u01f7\ufffd\u05a7\ufffd\u05b7\u0174\ufffd\ufffd\ufffd\u02be
	 */
	public PU_DEC_PORT_ABILITY(byte szOutPutModes[], NativeLong ulPortType, boolean bSupportEnlarge) {
		super();
		if ((szOutPutModes.length != this.szOutPutModes.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szOutPutModes = szOutPutModes;
		this.ulPortType = ulPortType;
		this.bSupportEnlarge = bSupportEnlarge;
	}
	public PU_DEC_PORT_ABILITY(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DEC_PORT_ABILITY implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DEC_PORT_ABILITY implements Structure.ByValue {
		
	};
}
