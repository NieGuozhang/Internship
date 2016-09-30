package com.internship.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;



public class Collect implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1300136221579492791L;
	private Integer coId;
	private Timestamp collecttime;     //�ղ�ʱ��
	private Student student;           //ѧ����
	private Position position;   //ְλ���
	

	public Integer getCoId() {
		return coId;
	}
	public void setCoId(Integer coId) {
		this.coId = coId;
	}
	
	public Timestamp getCollecttime() {
		return collecttime;
	}
	public void setCollecttime(Timestamp collecttime) {
		this.collecttime = collecttime;
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
	
	
}
