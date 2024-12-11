package com.kingsmen.kingsreach.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kingsmen.kingsreach.enums.ApprovedBy;
import com.kingsmen.kingsreach.enums.LeaveStatus;
import com.kingsmen.kingsreach.enums.LeaveType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "`leave`")
public class Leave {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leaveId;
	private LeaveType leaveType;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String reason;
	private LeaveStatus leaveStatus;
	private String employeeId;

	private int casualLeaveBalance = 10;
	private int sickLeaveBalance = 12;
	private int paidLeaveBalance = 12;
	private int emergencyLeaveBalance=7;

	@JsonIgnore
	@ManyToOne
	private Employee employee;

	private ApprovedBy approvedBy;

	public ApprovedBy getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(ApprovedBy approvedBy) {
		this.approvedBy = approvedBy;
	}

	public int getCasualLeaveBalance() {
		return casualLeaveBalance;
	}

	public void setCasualLeaveBalance(int casualLeaveBalance) {
		this.casualLeaveBalance = casualLeaveBalance;
	}

	public int getSickLeaveBalance() {
		return sickLeaveBalance;
	}

	public void setSickLeaveBalance(int sickLeaveBalance) {
		this.sickLeaveBalance = sickLeaveBalance;
	}

	public int getPaidLeaveBalance() {
		return paidLeaveBalance;
	}

	public void setPaidLeaveBalance(int paidLeaveBalance) {
		this.paidLeaveBalance = paidLeaveBalance;
	}

	public int getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LeaveStatus getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatus leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public int getEmergencyLeaveBalance() {
		return emergencyLeaveBalance;
	}

	public void setEmergencyLeaveBalance(int emergencyLeaveBalance) {
		this.emergencyLeaveBalance = emergencyLeaveBalance;
	}
}
