package com.internship.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.litepal.crud.DataSupport;

public class Student  extends DataSupport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5604653764330722892L;
	private Integer stuId;
	private String stuName;
	private String stuSex;
	private String idNum;
	private String email;
	private String tel;
	private String major;
	private String education;
	private String password;
	private String address;
	private CV cv;
	private School school;
	private Set applies = new HashSet<Apply>();
	private Set collects = new HashSet<Collect>();

	public Student() {
	}

	public Integer getStuId() {
		return stuId;
	}

	public void setStuId(Integer stuId) {
		this.stuId = stuId;
	}

	public String getStuName() {
		return stuName;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStuSex() {
		return stuSex;
	}

	public void setStuSex(String stuSex) {
		this.stuSex = stuSex;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set getApplies() {
		return applies;
	}

	public void setApplies(Set applies) {
		this.applies = applies;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	public Set getCollects() {
		return collects;
	}

	public void setCollects(Set collects) {
		this.collects = collects;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

}
