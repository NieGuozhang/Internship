package com.internship.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Apply implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1722137198936598194L;
	private Integer applyId;
	private Integer apply_state;// 0:正在实习1:申请中 2:审核通过3:审核未通过;4实习结束
	private String evaluation;
	private Float starevalu;
	private Timestamp applytime;// 申请时间
	private Timestamp settime;// 设置实习时间
	private Student student;
	private Position position;
	private String summary;// 总结

	public Integer getId() {
		return applyId;
	}

	public void setId(Integer applyId) {
		this.applyId = applyId;
	}

	public Integer getApply_state() {
		return apply_state;
	}

	public void setApply_state(Integer apply_state) {
		this.apply_state = apply_state;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public Float getStarevalu() {
		return starevalu;
	}

	public void setStarevalu(Float starevalu) {
		this.starevalu = starevalu;
	}

	public Integer getApplyId() {
		return applyId;
	}

	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	public Timestamp getApplytime() {
		return applytime;
	}

	public void setApplytime(Timestamp applytime) {
		this.applytime = applytime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Timestamp getSettime() {
		return settime;
	}

	public void setSettime(Timestamp settime) {
		this.settime = settime;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
