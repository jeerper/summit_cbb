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
public class PU_DEC_CALLBACK_INFO extends Structure {
	/**
	 * \u0368\ufffd\ufffd\ufffd\u00bc\ufffd<br>
	 * C type : PU_EVENT_COMMON_S
	 */
	public PU_EVENT_COMMON stPuEventCommon;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffdID<br>
	 * C type : CHAR[20 + 4]
	 */
	public byte[] szDecoderID = new byte[20 + 4];
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffdIP<br>
	 * C type : CHAR[16]
	 */
	public byte[] szDecIP = new byte[16];
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\ufffdport */
	public NativeLong ulDecPort;
	/**
	 * \ufffd\u8c78\ufffd\u037a\ufffd<br>
	 * C type : CHAR[32]
	 */
	public byte[] szDeviceType = new byte[32];
	/** \u05a7\ufffd\u05b5\ufffd\ufffd\ufffd\u01b5PayloadType\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u05b5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd16 */
	public NativeLong ulVideoPayloadTypeNum;
	/** \u05a7\ufffd\u05b5\ufffd\ufffd\ufffd\u01b5PayloadType\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u05b5\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd16 */
	public NativeLong ulAudioPayloadTypeNum;
	/** \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0427\u0368\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\udb8b\udef3\ufffd\ufffd\ufffd */
	public NativeLong ulChannelNum;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : CHAR[32 + 4]
	 */
	public byte[] szDecoderName = new byte[32 + 4];
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u02bd<br>
	 * C type : CHAR[16 + 4]
	 */
	public byte[] szDecoderEncoding = new byte[16 + 4];
	/**
	 * \u05a7\ufffd\u05b5\ufffd\ufffd\ufffd\u01b5PayloadType\ufffd\u0336\ufffd\ufffd\ufffd\ufffd\u9863\u052a\ufffd\u0638\ufffd\ufffd\ufffd\u03aa16\ufffd\ufffd\u02b5\ufffd\ufffd\u052a\ufffd\u0638\ufffd\ufffd\ufffd\u03aaulVideoPayloadTypeNum<br>
	 * C type : UCHAR[16]
	 */
	public byte[] ucVideoPayloadTypeArray = new byte[16];
	/**
	 * \u05a7\ufffd\u05b5\ufffd\ufffd\ufffd\u01b5PayloadType\ufffd\u0336\ufffd\ufffd\ufffd\ufffd\u9863\u052a\ufffd\u0638\ufffd\ufffd\ufffd\u03aa16\ufffd\ufffd\u02b5\ufffd\ufffd\u052a\ufffd\u0638\ufffd\ufffd\ufffd\u03aaulAudioPayloadTypeNum<br>
	 * C type : UCHAR[16]
	 */
	public byte[] ucAudioPayloadTypeArray = new byte[16];
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd\u03e2\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u052a\ufffd\u0638\ufffd\ufffd\ufffd\u03aaulChannelNum<br>
	 * C type : PU_DECODER_CHANNEL_INFO_S[32]
	 */
	public PU_DECODER_CHANNEL_INFO[] stChannelInfoArray = new PU_DECODER_CHANNEL_INFO[32];
	public PU_DEC_CALLBACK_INFO() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("stPuEventCommon", "szDecoderID", "szDecIP", "ulDecPort", "szDeviceType", "ulVideoPayloadTypeNum", "ulAudioPayloadTypeNum", "ulChannelNum", "szDecoderName", "szDecoderEncoding", "ucVideoPayloadTypeArray", "ucAudioPayloadTypeArray", "stChannelInfoArray");
	}
	public PU_DEC_CALLBACK_INFO(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DEC_CALLBACK_INFO implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DEC_CALLBACK_INFO implements Structure.ByValue {
		
	};
}
