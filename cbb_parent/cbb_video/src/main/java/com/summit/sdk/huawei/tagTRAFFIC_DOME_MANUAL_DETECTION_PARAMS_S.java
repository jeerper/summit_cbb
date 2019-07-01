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
public class tagTRAFFIC_DOME_MANUAL_DETECTION_PARAMS_S extends Structure {
	/** \u0368\ufffd\ufffdID */
	public NativeLong ulChannelId;
	/** C type : PU_TD_TRAFFIC_DOME_ILLEGAL_PARKINK_PARAMS_S */
	public PU_TD_TRAFFIC_DOME_ILLEGAL_PARKINK_PARAMS stIllegalParking;
	public tagTRAFFIC_DOME_MANUAL_DETECTION_PARAMS_S() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChannelId", "stIllegalParking");
	}
	/**
	 * @param ulChannelId \u0368\ufffd\ufffdID<br>
	 * @param stIllegalParking C type : PU_TD_TRAFFIC_DOME_ILLEGAL_PARKINK_PARAMS_S
	 */
	public tagTRAFFIC_DOME_MANUAL_DETECTION_PARAMS_S(NativeLong ulChannelId, PU_TD_TRAFFIC_DOME_ILLEGAL_PARKINK_PARAMS stIllegalParking) {
		super();
		this.ulChannelId = ulChannelId;
		this.stIllegalParking = stIllegalParking;
	}
	public tagTRAFFIC_DOME_MANUAL_DETECTION_PARAMS_S(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends tagTRAFFIC_DOME_MANUAL_DETECTION_PARAMS_S implements Structure.ByReference {
		
	};
	public static class ByValue extends tagTRAFFIC_DOME_MANUAL_DETECTION_PARAMS_S implements Structure.ByValue {
		
	};
}
