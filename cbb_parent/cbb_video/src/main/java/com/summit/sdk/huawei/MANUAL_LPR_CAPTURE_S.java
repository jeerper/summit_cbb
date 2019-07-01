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
public class MANUAL_LPR_CAPTURE_S extends Structure {
	public NativeLong ulSnapNum;
	/** C type : ULONG[5 - 1] */
	public NativeLong[] aulSnapInterval = new NativeLong[5 - 1];
	/** C type : CHAR[32] */
	public byte[] szReserved = new byte[32];
	public MANUAL_LPR_CAPTURE_S() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulSnapNum", "aulSnapInterval", "szReserved");
	}
	/**
	 * @param aulSnapInterval C type : ULONG[5 - 1]<br>
	 * @param szReserved C type : CHAR[32]
	 */
	public MANUAL_LPR_CAPTURE_S(NativeLong ulSnapNum, NativeLong aulSnapInterval[], byte szReserved[]) {
		super();
		this.ulSnapNum = ulSnapNum;
		if ((aulSnapInterval.length != this.aulSnapInterval.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.aulSnapInterval = aulSnapInterval;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public MANUAL_LPR_CAPTURE_S(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends MANUAL_LPR_CAPTURE_S implements Structure.ByReference {
		
	};
	public static class ByValue extends MANUAL_LPR_CAPTURE_S implements Structure.ByValue {
		
	};
}
