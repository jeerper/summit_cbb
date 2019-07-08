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
public class PU_DENOISE_PARA extends Structure {
	/**
	 * \u0123\u02bd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_DENOISE_MODE_E
	 */
	public int enDenoiseMode;
	/** \ufffd\ufffd\u03a7\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd1~5 \ufffd\ufffd\ufffd\ufffd1~3 */
	public NativeLong lNRLevel;
	/** \u02b1\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulTfcLevel;
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulSfcLevel;
	public PU_DENOISE_PARA() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("enDenoiseMode", "lNRLevel", "ulTfcLevel", "ulSfcLevel");
	}
	/**
	 * @param enDenoiseMode \u0123\u02bd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_DENOISE_MODE_E<br>
	 * @param lNRLevel \ufffd\ufffd\u03a7\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd1~5 \ufffd\ufffd\ufffd\ufffd1~3<br>
	 * @param ulTfcLevel \u02b1\ufffd\ufffd\ufffd\ufffd<br>
	 * @param ulSfcLevel \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd
	 */
	public PU_DENOISE_PARA(int enDenoiseMode, NativeLong lNRLevel, NativeLong ulTfcLevel, NativeLong ulSfcLevel) {
		super();
		this.enDenoiseMode = enDenoiseMode;
		this.lNRLevel = lNRLevel;
		this.ulTfcLevel = ulTfcLevel;
		this.ulSfcLevel = ulSfcLevel;
	}
	public PU_DENOISE_PARA(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DENOISE_PARA implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DENOISE_PARA implements Structure.ByValue {
		
	};
}
