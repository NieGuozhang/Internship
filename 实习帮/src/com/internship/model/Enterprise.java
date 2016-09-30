package com.internship.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Enterprise implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1383799635353022034L;
	private Integer enId;
	private String enName;
	private String enType;
	private String enHandler;
	private String enTel;
	private String enAdd;
	private String enIntro;
	private String enPwd;
	private Set positions = new HashSet<Position>();
	private Set verification = new HashSet<Verification>();
	

	public Integer getEnId() {
		return enId;
	}
	public void setEnId(Integer enId) {
		this.enId = enId;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getEnType() {
		return enType;
	}
	public void setEnType(String enType) {
		this.enType = enType;
	}
	public String getEnHandler() {
		return enHandler;
	}
	public void setEnHandler(String enHandler) {
		this.enHandler = enHandler;
	}
	public String getEnTel() {
		return enTel;
	}
	public void setEnTel(String enTel) {
		this.enTel = enTel;
	}
	public String getEnAdd() {
		return enAdd;
	}
	public void setEnAdd(String enAdd) {
		this.enAdd = enAdd;
	}
	public String getEnIntro() {
		return enIntro;
	}
	public void setEnIntro(String enIntro) {
		this.enIntro = enIntro;
	}
	public String getEnPwd() {
		return enPwd;
	}
	public void setEnPwd(String enPwd) {
		this.enPwd = enPwd;
	}

	public Set getPositions() {
		return positions;
	}
	public void setPositions(Set positions) {
		this.positions = positions;
	}

	public Set getVerification() {
		return verification;
	}
	public void setVerification(Set verification) {
		this.verification = verification;
	}
	
	
	
}
