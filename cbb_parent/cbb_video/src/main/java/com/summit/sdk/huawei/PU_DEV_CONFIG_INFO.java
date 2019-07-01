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
public class PU_DEV_CONFIG_INFO extends Structure {
	/**
	 * IP \ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_IPFILTER_RULES_INFOV2_S
	 */
	public PU_IPFILTER_RULES_INFOV2 stIPFilterPara;
	/**
	 * \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u073c\ufffd\ufffd\ufffd<br>
	 * C type : PU_TCP_ACCELERATE_PARA_S
	 */
	public PU_TCP_ACCELERATE_PARA stTcpAcceleratePara;
	/**
	 * \ufffd\u8c78\ufffd\u02ff\ufffd<br>
	 * C type : PU_DEVICE_PORT_CONFIG_S
	 */
	public PU_DEVICE_PORT_CONFIG stDevPortConfig;
	/**
	 * DDNS<br>
	 * C type : PU_DDNS_CONF_INFO_S
	 */
	public PU_DDNS_CONF_INFO stDDNSpara;
	/**
	 * 802.1x<br>
	 * C type : PU_DOT1X_CONFIG_INFO_S
	 */
	public PU_DOT1X_CONFIG_INFO stDot1xInfo;
	/**
	 * SSH<br>
	 * C type : PU_SSH_ENABLE_PARA_S
	 */
	public PU_SSH_ENABLE_PARA stSshPara;
	/**
	 * SFTP<br>
	 * C type : PU_SFTP_ENABLE_PARA_S
	 */
	public PU_SFTP_ENABLE_PARA stSftpPara;
	/**
	 * SNMPv1v2c\ufffd\ufffd\ufffd\ufffd\ufffd\u0431\ufffd<br>
	 * C type : PU_SNMP_COMMUNITY_LIST_S_EX
	 */
	public PU_SNMP_COMMUNITY_LIST_EX stCommunityList;
	/**
	 * SNMP v3\ufffd\u00fb\ufffd\ufffd\u0431\ufffd<br>
	 * C type : PU_SNMPV3_USER_LIST_S
	 */
	public PU_SNMPV3_USER_LIST stUserList;
	/**
	 * SNMPv3\ufffd\u00fb\ufffd\ufffd\ufffd\ufffd\ufffd<br>
	 * C type : PU_SNMPV3_USER_MGMT_S
	 */
	public PU_SNMPV3_USER_MGMT stUserMgmtInfo;
	/**
	 * SNMP Trap\ufffd\ufffd\ufffd\u0577\ufffd\ufffd\u0431\ufffd<br>
	 * C type : PU_SNMP_TRAP_LIST_S
	 */
	public PU_SNMP_TRAP_LIST stTrapList;
	/** C type : CHAR[32] */
	public byte[] szReserved = new byte[32];
	public PU_DEV_CONFIG_INFO() {
		super();
	}
	protected List<String > getFieldOrder() {
		return Arrays.asList("stIPFilterPara", "stTcpAcceleratePara", "stDevPortConfig", "stDDNSpara", "stDot1xInfo", "stSshPara", "stSftpPara", "stCommunityList", "stUserList", "stUserMgmtInfo", "stTrapList", "szReserved");
	}
	public PU_DEV_CONFIG_INFO(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends PU_DEV_CONFIG_INFO implements Structure.ByReference {
		
	};
	public static class ByValue extends PU_DEV_CONFIG_INFO implements Structure.ByValue {
		
	};
}
