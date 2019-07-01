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
public class PU_PIC_NAMING_ELEM extends Structure {
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_PIC_NAMING_TYPE_E
	 */
	public int enPicNamingType;
	/**
	 * \ufffd\u0536\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(24)]
	 */
	public byte[] acCustomContent = new byte[24];
	/**
	 * \u0524\ufffd\ufffd\ufffd\u05b6\ufffd<br>
	 * C type : CHAR[32]
	 */
	public byte[] szReserved = new byte[32];
	public PU_PIC_NAMING_ELEM() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("enPicNamingType", "acCustomContent", "szReserved");
	}
	/**
	 * @param enPicNamingType \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_PIC_NAMING_TYPE_E<br>
	 * @param acCustomContent \ufffd\u0536\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(24)]<br>
	 * @param szReserved \u0524\ufffd\ufffd\ufffd\u05b6\ufffd<br>
	 * C type : CHAR[32]
	 */
	public PU_PIC_NAMING_ELEM(int enPicNamingType, byte acCustomContent[], byte szReserved[]) {
		super();
		this.enPicNamingType = enPicNamingType;
		if ((acCustomContent.length != this.acCustomContent.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.acCustomContent = acCustomContent;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_PIC_NAMING_ELEM(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_PIC_NAMING_ELEM implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_PIC_NAMING_ELEM implements Structure.ByValue {
		
	};
}
