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
public class PU_SLAVE_ONVIF_PARAM extends Structure {
	/** \ufffd\ufffd\ufffd\u8c78ID */
	public NativeLong ulSlaveID;
	/** \u02b9\ufffd\ufffd */
	public boolean bEnable;
	/** \ufffd\ufffd\ufffd\u8c78\u0368\ufffd\ufffdid */
	public NativeLong ulChannelId;
	/** \ufffd\ufffd\ufffd\u8c78Onvif\ufffd\u02ff\ufffd */
	public NativeLong ulOnvifPort;
	/**
	 * \ufffd\ufffd\ufffd\u8c78Onvif\ufffd\u00fb\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(32 + 4)]
	 */
	public byte[] szUserName = new byte[32 + 4];
	/**
	 * \ufffd\ufffd\ufffd\u8c78Onvif\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(64 + 4)]
	 */
	public byte[] szPassWord = new byte[64 + 4];
	/** C type : CHAR[32] */
	public byte[] szReserved = new byte[32];
	public PU_SLAVE_ONVIF_PARAM() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulSlaveID", "bEnable", "ulChannelId", "ulOnvifPort", "szUserName", "szPassWord", "szReserved");
	}
	/**
	 * @param ulSlaveID \ufffd\ufffd\ufffd\u8c78ID<br>
	 * @param bEnable \u02b9\ufffd\ufffd<br>
	 * @param ulChannelId \ufffd\ufffd\ufffd\u8c78\u0368\ufffd\ufffdid<br>
	 * @param ulOnvifPort \ufffd\ufffd\ufffd\u8c78Onvif\ufffd\u02ff\ufffd<br>
	 * @param szUserName \ufffd\ufffd\ufffd\u8c78Onvif\ufffd\u00fb\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(32 + 4)]<br>
	 * @param szPassWord \ufffd\ufffd\ufffd\u8c78Onvif\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[(64 + 4)]<br>
	 * @param szReserved C type : CHAR[32]
	 */
	public PU_SLAVE_ONVIF_PARAM(NativeLong ulSlaveID, boolean bEnable, NativeLong ulChannelId, NativeLong ulOnvifPort, byte szUserName[], byte szPassWord[], byte szReserved[]) {
		super();
		this.ulSlaveID = ulSlaveID;
		this.bEnable = bEnable;
		this.ulChannelId = ulChannelId;
		this.ulOnvifPort = ulOnvifPort;
		if ((szUserName.length != this.szUserName.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szUserName = szUserName;
		if ((szPassWord.length != this.szPassWord.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szPassWord = szPassWord;
		if ((szReserved.length != this.szReserved.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szReserved = szReserved;
	}
	public PU_SLAVE_ONVIF_PARAM(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_SLAVE_ONVIF_PARAM implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_SLAVE_ONVIF_PARAM implements Structure.ByValue {
		
	};
}
