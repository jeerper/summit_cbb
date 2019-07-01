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
public class PU_ITS_ILLEGAI_DIC_PARA_UNIT extends Structure {
	/**
	 * \u03a5\ufffd\ufffd\ufffd\ufffd\ufffd\u0363\ufffd\ufffd\u07b8\ufffd\ufffd\ufffd\u0427,\ufffd\ufffd\udb8e\udd36\ufffdPU_ITS_ILLEGAL_TYPE_LEN_MAX - 4<br>
	 * C type : CHAR[(64 + 4)]
	 */
	public byte[] szIllTypeString = new byte[64 + 4];
	/**
	 * \u03a5\ufffd\ufffd\ufffd\ufffd\ufffd\ub8ec\ufffd\ufffd\udb8e\udd36\ufffdPU_ITS_ILLEGAL_NUM_LEN_MAX - 1<br>
	 * C type : CHAR[(8 + 1)]
	 */
	public byte[] szIllCodeString = new byte[8 + 1];
	/**
	 * \u03a5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u01a3\ufffd\ufffd\ufffd\udb8e\udd36\ufffdPU_ITS_ILLEGAL_NAME_LEN_MAX - 4<br>
	 * C type : CHAR[(64 + 4)]
	 */
	public byte[] szIllNameString = new byte[64 + 4];
	public PU_ITS_ILLEGAI_DIC_PARA_UNIT() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("szIllTypeString", "szIllCodeString", "szIllNameString");
	}
	/**
	 * @param szIllTypeString \u03a5\ufffd\ufffd\ufffd\ufffd\ufffd\u0363\ufffd\ufffd\u07b8\ufffd\ufffd\ufffd\u0427,\ufffd\ufffd\udb8e\udd36\ufffdPU_ITS_ILLEGAL_TYPE_LEN_MAX - 4<br>
	 * C type : CHAR[(64 + 4)]<br>
	 * @param szIllCodeString \u03a5\ufffd\ufffd\ufffd\ufffd\ufffd\ub8ec\ufffd\ufffd\udb8e\udd36\ufffdPU_ITS_ILLEGAL_NUM_LEN_MAX - 1<br>
	 * C type : CHAR[(8 + 1)]<br>
	 * @param szIllNameString \u03a5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u01a3\ufffd\ufffd\ufffd\udb8e\udd36\ufffdPU_ITS_ILLEGAL_NAME_LEN_MAX - 4<br>
	 * C type : CHAR[(64 + 4)]
	 */
	public PU_ITS_ILLEGAI_DIC_PARA_UNIT(byte szIllTypeString[], byte szIllCodeString[], byte szIllNameString[]) {
		super();
		if ((szIllTypeString.length != this.szIllTypeString.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szIllTypeString = szIllTypeString;
		if ((szIllCodeString.length != this.szIllCodeString.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szIllCodeString = szIllCodeString;
		if ((szIllNameString.length != this.szIllNameString.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szIllNameString = szIllNameString;
	}
	public PU_ITS_ILLEGAI_DIC_PARA_UNIT(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_ITS_ILLEGAI_DIC_PARA_UNIT implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_ITS_ILLEGAI_DIC_PARA_UNIT implements Structure.ByValue {
		
	};
}
