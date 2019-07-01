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
public class PU_DEVICE_VERSION extends Structure {
	/**
	 * \ufffd\ufffd\ufffd\ufffd\u6c7e<br>
	 * C type : CHAR[32]
	 */
	public byte[] szVerSoftware = new byte[32];
	/**
	 * UBoot\ufffd\u6c7e<br>
	 * C type : CHAR[32]
	 */
	public byte[] szVerUboot = new byte[32];
	/**
	 * \ufffd\u06ba\u02f0\u6c7e<br>
	 * C type : CHAR[32]
	 */
	public byte[] szVerKernel = new byte[32];
	/**
	 * \u04f2\ufffd\ufffd\ufffd\u6c7e<br>
	 * C type : CHAR[32]
	 */
	public byte[] szVerHardware = new byte[32];
	/**
	 * \u01f024 \ufffd\u05bd\u06b1\ufffd\u02beMac\ufffd\ufffd\u05b7,<br>
	 * C type : CHAR[32]
	 */
	public byte[] szReserved = new byte[32];
	public PU_DEVICE_VERSION() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("szVerSoftware", "szVerUboot", "szVerKernel", "szVerHardware", "szReserved");
	}
	/**
	 * @param szVerSoftware \ufffd\ufffd\ufffd\ufffd\u6c7e<br>
	 * C type : CHAR[32]<br>
	 * @param szVerUboot UBoot\ufffd\u6c7e<br>
	 * C type : CHAR[32]<br>
	 * @param szVerKernel \ufffd\u06ba\u02f0\u6c7e<br>
	 * C type : CHAR[32]<br>
	 * @param szVerHardware \u04f2\ufffd\ufffd\ufffd\u6c7e<br>
	 * C type : CHAR[32]<br>
	 * @param szReserved \u01f024 \ufffd\u05bd\u06b1\ufffd\u02beMac\ufffd\ufffd\u05b7,<br>
	 * C type : CHAR[32]
	 */
	public PU_DEVICE_VERSION(byte szVerSoftware[], byte szVerUboot[], byte szVerKernel[], byte szVerHardware[], byte szReserved[]) {
		super();
		if ((szVerSoftware.length != this.szVerSoftware.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szVerSoftware = szVerSoftware;
		if ((szVerUboot.length != this.szVerUboot.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szVerUboot = szVerUboot;
		if ((szVerKernel.length != this.szVerKernel.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szVerKernel = szVerKernel;
		if ((szVerHardware.length != this.szVerHardware.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szVerHardware = szVerHardware;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_DEVICE_VERSION(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DEVICE_VERSION implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DEVICE_VERSION implements Structure.ByValue {
		
	};
}
