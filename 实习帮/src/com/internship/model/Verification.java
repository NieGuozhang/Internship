package com.internship.model;

import java.io.Serializable;


public class Verification implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7965973287931395804L;
	private Integer verId;
	private String checkState;
	private Enterprise enterprise;
	private School school;
	
	public Integer getVerId() {
		return verId;
	}
	public void setVerId(Integer verId) {
		this.verId = verId;
	}
	
	public String getCheckState() {
		return checkState;
	}
	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}
	public Enterprise getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	

}
