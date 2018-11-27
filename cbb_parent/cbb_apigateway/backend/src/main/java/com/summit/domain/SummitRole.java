package com.summit.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author yt
 *
 */
public class SummitRole implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private SummitRoleName summitRoleName;
	private String description;
	private Boolean available = Boolean.FALSE; 
	private List<User> summitUsers;

	public SummitRole() {
	}

	public SummitRole(SummitRoleName summitRoleName) {
		this.summitRoleName = summitRoleName;
		this.available = Boolean.TRUE;
	}

	public SummitRole(SummitRoleName summitRoleName, String description) {
		this.summitRoleName = summitRoleName;
		this.description = description;
		this.available = Boolean.TRUE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SummitRoleName getSummitRoleName() {
		return summitRoleName;
	}

	public void setSummitRoleName(SummitRoleName summitRoleName) {
		this.summitRoleName = summitRoleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public List<User> getSummitUsers() {
		return summitUsers;
	}

	public void setSummitUsers(List<User> summitUsers) {
		this.summitUsers = summitUsers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SummitRole [id=" + id + ", roleName=" + summitRoleName + ", description=" + description + ", available="
				+ available + ", summitUsers=" + summitUsers + "]";
	}

}