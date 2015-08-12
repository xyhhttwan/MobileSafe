package com.xb.mobilesafe.entity;

import java.io.Serializable;

public class HomeFuntionItem implements Serializable {


	private static final long serialVersionUID = -2898762929863654916L;
	
	private String name;
	
	private int id;
	
	private String code ;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "HomeFuntionItem [name=" + name + ", id=" + id + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
