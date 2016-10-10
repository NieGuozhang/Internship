package com.internship.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.litepal.crud.DataSupport;


public class School  extends DataSupport implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6695631622935407550L;
	private Integer schId;
	private String schName;
	private String schNum;
	private String schIntro;
	private String schAdd;
	private Integer pcode;
	private String schPwd;
	private Set verifications = new HashSet<Verification>();
	private Set students = new HashSet<Student>();
	
	public School(){}
	

	public Integer getSchId() {
		return schId;
	}
	public void setSchId(Integer schId) {
		this.schId = schId;
	}
	public String getSchName() {
		return schName;
	}
	public void setSchName(String schName) {
		this.schName = schName;
	}
	public String getSchIntro() {
		return schIntro;
	}
	public void setSchIntro(String schIntro) {
		this.schIntro = schIntro;
	}
	public String getSchAdd() {
		return schAdd;
	}
	public void setSchAdd(String schAdd) {
		this.schAdd = schAdd;
	}
	public String getSchNum() {
		return schNum;
	}
	public void setSchNum(String schNum) {
		this.schNum = schNum;
	}
	public Integer getPcode() {
		return pcode;
	}
	public void setPcode(Integer pcode) {
		this.pcode = pcode;
	}
	public String getSchPwd() {
		return schPwd;
	}
	public void setSchPwd(String schPwd) {
		this.schPwd = schPwd;
	}
	public Set getVerifications() {
		return verifications;
	}
	public void setVerifications(Set verifications) {
		this.verifications = verifications;
	}
	public Set getStudents() {
		return students;
	}
	public void setStudents(Set students) {
		this.students = students;
	}
	
	
}
