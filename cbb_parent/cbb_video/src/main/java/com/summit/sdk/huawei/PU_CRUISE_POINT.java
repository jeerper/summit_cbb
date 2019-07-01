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
public class PU_CRUISE_POINT extends Structure {
	/** \u0524\ufffd\ufffd\u03bb\ufffd\ufffd\ufffd\u043a\ufffd */
	public NativeLong ulPresetIndex;
	/** \u0524\ufffd\ufffd\u03bb\u0363\ufffd\ufffd\u02b1\ufffd\ufffd(3-3600s) */
	public NativeLong ulDwellTime;
	/** \u05ea\ufffd\ufffd\ufffd\ufffd\u04bb\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0676\ufffd(1-10) */
	public NativeLong ulSpeed;
	public PU_CRUISE_POINT() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulPresetIndex", "ulDwellTime", "ulSpeed");
	}
	/**
	 * @param ulPresetIndex \u0524\ufffd\ufffd\u03bb\ufffd\ufffd\ufffd\u043a\ufffd<br>
	 * @param ulDwellTime \u0524\ufffd\ufffd\u03bb\u0363\ufffd\ufffd\u02b1\ufffd\ufffd(3-3600s)<br>
	 * @param ulSpeed \u05ea\ufffd\ufffd\ufffd\ufffd\u04bb\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0676\ufffd(1-10)
	 */
	public PU_CRUISE_POINT(NativeLong ulPresetIndex, NativeLong ulDwellTime, NativeLong ulSpeed) {
		super();
		this.ulPresetIndex = ulPresetIndex;
		this.ulDwellTime = ulDwellTime;
		this.ulSpeed = ulSpeed;
	}
	public PU_CRUISE_POINT(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_CRUISE_POINT implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_CRUISE_POINT implements Structure.ByValue {
		
	};
}
