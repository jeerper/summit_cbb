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
public class PU_TD_DTLINE_ENABLE extends Structure {
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u02b9\ufffd\ufffd<br>
	 * C type : BOOL[(4)]
	 */
	public boolean[] abRoadLane = new boolean[4];
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\u04b1\u07fd\ufffd\ufffd\ufffd */
	public boolean bLaneRhtBorderLine;
	public PU_TD_DTLINE_ENABLE() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("abRoadLane", "bLaneRhtBorderLine");
	}
	/**
	 * @param abRoadLane \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u02b9\ufffd\ufffd<br>
	 * C type : BOOL[(4)]<br>
	 * @param bLaneRhtBorderLine \ufffd\ufffd\ufffd\ufffd\ufffd\u04b1\u07fd\ufffd\ufffd\ufffd
	 */
	public PU_TD_DTLINE_ENABLE(boolean abRoadLane[], boolean bLaneRhtBorderLine) {
		super();
		if ((abRoadLane.length != this.abRoadLane.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.abRoadLane = abRoadLane;
		this.bLaneRhtBorderLine = bLaneRhtBorderLine;
	}
	public PU_TD_DTLINE_ENABLE(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_TD_DTLINE_ENABLE implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_TD_DTLINE_ENABLE implements Structure.ByValue {
		
	};
}
