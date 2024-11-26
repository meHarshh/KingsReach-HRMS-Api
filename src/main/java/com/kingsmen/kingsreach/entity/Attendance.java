package com.kingsmen.kingsreach.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Attendance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int attendanceId;
	private int totalWorkMinutes;
	private String firstPunchIn;
	private String lastPunchOut;
	
	@Transient
	private String employeeId;
	
	private Employee employee;
	
	
	
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
	public int getAttendanceId() {
		return attendanceId;
	}
	public void setAttendanceId(int attendanceId) {
		this.attendanceId = attendanceId;
	}
	public int getTotalWorkMinutes() {
		return totalWorkMinutes;
	}
	public void setTotalWorkMinutes(int totalWorkMinutes) {
		this.totalWorkMinutes = totalWorkMinutes;
	}
	public String getFirstPunchIn() {
		return firstPunchIn;
	}
	public void setFirstPunchIn(String firstPunchIn) {
		this.firstPunchIn = firstPunchIn;
	}
	public String getLastPunchOut() {
		return lastPunchOut;
	}
	public void setLastPunchOut(String lastPunchOut) {
		this.lastPunchOut = lastPunchOut;
	}
	
	
	
	
	

}
