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
public class PU_MASTER_SLAVE_TRACK_PARA extends Structure {
	/** \ufffd\ufffd\ufffd\u04f8\ufffd\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd */
	public boolean bEnable;
	/** \ufffd\ufffd\ufffd\u04f9\ufffd\ufffd\ufffdID, \ufffd\ufffd\u0579\u0524\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u01f0\ufffd\ufffd\u02b9\ufffd\ufffd */
	public NativeLong ulGroupID;
	/**
	 * \ufffd\ufffd\ufffd\u0672\ufffd\ufffd\ufffd<br>
	 * C type : PU_MASTER_SLAVE_TRACK_MODE_E
	 */
	public int enMode;
	/**
	 * \u013f\ufffd\ufffd\ufffd\u0421\u057c\ufffd\u04fb\ufffd\ufffd\ufffd\u0131\ufffd\ufffd\ufffd<br>
	 * C type : PU_TRACK_OBJECT_SIZE_E
	 */
	public int enObjectSize;
	/** \ufffd\ufffd\ufffd\ufffd\u02b1\ufffd\ufffd,\ufffd\ufffd\u03bbs */
	public int iTrackTime;
	/** C type : CHAR[32] */
	public byte[] szReserved = new byte[32];
	public PU_MASTER_SLAVE_TRACK_PARA() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("bEnable", "ulGroupID", "enMode", "enObjectSize", "iTrackTime", "szReserved");
	}
	/**
	 * @param bEnable \ufffd\ufffd\ufffd\u04f8\ufffd\ufffd\ufffd\ufffd\u01f7\ufffd\u02b9\ufffd\ufffd<br>
	 * @param ulGroupID \ufffd\ufffd\ufffd\u04f9\ufffd\ufffd\ufffdID, \ufffd\ufffd\u0579\u0524\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u01f0\ufffd\ufffd\u02b9\ufffd\ufffd<br>
	 * @param enMode \ufffd\ufffd\ufffd\u0672\ufffd\ufffd\ufffd<br>
	 * C type : PU_MASTER_SLAVE_TRACK_MODE_E<br>
	 * @param enObjectSize \u013f\ufffd\ufffd\ufffd\u0421\u057c\ufffd\u04fb\ufffd\ufffd\ufffd\u0131\ufffd\ufffd\ufffd<br>
	 * C type : PU_TRACK_OBJECT_SIZE_E<br>
	 * @param iTrackTime \ufffd\ufffd\ufffd\ufffd\u02b1\ufffd\ufffd,\ufffd\ufffd\u03bbs<br>
	 * @param szReserved C type : CHAR[32]
	 */
	public PU_MASTER_SLAVE_TRACK_PARA(boolean bEnable, NativeLong ulGroupID, int enMode, int enObjectSize, int iTrackTime, byte szReserved[]) {
		super();
		this.bEnable = bEnable;
		this.ulGroupID = ulGroupID;
		this.enMode = enMode;
		this.enObjectSize = enObjectSize;
		this.iTrackTime = iTrackTime;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_MASTER_SLAVE_TRACK_PARA(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_MASTER_SLAVE_TRACK_PARA implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_MASTER_SLAVE_TRACK_PARA implements Structure.ByValue {
		
	};
}
