package com.internship.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Position implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8418348366903239337L;
	private Integer poId;
	private String poName;
	private String workTime;
	private Integer salarymax;
	private Integer salarymin;
	private String poAddress;
	private String poEducation;
	private String poType;
	private Integer numbers;
	private Integer poNum;
	private String description;
	private Timestamp closingdate;
	private Timestamp workstarttime;
	private Set applies = new HashSet<Apply>();
	private Enterprise enterprise;

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public String getPoName() {
		return poName;
	}

	public void setPoName(String poName) {
		this.poName = poName;
	}

	public Integer getSalarymax() {
		return salarymax;
	}

	public void setSalarymax(Integer salarymax) {
		this.salarymax = salarymax;
	}

	public Integer getSalarymin() {
		return salarymin;
	}

	public void setSalarymin(Integer salarymin) {
		this.salarymin = salarymin;
	}

	public String getPoAddress() {
		return poAddress;
	}

	public void setPoAddress(String poAddress) {
		this.poAddress = poAddress;
	}

	public String getPoEducation() {
		return poEducation;
	}

	public void setPoEducation(String poEducation) {
		this.poEducation = poEducation;
	}

	public String getPoType() {
		return poType;
	}

	public void setPoType(String poType) {
		this.poType = poType;
	}

	public Integer getPoNum() {
		return poNum;
	}

	public void setPoNum(Integer poNum) {
		this.poNum = poNum;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public Timestamp getClosingdate() {
		return closingdate;
	}

	public void setClosingdate(Timestamp closingdate) {
		this.closingdate = closingdate;
	}

	public Set getApplies() {
		return applies;
	}

	public void setApplies(Set applies) {
		this.applies = applies;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNumbers() {
		return numbers;
	}

	public void setNumbers(Integer numbers) {
		this.numbers = numbers;
	}

	public Timestamp getWorkstarttime() {
		return workstarttime;
	}

	public void setWorkstarttime(Timestamp workstarttime) {
		this.workstarttime = workstarttime;
	}



}
