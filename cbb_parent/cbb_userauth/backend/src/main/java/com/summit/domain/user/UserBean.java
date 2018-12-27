package com.summit.domain.user;

import java.io.Serializable;

public class UserBean implements  Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String userName;
	private String password;
	private String email;
	private String phoneNumber;
	private Integer isEnabled;
	private Integer state;
	private String lastUpdateTime;
	private String note;

	public UserBean() {
		super();
	}

	public UserBean(String name, String userName, String password,
			String email, String phoneNumber, Integer isEnabled, Integer state,
			String lastUpdateTime, String note) {
		super();
		this.name = name;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.isEnabled = isEnabled;
		this.state = state;
		this.lastUpdateTime = lastUpdateTime;
		this.note = note;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}




	public boolean isEnabled() {
		if (isEnabled == 1) {
			return true;
		}
		return false;
	}

}
