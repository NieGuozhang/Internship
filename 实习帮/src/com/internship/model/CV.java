package com.internship.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.litepal.crud.DataSupport;

public class CV  extends DataSupport implements Serializable {

	/**
	 * 简历表实体类
	 */
	private static final long serialVersionUID = 7196434117584153581L;
	private Integer cvId;
	private String email;
	private String phone;
	private String interestion;
	private Timestamp graduatetime;
	private String honour;
	private String introduction;
	private Student student;

	public CV() {
	}

	public Integer getCvId() {
		return cvId;
	}

	public void setCvId(Integer cvId) {
		this.cvId = cvId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getInterestion() {
		return interestion;
	}

	public void setInterestion(String interestion) {
		this.interestion = interestion;
	}

	public Timestamp getGraduatetime() {
		return graduatetime;
	}

	public void setGraduatetime(Timestamp graduatetime) {
		this.graduatetime = graduatetime;
	}

	public String getHonour() {
		return honour;
	}

	public void setHonour(String honour) {
		this.honour = honour;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
