package com.summit.domain.log;

public class LogBean {
	
	private String id;
	private String userName;
	private String callerIP;
	private String funName;
	private String sTime;
	private String eTime;
	private String actionTime;
	private String actionFlag;
	private String erroInfo;
	
	
	public LogBean(){}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCallerIP() {
		return callerIP;
	}
	public void setCallerIP(String callerIP) {
		this.callerIP = callerIP;
	}
	public String getFunName() {
		return funName;
	}
	public void setFunName(String funName) {
		this.funName = funName;
	}
	public String getsTime() {
		return sTime;
	}
	public void setsTime(String sTime) {
		this.sTime = sTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getActionTime() {
		return actionTime;
	}
	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}
	public String getActionFlag() {
		return actionFlag;
	}
	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}
	public String getErroInfo() {
		return erroInfo;
	}
	public void setErroInfo(String erroInfo) {
		this.erroInfo = erroInfo;
	}
	
	
	
	public LogBean(String id, String userName, String callerIP, String funName,
			String sTime, String eTime, String actionTime, String actionFlag,
			String erroInfo) {
		super();
		this.id = id;
		this.userName = userName;
		this.callerIP = callerIP;
		this.funName = funName;
		this.sTime = sTime;
		this.eTime = eTime;
		this.actionTime = actionTime;
		this.actionFlag = actionFlag;
		this.erroInfo = erroInfo;
	}
	
	
	
	
}
