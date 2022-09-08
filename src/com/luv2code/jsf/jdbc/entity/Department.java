package com.luv2code.jsf.jdbc.entity;

import javax.annotation.ManagedBean;

@ManagedBean
public class Department {

	private int id;
	private String title;

	
	public Department() {
	}
	
	public Department(int id, String title) {
		this.id = id;
		this.title = title;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", title=" + title + "]";
	}



}
