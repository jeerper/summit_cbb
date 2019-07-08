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
public class PU_CAM_GEOG_POSITION extends Structure {
	/** \u0368\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulChnID;
	/**
	 * \ufffd\ufffd\u03bb\ufffd\ufffd<br>
	 * C type : CHAR[(15)]
	 */
	public byte[] szAzimuth = new byte[15];
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(15)]
	 */
	public byte[] szPitch = new byte[15];
	/**
	 * \ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(15)]
	 */
	public byte[] szLongitude = new byte[15];
	/**
	 * \u03b3\ufffd\ufffd<br>
	 * C type : CHAR[(15)]
	 */
	public byte[] szLatitude = new byte[15];
	/**
	 * szReserved[0]\ufffd\ufffd\ufffd\ufffd\u0221\ufffd\ufffd\u03b3\ufffd\u0237\ufffd\u02bd; szReserved[1] bit0\ufffd\ufffd\u04e6nHeight, bit1\ufffd\ufffd\u04e6fAzimuth, bit2\ufffd\ufffd\u04e6fPitch, bit3\ufffd\ufffd\u04e6dLongitude\ufffd\ufffdbit4\ufffd\ufffd\u04e6dLatitude<br>
	 * C type : CHAR[32]
	 */
	public byte[] szReserved = new byte[32];
	public PU_CAM_GEOG_POSITION() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChnID", "szAzimuth", "szPitch", "szLongitude", "szLatitude", "szReserved");
	}
	/**
	 * @param ulChnID \u0368\ufffd\ufffd\ufffd\ufffd<br>
	 * @param szAzimuth \ufffd\ufffd\u03bb\ufffd\ufffd<br>
	 * C type : CHAR[(15)]<br>
	 * @param szPitch \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(15)]<br>
	 * @param szLongitude \ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(15)]<br>
	 * @param szLatitude \u03b3\ufffd\ufffd<br>
	 * C type : CHAR[(15)]<br>
	 * @param szReserved szReserved[0]\ufffd\ufffd\ufffd\ufffd\u0221\ufffd\ufffd\u03b3\ufffd\u0237\ufffd\u02bd; szReserved[1] bit0\ufffd\ufffd\u04e6nHeight, bit1\ufffd\ufffd\u04e6fAzimuth, bit2\ufffd\ufffd\u04e6fPitch, bit3\ufffd\ufffd\u04e6dLongitude\ufffd\ufffdbit4\ufffd\ufffd\u04e6dLatitude<br>
	 * C type : CHAR[32]
	 */
	public PU_CAM_GEOG_POSITION(NativeLong ulChnID, byte szAzimuth[], byte szPitch[], byte szLongitude[], byte szLatitude[], byte szReserved[]) {
		super();
		this.ulChnID = ulChnID;
		if ((szAzimuth.length != this.szAzimuth.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szAzimuth = szAzimuth;
		if ((szPitch.length != this.szPitch.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szPitch = szPitch;
		if ((szLongitude.length != this.szLongitude.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szLongitude = szLongitude;
		if ((szLatitude.length != this.szLatitude.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szLatitude = szLatitude;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_CAM_GEOG_POSITION(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_CAM_GEOG_POSITION implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_CAM_GEOG_POSITION implements Structure.ByValue {
		
	};
}
