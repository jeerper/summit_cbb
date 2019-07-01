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
public class PU_IRIS_CONTROL extends Structure {
	/**
	 * \ufffd\ufffd\ufffd\u01bb\ufffd\u043e\ufffd\ufffd\u0226(DC)<br>
	 * C type : PU_COMMONMODE_SWITCH_E
	 */
	public int enIris;
	/**
	 * \ufffd\ufffd\u0226\u05b5(P-Iris)<br>
	 * C type : PU_IRIS_VALUE_E
	 */
	public int enIRISValue;
	/** C type : CHAR[32] */
	public byte[] szReserve = new byte[32];
	public PU_IRIS_CONTROL() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("enIris", "enIRISValue", "szReserve");
	}
	/**
	 * @param enIris \ufffd\ufffd\ufffd\u01bb\ufffd\u043e\ufffd\ufffd\u0226(DC)<br>
	 * C type : PU_COMMONMODE_SWITCH_E<br>
	 * @param enIRISValue \ufffd\ufffd\u0226\u05b5(P-Iris)<br>
	 * C type : PU_IRIS_VALUE_E<br>
	 * @param szReserve C type : CHAR[32]
	 */
	public PU_IRIS_CONTROL(int enIris, int enIRISValue, byte szReserve[]) {
		super();
		this.enIris = enIris;
		this.enIRISValue = enIRISValue;
		if ((szReserve.length != this.szReserve.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserve = szReserve;
	}
	public PU_IRIS_CONTROL(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_IRIS_CONTROL implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_IRIS_CONTROL implements Structure.ByValue {
		
	};
}
