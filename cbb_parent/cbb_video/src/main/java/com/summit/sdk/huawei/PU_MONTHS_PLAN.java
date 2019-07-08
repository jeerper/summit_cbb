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
public class PU_MONTHS_PLAN extends Structure {
	/** \ufffd\ufffd\u04e6\ufffd\u00b7\u0763\ufffd\ufffd\ufffd\ufffd\u03aa0\ufffd\ufffd\u02be12\ufffd\ufffd\ufffd\u00b7\ufffd\u022b\ufffd\ufffd\u04aa\ufffd\ufffd\ufffd\u00e3\ufffd1~12\ufffd\ufffd\ufffd\u04e6\ufffd\ufffd\ufffd\u00b7\ufffd\ufffd\ufffd\u04aa\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulMonth;
	/** \u02b9\ufffd\ufffd\ufffd\ufffd\ufffd */
	public boolean bEnable;
	/** C type : PU_TIME_QUANTUM_LIST_S[12] */
	public PU_TIME_QUANTUM_LIST[] stTimeQaumtumMonths = new PU_TIME_QUANTUM_LIST[12];
	/**
	 * szReserve[0]\ufffd\ufffd\u04aa\u02b9\ufffd\u00e3\ufffdSDK\ufffd\u06b2\ufffd\u02b9\ufffd\ufffd<br>
	 * C type : CHAR[32]
	 */
	public byte[] szReserved = new byte[32];
	public PU_MONTHS_PLAN() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulMonth", "bEnable", "stTimeQaumtumMonths", "szReserved");
	}
	/**
	 * @param ulMonth \ufffd\ufffd\u04e6\ufffd\u00b7\u0763\ufffd\ufffd\ufffd\ufffd\u03aa0\ufffd\ufffd\u02be12\ufffd\ufffd\ufffd\u00b7\ufffd\u022b\ufffd\ufffd\u04aa\ufffd\ufffd\ufffd\u00e3\ufffd1~12\ufffd\ufffd\ufffd\u04e6\ufffd\ufffd\ufffd\u00b7\ufffd\ufffd\ufffd\u04aa\ufffd\ufffd\ufffd\ufffd<br>
	 * @param bEnable \u02b9\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * @param stTimeQaumtumMonths C type : PU_TIME_QUANTUM_LIST_S[12]<br>
	 * @param szReserved szReserve[0]\ufffd\ufffd\u04aa\u02b9\ufffd\u00e3\ufffdSDK\ufffd\u06b2\ufffd\u02b9\ufffd\ufffd<br>
	 * C type : CHAR[32]
	 */
	public PU_MONTHS_PLAN(NativeLong ulMonth, boolean bEnable, PU_TIME_QUANTUM_LIST stTimeQaumtumMonths[], byte szReserved[]) {
		super();
		this.ulMonth = ulMonth;
		this.bEnable = bEnable;
		if ((stTimeQaumtumMonths.length != this.stTimeQaumtumMonths.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.stTimeQaumtumMonths = stTimeQaumtumMonths;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_MONTHS_PLAN(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_MONTHS_PLAN implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_MONTHS_PLAN implements Structure.ByValue {
		
	};
}
