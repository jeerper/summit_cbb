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
public class PU_HARD_DISK_INFO extends Structure {
	/** C type : CHAR[12] */
	public byte[] szHardDiskFac = new byte[12];
	/** C type : PU_DISK_TYPE_E */
	public int enHardDiskType;
	public NativeLong ulAllSpace;
	public NativeLong ulLeftSpace;
	public NativeLong ulUsedSpace;
	public NativeLong ulUsedPercentage;
	/**
	 * \u0524\ufffd\ufffd\ufffd\u05b6\ufffd<br>
	 * C type : CHAR[32]
	 */
	public byte[] szReserved = new byte[32];
	public PU_HARD_DISK_INFO() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("szHardDiskFac", "enHardDiskType", "ulAllSpace", "ulLeftSpace", "ulUsedSpace", "ulUsedPercentage", "szReserved");
	}
	/**
	 * @param szHardDiskFac C type : CHAR[12]<br>
	 * @param enHardDiskType C type : PU_DISK_TYPE_E<br>
	 * @param szReserved \u0524\ufffd\ufffd\ufffd\u05b6\ufffd<br>
	 * C type : CHAR[32]
	 */
	public PU_HARD_DISK_INFO(byte szHardDiskFac[], int enHardDiskType, NativeLong ulAllSpace, NativeLong ulLeftSpace, NativeLong ulUsedSpace, NativeLong ulUsedPercentage, byte szReserved[]) {
		super();
		if ((szHardDiskFac.length != this.szHardDiskFac.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szHardDiskFac = szHardDiskFac;
		this.enHardDiskType = enHardDiskType;
		this.ulAllSpace = ulAllSpace;
		this.ulLeftSpace = ulLeftSpace;
		this.ulUsedSpace = ulUsedSpace;
		this.ulUsedPercentage = ulUsedPercentage;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_HARD_DISK_INFO(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_HARD_DISK_INFO implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_HARD_DISK_INFO implements Structure.ByValue {
		
	};
}
