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
public class PU_DECODER_CHANNEL_INFO extends Structure {
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd */
	public NativeLong ulChannelID;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : char[32 + 1]
	 */
	public byte[] szChannelName = new byte[32 + 1];
	public PU_DECODER_CHANNEL_INFO() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChannelID", "szChannelName");
	}
	/**
	 * @param ulChannelID \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd<br>
	 * @param szChannelName \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : char[32 + 1]
	 */
	public PU_DECODER_CHANNEL_INFO(NativeLong ulChannelID, byte szChannelName[]) {
		super();
		this.ulChannelID = ulChannelID;
		if ((szChannelName.length != this.szChannelName.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.szChannelName = szChannelName;
	}
	public PU_DECODER_CHANNEL_INFO(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DECODER_CHANNEL_INFO implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DECODER_CHANNEL_INFO implements Structure.ByValue {
		
	};
}
