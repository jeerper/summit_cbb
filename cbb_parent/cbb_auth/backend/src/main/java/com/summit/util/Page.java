package com.summit.util;

import java.util.List;

public class Page<T> {

	private List<T> content;
	private Integer totalElements;

	public Page() {
		super();
	}

	public Page(List<T> content, Integer totalElements) {
		super();
		this.content = content;
		this.totalElements = totalElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public Integer getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}

}
