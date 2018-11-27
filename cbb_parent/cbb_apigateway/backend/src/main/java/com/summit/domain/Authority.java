package com.summit.domain;

import java.io.Serializable;

import org.springframework.security.core.GrantedAuthority;

/**
 * 用戶授權角色類
 * @author Administrator
 *
 */

public class Authority  implements GrantedAuthority , Serializable{


	private String authName;
	
	public Authority() {
		super();
	}
	@Override
	public String toString() {
		return getAuthority();
	}
	public Authority(String authName) {
		super();
	
		this.authName = authName;
	}

	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	@Override
	public String getAuthority() {
		//權限名稱
		return authName;
	}
	
}
