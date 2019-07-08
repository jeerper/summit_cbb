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
public class PU_MANAUL_DEFOG_ENABLE extends Structure {
	public boolean bEnable;
	/** C type : CHAR[32] */
	public byte[] szReserved = new byte[32];
	public PU_MANAUL_DEFOG_ENABLE() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("bEnable", "szReserved");
	}
	/** @param szReserved C type : CHAR[32] */
	public PU_MANAUL_DEFOG_ENABLE(boolean bEnable, byte szReserved[]) {
		super();
		this.bEnable = bEnable;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_MANAUL_DEFOG_ENABLE(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_MANAUL_DEFOG_ENABLE implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_MANAUL_DEFOG_ENABLE implements Structure.ByValue {
		
	};
}
