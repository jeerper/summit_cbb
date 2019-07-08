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
public class PU_TD_ILLEGAL_PARKINK_SNAP_RULE extends Structure {
	/** \u05e5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulFrameNum;
	/**
	 * \u05e5\ufffd\ufffd\u037c\u01ac\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_IBALL_FRAME_TYPE_E[(4)]
	 */
	public int[] enFrameType = new int[4];
	/**
	 * \u05e5\ufffd\u013c\ufffd\ufffd<br>
	 * C type : ULONG[(4)]
	 */
	public NativeLong[] ulInterval = new NativeLong[4];
	public PU_TD_ILLEGAL_PARKINK_SNAP_RULE() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulFrameNum", "enFrameType", "ulInterval");
	}
	/**
	 * @param ulFrameNum \u05e5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * @param enFrameType \u05e5\ufffd\ufffd\u037c\u01ac\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_IBALL_FRAME_TYPE_E[(4)]<br>
	 * @param ulInterval \u05e5\ufffd\u013c\ufffd\ufffd<br>
	 * C type : ULONG[(4)]
	 */
	public PU_TD_ILLEGAL_PARKINK_SNAP_RULE(NativeLong ulFrameNum, int enFrameType[], NativeLong ulInterval[]) {
		super();
		this.ulFrameNum = ulFrameNum;
		if ((enFrameType.length != this.enFrameType.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.enFrameType = enFrameType;
		if ((ulInterval.length != this.ulInterval.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.ulInterval = ulInterval;
	}
	public PU_TD_ILLEGAL_PARKINK_SNAP_RULE(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_TD_ILLEGAL_PARKINK_SNAP_RULE implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_TD_ILLEGAL_PARKINK_SNAP_RULE implements Structure.ByValue {
		
	};
}
