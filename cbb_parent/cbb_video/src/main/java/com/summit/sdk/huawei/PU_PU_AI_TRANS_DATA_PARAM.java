package com.summit.sdk.huawei;
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
public class PU_PU_AI_TRANS_DATA_PARAM extends Structure {
	/** C type : VOID* */
	public Pointer pucData;
	/** \ufffd\ufffd\u0775\u0133\ufffd\ufffd\ufffd */
	public int uiLen;
	public PU_PU_AI_TRANS_DATA_PARAM() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("pucData", "uiLen");
	}
	/**
	 * @param pucData C type : VOID*<br>
	 * @param uiLen \ufffd\ufffd\u0775\u0133\ufffd\ufffd\ufffd
	 */
	public PU_PU_AI_TRANS_DATA_PARAM(Pointer pucData, int uiLen) {
		super();
		this.pucData = pucData;
		this.uiLen = uiLen;
	}
	public PU_PU_AI_TRANS_DATA_PARAM(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_PU_AI_TRANS_DATA_PARAM implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_PU_AI_TRANS_DATA_PARAM implements Structure.ByValue {
		
	};
}
