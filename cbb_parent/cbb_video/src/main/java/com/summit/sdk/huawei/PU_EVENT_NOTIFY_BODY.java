package com.summit.sdk.huawei;
import com.sun.jna.Pointer;
import com.sun.jna.Union;
/**
 * <i>native declaration : E:\video\HWPuSDK.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class PU_EVENT_NOTIFY_BODY extends Union {
	/**
	 * \u0368\ufffd\ufffd\ufffd\u00bc\ufffd<br>
	 * C type : PU_EVENT_COMMON_S
	 */
	public PU_EVENT_COMMON stPuEventCommon;
	/**
	 * IPC\ufffd\ufffd\ufffd\u05f2\u0371\ufffd\ufffd<br>
	 * C type : PU_EVENT_STEAM_PACKAGE_CHANGE_S
	 */
	public PU_EVENT_STEAM_PACKAGE_CHANGE stPuStreamPackgeChange;
	/**
	 * \u01f0\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u03f4\ufffd\u037c\u01acURL<br>
	 * C type : PU_EVENT_UPLOAD_IMAGE_URL_S
	 */
	public PU_EVENT_UPLOAD_IMAGE_URL stPuUploadImageURL;
	/**
	 * \u01f0\ufffd\ufffd\u037c\u01ac\ufffd\u03f4\ufffd\ufffd\ufffd\ufffd\u0368\u05aa<br>
	 * C type : PU_EVENT_UPLOAD_IMAGE_COMP_NOTIFY_S
	 */
	public PU_EVENT_UPLOAD_IMAGE_COMP_NOTIFY stUploadCompNotify;
	/**
	 * \u01f0\ufffd\ufffdDEC\ufffd\ufffd\ufffd\ufffd\u05e2\ufffd\ufffd<br>
	 * C type : PU_DEC_CALLBACK_INFO_S
	 */
	public PU_DEC_CALLBACK_INFO stPuDecRegister;
	/**
	 * \u01f0\ufffd\ufffd\ufffd\u8c78\ufffd\ufffd\ufffd\ufffd\u05f4\u032c<br>
	 * C type : PU_CONNECT_STATUS_S
	 */
	public PU_CONNECT_STATUS stPuConnetStatus;
	/**
	 * \u01f0\ufffd\ufffdIPC\ufffd\ufffd\ufffd\ufffd\u05e2\ufffd\ufffd<br>
	 * C type : PU_EVENT_REGISTER_S
	 */
	public PU_EVENT_REGISTER stPuIpcRegister;
	/**
	 * \u0378\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u03f1\ufffd<br>
	 * C type : PU_EVENT_TRANSPARENT_S
	 */
	public PU_EVENT_TRANSPARENT stTransparentChannelNotify;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\u02ee\u04e1<br>
	 * C type : PU_WATER_MARK_S
	 */
	public PU_WATER_MARK stPuWaterMark;
	/**
	 * \ufffd\ufffd\ufffd\u04fb\ufffd\ufffd\ufffd\u03e2\ufffd\u03f1\ufffd<br>
	 * C type : PU_PTZ_REPORT_INFO_S
	 */
	public PU_PTZ_REPORT_INFO stVisualInfo;
	public PU_EVENT_NOTIFY_BODY() {
		super();
	}
	/**
	 * @param stPuEventCommon \u0368\ufffd\ufffd\ufffd\u00bc\ufffd<br>
	 * C type : PU_EVENT_COMMON_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_EVENT_COMMON stPuEventCommon) {
		super();
		this.stPuEventCommon = stPuEventCommon;
		setType(PU_EVENT_COMMON.class);
	}
	/**
	 * @param stPuStreamPackgeChange IPC\ufffd\ufffd\ufffd\u05f2\u0371\ufffd\ufffd<br>
	 * C type : PU_EVENT_STEAM_PACKAGE_CHANGE_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_EVENT_STEAM_PACKAGE_CHANGE stPuStreamPackgeChange) {
		super();
		this.stPuStreamPackgeChange = stPuStreamPackgeChange;
		setType(PU_EVENT_STEAM_PACKAGE_CHANGE.class);
	}
	/**
	 * @param stPuUploadImageURL \u01f0\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u03f4\ufffd\u037c\u01acURL<br>
	 * C type : PU_EVENT_UPLOAD_IMAGE_URL_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_EVENT_UPLOAD_IMAGE_URL stPuUploadImageURL) {
		super();
		this.stPuUploadImageURL = stPuUploadImageURL;
		setType(PU_EVENT_UPLOAD_IMAGE_URL.class);
	}
	/**
	 * @param stUploadCompNotify \u01f0\ufffd\ufffd\u037c\u01ac\ufffd\u03f4\ufffd\ufffd\ufffd\ufffd\u0368\u05aa<br>
	 * C type : PU_EVENT_UPLOAD_IMAGE_COMP_NOTIFY_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_EVENT_UPLOAD_IMAGE_COMP_NOTIFY stUploadCompNotify) {
		super();
		this.stUploadCompNotify = stUploadCompNotify;
		setType(PU_EVENT_UPLOAD_IMAGE_COMP_NOTIFY.class);
	}
	/**
	 * @param stPuDecRegister \u01f0\ufffd\ufffdDEC\ufffd\ufffd\ufffd\ufffd\u05e2\ufffd\ufffd<br>
	 * C type : PU_DEC_CALLBACK_INFO_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_DEC_CALLBACK_INFO stPuDecRegister) {
		super();
		this.stPuDecRegister = stPuDecRegister;
		setType(PU_DEC_CALLBACK_INFO.class);
	}
	/**
	 * @param stPuConnetStatus \u01f0\ufffd\ufffd\ufffd\u8c78\ufffd\ufffd\ufffd\ufffd\u05f4\u032c<br>
	 * C type : PU_CONNECT_STATUS_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_CONNECT_STATUS stPuConnetStatus) {
		super();
		this.stPuConnetStatus = stPuConnetStatus;
		setType(PU_CONNECT_STATUS.class);
	}
	/**
	 * @param stPuIpcRegister \u01f0\ufffd\ufffdIPC\ufffd\ufffd\ufffd\ufffd\u05e2\ufffd\ufffd<br>
	 * C type : PU_EVENT_REGISTER_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_EVENT_REGISTER stPuIpcRegister) {
		super();
		this.stPuIpcRegister = stPuIpcRegister;
		setType(PU_EVENT_REGISTER.class);
	}
	/**
	 * @param stTransparentChannelNotify \u0378\ufffd\ufffd\u0368\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u03f1\ufffd<br>
	 * C type : PU_EVENT_TRANSPARENT_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_EVENT_TRANSPARENT stTransparentChannelNotify) {
		super();
		this.stTransparentChannelNotify = stTransparentChannelNotify;
		setType(PU_EVENT_TRANSPARENT.class);
	}
	/**
	 * @param stPuWaterMark \ufffd\ufffd\ufffd\ufffd\u02ee\u04e1<br>
	 * C type : PU_WATER_MARK_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_WATER_MARK stPuWaterMark) {
		super();
		this.stPuWaterMark = stPuWaterMark;
		setType(PU_WATER_MARK.class);
	}
	/**
	 * @param stVisualInfo \ufffd\ufffd\ufffd\u04fb\ufffd\ufffd\ufffd\u03e2\ufffd\u03f1\ufffd<br>
	 * C type : PU_PTZ_REPORT_INFO_S
	 */
	public PU_EVENT_NOTIFY_BODY(PU_PTZ_REPORT_INFO stVisualInfo) {
		super();
		this.stVisualInfo = stVisualInfo;
		setType(PU_PTZ_REPORT_INFO.class);
	}
	public PU_EVENT_NOTIFY_BODY(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_EVENT_NOTIFY_BODY implements com.sun.jna.Structure.ByReference {
		
	};
	public static class ByValue extends PU_EVENT_NOTIFY_BODY implements com.sun.jna.Structure.ByValue {
		
	};
}
