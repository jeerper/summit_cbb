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
public class PU_TD_TOUR_PLAN_PARAMS extends Structure {
	/** \u0368\ufffd\ufffdID */
	public NativeLong ulChannelId;
	/** \u0472\ufffd\ufffd\ufffd\u01bb\ufffd\u02b9\ufffd\ufffd */
	public boolean bTourPlanEnable;
	/**
	 * \u046d\ufffd\ufffd\u0123\u02bd<br>
	 * C type : PU_CYCLIC_MODE_E
	 */
	public int enCyclicMode;
	/**
	 * \u0472\ufffd\ufffd\u0123\u02bd<br>
	 * C type : PU_TOUR_MODE_E
	 */
	public int enTourMode;
	/**
	 * \u0472\ufffd\ufffd\ufffd\u01bb\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_TD_TOUR_PLAN_ONE_DAY_PARAMS_S[(7)]
	 */
	public PU_TD_TOUR_PLAN_ONE_DAY_PARAMS[] stTourPlanDay = new PU_TD_TOUR_PLAN_ONE_DAY_PARAMS[7];
	public PU_TD_TOUR_PLAN_PARAMS() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("ulChannelId", "bTourPlanEnable", "enCyclicMode", "enTourMode", "stTourPlanDay");
	}
	/**
	 * @param ulChannelId \u0368\ufffd\ufffdID<br>
	 * @param bTourPlanEnable \u0472\ufffd\ufffd\ufffd\u01bb\ufffd\u02b9\ufffd\ufffd<br>
	 * @param enCyclicMode \u046d\ufffd\ufffd\u0123\u02bd<br>
	 * C type : PU_CYCLIC_MODE_E<br>
	 * @param enTourMode \u0472\ufffd\ufffd\u0123\u02bd<br>
	 * C type : PU_TOUR_MODE_E<br>
	 * @param stTourPlanDay \u0472\ufffd\ufffd\ufffd\u01bb\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_TD_TOUR_PLAN_ONE_DAY_PARAMS_S[(7)]
	 */
	public PU_TD_TOUR_PLAN_PARAMS(NativeLong ulChannelId, boolean bTourPlanEnable, int enCyclicMode, int enTourMode, PU_TD_TOUR_PLAN_ONE_DAY_PARAMS stTourPlanDay[]) {
		super();
		this.ulChannelId = ulChannelId;
		this.bTourPlanEnable = bTourPlanEnable;
		this.enCyclicMode = enCyclicMode;
		this.enTourMode = enTourMode;
		if ((stTourPlanDay.length != this.stTourPlanDay.length)) 
			throw new IllegalArgumentException("Wrong array size !");
		this.stTourPlanDay = stTourPlanDay;
	}
	public PU_TD_TOUR_PLAN_PARAMS(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_TD_TOUR_PLAN_PARAMS implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_TD_TOUR_PLAN_PARAMS implements Structure.ByValue {
		
	};
}
